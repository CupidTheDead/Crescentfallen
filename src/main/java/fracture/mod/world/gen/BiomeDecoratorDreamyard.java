package fracture.mod.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.dreamyard.biome.gen.BiomeDreamyardDesertMud;
import fracture.mod.planets.dreamyard.biome.gen.WorldGenEtherialMushroom;
import fracture.mod.planets.dreamyard.biome.gen.WorldGenSurfaceCluster;
import fracture.mod.world.biome.BiomeDecoratorExoBase;

////ALRIGHT SO 
//THERE ARE TWO CLASSES 
//
//BiomeDecoratorSpacedreamyard AND BiomeDecoratorDreamyard
//
//HALF OF THE THINGS IN EACH CLASS ARE NON FUCTIONAL
//THE FLOWER GENERATION THAT WORKS IS CONTAINED IN BiomeDecoratorDreamyard, AND IS THE NEWER ONE
//ITS DESIGNATION IS fracture.mod.world.gen;
//THE OLDER ONES DESIGNATION IS fracture.mod.planets.dreamyard.biome;

public class BiomeDecoratorDreamyard extends BiomeDecoratorExoBase {
	
	   private final WorldGenerator etherialMushroomGenBlue;
	    private final WorldGenerator etherialMushroomGenGreen;
	    private final WorldGenerator etherialMushroomGenPurple;
	    
        IBlockState baseDreamstone = BlockInit.DREAMSTONE.getDefaultState();
        {
        this.etherialMushroomGenBlue   = new WorldGenEtherialMushroom(BlockInit.ETHERIAL_MUSHROOM_BLUE.getDefaultState(), baseDreamstone);
        this.etherialMushroomGenGreen  = new WorldGenEtherialMushroom(BlockInit.ETHERIAL_MUSHROOM_GREEN.getDefaultState(), baseDreamstone);
        this.etherialMushroomGenPurple = new WorldGenEtherialMushroom(BlockInit.ETHERIAL_MUSHROOM_PURPLE.getDefaultState(), baseDreamstone);
        }

	
    public int grassPerChunk = 10;
    public int flowersPerChunk = 2;
    public int deadBushPerChunk = 1;
    public int treesPerChunk = 2;
    public float extraTreeChance = 0.1F;

    // Tunables (adjust as desired)
    public double specialFlowerChancePerChunk = 0.30D; // each special flower type chance to attempt a patch
    public int specialPatchMin = 3;  // patch sizes 3..9
    public int specialPatchMax = 9;

    public double alienWeedsChancePerChunk = 0.10D; // smaller chance for alien weeds
    public int alienWeedsPatchMin = 2;
    public int alienWeedsPatchMax = 6;

    public int dreamyardGrassPatchMin = 4; // patch size for DREAMYARD_GRASS
    public int dreamyardGrassPatchMax = 10;
    public double dreamyardGrassChancePerChunk = 0.6D; // chance per attempt (tweak)

    @Override
    protected void generate(Biome biome, World world, Random rand) {
        // original random small flowers
        this.generateDreamyardFlowers(world, rand, this.chunkPos);

        // DREAMYARD_GRASS patches on OVERGRASS_BLOCK
        this.generateDreamyardGrass(world, rand, this.chunkPos);

        // special large-flower patches (each type independently)
        this.generateSpecialFlowerPatches(world, rand, this.chunkPos);

        // small chance alien weeds
        this.generateAlienWeeds(world, rand, this.chunkPos);
        
        
        // chunkPos is provided by the parent BiomeDecorator. Defensive fallback below.
        BlockPos chunkOrigin = this.chunkPos;
        if (chunkOrigin == null) {
            // defensive: shouldn't normally happen, but prevents NPE during compile-time testing
            chunkOrigin = new BlockPos(0, 64, 0);
        }

        // Try a handful of attempts per chunk to place small cave mushroom patches
        final int attempts = 10;
        for (int i = 0; i < attempts; i++) {
            // pick a random spot within/near the chunk
            BlockPos pos = chunkOrigin.add(rand.nextInt(16) + 8, rand.nextInt(56) + 5, rand.nextInt(16) + 8);

            // randomly decide which generators to try (tweak probabilities as desired)
            if (rand.nextFloat() < 0.60F) this.etherialMushroomGenBlue.generate(world, rand, pos);
            if (rand.nextFloat() < 0.50F) this.etherialMushroomGenGreen.generate(world, rand, pos);
            if (rand.nextFloat() < 0.40F) this.etherialMushroomGenPurple.generate(world, rand, pos);
        }
        
     // Occasionally spawn Dreamyard stonebrick clusters in the desert mud biome
        if (biome instanceof BiomeDreamyardDesertMud) {
            if (rand.nextInt(6) == 0) { // ~16% chance per chunk
                WorldGenSurfaceCluster clusterGen = new WorldGenSurfaceCluster(
                    BlockInit.STONEBRICK_DREAMYARD_SMALL.getDefaultState(),
                    4 + rand.nextInt(3) // cluster size 4–6
                );

                BlockPos pos = this.chunkPos.add(rand.nextInt(16) + 8, 0, rand.nextInt(16) + 8);
                clusterGen.generate(world, rand, pos);
            }
        }
        
    }

    private void generateDreamyardFlowers(World world, Random rand, BlockPos chunkPos) {
        Block[] flowers = new Block[] {
            BlockInit.BLOODFLOWER,
            BlockInit.RED_FLOWERS,
            BlockInit.BLUE_FLOWERS,
            BlockInit.YELLOW_FLOWERS,
            BlockInit.DREAMYARD_LOTUS,
            BlockInit.ETHERIAL_MUSHROOM_BLUE,
            BlockInit.ETHERIAL_MUSHROOM_GREEN,
            BlockInit.ETHERIAL_MUSHROOM_PURPLE,
            BlockInit.GUILDED_ALLIUM,
            BlockInit.HYACINTH,
            BlockInit.KING_ALLARIUS,
            BlockInit.MINIGRASS1,
            BlockInit.MINIGRASS2,
            BlockInit.PAEONIA,
            BlockInit.RAINBOW_ROD,
            BlockInit.ALIEN_WEEDS,
            BlockInit.ALPHA_ROSE
        };

        for (int i = 0; i < this.flowersPerChunk; i++) {
            Block flower = flowers[rand.nextInt(flowers.length)];
            IBlockState flowerState = flower.getDefaultState();

            int x = chunkPos.getX() + rand.nextInt(16);
            int z = chunkPos.getZ() + rand.nextInt(16);

            // safe surface Y computation
            int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
            BlockPos pos = new BlockPos(x, y, z);

            IBlockState below = world.getBlockState(pos.down());
            if (world.isAirBlock(pos) &&
                (below.getBlock() == BlockInit.DREAMYARD_GRASS || below.getBlock() == BlockInit.OVERGRASS_BLOCK)) {
                world.setBlockState(pos, flowerState, 2);
            }
        }
    }

    /**
     * Place patches of DREAMYARD_GRASS on top of OVERGRASS_BLOCK.
     */
    private void generateDreamyardGrass(World world, Random rand, BlockPos chunkPos) {
        int attempts = this.grassPerChunk;
        for (int a = 0; a < attempts; a++) {
            if (rand.nextDouble() > this.dreamyardGrassChancePerChunk) continue;

            int cx = chunkPos.getX() + rand.nextInt(16);
            int cz = chunkPos.getZ() + rand.nextInt(16);

            int cy = world.getTopSolidOrLiquidBlock(new BlockPos(cx, 0, cz)).getY();
            BlockPos center = new BlockPos(cx, cy, cz);

            int patchSize = dreamyardGrassPatchMin + rand.nextInt(dreamyardGrassPatchMax - dreamyardGrassPatchMin + 1);
            placePatchOnGround(world, rand, center, BlockInit.DREAMYARD_GRASS, BlockInit.OVERGRASS_BLOCK, patchSize, 4);
        }
    }

    /**
     * For each special flower type (BLOODFLOWER, RED_FLOWERS, BLUE_FLOWERS, YELLOW_FLOWERS,
     * DREAMYARD_LOTUS, RAINBOW_ROD), attempt to spawn a patch of 3-9 of that flower on OVERGRASS_BLOCK.
     */
    private void generateSpecialFlowerPatches(World world, Random rand, BlockPos chunkPos) {
        Block[] specialFlowers = new Block[] {
            BlockInit.BLOODFLOWER,
            BlockInit.RED_FLOWERS,
            BlockInit.BLUE_FLOWERS,
            BlockInit.YELLOW_FLOWERS,
            BlockInit.DREAMYARD_LOTUS,
            BlockInit.RAINBOW_ROD
        };

        for (Block flower : specialFlowers) {
            if (rand.nextDouble() > this.specialFlowerChancePerChunk) continue;

            int cx = chunkPos.getX() + rand.nextInt(16);
            int cz = chunkPos.getZ() + rand.nextInt(16);

            int cy = world.getTopSolidOrLiquidBlock(new BlockPos(cx, 0, cz)).getY();
            BlockPos center = new BlockPos(cx, cy, cz);

            int patchSize = specialPatchMin + rand.nextInt(specialPatchMax - specialPatchMin + 1);
            placePatchOnGround(world, rand, center, flower, BlockInit.OVERGRASS_BLOCK, patchSize, 4);
        }
    }

    /**
     * Scatter ALIEN_WEEDS with a smaller chance on OVERGRASS_BLOCK.
     */
    private void generateAlienWeeds(World world, Random rand, BlockPos chunkPos) {
        if (rand.nextDouble() > this.alienWeedsChancePerChunk) return;

        int patches = 1 + rand.nextInt(2); // 1-2 small patches
        for (int p = 0; p < patches; p++) {
            int cx = chunkPos.getX() + rand.nextInt(16);
            int cz = chunkPos.getZ() + rand.nextInt(16);

            int cy = world.getTopSolidOrLiquidBlock(new BlockPos(cx, 0, cz)).getY();
            BlockPos center = new BlockPos(cx, cy, cz);

            int patchSize = alienWeedsPatchMin + rand.nextInt(alienWeedsPatchMax - alienWeedsPatchMin + 1);
            placePatchOnGround(world, rand, center, BlockInit.ALIEN_WEEDS, BlockInit.OVERGRASS_BLOCK, patchSize, 5);
        }
    }

    /**
     * Helper: place a small patch of `blockToPlace` around `center`.
     * Only places where the block below equals groundBlock and the position is air.
     *
     * @param world the world
     * @param rand random
     * @param center center position (x,z used; y will be recalculated per position)
     * @param blockToPlace block to set
     * @param groundBlock required block below placement
     * @param count number of blocks to place
     * @param radius maximum horizontal scatter from center
     */
    private void placePatchOnGround(World world, Random rand, BlockPos center, Block blockToPlace, Block groundBlock, int count, int radius) {
        for (int i = 0; i < count; i++) {
            int dx = rand.nextInt(radius * 2 + 1) - radius;
            int dz = rand.nextInt(radius * 2 + 1) - radius;
            int x = center.getX() + dx;
            int z = center.getZ() + dz;

            int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();
            BlockPos pos = new BlockPos(x, y, z);

            // must be air and the block below must be the groundBlock
            if (!world.isAirBlock(pos)) continue;
            if (world.getBlockState(pos.down()).getBlock() != groundBlock) continue;

            world.setBlockState(pos, blockToPlace.getDefaultState(), 2);
        }
    }
}
