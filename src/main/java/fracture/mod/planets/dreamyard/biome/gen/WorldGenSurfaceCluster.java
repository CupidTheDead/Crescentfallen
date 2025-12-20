package fracture.mod.planets.dreamyard.biome.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

/**
 * Generates small surface clusters of a given block, replacing top solid blocks.
 */
public class WorldGenSurfaceCluster extends WorldGenerator {

    private final IBlockState blockToPlace;
    private final int clusterSize;

    public WorldGenSurfaceCluster(IBlockState blockToPlace, int clusterSize) {
        this.blockToPlace = blockToPlace;
        this.clusterSize = clusterSize;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        // find the topmost solid block at this position
        BlockPos top = world.getHeight(pos);

        for (int i = 0; i < clusterSize; i++) {
            int dx = rand.nextInt(4) - rand.nextInt(4);
            int dz = rand.nextInt(4) - rand.nextInt(4);
            BlockPos target = top.add(dx, 0, dz);

            if (world.isAirBlock(target.up()) && world.getBlockState(target).isOpaqueCube()) {
                world.setBlockState(target.up(), blockToPlace, 2);
            }
        }
        return true;
    }
}
