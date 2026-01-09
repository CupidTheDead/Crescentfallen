package fracture.mod.objects.items;

import com.mrcrayfish.guns.item.ItemGun;
import fracture.mod.CFMain;
import fracture.mod.init.ItemInit;
import fracture.mod.util.IHasModel;
import fracture.mod.util.Reference;
import net.minecraft.util.ResourceLocation;

public class GunBase extends ItemGun implements IHasModel
{
    public GunBase(String name) 
    {
        // assets/fracture/guns/name.json
        super(new ResourceLocation(Reference.MODID, name)); 
        setTranslationKey(name); 
        setCreativeTab(CFMain.CrescentfallenGuns); 
        ItemInit.ITEMS.add(this);
    }

    @Override
    public void registerModels()
    {
        CFMain.proxy.registerItemRenderer(this, 0, "Inventory");
    }
}