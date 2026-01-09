package fracture.mod.init;

import java.util.ArrayList;
import java.util.List;
import com.mrcrayfish.guns.object.Gun;
import fracture.mod.objects.items.GunBase;
import fracture.mod.objects.items.ItemBase;
import fracture.mod.objects.items.ItemCustomGun;
import net.minecraft.item.Item;

public class ItemInit {
    
	   
    public static final List<Item> ITEMS = new ArrayList<Item>();
    
    public static final Item CENTURIUM_INGOT = new ItemBase("ingot_centurium");
    public static final Item SERITONIUM_INGOT = new ItemBase("ingot_seritonium");
    public static final Item TANZANITE = new ItemBase("tanzanite");
    public static final Item FLAMINGO_FEATHER = new ItemBase("flamingo_feather");

    // GUNS
    
    // MRCRAYFISHBASE
    public static final Item LAER_15 = new GunBase("laer_15");
    // HYBRID RENDERER
    // for ending number, 3500 = 3.5 seconds, 2000 = 2 seconds, ext.
    public static final Item OLD_EARTH_RIFLE = new ItemCustomGun(new Gun(), "old_earth_rifle", 2000);
}