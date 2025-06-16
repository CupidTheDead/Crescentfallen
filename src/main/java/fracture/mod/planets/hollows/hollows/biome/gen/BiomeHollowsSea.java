package fracture.mod.planets.hollows.hollows.biome.gen;

import java.util.List;

import com.google.common.collect.Lists;

import fracture.mod.planets.hollows.hollows.biome.HollowsBiomes;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeHollowsSea extends HollowsBiomes {

	public BiomeHollowsSea(BiomeProperties properties) {
		super(properties);
        this.topBlock = Blocks.MYCELIUM.getDefaultState(); //TODO change this
        this.fillerBlock = Blocks.OBSIDIAN.getDefaultState(); //TODO change this
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