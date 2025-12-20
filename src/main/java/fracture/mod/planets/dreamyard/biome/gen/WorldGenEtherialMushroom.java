package fracture.mod.planets.dreamyard.biome.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenEtherialMushroom extends WorldGenerator {

    private final IBlockState mushroom;
    private final IBlockState ground;

    public WorldGenEtherialMushroom(IBlockState mushroom, IBlockState ground) {
        this.mushroom = mushroom;
        this.ground = ground;
    }

    @Override
    public boolean generate(World world, Random random, BlockPos position) {
        for (int i = 0; i < 5; ++i) {
            // Random nearby position
            BlockPos newPos = position.add(
                    random.nextInt(8) - random.nextInt(8),
                    random.nextInt(4) - random.nextInt(4),
                    random.nextInt(8) - random.nextInt(8)
            );

            // Only generate if air and the block below is DREAMSTONE
            if (world.isAirBlock(newPos) &&
                world.getBlockState(newPos.down()).equals(ground)) {

                world.setBlockState(newPos, mushroom, 2);
            }
        }
        return true;
    }
}