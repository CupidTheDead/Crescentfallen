package fracture.mod.world.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import fracture.mod.init.BlockInit; // adjust if your BlockInit package/name differs

/**
 * Simple generator that places a small cluster of ALPHA_ROSE blocks.
 * Note: this is currently broken. fix later.
 */
public class AlphaRoseGenerator extends WorldGenerator {

    // minimum and maximum size of a single patch
    private final int minPerPatch;
    private final int maxPerPatch;

    public AlphaRoseGenerator(int minPerPatch, int maxPerPatch) {
        this.minPerPatch = Math.max(1, minPerPatch);
        this.maxPerPatch = Math.max(this.minPerPatch, maxPerPatch);
    }

    public AlphaRoseGenerator() {
        this(2, 9); // define flowers per patch 
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (world.isRemote) return false;

        //attempt to place N flowers around pos (pos should be roughly top-of-chunk area)
        final int count = this.minPerPatch + rand.nextInt(this.maxPerPatch - this.minPerPatch + 1);
        boolean placedAny = false;

        for (int i = 0; i < count; i++) {
            // scatter around pos within a small radius
            int dx = rand.nextInt(5) - rand.nextInt(5); // -4..4 biased
            int dz = rand.nextInt(5) - rand.nextInt(5);
            int x = pos.getX() + dx;
            int z = pos.getZ() + dz;

            // find a sensible Y for surface: top solid or liquid block, then up one for placement
            int surfaceY = world.getHeight(x, z); // returns height 
            BlockPos spawn = new BlockPos(x, surfaceY, z);

            // place on top of the ground 
            BlockPos below = spawn.down();

            // If spawn is not air, skip
            if (!world.isAirBlock(spawn)) continue;

            IBlockState belowState = world.getBlockState(below);
            // Only allow on typical soil surfaces (grass/dirt/mycelium) — you can add more if desired
            boolean soilOk = belowState.getBlock() == Blocks.GRASS ||
                             belowState.getBlock() == Blocks.DIRT ||
                             belowState.getBlock() == Blocks.MYCELIUM;

            // require darkness (scrapped)
            int light = world.getLight(spawn);
            boolean darkEnough = light <= 15;

            if (soilOk && darkEnough) {
                // set the block
                world.setBlockState(spawn, BlockInit.ALPHA_ROSE.getDefaultState(), 2);
                placedAny = true;
            }
        }

        return placedAny;
    }
}