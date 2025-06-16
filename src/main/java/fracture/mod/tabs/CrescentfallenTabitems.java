package fracture.mod.tabs;

import fracture.mod.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CrescentfallenTabitems extends CreativeTabs
{
	public CrescentfallenTabitems(String label) { super("crescentfallentab");
	this.setBackgroundImageName("crescentfallen.png"); }
	public ItemStack getTabIconItem() { return new ItemStack(ItemInit.FLAMINGO_FEATHER);}
}
