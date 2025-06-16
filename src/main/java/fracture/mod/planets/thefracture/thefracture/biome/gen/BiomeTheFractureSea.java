package fracture.mod.planets.thefracture.thefracture.biome.gen;

import java.util.List;

import com.google.common.collect.Lists;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.thefracture.thefracture.biome.TheFractureBiomes;
//import fracture.mod.planets.hollows.planetone_s1.biome.PlanetOneS1Biomes;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeTheFractureSea extends TheFractureBiomes {

	public BiomeTheFractureSea(BiomeProperties properties) {
		super(properties);
		

        this.topBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState(); //TODO change this
        this.fillerBlock = BlockInit.BURNT_STONE_FRACTURE.getDefaultState(); //TODO change this
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
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