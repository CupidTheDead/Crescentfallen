package fracture.mod.client.render;

import fracture.mod.objects.items.ItemCustomGun;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class GunRenderHandler {
	
	//Basis for Hybrid gun renderer
	//stops Mr crayfishes gun from rendering, and displays geckolib model and animations
    private static final HybridGunRenderer renderer = new HybridGunRenderer();

    @SubscribeEvent
    public static void onRenderHand(RenderSpecificHandEvent event) {
        Item item = event.getItemStack().getItem();

        if (item instanceof ItemCustomGun) {
            
            // Stop the default MrCrayfish/Minecraft rendering
            event.setCanceled(true);

            // Force Hybrid rendering system to run
            GlStateManager.pushMatrix();
            
            // Default positioning
            // (X, Y, Z)
            GlStateManager.translate(0.5, -0.5, -0.5); 

            // Rotation
            // GlStateManager.rotate(-90, 0, 1, 0); 
            
            // Scale
            // GlStateManager.scale(1.5, 1.5, 1.5);

            renderer.renderByItem(event.getItemStack());
            
            GlStateManager.popMatrix();
        }
    }
}