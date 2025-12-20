package fracture.mod.planets.dreamyard.biome.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import fracture.mod.init.BlockInit;
import net.minecraft.init.Blocks;

/**
 * Places small clusters of a mushroom block on top of BlockInit.DREAMSTONE
 * in low-light underground positions (cave-like).
 */
public class DreamyardCaveMushroomGenerator extends WorldGenerator {

    private final IBlockState mushroomState;
    private final int minPerPatch;
    private final int maxPerPatch;

    /**
     * @param mushroomState the mushroom block state to place
     * @param minPerPatch minimum mushrooms in a patch
     * @param maxPerPatch maximum mushrooms in a patch
     */
    public DreamyardCaveMushroomGenerator(IBlockState mushroomState, int minPerPatch, int maxPerPatch) {
        this.mushroomState = mushroomState;
        this.minPerPatch = Math.max(1, minPerPatch);
        this.maxPerPatch = Math.max(this.minPerPatch, maxPerPatch);
    }

    public DreamyardCaveMushroomGenerator(IBlockState mushroomState) {
        this(mushroomState, 2, 6);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (world.isRemote) return false;

        // select a target Y somewhere underground near the pos
        // generation in Y range
        final int minY = 5;
        final int maxY = 70;

        int attempts = 1 + rand.nextInt(2); // 1-2 sub-patches from this call
        boolean placedAny = false;

        for (int a = 0; a < attempts; a++) {
            int rx = pos.getX() + rand.nextInt(16);
            int rz = pos.getZ() + rand.nextInt(16);
            int ry = minY + rand.nextInt(Math.max(1, maxY - minY + 1));
            BlockPos center = new BlockPos(rx, ry, rz);

            // Find nearby air space above a dreamstone floor:
            // scan down/up a few blocks to find a valid spot
            BlockPos spawn = null;
            for (int dy = 0; dy < 16; dy++) {
                BlockPos candidate = center.up(dy - 8); // search -8..+7
                if (!world.isAirBlock(candidate)) continue;
                // block below must be Dreamstone
                if (world.getBlockState(candidate.down()).getBlock() == BlockInit.DREAMSTONE) {
                    // low light requirement
                    if (world.getLight(candidate) <= 7) {
                        spawn = candidate;
                        break;
                    }
                }
            }
            if (spawn == null) continue;

            // make a small patch of mushrooms around spawn
            int count = this.minPerPatch + rand.nextInt(this.maxPerPatch - this.minPerPatch + 1);
            for (int i = 0; i < count; i++) {
                int dx = rand.nextInt(3) - rand.nextInt(3); // -2..2 bias
                int dz = rand.nextInt(3) - rand.nextInt(3);
                int dy = rand.nextInt(2) - rand.nextInt(2); // small vertical offset
                BlockPos target = spawn.add(dx, dy, dz);

                if (!world.isAirBlock(target)) continue;
                if (world.getBlockState(target.down()).getBlock() != BlockInit.DREAMSTONE) continue;
                if (world.getLight(target) > 7) continue;

                world.setBlockState(target, this.mushroomState, 2);
                placedAny = true;
            }
        }

        return placedAny;
    }
}
