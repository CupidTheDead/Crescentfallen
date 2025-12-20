package fracture.mod.planets.dreamyard.biome;

import fracture.mod.init.BlockInit;
import fracture.mod.planets.dreamyard.biome.gen.DreamyardCaveMushroomGenerator;
import fracture.mod.planets.dreamyard.biome.gen.WorldGenEtherialMushroom;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

import java.util.Random;




////ALRIGHT SO 
//THERE ARE TWO CLASSES 
//
//BiomeDecoratorSpacedreamyard AND BiomeDecoratorDreamyard
//
//HALF OF THE THINGS IN EACH CLASS ARE NON FUCTIONAL
//THE FLOWER GENERATION THAT WORKS IS CONTAINED IN BiomeDecoratorDreamyard, AND IS THE NEWER ONE
//ITS DESIGNATION IS fracture.mod.world.gen;
//THE OLDER ONES DESIGNATION IS fracture.mod.planets.dreamyard.biome;




public class BiomeDecoratorSpacedreamyard extends BiomeDecoratorSpace {



    private World currentWorld;
    private Random rand;


    // Example Dreamyard plant blocks (replace with your actual blocks from BlockInit or similar)
    public static final Block BLOODFLOWER = BlockInit.BLOODFLOWER;
    public static final Block RED_FLOWERS = BlockInit.RED_FLOWERS;
    public static final Block BLUE_FLOWERS = BlockInit.BLUE_FLOWERS;
    public static final Block YELLOW_FLOWERS = BlockInit.YELLOW_FLOWERS;
    public static final Block DREAMYARD_LOTUS = BlockInit.DREAMYARD_LOTUS;
    public static final Block ETHERIAL_MUSHROOM_BLUE = BlockInit.ETHERIAL_MUSHROOM_BLUE;
    public static final Block ETHERIAL_MUSHROOM_GREEN = BlockInit.ETHERIAL_MUSHROOM_GREEN;
    public static final Block ETHERIAL_MUSHROOM_PURPLE = BlockInit.ETHERIAL_MUSHROOM_PURPLE;
    public static final Block GUILDED_ALLIUM = BlockInit.GUILDED_ALLIUM;
    public static final Block HYACINTH = BlockInit.HYACINTH;
    public static final Block KING_ALLARIUS = BlockInit.KING_ALLARIUS;
    public static final Block MINIGRASS1 = BlockInit.MINIGRASS1;
    public static final Block MINIGRASS2 = BlockInit.MINIGRASS2;
    public static final Block PAEONIA = BlockInit.PAEONIA;
    public static final Block RAINBOW_ROD = BlockInit.RAINBOW_ROD;
    public static final Block BIRDS_OF_PARADISE = BlockInit.BIRDS_OF_PARADISE;
    public static final Block DREAMYARD_TALLGRASS = BlockInit.DREAMYARD_TALLGRASS;
    public static final Block ALIEN_WEEDS = BlockInit.ALIEN_WEEDS;

    
    public BiomeDecoratorSpacedreamyard() {
        super();
        //IBlockState base = BlockInit.DREAMSTONE.getDefaultState();
        //genBlue = new DreamyardCaveMushroomGenerator(BlockInit.ETHERIAL_MUSHROOM_BLUE.getDefaultState(), base);
        //genGreen = new DreamyardCaveMushroomGenerator(BlockInit.ETHERIAL_MUSHROOM_GREEN.getDefaultState(), base);
        // = new DreamyardCaveMushroomGenerator(BlockInit.ETHERIAL_MUSHROOM_PURPLE.getDefaultState(), base);
    }
   
        // Initialize generators (WorldGenEtherialMushroom should exist in the package indicated)


    
    
    

    @Override
    protected void setCurrentWorld(World world) {
        this.currentWorld = world;
    }

    @Override
    protected World getCurrentWorld() {
        return this.currentWorld;
    }

    @Override
    protected void decorate() {
        this.rand = this.rand == null ? new Random() : this.rand;

        //decorates the current chunk with multiple plant types.
        // The chunk start position:
        BlockPos chunkPos = new BlockPos(this.posX * 16, 0, this.posZ * 16);

        // For example, try generating 10 random plants per chunk
        for (int i = 0; i < 10; i++) {
            BlockPos pos = chunkPos.add(rand.nextInt(16), 60 + rand.nextInt(30), rand.nextInt(16));
            placePlantAtRandom(pos);
        }
    }

    private void placePlantAtRandom(BlockPos pos) {
        Block plant = getRandomPlant(rand);
        if (plant != null && canPlaceAt(pos)) {
            // Place the block only if the spot is replaceable and the block below is solid ground
            currentWorld.setBlockState(pos, plant.getDefaultState(), 2);
        }
    }

    private boolean canPlaceAt(BlockPos pos) {
        // Check if the block at pos is air and block below is solid ground
        return currentWorld.isAirBlock(pos) &&
                currentWorld.getBlockState(pos.down()).getMaterial() == Material.GRASS;
    }

    private Block getRandomPlant(Random rand) {
        // Return a random plant from the Dreamyard plants
        int choice = rand.nextInt(18);
        switch (choice) {
            case 0: return BLOODFLOWER;
            case 1: return RED_FLOWERS;
            case 2: return BLUE_FLOWERS;
            case 3: return YELLOW_FLOWERS;
            case 4: return DREAMYARD_LOTUS;
            case 5: return ETHERIAL_MUSHROOM_BLUE;
            case 6: return ETHERIAL_MUSHROOM_GREEN;
            case 7: return ETHERIAL_MUSHROOM_PURPLE;
            case 8: return GUILDED_ALLIUM;
            case 9: return HYACINTH;
            case 10: return KING_ALLARIUS;
            case 11: return MINIGRASS1;
            case 12: return MINIGRASS2;
            case 13: return PAEONIA;
            case 14: return RAINBOW_ROD;
            case 15: return BIRDS_OF_PARADISE;
            case 16: return DREAMYARD_TALLGRASS;
            case 17: return ALIEN_WEEDS;
            default: return null;
        }
    } 

       

        }
    
