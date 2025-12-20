package fracture.mod.init;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class SpaceStone extends Block implements IHasModel 
{
	//public SpaceStone(Material materialIn) {
	public SpaceStone(String name, Material material) 
	{

		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CFMain.CrescentfallenBlocks);
		setHardness(1.7f);
		setResistance(6.0f);
		setLightLevel(0.0f);
		setLightOpacity(2222);
		//setDefaultSlipperiness(slipperiness);
		setHarvestLevel("pickaxe", 0);
		setSoundType(blockSoundType.STONE);
		//setBlockUnbreakable();
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	//@Override
	//public Item getItemDropped(IBlockState state, Random rand, int fortune) 
	////{
		
	//	return ItemInit.TANZANITE;
	//}
	//The statement above is used if you want a block to drop an item(i.e. serotonin shale drops seritonium nuggets)

	
	//	{
	//	@Override
		//public boolean isFullBlock(IBlockState state)
		//@Override
	//	boolean isFullBlock; IBlockState state;
	//	{
	//		return true;
	//	}
	//	@Override
	//	boolean isFullCube; IBlockState state1;
	//	{
	//		return true;
		//}
	//Statement above is non functional, fix this later	
		
		
	@Override
	public void registerModels()
	{
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
	}

}
