package fracture.mod.planets.dreamyard.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import fracture.mod.init.CFplanets;
import fracture.mod.planets.dreamyard.biome.gen.GenLayerDreamyard;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeAdaptive;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeProviderdreamyard extends BiomeProvider {
	private GenLayer baseBiomes;
	private GenLayer zoomedBiomes;
	private BiomeCache biomeCache;
	private List<Biome> biomesToSpawnIn;
	private CelestialBody body;

	
	protected BiomeProviderdreamyard() {
		this.body = CFplanets.dreamyard;
		this.biomeCache = new BiomeCache(this);
		this.biomesToSpawnIn = new ArrayList<>();

		//Add Dreamyard biomes suitable for spawning here

		this.biomesToSpawnIn.add(DreamyardBiomes.BiomeDreamyardMeadows);
		this.biomesToSpawnIn.add(DreamyardBiomes.BiomeDreamyardDesertMud);
		this.biomesToSpawnIn.add(DreamyardBiomes.BiomeDreamyardLakes);
		// ..

	}

	public BiomeProviderdreamyard(long seed, WorldType type) {
		this();
		//GenLayerDreamyard creates biome layers
		GenLayer[] layers = GenLayerDreamyard.createWorld(seed);
		this.baseBiomes = layers[0];
		this.zoomedBiomes = layers[1];
	}

	public BiomeProviderdreamyard(World world) {
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
	}

	@Override
	public List<Biome> getBiomesToSpawnIn() {
		return this.biomesToSpawnIn;
	}

	@Override
	public Biome getBiome(BlockPos pos, Biome defaultBiome) {
		//defines with BiomeAdaptive what celestial body we are on (for multi-biome behavior)
		BiomeAdaptive.setBodyMultiBiome(this.body);
		return this.biomeCache.getBiome(pos.getX(), pos.getZ(), BiomeAdaptive.biomeDefault);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getTemperatureAtHeight(float temp, int height) {
		//add variation by height (make mountains get colder with altitude)
		return temp;
	}

	@Override
	public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int height, int width) {
		IntCache.resetIntCache();
		BiomeAdaptive.setBodyMultiBiome(this.body);

		if (biomes == null || biomes.length < width * height) {
			biomes = new Biome[width * height];
		}

		int[] biomeIds = this.baseBiomes.getInts(x, z, width, height);

		for (int i = 0; i < width * height; i++) {
			biomes[i] = biomeIds[i] >= 0 ? Biome.getBiome(biomeIds[i]) : BiomeAdaptive.biomeDefault;
		}
		return biomes;
	}

	@Override
	public Biome[] getBiomes(@Nullable Biome[] reuse, int x, int z, int width, int height) {
		return getBiomes(reuse, x, z, width, height, true);
	}

	@Override
	public Biome[] getBiomes(@Nullable Biome[] reuse, int x, int z, int width, int height, boolean cacheFlag) {
		IntCache.resetIntCache();
		BiomeAdaptive.setBodyMultiBiome(this.body);

		if (reuse == null || reuse.length < width * height) {
			reuse = new Biome[width * height];
		}

		//use cached biomes for full chunks to improve performance
		if (cacheFlag && width == 16 && height == 16 && (x & 15) == 0 && (z & 15) == 0) {
			Biome[] cached = this.biomeCache.getCachedBiomes(x, z);
			System.arraycopy(cached, 0, reuse, 0, width * height);
			return reuse;
		}

		int[] biomeIds = this.zoomedBiomes.getInts(x, z, width, height);
		for (int i = 0; i < width * height; i++) {
			reuse[i] = biomeIds[i] >= 0 ? Biome.getBiome(biomeIds[i]) : BiomeAdaptive.biomeDefault;
		}
		return reuse;
	}

	@Override
	public boolean areBiomesViable(int x, int z, int radius, List<Biome> viableBiomes) {
		int startX = (x - radius) >> 2;
		int startZ = (z - radius) >> 2;
		int endX = (x + radius) >> 2;
		int endZ = (z + radius) >> 2;
		int width = (endX - startX) + 1;
		int height = (endZ - startZ) + 1;

		int[] biomeIds = this.baseBiomes.getInts(startX, startZ, width, height);

		for (int i = 0; i < width * height; i++) {
			Biome biome = Biome.getBiome(biomeIds[i]);
			if (!viableBiomes.contains(biome)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public BlockPos findBiomePosition(int x, int z, int radius, List<Biome> biomes, Random random) {
		int startX = (x - radius) >> 2;
		int startZ = (z - radius) >> 2;
		int endX = (x + radius) >> 2;
		int endZ = (z + radius) >> 2;
		int width = (endX - startX) + 1;
		int height = (endZ - startZ) + 1;

		int[] biomeIds = this.baseBiomes.getInts(startX, startZ, width, height);
		BlockPos result = null;
		int foundCount = 0;

		for (int i = 0; i < biomeIds.length; i++) {
			int posX = (startX + i % width) << 2;
			int posZ = (startZ + i / width) << 2;
			Biome biome = Biome.getBiome(biomeIds[i]);

			if (biomes.contains(biome) && (result == null || random.nextInt(foundCount + 1) == 0)) {
				result = new BlockPos(posX, 0, posZ);
				foundCount++;
			}
		}

		return result;
	}

	@Override
	public void cleanupCache() {
		this.biomeCache.cleanupCache();
	}

}
