package fracture.mod.world.epchanges;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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

	// Note: change this to use config later
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
		if (event.getWorld().isRemote)
			return;
		if (event.getWorld().provider.getDimension() != EUROPA_DIM_ID)
			return;

		if (!initialized) {
			initBlocks();
			initialized = true;
		}

		if (denseIceState == null || saltWaterState == null)
			return;

		Chunk chunk = event.getChunk();
		boolean modified = false;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				int worldX = (chunk.x << 4) + x;
				int worldZ = (chunk.z << 4) + z;

				Biome biome = chunk.getBiome(new BlockPos(x, 60, z), event.getWorld().getBiomeProvider());
				String biomePath = (biome.getRegistryName() != null) ? biome.getRegistryName().getPath() : "";

				// Biome filter for specific terrain changes
				// europa and europa ice valleys are blacklisted from changes to Europa salt sea
				boolean isProtectedBiome = biomePath.equals("europa") || biomePath.equals("europa_ice_valleys");
				boolean enableVCM = !isProtectedBiome;

				// Ocean floor
				double noiseVal = getNoise(worldX, worldZ, noiseScale);
				int floorY = 50;

				if (noiseVal > oceanThreshold) {
					double deepness = (noiseVal - oceanThreshold) * 4.5;
					if (deepness > 1.0)
						deepness = 1.0;
					double baseFloor = 50 - (28 * deepness);
					double roughness = (Math.sin(worldX * 0.15) + Math.cos(worldZ * 0.19)) * 0.8;
					roughness += (Math.sin(worldX * 0.5) * 0.3);
					floorY = (int) (baseFloor + roughness);
				}

				// Beta spikes
				int spikeHeight = 0;
				if (floorY < 45) {
					double spikeNoise = getOrganicNoise(worldX, worldZ, 0.25);
					if (spikeNoise > 0.55) {
						spikeHeight = (int) ((spikeNoise - 0.55) * 25.0);
					}
				}

				// Surface topography
				int surfaceBaseY = 62;

				if (enableVCM) {
					// Vallys hairline cracks and mountains of diffrent types

					double surfaceWobble = getOrganicNoise(worldX, worldZ, 0.05);
					surfaceBaseY = 62 + (int) (surfaceWobble * 4.0);
					double europaMountainL = getNoise(worldX, worldZ, 0.004);
					if (europaMountainL > 0.3) {
						double lift = (europaMountainL - 0.3) * 85.0;
						double peakJaggedness = getOrganicNoise(worldX, worldZ, 0.1) * 2.0;
						surfaceBaseY += (int) (lift + peakJaggedness);
					}

					double crackNoise1 = getOrganicNoise(worldX, worldZ, 0.11);
					double crackNoise2 = getOrganicNoise(worldX + 1200, worldZ + 1200, 0.09);

					double dist1 = Math.abs(crackNoise1);
					double dist2 = Math.abs(crackNoise2);
					double distFC = Math.min(dist1, dist2);

					double crackWidth = 0.15;
					double rimWidth = 0.28;

					int heightModifier = 0;

					if (surfaceBaseY < 110) {
						if (distFC < crackWidth) {
							double deepNoise = Math.sin(worldX * 0.4) + Math.cos(worldZ * 0.4);
							int depth = 3 + (int) (Math.abs(deepNoise) * 4.0);
							heightModifier = -depth;
						} else if (distFC < rimWidth) {
							heightModifier = 1 + (rand.nextBoolean() ? 1 : 0);
						}
					}
					surfaceBaseY += heightModifier;
				} else {
					double lowNoise = getNoise(worldX, worldZ, 0.04);
					surfaceBaseY = 62 + (int) (lowNoise * 2.5);
				}

				// additional checking
				int jaggedDepth = rand.nextInt(4);
				int iceBottomY = 53 + jaggedDepth;

				if (surfaceBaseY <= iceBottomY) {
					surfaceBaseY = iceBottomY + 1;
				}

				// Block placement
				// Scan range: 140 down to 20
				for (int y = 140; y >= 20; y--) {

					ExtendedBlockStorage storage = chunk.getBlockStorageArray()[y >> 4];
					if (storage == null) {
						if (y > 50 && y <= surfaceBaseY + 2) {
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

					// Surface
					if (y > iceBottomY) {
						if (y <= surfaceBaseY) {
							boolean isReplaceable = (b == Blocks.AIR || targetWaterBlocks.contains(b)
									|| isVanillaGravel);

							// Bug fix
							if (isReplaceable) {
								if (b == stoneState.getBlock() || b == Blocks.STONE) {
									// Preserve Stone/Europa Block if it's high up
									// (targetWaterBlocks contains stone, so we must explicitly exempt it here)
									// Do nothing (Keep the stone)
								} else {
									storage.set(x, localY, z, denseIceState);
									modified = true;
								}
							}
						} else {
							// remove vanilla gravel from original generation
							if (b == denseIceState.getBlock() || isVanillaGravel) {
								storage.set(x, localY, z, Blocks.AIR.getDefaultState());
								modified = true;
							}
						}
					}

					// Ocean
					else if (y > floorY) {
						if (y <= floorY + spikeHeight) {
							storage.set(x, localY, z, spikeState);
							modified = true;
						} else {
							// Convert stone/air to water
							if (b == Blocks.AIR || targetWaterBlocks.contains(b) || b == stoneState.getBlock()
									|| b == Blocks.STONE || isVanillaGravel) {
								storage.set(x, localY, z, saltWaterState);
								modified = true;
							}
						}
					}

					// Floor
					else if (y <= floorY) {
						if (y >= floorY - 3) {
							if (isVanillaGravel || b == Blocks.AIR || targetWaterBlocks.contains(b)) {
								storage.set(x, localY, z, gravelState);
								modified = true;
							}
						} else {
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

	private double getNoise(int x, int z, double scale) {
		double d1 = Math.sin(x * scale) + 0.5 * Math.cos(z * scale * 1.3);
		double d2 = Math.cos(x * scale * 0.7) + 0.5 * Math.sin(z * scale);
		return (d1 + d2) / 3.0;
	}

	private double getOrganicNoise(int x, int z, double scale) {
		double val = 0;
		val += Math.sin(x * scale + z * scale * 0.5);
		val += Math.cos(x * scale * 0.8 - z * scale * 1.2) * 0.5;
		val += Math.sin(x * scale * 2.3 + z * scale * 0.2) * 0.25;
		return val / 1.75;
	}

	@SuppressWarnings("deprecation")
	private void initBlocks() {
		System.out.println("[Fracture] CFEuropaChunkgenv7 Initializing...");

		Block ice = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "dense_ice"));
		denseIceState = (ice != null) ? ice.getDefaultState() : Blocks.PACKED_ICE.getDefaultState();

		Block stone = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "europa"));
		if (stone != null) {
			stoneState = stone.getDefaultState();
			try {
				spikeState = stone.getStateFromMeta(1);
			} catch (Exception e) {
				spikeState = stoneState;
			}
		} else {
			stoneState = Blocks.STONE.getDefaultState();
			spikeState = Blocks.STONE.getDefaultState();
		}

		// I found the name :)
		Block gravel = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "europa_gravel"));
		gravelState = (gravel != null) ? gravel.getDefaultState() : Blocks.GRAVEL.getDefaultState();

		Block waterBlock = Blocks.WATER;
		Block epFluid = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "salt_fluid"));
		if (epFluid != null && epFluid != Blocks.AIR) {
			waterBlock = epFluid;
		} else {
			epFluid = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", "salt_water"));
			if (epFluid != null && epFluid != Blocks.AIR)
				waterBlock = epFluid;
		}
		saltWaterState = waterBlock.getDefaultState();

		String[] candidates = { "salt", "salt_fluid", "salt_water", "crystallized_water", "clean_water" };
		for (String name : candidates) {
			Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation("extraplanets", name));
			if (b != null)
				targetWaterBlocks.add(b);
		}
		targetWaterBlocks.add(Blocks.WATER);
		targetWaterBlocks.add(Blocks.FLOWING_WATER);
		targetWaterBlocks.add(stoneState.getBlock());
		targetWaterBlocks.add(Blocks.GRAVEL);
	}
}