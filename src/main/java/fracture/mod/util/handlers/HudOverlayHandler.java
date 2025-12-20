package fracture.mod.util.handlers;

import fracture.mod.CFInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HudOverlayHandler {
	

    private static final ResourceLocation dashIcon = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/dash_icon.png");
    private static final ResourceLocation slideIcon = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/slide_icon.png"); // Add slide icon texture
    private static final int DEFAULT_COOLDOWN_TICKS = 20;
    private static final int DASH_DISPLAY_TICKS = 40; // 2 seconds at 20 ticks/sec

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null || player.isCreative()) return;

        int cooldown = player.getEntityData().getInteger("diveCooldown");
        int maxCooldown = DEFAULT_COOLDOWN_TICKS;
        if (maxCooldown <= 0) maxCooldown = 1;

        int slideTicks = player.getEntityData().getInteger("slideTicks"); // custom counter for slide display
        boolean sliding = player.getEntityData().getString("diveState").equals("SLIDING");

        // Update slideTicks
        if (sliding) {
            slideTicks = DASH_DISPLAY_TICKS; // reset slide display timer
        } else if (slideTicks > 0) {
            slideTicks--;
        }
        player.getEntityData().setInteger("slideTicks", slideTicks);

        // Use ScaledResolution for proper GUI scaling
        ScaledResolution res = new ScaledResolution(mc);
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();

        // -----------------------------
        // DASH ICON
        // -----------------------------
        if (cooldown > 0 || cooldown <= 0) { // always show icon for 2 seconds after dash
            int dashX = screenWidth / 2 + 1;
            int dashY = screenHeight - 36;
            GlStateManager.pushMatrix();
            mc.getTextureManager().bindTexture(dashIcon);
            mc.ingameGUI.drawTexturedModalRect(dashX, dashY, 0, 0, 16, 16);
            GlStateManager.popMatrix();

            // Draw cooldown bar under icon
            if (cooldown > 0) {
                int barWidth = 16;
                int barHeight = 2;
                float fraction = 1.0f - ((float) cooldown / (float) maxCooldown);
                int filled = (int) (barWidth * fraction);
                GuiIngame.drawRect(dashX, dashY + 16, dashX + filled, dashY + 16 + barHeight, 0xFF00FFFF);
            }

            // Draw "Dive Ready..." only when cooldown is 0
            if (cooldown == 0) {
                mc.fontRenderer.drawStringWithShadow("DR", dashX + 20, dashY + 3, 0xF74510);
            }
        }

        // -----------------------------
        // SLIDE ICON
        // -----------------------------
        if (slideTicks > 0) {
            int slideX = screenWidth / 2 - 20; // left of dash icon
            int slideY = screenHeight - 36;
            GlStateManager.pushMatrix();
            mc.getTextureManager().bindTexture(slideIcon);
            mc.ingameGUI.drawTexturedModalRect(slideX, slideY, 0, 0, 16, 16);
            GlStateManager.popMatrix();

            // Optional: draw a semi-transparent overlay or bar for slide progress
            int barWidth = 16;
            int barHeight = 2;
            float fraction = (float) slideTicks / DASH_DISPLAY_TICKS;
            int filled = (int) (barWidth * fraction);
            GuiIngame.drawRect(slideX, slideY + 16, slideX + filled, slideY + 16 + barHeight, 0xAAFFAA00);
        }
    }
}