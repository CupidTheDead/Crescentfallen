package fracture.mod.planets.thefracture.thefracture.biome.gen.feature;

import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenTheFractureburns extends WorldGenerator {

    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        int x = pos.getX();
        int z = pos.getZ();
        int y = rand.nextInt(40) + 10; // Random underground Y level between 10–50

        // Spawn two blocks of crude oil underground
        for (int i = 0; i < 2; i++) {
            int dx = x + rand.nextInt(8) - 4;
            int dz = z + rand.nextInt(8) - 4;
            int dy = y + rand.nextInt(4) - 2;

            BlockPos oilPos = new BlockPos(dx, dy, dz);

            if (world.isAirBlock(oilPos)) {
                world.setBlockState(oilPos, GCBlocks.crudeOil.getDefaultState(), 2);
            }
        }

        return true;
    }


//@Override
//public void decorate(World worldIn, Random rand, BlockPos pos) {
//    super.decorate(worldIn, rand, pos);
//
//    // Random chance to spawn oil pockets
//    if (rand.nextInt(4) == 0) { // 25% chance per chunk
//        BlockPos genPos = pos.add(rand.nextInt(16), 0, rand.nextInt(16));
//        new WorldGenTheFractureburns().generate(worldIn, rand, genPos);
//    }
	
}




