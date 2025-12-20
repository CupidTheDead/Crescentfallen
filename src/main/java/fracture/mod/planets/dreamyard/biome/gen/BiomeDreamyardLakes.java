package fracture.mod.planets.dreamyard.biome.gen;

import java.util.List;

import com.google.common.collect.Lists;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.dreamyard.biome.DreamyardBiomes;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeDreamyardLakes extends DreamyardBiomes {

    public BiomeDreamyardLakes(BiomeProperties properties) {
        super(properties);

        // We'll use water as the visible top (we'll fill low columns to sea level in genTerrainBlocks)
        this.topBlock = Blocks.WATER.getDefaultState();
        // The lakebed filler should be your gravel
        this.fillerBlock = BlockInit.GRAVEL_DREAMYARD.getDefaultState();

        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
    }

    @Override
    public List<Biome.SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        return Lists.<Biome.SpawnListEntry>newArrayList();
    }

    @Override
    public void registerTypes(Biome biome) {
        BiomeDictionary.addTypes(biome, BiomeDictionary.Type.WATER, BiomeDictionary.Type.SANDY,
                BiomeDictionary.Type.COLD);
    }

    /**
     * Ensure low columns become water up to sea level; make a compact gravel lakebed beneath.
     * This runs after the normal terrain generation for this biome (which we've biased low).
     */
    @Override
    public void genTerrainBlocks(World world, java.util.Random rand, ChunkPrimer chunk, int x, int z, double noise) {
        // First let the parent create the base terrain (keeping your existing behaviour)
        super.genTerrainBlocks(world, rand, chunk, x, z, noise);

        final int localX = x & 15;
        final int localZ = z & 15;
        final int seaLevel = 63; // change if your world uses a different sea level

        // Find top-most non-air block in this column (search the chunk)
        int topY = -1;
        for (int y = 255; y >= 0; y--) {
            if (chunk.getBlockState(localX, y, localZ).getMaterial() != net.minecraft.block.material.Material.AIR) {
                topY = y;
                break;
            }
        }

        if (topY == -1) return;

        // Only operate on columns lower than sea level
        if (topY < seaLevel) {
            // Settings you can tune:
            final int flatRadius = 3; // neighborhood radius in blocks (keeps changes local to this chunk)
            final int bedDepth = 4;   // how many blocks under top become gravel lakebed
            final int targetWaterY = seaLevel; // flat water level to fill up to

            // Clamp neighborhood to the current chunk (0..15)
            int startX = Math.max(0, localX - flatRadius);
            int endX = Math.min(15, localX + flatRadius);
            int startZ = Math.max(0, localZ - flatRadius);
            int endZ = Math.min(15, localZ + flatRadius);

            for (int lx = startX; lx <= endX; lx++) {
                for (int lz = startZ; lz <= endZ; lz++) {
                    // find column top for this neighbor column
                    int colTop = -1;
                    for (int y = 255; y >= 0; y--) {
                        if (chunk.getBlockState(lx, y, lz).getMaterial() != net.minecraft.block.material.Material.AIR) {
                            colTop = y;
                            break;
                        }
                    }
                    if (colTop == -1) continue;

                    // If the neighbor column is also below the target water level, fill it up to the same target height
                   // if (colTop < targetWaterY) {
                        // place water source blocks from just above the existing top up to targetWaterY
                        for (int y = colTop + 1; y <= targetWaterY; y++) {
                            chunk.setBlockState(lx, y, lz, net.minecraft.init.Blocks.WATER.getDefaultState());
                        }
                        // set lakebed below the top to your gravel
                        for (int d = 0; d < bedDepth; d++) {
                            int yy = colTop - d;
                            if (yy < 0) break;
                            chunk.setBlockState(lx, yy, lz, BlockInit.GRAVEL_DREAMYARD.getDefaultState());
                        }
                    }
                }
            }
        }
    }
