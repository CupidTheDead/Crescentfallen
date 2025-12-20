package fracture.mod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import fracture.mod.init.CFdimensions;
import fracture.mod.planets.WorldProviderTheFracture;
import fracture.mod.util.Reference;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends Proxy {

    private boolean skyRendererSet = false;

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

    @Override
    public void registerVariantRenderer(Item item, int meta, String filename, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Reference.MODID, filename), id));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        
        
        
        
        
        //THIS IS TESTING CODE 6/3/25
        
       
     //   // Defer sky renderer setup to post world-load tick
     //   MinecraftForge.EVENT_BUS.register(this);
    }

 //   @SubscribeEvent
 //   public void onClientTick(TickEvent.ClientTickEvent event) {
//
        // Wait until world is loaded and in the correct dimension
 //       if (!skyRendererSet &&
  //          Minecraft.getMinecraft().world != null &&
   //         Minecraft.getMinecraft().world.provider instanceof WorldProviderTheFracture) {
//
     //       ((WorldProviderTheFracture) Minecraft.getMinecraft().world.provider)
     //           .setSkyRenderer(new SkyObjectTestTheFracture());
///
     //       skyRendererSet = true;
     //       MinecraftForge.EVENT_BUS.unregister(this); // Unregister after setting
    //    }
 //   }

    
    //code snip2
    
   // public static void registerSkyRenderers() {
   //     DimensionManager.getWorld(AddonDimensions.dimPlanetTwoS1.getId())
    //        .provider.setSkyRenderer(new SkyObjectTestTheFracture());
    
    //{} Only safe on client
    //GalacticraftRegistry.registerSkyProvider(AddonDimensions.dimPlanetTwoS1.getId(), new SkyObjectTestTheFracture());
//}
    
    
    
    
    //THIS DOES SOMETHING BUT IT DOES NOT DO THE RIGHT THING 6/3/25
    
//    @SubscribeEvent
 //   @SideOnly(Side.CLIENT)
 //   public void onWorldLoad(net.minecraftforge.event.world.WorldEvent.Load event) {
  //      if (event.getWorld() instanceof net.minecraft.client.multiplayer.WorldClient) {
   //         WorldClient clientWorld = (WorldClient) event.getWorld();

   //         if (clientWorld.provider instanceof WorldProviderTheFracture) {
   //             ((WorldProviderTheFracture) clientWorld.provider)
    //                .setSkyRenderer(new SkyObjectTestTheFracture());
   //         }
  //      }
  //  }
    
    
    //THIS IS TESTING CODE 6/3/25

    
    
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);{
        
            MinecraftForge.EVENT_BUS.register(this); // To receive world load event(this is also new code 6/3/2025)
            
        }
    }
}