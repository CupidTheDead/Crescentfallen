package fracture.mod.planets.thefracture;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.thefracture.thefracture.biome.BiomeDecoratorTheFracture;
//import fracture.mod.planets.hollows.planetone_s1.biome.BiomeDecoratorPlanetOneS1;
import fracture.mod.world.MapGenAddonCaveGen;
import fracture.mod.world.MapGenAddonRavinGen;

import fracture.mod.world.chunk.ChunkProviderFractureBase;
//IF ABOVE BREAKS REMOVE IT AND USE THE DEFAULT ONE FROM GC
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.perlin.generator.Gradient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class ChunkProviderthefracture extends ChunkProviderFractureBase {

	private final BiomeDecoratorTheFracture decorator = new BiomeDecoratorTheFracture();
	private final MapGenAddonRavinGen ravineGenerator = new MapGenAddonRavinGen();
	private final MapGenAddonCaveGen caveGenerator = new MapGenAddonCaveGen(
			BlockInit.SURFACE_FRACTURE.getDefaultState(), Blocks.LAVA.getDefaultState(),
			Sets.newHashSet(BlockInit.SURFACE_FRACTURE, BlockInit.STONE_FRACTURE));
	// private final MapGenAddonCaveGen caveGenerator = new
	// MapGenAddonCaveGen(Blocks.DIAMOND_BLOCK.getDefaultState(),
	// Blocks.LAVA.getDefaultState(),
	// Sets.newHashSet(BlockInit.SURFACE_FRACTURE, BlockInit.STONE_FRACTURE));

	public ChunkProviderthefracture(World par1World, long seed, boolean mapFeaturesEnabled) {
		super(par1World, seed, mapFeaturesEnabled);

	}

	@Override
	protected List<MapGenBaseMeta> getWorldGenerators() {
		List<MapGenBaseMeta> generators = Lists.newArrayList();
		generators.add(this.caveGenerator);
		return generators;
	}

	@Override
	public int getCraterProbability() {
		return 2000;
	}

	@Override
	public void onChunkProvide(int cX, int cZ, ChunkPrimer primer) {
		this.ravineGenerator.generate(this.worldObj, cX, cZ, primer);
	}

	@Override
	public void onPopulate(int cX, int cZ) {

	}

	@Override
	public void recreateStructures(Chunk chunk, int x, int z) {
	}

	@Override
	protected void decoratePlanet(World world, Random rand, int x, int z) {
		this.decorator.decorate(this.worldObj, rand, x, z);
	}

//	@Override
//	protected IBlockState getTopBlock() {
	// TODO Auto-generated method stub
//		return null;
//	}

//	@Override
//	protected IBlockState getSubBlock() {
	// TODO Auto-generated method stub
	// return null;
}
//}