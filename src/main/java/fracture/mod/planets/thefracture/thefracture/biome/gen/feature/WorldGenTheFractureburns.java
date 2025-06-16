package fracture.mod.planets.thefracture.thefracture.biome.gen.feature;

import java.util.Random;

import fracture.mod.init.BlockInit;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;



//!!!CATION: THIS IS A TESTING PEICE OF CODE ONLY!!!!! REMEMBER TO REWROGHT THIS!!!!!


public class WorldGenTheFractureburns extends WorldGenerator
{
    private final int numberOfBlocks;

    public WorldGenTheFractureburns(int num)
    {
        this.numberOfBlocks = num;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() != GCBlocks.crudeOil)
        {
            return false;
        }
        else
        {
            int i = rand.nextInt(this.numberOfBlocks - 2) + 2;
            int j = 1;

            for (int k = pos.getX() - i; k <= pos.getX() + i; ++k)
            {
                for (int l = pos.getZ() - i; l <= pos.getZ() + i; ++l)
                {
                    int i1 = k - pos.getX();
                    int j1 = l - pos.getZ();

                    if (i1 * i1 + j1 * j1 <= i * i)
                    {
                        for (int k1 = pos.getY() - j; k1 <= pos.getY() + j; ++k1)
                        {
                            BlockPos blockpos = new BlockPos(k, k1, l);
                            Block block = world.getBlockState(blockpos).getBlock();

                            if (block == BlockInit.STONE_FRACTURE || block == BlockInit.BURNT_STONE_FRACTURE)
                            {
                                world.setBlockState(blockpos, BlockInit.BURNT_STONE_FRACTURE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}