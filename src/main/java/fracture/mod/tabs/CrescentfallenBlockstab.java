package fracture.mod.tabs;

import fracture.mod.init.BlockInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CrescentfallenBlockstab extends CreativeTabs
{
    public CrescentfallenBlockstab(String label) { 
    super("CrescentfallenTab");this.setBackgroundImageName("crescentfallen.png"); 
    }
    @Override
    public ItemStack createIcon() { 
        return new ItemStack(Item.getItemFromBlock(BlockInit.ASTROID_FRACTURE));
    }
}