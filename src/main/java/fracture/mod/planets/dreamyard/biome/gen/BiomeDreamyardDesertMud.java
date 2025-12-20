package fracture.mod.planets.dreamyard.biome.gen;
import java.util.List;

import com.google.common.collect.Lists;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.dreamyard.biome.DreamyardBiomes;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeDreamyardDesertMud extends DreamyardBiomes {



	public BiomeDreamyardDesertMud(BiomeProperties properties) {
		super(properties);
		this.topBlock = BlockInit.DREAM_SAND.getDefaultState(); 
		this.fillerBlock = BlockInit.DREAM_SAND.getDefaultState();
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
	}

	@Override
	public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
		return Lists.<Biome.SpawnListEntry>newArrayList();
	}

	@Override
	public void registerTypes(Biome b) {
		BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD,
				BiomeDictionary.Type.SAVANNA);
	}
}