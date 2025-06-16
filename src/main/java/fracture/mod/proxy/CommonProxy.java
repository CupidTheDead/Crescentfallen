package fracture.mod.proxy;

import net.minecraft.item.Item;
import fracture.mod.proxy.ClientProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy extends Proxy 
{
	public void registerItemRenderer(Item item, int meta, String id) {}
	public void registerVariantRenderer(Item item, int meta, String filename, String id) {}
@Override
public void preInit(FMLPreInitializationEvent event) {

}

@Override
public void init(FMLInitializationEvent event) {

}
@Override
public void postInit(FMLPostInitializationEvent event) {
	
}
}
