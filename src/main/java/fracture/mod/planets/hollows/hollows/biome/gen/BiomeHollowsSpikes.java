package fracture.mod.planets.hollows.hollows.biome.gen;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.hollows.hollows.biome.HollowsBiomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeHollowsSpikes extends HollowsBiomes {

	public BiomeHollowsSpikes(BiomeProperties properties) {
		super(properties);
        this.topBlock = BlockInit.SURFACE_HOLLOWS.getDefaultState(); 
        this.fillerBlock = BlockInit.STONE_HOLLOWS.getDefaultState(); 
        
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        //this.setTemperatureRainfall(0.0F, 0.5F); // 0.0F = very cold, triggers snowfall
        //this.enableSnow = true; 
        
        
	}

	@Override
	public void registerTypes(Biome b) {
	    BiomeDictionary.addTypes(b, BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.WASTELAND);

	}
}