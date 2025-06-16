package fracture.mod.init;

import net.minecraft.block.Block;

import fracture.mod.Main;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class SpaceOre0 extends Block implements IHasModel 
{
	
	public SpaceOre0(String name, Material material) 
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
		setHarvestLevel("pickaxe", 0);
		setSoundType(blockSoundType.STONE);
		//setBlockUnbreakable();
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
@Override
public void registerModels()
{
	Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
}

}
	
