package fracture.mod.world.epchanges;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

// This is a wip and is not working correctly

public class CfOberonChunkgen {

    private static final int OBERON_DIM_ID = -1509; 
    
    private IBlockState surfaceState = null;
    private IBlockState subSurfaceState = null;
    private IBlockState stoneState = null; 
    private IBlockState gravelState = null;
    private IBlockState iceState = null;
    
    private Set<Block> targetReplaceBlocks = new HashSet<>();
    private boolean initialized = false;
    private final Random rand = new Random();

    // --- TERRAIN SETTINGS ---
    // 1. REGION SELECTOR (Larger number = Larger biomes of mountains vs valleys)
    // 0.003 makes massive regions (approx 500-1000 blocks wide)
    private final double regionScale = 0.003; 

    // 2. MOUNTAINS
    private final double mountainScale = 0.015; // Width
    private final int mountainHeight = 40;      // Hight

    // 3. VALLEYS (The "Line" cuts)
    private final double valleyScale = 0.04;    // Frequency
    private final double valleyWidth = 0.15;    // Width

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (event.getWorld().isRemote) return;
        if (event.getWorld().provider.getDimension() != OBERON_DIM_ID) return;

        if (!initialized) {
            initBlocks();
            initialized = true;
        }
        
        if (surfaceState == null) return;

        Chunk chunk = event.getChunk();
        boolean modified = false;
        
        // DEBUG: Print once per 100 chunks to confirm execution without spamming
        if (rand.nextInt(100) == 0) {
            System.out.println("[Fracture] Oberon Gen active at Chunk: " + chunk.x + ", " + chunk.z);
        }

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                
                int worldX = (chunk.x << 4) + x;
                int worldZ = (chunk.z << 4) + z;

                // Calculate region
                // This should create large blobs. 
                // Positive numbers = Mountainous Region
                // Negative numbers = Valley Region
                double regionNoise = getNoise(worldX, worldZ, regionScale);

                int surfaceY = 70; // Base height of Oberon
                boolean isValley = false;
                
                // determine hight based on region
                if (regionNoise > 0.1) {
                    // Mountains
                    // Smooth, rolling semi-smooth mountains
                    double mNoise = getNoise(worldX, worldZ, mountainScale);
                    // Map -1 to 1 range into 0 to 1 range
                    double heightMod = (mNoise + 1.0) / 2.0; 
                    surfaceY += (int)(heightMod * mountainHeight);
                } 
                else if (regionNoise < -0.1) {
                    // Vallys
                    // We want "Line like valleys". We use Math.abs() to create sharp cuts.
                    
                    // Primary valley noise
                    double vNoise = getOrganicNoise(worldX, worldZ, valleyScale);
                    
                    // "distToCenter" is 0 when right in the middle of a crack line
                    double distToCenter = Math.abs(vNoise);
                    
                    if (distToCenter < valleyWidth) {
                        isValley = true;
                        // The closer to 0, the deeper it goes
                        double depth = (valleyWidth - distToCenter) * 120.0; // Deep cuts
                        surfaceY -= (int)depth;
                    } else {
                        // Flat plains between cracks
                        surfaceY += (int)(getNoise(worldX, worldZ, 0.05) * 3);
                    }
                }
                else {
                    // Feature transitions
                    // Just basic terrain
                    surfaceY += (int)(getNoise(worldX, worldZ, 0.02) * 8);
                }

                // Dont break bedrock
                if (surfaceY < 5) surfaceY = 5;

                // Block placement
                // Scan column
                for (int y = 160; y >= 0; y--) {
                    
                    ExtendedBlockStorage storage = chunk.getBlockStorageArray()[y >> 4];
                    if (storage == null) {
                        if (y < surfaceY + 2) { 
                             storage = new ExtendedBlockStorage(y >> 4 << 4, event.getWorld().provider.hasSkyLight());
                             chunk.getBlockStorageArray()[y >> 4] = storage;
                        } else {
                             continue;
                        }
                    }

                    int localY = y & 15;
                    IBlockState current = storage.get(x, localY, z);
                    Block b = current.getBlock();
                    
                    // We treat standard Oberon blocks as "replacable" for sculpting
                    boolean isTerrain = (b == surfaceState.getBlock() || b == stoneState.getBlock() || b == subSurfaceState.getBlock() || b == Blocks.AIR);
                    
                    if (!isTerrain) continue; 

                    if (y <= surfaceY) {
                        // We are UNDER the new ground level
                        if (b == Blocks.AIR) {
                            // FILL LOGIC
                            if (isValley && y < 30) {
                                // Valley Floor Mix (Ice, Gravel, Stone)
                                int r = rand.nextInt(3);
                                if (r == 0) storage.set(x, localY, z, iceState);
                                else if (r == 1) storage.set(x, localY, z, gravelState);
                                else storage.set(x, localY, z, stoneState);
                                modified = true;
                            } else {
                                // Normal Ground Fill
                                if (y == surfaceY) storage.set(x, localY, z, surfaceState);
                                else if (y > surfaceY - 4) storage.set(x, localY, z, subSurfaceState);
                                else storage.set(x, localY, z, stoneState);
                                modified = true;
                            }
                        }
                    } else {
                        // We are ABOVE the new ground level
                        if (b != Blocks.AIR) {
                            // CUT LOGIC (Delete existing terrain blocks)
                            storage.set(x, localY, z, Blocks.AIR.getDefaultState());
                            modified = true;
                        }
                    }
                }
            }
        }
        
        if (modified) {
            chunk.markDirty();
        }
    }

    // Smooth noise
    private double getNoise(int x, int z, double scale) {
        return Math.sin(x * scale) + Math.sin(z * scale * 1.3) * 0.5; 
    }

    // More chaotic noise for cracks
    private double getOrganicNoise(int x, int z, double scale) {
        double val = 0;
        val += Math.sin(x * scale + z * scale);
        val += Math.cos(x * scale * 1.5 - z * scale * 0.5);
        return val / 2.0; 
    }

    private void initBlocks() {
        System.out.println("[Fracture] CFOberonChunkgen: Initializing Blocks...");

        // Note: Ensure these registry names are correct. 
        // Based on ExtraPlanets 1.12.2 source/standards:
        Block sBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "oberon_surface"));
        surfaceState = (sBlock != null) ? sBlock.getDefaultState() : Blocks.STONE.getDefaultState();

        Block subBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "oberon_sub_surface"));
        subSurfaceState = (subBlock != null) ? subBlock.getDefaultState() : Blocks.STONE.getDefaultState();

        Block stBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "oberon_stone"));
        stoneState = (stBlock != null) ? stBlock.getDefaultState() : Blocks.STONE.getDefaultState();

        Block gBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "oberon_gravel"));
        gravelState = (gBlock != null) ? gBlock.getDefaultState() : Blocks.GRAVEL.getDefaultState();
        
        Block iBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "dense_ice"));
        iceState = (iBlock != null) ? iBlock.getDefaultState() : Blocks.PACKED_ICE.getDefaultState();
    }
}