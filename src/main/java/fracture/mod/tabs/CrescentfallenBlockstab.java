package fracture.mod.tabs;

import fracture.mod.init.BlockInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CrescentfallenBlockstab extends CreativeTabs
{
    public CrescentfallenBlockstab(String label) { super("CrescentfallenTab");
    this.setBackgroundImageName("crescentfallen.png"); }
    public ItemStack getTabIconItem() { return new ItemStack(Item.getItemFromBlock(BlockInit.ASTROID_FRACTURE));}
}
















//import fracture.mod.init.BlockInit;
//import fracture.mod.init.ItemInit;
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.item.ItemStack;

//public class CresentfallenTab extends CreativeTabs
//{
//	public CresentfallenTab(String label) { super("CresentfallenTab");
//	this.setBackgroundImageName("Cresentfallen.png"); }
//	public ItemStack getTabIconItem() { return new ItemStack(BlockInit.BLOCK_CENTURIUM);}
//}
