package fracture.mod.objects.blocks;

import fracture.mod.CFMain;
import fracture.mod.init.BlockInit;
import fracture.mod.init.ItemInit;
import fracture.mod.tabs.CrescentfallenBlockstab;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel
{
	public BlockBase(String name, Material material)
	{
		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CFMain.CrescentfallenBlocks);
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	@Override
	public void registerModels()
	{
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
	}
}
