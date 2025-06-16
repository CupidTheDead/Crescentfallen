package fracture.mod.planets.thefracture.thefracture.biome.gen;

import java.util.List;
import java.util.Random;

import fracture.mod.init.BlockInit;
//import fracture.mod.planets.hollows.planetone_s1.biome.PlanetOneS1Biomes;
import fracture.mod.planets.thefracture.thefracture.biome.TheFractureBiomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeTheFracturePlains extends TheFractureBiomes {

	public BiomeTheFracturePlains(BiomeProperties properties) {
		super(properties);
		//((List<SpawnListEntry>) this.topBlock).clear();
		//((List<SpawnListEntry>) this.fillerBlock).clear();
        this.topBlock = BlockInit.SURFACE_FRACTURE.getDefaultState(); 
        this.fillerBlock = BlockInit.STONE_FRACTURE.getDefaultState(); 
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
	}
	
	// @Override
	//    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int chunkX, int chunkZ, double noiseVal)
	 //   {
	   //     this.topBlock = BlockInit.SURFACE_FRACTURE.getDefaultState();
	   //     this.fillerBlock = BlockInit.STONE_FRACTURE.getDefaultState();

	      //  if (noiseVal > 1.0D)
	    //    {
	     //       this.topBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState();
	     //       this.fillerBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState();
	     //   }
	     //   super.genTerrainBlocks(world, rand, primer, chunkX, chunkZ, noiseVal);
	  //  }

	@Override
	public void registerTypes(Biome b) {
            BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);

	}
}