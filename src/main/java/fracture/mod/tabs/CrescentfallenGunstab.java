package fracture.mod.tabs;

import com.mrcrayfish.guns.MrCrayfishGunMod;
import fracture.mod.init.BlockInit;
import fracture.mod.init.ItemInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CrescentfallenGunstab extends CreativeTabs
{
    public CrescentfallenGunstab(String label) { 
        super("crescentfallentabguns");
        this.setBackgroundImageName("crescentfallen.png"); 
    }

    @Override
    public ItemStack createIcon() { 
        return new ItemStack(ItemInit.LAER_15);
    }
}
