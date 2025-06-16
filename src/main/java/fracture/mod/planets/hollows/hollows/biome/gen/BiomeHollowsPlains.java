package fracture.mod.planets.hollows.hollows.biome.gen;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.hollows.hollows.biome.HollowsBiomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeHollowsPlains extends HollowsBiomes {

	public BiomeHollowsPlains(BiomeProperties properties) {
		super(properties);
        this.topBlock = BlockInit.SURFACE_KONA.getDefaultState(); 
        this.fillerBlock = BlockInit.STONE_SUBSURFACE_KONA.getDefaultState(); 
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
	}

	@Override
	public void registerTypes(Biome b) {
            BiomeDictionary.addTypes(b, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);

	}
}