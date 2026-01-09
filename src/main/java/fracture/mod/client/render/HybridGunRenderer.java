package fracture.mod.client.render;

import fracture.mod.objects.items.ItemCustomGun;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HybridGunRenderer extends GeoItemRenderer<ItemCustomGun> {

    public HybridGunRenderer() {
        super(new ModelCustomGun());
    }

    @Override
    public void renderByItem(ItemStack itemStack) {
        // --- MODE: GECKOLIB ONLY ---
        
        GlStateManager.pushMatrix();
        
        // FIX 1: Center the model in the hand
        // (0.5, 0.5, 0.5) is the center of the block.
        GlStateManager.translate(0.5, 0.5, 0.5);
        
        // FIX 2: Correct the scale (1.0 is standard, adjust if tiny/huge)
        GlStateManager.scale(1.0F, 1.0F, 1.0F); 

        // RENDER
        // This delegates to the standard GeckoLib render pipeline
        super.renderByItem(itemStack);
        
        GlStateManager.popMatrix();
    }
}