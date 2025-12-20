package fracture.mod.init;
import java.util.Random;

import fracture.mod.CFConfig;
import fracture.mod.CFMain;
import fracture.mod.planets.dreamyard.biome.DreamyardBiomes;
import fracture.mod.util.IHasModel;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class SpaceGDgrass extends BlockBush implements IHasModel {

	
//
//	@Override
//	public void generateWorld(Random random, int chunkX, int chunkZ, World world, int dimID, IChunkGenerator cg, IChunkProvider cp) {
//	    // Check for your custom dimension ID instead of overwriting later
//	    if (dimID != CFConfig.AddonDimensions.dreamyardID) return; // Replace with your Dreamyard dimension ID
//
//	    BlockPos chunkPos = new BlockPos(chunkX, 0, chunkZ);
//	    Biome biome = world.getBiome(chunkPos);
//
//	    // Check if biome matches your custom biome instance
//	    if (biome != DreamyardBiomes.BiomeDreamyardMeadows) return;
//
//	    // Generate the grass
//	    for (int i = 0; i < 20; i++) {
//	        int x = chunkX + random.nextInt(16) + 8;
//	        int y = random.nextInt(128);
//	        int z = chunkZ + random.nextInt(16) + 8;
//	        BlockPos pos = new BlockPos(x, y, z);
//
//	        // Only place if the position is air and block below is grass (or your custom grass)
//	        IBlockState stateBelow = world.getBlockState(pos.down());
//	        if (world.isAirBlock(pos) && stateBelow.getBlock() == BlockInit.OVERGRASS_BLOCK) {
//	            world.setBlockState(pos, this.getDefaultState(), 2);
//	        }
//	    }
//	}

	
	public SpaceGDgrass(String name, Material material) {
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setHardness(0.0F);
		setSoundType(SoundType.PLANT);
		setLightLevel(0.0F);
		setCreativeTab(CFMain.CrescentfallenBlocks); 
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	


	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT; 
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

    /**
     * Enables slight random offset like vanilla grass and flowers.
     */
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }
	
	@Override
	public void registerModels() {
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

}

