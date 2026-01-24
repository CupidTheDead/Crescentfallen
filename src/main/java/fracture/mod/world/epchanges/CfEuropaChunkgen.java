package fracture.mod.world.epchanges;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CfEuropaChunkgen {

	// Note: go back later and have this draw from players config
    private static final int EUROPA_DIM_ID = -1501;
    
    private IBlockState denseIceState = null;
    private IBlockState saltWaterState = null;
    private IBlockState stoneState = null; 
    private IBlockState spikeState = null; 
    private IBlockState gravelState = null;
    
    private Set<Block> targetWaterBlocks = new HashSet<>();
    private boolean initialized = false;
    private final Random rand = new Random();
    
    // Ocean settings
    private final double noiseScale = 0.008; 
    private final double oceanThreshold = 0.20; 

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (event.getWorld().isRemote) return;
        if (event.getWorld().provider.getDimension() != EUROPA_DIM_ID) return;

        if (!initialized) {
            initBlocks();
            initialized = true;
        }
        
        if (denseIceState == null || saltWaterState == null) return;

        Chunk chunk = event.getChunk();
        boolean modified = false;

        // Iterate columns
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                
                int worldX = (chunk.x << 4) + x;
                int worldZ = (chunk.z << 4) + z;
                
                // Get biome for extended surface noise logic
                Biome biome = chunk.getBiome(new BlockPos(x, 60, z), event.getWorld().getBiomeProvider());
                String biomeName = (biome.getRegistryName() != null) ? biome.getRegistryName().getPath() : "";
                
                // Target biomes for surface features
                boolean isEuropaBiome = biomeName.contains("europa"); // Covers "europa", "europa_ice_valleys", "europa_salt_sea"

                // Creates ocean bowl for undersea ocean caves
                double noiseVal = getNoise(worldX, worldZ, noiseScale);
                int floorY = 50; 

                if (noiseVal > oceanThreshold) {
                    double deepness = (noiseVal - oceanThreshold) * 4.5; 
                    if (deepness > 1.0) deepness = 1.0;
                    double baseFloor = 50 - (28 * deepness);
                    
                    // Flattened but Varied floor
                    // Uses two organic waves to prevent the grid pattern.
                    // Lower amplitude (0.8) makes it flatter. 
                    double roughness = (Math.sin(worldX * 0.15) + Math.cos(worldZ * 0.19)) * 0.8; 
                    roughness += (Math.sin(worldX * 0.5) * 0.3); 
                    
                    floorY = (int) (baseFloor + roughness);
                }

                // Spike calculation
                // Fractal sum to break the grid pattern
                // If the noise is high enough, a spike is generated
                int spikeHeight = 0;
                if (floorY < 45) { // Only in deep water
                    double spikeNoise = getOrganicNoise(worldX, worldZ, 0.25);
                    // Defines sparceness
                    if (spikeNoise > 0.55) {
                        // Height depends on how intense the noise is at this spot
                        spikeHeight = (int) ((spikeNoise - 0.55) * 25.0); 
                    }
                }

                // Extended surface noise
                int mountainY = 62;
                if (isEuropaBiome) {
                    double mountNoise = getOrganicNoise(worldX, worldZ, 0.04); // Large scale mountains
                    if (mountNoise > 0.3) {
                        // Mountains can go up to ~Y90
                        mountainY = 62 + (int)((mountNoise - 0.3) * 45.0); 
                    }
                }

                // Ice ceiling
                int jaggedDepth = rand.nextInt(4);
                int iceBottomY = 53 + jaggedDepth; 

                // VERTICAL SCAN (Scan HIGHER now for mountains, e.g. 100 -> 20)
                for (int y = 100; y >= 20; y--) {
                    
                    ExtendedBlockStorage storage = chunk.getBlockStorageArray()[y >> 4];
                    if (storage == null) {
                         // Create storage if generating mountains in empty air
                         if (y > 62 && y <= mountainY) {
                             storage = new ExtendedBlockStorage(y >> 4 << 4, event.getWorld().provider.hasSkyLight());
                             chunk.getBlockStorageArray()[y >> 4] = storage;
                         } else {
                             continue;
                         }
                    }

                    int localY = y & 15;
                    IBlockState current = storage.get(x, localY, z);
                    Block b = current.getBlock();
                    boolean isVanillaGravel = (b == Blocks.GRAVEL);

                    // Surface terrain gen
                    if (y > 62) {
                        if (y <= mountainY) {
                             // Build mountains
                             if (b == Blocks.AIR || targetWaterBlocks.contains(b)) {
                                 storage.set(x, localY, z, denseIceState); 
                                 modified = true;
                             }
                        }
                        continue;
                    }

                    // Cap (not working)
                    if (y == 62) {
                        // If in a target biome, FORCE a cap over air/water
                        if (isEuropaBiome && (b == Blocks.AIR || targetWaterBlocks.contains(b) || isVanillaGravel)) {
                             storage.set(x, localY, z, denseIceState);
                             modified = true;
                        } else if (isVanillaGravel) {
                             storage.set(x, localY, z, denseIceState);
                             modified = true;
                        }
                        continue;
                    }

                    // Ice crust (Below 62, above iceBottom)
                    if (y > iceBottomY) {
                        if (targetWaterBlocks.contains(b) || b == Blocks.AIR || isVanillaGravel) {
                            storage.set(x, localY, z, denseIceState);
                            modified = true;
                        }
                    }
                    
                    // OCEAN
                    else if (y > floorY) {
                        
                        // Spikes
                        if (y <= floorY + spikeHeight) {
                            storage.set(x, localY, z, spikeState); // Subsurface Stone
                            modified = true;
                        } else {
                            // Water
                            if (b == Blocks.AIR || targetWaterBlocks.contains(b) || 
                                b == stoneState.getBlock() || b == Blocks.STONE || isVanillaGravel) {
                                storage.set(x, localY, z, saltWaterState);
                                modified = true;
                            }
                        }
                    }

                    // Ocean floor
                    else if (y <= floorY) {
                        if (y >= floorY - 3) {
                            // Gravel layer
                            if (isVanillaGravel || b == Blocks.AIR || targetWaterBlocks.contains(b)) {
                                storage.set(x, localY, z, gravelState);
                                modified = true;
                            }
                        } else {
                            // Bedrock / Deep Stone
                            if (b == Blocks.AIR || targetWaterBlocks.contains(b) || isVanillaGravel) {
                                storage.set(x, localY, z, spikeState); 
                                modified = true;
                            }
                        }
                    }
                }
            }
        }
        
        if (modified) {
            chunk.markDirty();
        }
    }

    // Standard noise for broad ocean shape
    private double getNoise(int x, int z, double scale) {
        double d1 = Math.sin(x * scale) + 0.5 * Math.cos(z * scale * 1.3);
        double d2 = Math.cos(x * scale * 0.7) + 0.5 * Math.sin(z * scale);
        return (d1 + d2) / 3.0; 
    }

    // "Organic" Noise - breaks grid patterns by mixing irrational frequencies
    private double getOrganicNoise(int x, int z, double scale) {
        double val = 0;
        // Layer 1
        val += Math.sin(x * scale + z * scale * 0.5);
        // Layer 2 (Irrational offset)
        val += Math.cos(x * scale * 0.8 - z * scale * 1.2) * 0.5;
        // Layer 3 (High freq detail)
        val += Math.sin(x * scale * 2.3 + z * scale * 0.2) * 0.25;
        
        // Normalize roughly to -1 to 1 range (current sum is approx -1.75 to 1.75)
        return val / 1.75; 
    }

    @SuppressWarnings("deprecation")
    private void initBlocks() {
        System.out.println("[Fracture] EuropaChunkRetrogen: Initializing v4 (Organic Spikes & Mountains)...");

        Block ice = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "dense_ice"));
        denseIceState = (ice != null) ? ice.getDefaultState() : Blocks.PACKED_ICE.getDefaultState();

        Block stone = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "europa")); 
        if (stone != null) {
            stoneState = stone.getDefaultState(); // Meta 0
            try {
                spikeState = stone.getStateFromMeta(1);
            } catch (Exception e) {
                spikeState = stoneState;
            }
        } else {
            stoneState = Blocks.STONE.getDefaultState();
            spikeState = Blocks.STONE.getDefaultState();
        }

        Block gravel = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "europa_gravel"));
        gravelState = (gravel != null) ? gravel.getDefaultState() : Blocks.GRAVEL.getDefaultState();

        Block waterBlock = Blocks.WATER;
        Block epFluid = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "salt_fluid"));
        if (epFluid != null && epFluid != Blocks.AIR) {
            waterBlock = epFluid;
        } else {
            epFluid = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "salt_water"));
            if (epFluid != null && epFluid != Blocks.AIR) waterBlock = epFluid;
        }
        saltWaterState = waterBlock.getDefaultState();

        String[] candidates = { "salt", "salt_fluid", "salt_water", "crystallized_water", "clean_water" };
        for (String name : candidates) {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", name));
            if (b != null) targetWaterBlocks.add(b);
        }
        targetWaterBlocks.add(Blocks.WATER);
        targetWaterBlocks.add(Blocks.FLOWING_WATER);
        targetWaterBlocks.add(stoneState.getBlock());
        targetWaterBlocks.add(Blocks.GRAVEL); 
    }
}