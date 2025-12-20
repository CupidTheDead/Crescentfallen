package fracture.mod.planets.dreamyard.biome.gen;

import fracture.mod.init.CFplanets;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeAdaptive;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerDreamyardBiomes extends GenLayer {

    private static final Biome[] biomes = BiomeAdaptive.getBiomesListFor(CFplanets.dreamyard).toArray(new Biome[0]);

    public GenLayerDreamyardBiomes(long l, GenLayer parent) {
        super(l);
        this.parent = parent;
    }

    public GenLayerDreamyardBiomes(long l) {
        super(l);
    }

    @Override
    public int[] getInts(int x, int z, int width, int depth) {
        int[] dest = IntCache.getIntCache(width * depth);

        for (int dz = 0; dz < depth; ++dz) {
            for (int dx = 0; dx < width; ++dx) {
                initChunkSeed(x + dx, z + dz);

                // small chance for a lake biome (makes them rarer / smaller)
                int roll = nextInt(100);
                if (roll < 6) { // ~6% chance -> tweak lower for rarer lakes
                    // find the lake biome in biomes[] (by class/name). We assume the lake biome is registered and returned by BiomeAdaptive
                    // fallback: just pick a random biome (rare) from the array (we'll pick the Lakes one by detecting a name match)
                    Biome chosen = null;
                    for (Biome b : biomes) {
                        if (b != null && b.getBiomeName() != null && b.getBiomeName().toLowerCase().contains("lake")) {
                            chosen = b;
                            break;
                        }
                    }
                    if (chosen == null) chosen = biomes[nextInt(biomes.length)];
                    dest[dx + dz * width] = Biome.getIdForBiome(chosen);
                } else {
                    // choose non-lake biome normally
                    // optionally weight the other biomes more; here choose uniformly from the array
                    Biome chosen = biomes[nextInt(biomes.length)];
                    // If we accidentally chose a lake biome from the random pick, re-roll to keep lakes rare
                    if (chosen.getBiomeName() != null && chosen.getBiomeName().toLowerCase().contains("lake")) {
                        // pick another (simple re-roll)
                        chosen = biomes[nextInt(biomes.length)];
                    }
                    dest[dx + dz * width] = Biome.getIdForBiome(chosen);
                }
            }
        }

        return dest;
    }
}