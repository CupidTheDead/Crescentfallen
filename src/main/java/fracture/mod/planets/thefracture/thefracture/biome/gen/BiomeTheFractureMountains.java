package fracture.mod.planets.thefracture.thefracture.biome.gen;

import java.util.List;
import java.util.Random;

import org.apache.commons.compress.archivers.zip.UnsupportedZipFeatureException.Feature;

import com.google.common.collect.Lists;

import fracture.mod.init.BlockInit;
//import fracture.mod.planets.hollows.planetone_s1.biome.PlanetOneS1Biomes;
import fracture.mod.planets.thefracture.thefracture.biome.TheFractureBiomes;
import fracture.mod.planets.thefracture.thefracture.biome.gen.feature.WorldGenTheFractureburns;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeTheFractureMountains extends TheFractureBiomes {

	public BiomeTheFractureMountains(BiomeProperties properties) {
		super(properties);
        this.topBlock = BlockInit.SURFACE_FRACTURE.getDefaultState(); 
        this.fillerBlock = BlockInit.STONE_FRACTURE.getDefaultState(); 
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
	}
	
	
	
	
	
	
	
	
	// @Override
//	    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int chunkX, int chunkZ, double noiseVal)
//	    {
//	        this.topBlock = BlockInit.SURFACE_FRACTURE.getDefaultState();
//	        this.fillerBlock = BlockInit.STONE_FRACTURE.getDefaultState();

//	        if (noiseVal > 1.0D)
//	        {
	//            this.topBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState();
	//            this.fillerBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState();
	//        }
	//        super.genTerrainBlocks(world, rand, primer, chunkX, chunkZ, noiseVal);
	//    }
	
	 @Override
	    public void decorate(World world, Random rand, BlockPos pos)
	    {
	        if (rand.nextInt(25) == 0)
	        {
	            WorldGenTheFractureburns worldGenTheFractureburns = new WorldGenTheFractureburns(0);
				worldGenTheFractureburns.generate(world, rand, pos);
	        }
	        if (rand.nextInt(64) == 0)
	        {
	            //BiomeNibiru.FOSSILS.generate(world, rand, pos);
	        }
	        super.decorate(world, rand, pos);
	    }
	
	

	@Override
	public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
		return Lists.<Biome.SpawnListEntry> newArrayList();
	}

	@Override
	public void registerTypes(Biome b) {
            BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.OCEAN);
	}
}
