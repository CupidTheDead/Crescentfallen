package fracture.mod.util.handlers;

import fracture.mod.client.sky.GanymedeSkyProvider;
import fracture.mod.world.epchanges.CfGanymedeChunkgenWrapper;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

public class GanymedeInjector {

    // The ID for Ganymede
    private static final int GANYMEDE_ID = -1506;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        World world = event.getWorld();
        if (world.provider.getDimension() == GANYMEDE_ID) {
          
            if (world.isRemote) {
                world.provider.setSkyRenderer(new GanymedeSkyProvider());
            } 
            
            else if (world instanceof net.minecraft.world.WorldServer) {
                injectChunkGenerator((net.minecraft.world.WorldServer) world);
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START && net.minecraft.client.Minecraft.getMinecraft().world != null) {
            World world = net.minecraft.client.Minecraft.getMinecraft().world;
            if (world.provider.getDimension() == GANYMEDE_ID) {
                if (!(world.provider.getSkyRenderer() instanceof GanymedeSkyProvider)) {
                    world.provider.setSkyRenderer(new GanymedeSkyProvider());
                }
            }
        }
    }

    private void injectChunkGenerator(net.minecraft.world.WorldServer world) {
        try {
            ChunkProviderServer provider = world.getChunkProvider();
            
            // Use Reflection to access the private 'chunkGenerator' field
            // 'field_186029_c' is the obfuscated name for chunkGenerator in 1.12.2
            Field genField = ReflectionHelper.findField(ChunkProviderServer.class, "chunkGenerator", "field_186029_c");
            genField.setAccessible(true);

            IChunkGenerator originalGen = (IChunkGenerator) genField.get(provider);

            // Prevent double-wrapping if this event fires twice
            if (!(originalGen instanceof CfGanymedeChunkgenWrapper)) {
                System.out.println("[Fracture] Injecting Wrapper Generator into Ganymede...");
                
                // Injector
                IChunkGenerator myWrapper = new CfGanymedeChunkgenWrapper(originalGen);
                genField.set(provider, myWrapper);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}