package fracture.mod.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class Proxy {

	public void registerItemRenderer(Item item, int meta, String id) {
	}
	// public void registerVariantRenderer(Item item, int meta, String filename,
	// String id) {}

	public void preInit(FMLPreInitializationEvent event) {

	}

	public void init(FMLInitializationEvent event) {
	}

	public void receiveIMC(IMCEvent event) {
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

	public World getWorld() {
		return null;
	}

	public void registerVariantRenderer(Item item, int meta, String filename, String id) {
	}

}

//}