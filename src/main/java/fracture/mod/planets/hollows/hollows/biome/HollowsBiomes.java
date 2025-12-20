package fracture.mod.planets.hollows.hollows.biome;

import java.util.Random;

import fracture.mod.planets.hollows.hollows.biome.gen.BiomeHollowsSpikes;
import fracture.mod.planets.thefracture.thefracture.biome.gen.BiomeTheFracturePlains;
import fracture.mod.init.BlockInit;
import fracture.mod.planets.hollows.hollows.biome.gen.BiomeHollowsIceLakes;
import fracture.mod.planets.hollows.hollows.biome.gen.BiomeHollowsSpikes;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.chunk.ChunkPrimer;

public class HollowsBiomes extends BiomeGenBaseGC {

	public static final Biome BiomeHollowsSpikes = new BiomeHollowsSpikes(new BiomeProperties("Hollows Spikes").setBaseHeight(0.1F).setHeightVariation(0.026F).setRainfall(0.5F).setSnowEnabled());
	public static final Biome BiomeHollowsIceLakes = new BiomeHollowsIceLakes(new BiomeProperties("Hollows Ice Lakes").setBaseHeight(-0.10F).setHeightVariation(0.07F).setRainfall(0.5F).setSnowEnabled());

    public static final Biome[] biomes = {BiomeHollowsSpikes,BiomeHollowsIceLakes};
    

    protected HollowsBiomes(BiomeProperties properties) {
        super(properties, true);
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunk, int x, int z, double stoneNoise) {
        generateBiomeTerrain(world, rand, chunk, x, z, stoneNoise);
    }
    public final void generateBiomeTerrain(Random rand, ChunkPrimer chunk, int x, int z, double stoneNoise) {
        IBlockState top = this.topBlock;
        IBlockState filler = this.fillerBlock;
        int depth = -1;
        int k = (int)(stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D); // lower variance for subtle layers
        int l = x & 15;
        int i1 = z & 15;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        for (int y = 255; y >= 0; --y) {
            if (y <= rand.nextInt(5)) {
                chunk.setBlockState(i1, y, l, Blocks.BEDROCK.getDefaultState());
            } else {
                IBlockState current = chunk.getBlockState(i1, y, l);
                if (current.getMaterial() == Material.AIR) {
                    depth = -1;
                } else if (current.getBlock() == BlockInit.STONE_HOLLOWS) {
                    if (depth == -1) {
                        // Detect biome border (neighboring biome mismatch)
                        Biome centerBiome = this;
                        //Biome west = world.getBiome(new BlockPos(x - 1, y, z));
                       // Biome east = world.getBiome(new BlockPos(x + 1, y, z));
                        //Biome north = world.getBiome(new BlockPos(x, y, z - 1));
                        //Biome south = world.getBiome(new BlockPos(x, y, z + 1));

                       // boolean isBorder = west != centerBiome || east != centerBiome || north != centerBiome || south != centerBiome;

                        //int heightBoost = isBorder ? 2 : 0;

                        depth = k;

                        // Ice patches in ground
                        if (y < 63 && rand.nextFloat() < 0.1F) {
                            chunk.setBlockState(i1, y - 1, l, Blocks.PACKED_ICE.getDefaultState());
                            if (rand.nextBoolean()) {
                                chunk.setBlockState(i1, y - 2, l, Blocks.PACKED_ICE.getDefaultState());
                            }
                        }

                        // Biome-specific terrain
                        if (this == BiomeHollowsIceLakes) {
                            top = Blocks.PACKED_ICE.getDefaultState();
                            filler = Blocks.PACKED_ICE.getDefaultState();
                        } else if (this == BiomeHollowsSpikes) {
                            top = BlockInit.SURFACE_HOLLOWS.getDefaultState();
                            filler = BlockInit.STONE_HOLLOWS.getDefaultState();
                        }

                        if (y < 63) {
                            top = Blocks.PACKED_ICE.getDefaultState();
                        }

                        // Raise surface on border
                        //chunk.setBlockState(i1, y + heightBoost, l, top);
                    } else if (depth > 0) {
                        --depth;
                        chunk.setBlockState(i1, y, l, filler);
                    }
                }
            }
        }
        
    }}
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
//        for (int y = 255; y >= 0; --y) {
//            if (y <= rand.nextInt(5)) {
//                chunk.setBlockState(i1, y, l, Blocks.BEDROCK.getDefaultState());
//            } else {
//                IBlockState current = chunk.getBlockState(i1, y, l);
//                if (current.getMaterial() == Material.AIR) {
//                    depth = -1;
//                } else if (current.getBlock() == BlockInit.STONE_HOLLOWS) {
//                    if (depth == -1) {
//                        depth = k;
//                        
//                        //
//                        // Add patches of ice blocks in the ground
//                        if (y < 63 && rand.nextFloat() < 0.1F) {
//                            chunk.setBlockState(i1, y - 1, l, Blocks.PACKED_ICE.getDefaultState());
//                            if (rand.nextBoolean()) {
//                                chunk.setBlockState(i1, y - 2, l, Blocks.PACKED_ICE.getDefaultState());
//                            }
//                        }
//                        //
//                        
//                        // Check for biome specific terrain
//                        if (this == BiomeHollowsIceLakes) {
//                            top = Blocks.PACKED_ICE.getDefaultState();
//                            filler = Blocks.PACKED_ICE.getDefaultState();
//                        } else if (this == BiomeHollowsSpikes) {
//                            top = BlockInit.SURFACE_HOLLOWS.getDefaultState();
//                            filler = BlockInit.STONE_HOLLOWS.getDefaultState();
//                        }
//
//                        if (y < 63) {
//                            top = Blocks.PACKED_ICE.getDefaultState();
//                        }
//
//                        chunk.setBlockState(i1, y, l, top);
//                    } else if (depth > 0) {
//                        --depth;
//                        chunk.setBlockState(i1, y, l, filler);
//                    }
//                }
//            }
//        }
//    }
//}
