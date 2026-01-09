package fracture.mod.objects.items;

import fracture.mod.CFMain;
import fracture.mod.init.ItemInit;
import fracture.mod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import fracture.mod.proxy.Proxy;


public class ItemBase extends Item implements IHasModel
{
	public ItemBase(String name) 
	{
			setTranslationKey(name);
			setRegistryName(name);
			setCreativeTab(CFMain.CrescentfallenItems);
			
			ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels()
	{
		CFMain.proxy.registerItemRenderer(this, 0, "Inventory"); {}
	}
}
