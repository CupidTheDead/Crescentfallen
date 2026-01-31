package fracture.mod.util.handlers;

import com.mjr.extraplanets.moons.Oberon.SkyProviderOberon;

import fracture.mod.client.sky.EuropaSkyProvider;
import fracture.mod.client.sky.IoSkyProvider;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

    private static final int IO_DIMENSION_ID = -1500; 
    private static final int EUROPA_DIMENSION_ID = -1501;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote) return; // Client only

        int dimID = event.getWorld().provider.getDimension();

        if (event.getWorld().provider instanceof IGalacticraftWorldProvider) {
            IGalacticraftWorldProvider gcProvider = (IGalacticraftWorldProvider) event.getWorld().provider;

            // IO sky
            if (dimID == IO_DIMENSION_ID) {
                event.getWorld().provider.setSkyRenderer(new IoSkyProvider(gcProvider));
            }
            	// Oberon ID
                if (event.getWorld().isRemote && event.getWorld().provider.getDimension() == -1509) { 
                    // Cast provider to Galacticraft provider if needed, or pass 'null' if your constructor allows it 
                    // (Note: your current constructor requires IGalacticraftWorldProvider to get Solar Size)
                    
                    if (event.getWorld().provider instanceof IGalacticraftWorldProvider) {
                        event.getWorld().provider.setSkyRenderer(new SkyProviderOberon((IGalacticraftWorldProvider) event.getWorld().provider));
                    }
                }
            
            // Europa sky
            else if (dimID == EUROPA_DIMENSION_ID) {
                event.getWorld().provider.setSkyRenderer(new EuropaSkyProvider(gcProvider));
                System.out.println("[Fracture] Europa Sky Provider ATTACHED.");
            }
        }
    }
}