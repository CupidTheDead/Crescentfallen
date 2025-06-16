package fracture.mod.planets.thefracture.thefracture.biome.gen;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.hollows.hollows.biome.HollowsBiomes;
import fracture.mod.planets.thefracture.thefracture.biome.TheFractureBiomes;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenFossils;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeTheFracturePiles extends TheFractureBiomes {
	
	//protected static final WorldGenAbstractTree TREE = new 
//@Override
	public BiomeTheFracturePiles(BiomeProperties properties) {
		super(properties);
        topBlock = BlockInit.DRIED_DIRT.getDefaultState();
        fillerBlock = BlockInit.DRIED_DIRT.getDefaultState(); 
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.add(new SpawnListEntry(EntityEvolvedZombie.class, 10, 1, 5));
        
        //this.decorator.coalGen = new WorldGenMinable(WATER, getWaterColor())
	}
	
	// @Override
	  //  public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int chunkX, int chunkZ, double noiseVal)
	   // {
	      //  this.topBlock = BlockInit.SURFACE_FRACTURE.getDefaultState();
	      //  this.fillerBlock = BlockInit.STONE_FRACTURE.getDefaultState();

	     //   if (noiseVal > 1.0D)
	     //   {
	         //   this.topBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState();
	         //   this.fillerBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState();
	      //  }
	      //  super.genTerrainBlocks(world, rand, primer, chunkX, chunkZ, noiseVal);
	//    }

	@Override
	public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
		return Lists.<Biome.SpawnListEntry> newArrayList();
	}

	@Override
	public void registerTypes(Biome b) {
            BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.WET, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.OCEAN);
	}
}