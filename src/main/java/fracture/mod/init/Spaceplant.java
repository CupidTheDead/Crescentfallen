package fracture.mod.init;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumBlockRenderType;

public class Spaceplant extends BlockBush implements IHasModel {

	public Spaceplant(String name, Material material) {
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

