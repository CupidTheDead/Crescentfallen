package fracture.mod.planets.dreamyard.biome.gen;

import fracture.mod.planets.thefracture.thefracture.biome.gen.GenLayerTheFractureBiomes;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;


public abstract class GenLayerDreamyard extends GenLayer {

	public GenLayerDreamyard(long l) {
		super(l);
	}

	public static GenLayer[] createWorld(long l) {
		GenLayer biomes = new GenLayerDreamyardBiomes(l);
		biomes = new GenLayerZoom(1000L, biomes);
		biomes = new GenLayerZoom(1001L, biomes);
		biomes = new GenLayerZoom(1002L, biomes);
		biomes = new GenLayerZoom(1003L, biomes);
		biomes = new GenLayerZoom(1004L, biomes);
		biomes = new GenLayerZoom(1005L, biomes);
		GenLayer genLayerVeronoiZoom = new GenLayerVoronoiZoom(100L, biomes);
		biomes.initWorldGenSeed(l);
		genLayerVeronoiZoom.initWorldGenSeed(l);

		return new GenLayer[] { biomes, genLayerVeronoiZoom };
	}
}
