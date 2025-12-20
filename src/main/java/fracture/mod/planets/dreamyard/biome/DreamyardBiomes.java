package fracture.mod.planets.dreamyard.biome;

import java.util.Random;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.dreamyard.biome.gen.*;
import fracture.mod.world.chunk.ChunkProviderDreamyardBase;
import fracture.mod.world.chunk.ChunkProviderFractureBase;
import micdoodle8.mods.galacticraft.api.world.BiomeGenBaseGC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class DreamyardBiomes extends BiomeGenBaseGC {

	public static final Biome BiomeDreamyardMeadows = new BiomeDreamyardMeadows(new BiomeProperties("Dreamyard Meadows").setBaseHeight(0.125F).setHeightVariation(0.2F).setRainfall(0.8F));
	public static final Biome BiomeDreamyardDesertMud = new BiomeDreamyardDesertMud(new BiomeProperties("Muddy Dreamyard Dessert").setBaseHeight(0.18F).setHeightVariation(0.3F).setRainfall(0.9F));
	public static final Biome BiomeDreamyardLakes = new BiomeDreamyardLakes(new BiomeProperties("Dreamyard Lakes").setBaseHeight(-0.4F).setHeightVariation(0.05F).setRainfall(0.95F));

	
	public static final Biome[] biomes = {BiomeDreamyardMeadows, BiomeDreamyardDesertMud, BiomeDreamyardLakes};

	protected DreamyardBiomes(BiomeProperties properties) {
		super(properties, true);
		// this.topBlock = Blocks.GRASS.getDefaultState();
		// this.fillerBlock = Blocks.DIRT.getDefaultState();
	}

	@Override
	public void genTerrainBlocks(World world, Random rand, ChunkPrimer chunk, int x, int z, double noise) {
		generateBiomeTerrain(rand, chunk, x, z, noise);
	}

	public final void generateBiomeTerrain(Random rand, ChunkPrimer chunk, int x, int z, double stoneNoise) {
		IBlockState iblockstate = this.topBlock;
		IBlockState iblockstate1 = this.fillerBlock;
		int j = -1;
		int k = (int) (stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 5.25D);
		int l = x & 15;
		int i1 = z & 15;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int j1 = 255; j1 >= 0; --j1) {
			if (j1 <= rand.nextInt(5)) {
				chunk.setBlockState(i1, j1, l, Blocks.BEDROCK.getDefaultState());
			} else {
				IBlockState iblockstate2 = chunk.getBlockState(i1, j1, l);
				if (iblockstate2.getMaterial() == Material.AIR) {
					j = -1;
				} else if (iblockstate2.getBlock() == BlockInit.DREAMYARD_GRASS.getDefaultState()) {
					if (j == -1) {
						if (k <= 0) {
							iblockstate = null;
							iblockstate1 = Blocks.COBBLESTONE.getDefaultState();
						} else if (j1 >= 63 - 4 && j1 <= 63 + 1) {
							iblockstate = this.topBlock;
							iblockstate1 = this.fillerBlock;
						}

						if (j1 < 63 && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
							if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
								iblockstate = Blocks.ICE.getDefaultState();
							} else {
								iblockstate = Blocks.WATER.getDefaultState();
							}
						}

						j = k;

						if (j1 >= 63 - 1) {
							chunk.setBlockState(i1, j1, l, iblockstate);
						} else if (j1 < 63 - 7 - k) {
							iblockstate = null;
							iblockstate1 = ((Block) Blocks.COBBLESTONE).getDefaultState();
							chunk.setBlockState(i1, j1, l, Blocks.GRAVEL.getDefaultState());
						} else {
							chunk.setBlockState(i1, j1, l, iblockstate1);
						}
					} else if (j > 0) {
						--j;
						chunk.setBlockState(i1, j1, l, iblockstate1);
					}
				}
			}
		}
	}
}