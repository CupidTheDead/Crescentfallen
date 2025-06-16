package fracture.mod.objects.items;

import fracture.mod.Main;
import fracture.mod.init.ItemInit;
import fracture.mod.util.IHasModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import fracture.mod.proxy.Proxy;


public class ItemBase extends Item implements IHasModel
{
	public ItemBase(String name) 
	{
			setUnlocalizedName(name);
			setRegistryName(name);
			setCreativeTab(fracture.mod.Main.Crescentfallenitems);
			
			ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, "Inventory"); {}
	}
}
