package fracture.mod.init;

import java.util.Random;

import fracture.mod.Main;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class SpaceOreTanz extends Block implements IHasModel 
{
	//public SpaceStone(Material materialIn) {
	public SpaceOreTanz(String name, Material material) 
	{

		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(fracture.mod.Main.CrescentfallenBlocks);
		setHardness(3.0f);
		setResistance(3.0f);
		setLightLevel(0.0f);
		setLightOpacity(0);
		//setDefaultSlipperiness(slipperiness);
		setHarvestLevel("pickaxe", 1);
		setSoundType(blockSoundType.STONE);
		//setBlockUnbreakable();
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) 
	{
		
		return ItemInit.TANZANITE;
	}
	//ABOVE IS USED IF YOU WANT A BLOCK TO DROP AN ITEM(ie Serotonin Shale drops seritonium nuggets)
	
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
	//ABOVE IS NON FUCTIONING, FIX THIS LATER	
		
		
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
	}

}