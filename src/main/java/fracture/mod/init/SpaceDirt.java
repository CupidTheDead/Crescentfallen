 	package fracture.mod.init;

import net.minecraft.block.Block;
import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SpaceDirt extends Block implements IHasModel 
{
	
	public SpaceDirt(String name, Material material) 
	{

		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(fracture.mod.CFMain.CrescentfallenBlocks);
		setHardness(0.5f);
		setResistance(0.5f);
		setLightLevel(0.0f);
		setLightOpacity(0);
		//setDefaultSlipperiness(slipperiness);
		setHarvestLevel("shovel", 0);
		setSoundType(blockSoundType.GROUND);
		//setBlockUnbreakable();
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
@Override
public void registerModels()
{
	CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
}
@Override
public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
net.minecraftforge.common.IPlantable plantable) {
	return true;
}

}
	
