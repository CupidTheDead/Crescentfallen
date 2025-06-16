package fracture.mod.planets.thefracture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import fracture.mod.ModInfo;
import micdoodle8.mods.galacticraft.core.client.SkyProviderOverworld;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.math.Vec3d;

public class SkyObjectTestTheFracture extends SkyProviderOverworld {

    private static final ResourceLocation HOLLOWS_SKY_OBJECT = new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/Hollows.png");

    public void render(float partialTicks, int pass, float alpha) {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Minecraft.getMinecraft().getTextureManager().bindTexture(HOLLOWS_SKY_OBJECT);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        float size = 50.0F; // Half width/height of the quad
        float distance = 500.0F; // How far away the quad is rendered

        	// Get player view angles
        float yaw = Minecraft.getMinecraft().player.rotationYaw;
        float pitch = Minecraft.getMinecraft().player.rotationPitch;

        GlStateManager.pushMatrix();

        	// Rotate the image to always face the player
        GlStateManager.rotate(-yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);

        	// Translate it far into the sky
        GlStateManager.translate(120.0F, 100.0F, -distance);

        	// Render the flat quad
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-size,  size, 0.0D).tex(0.0D, 0.0D).endVertex(); // Top Left
        buffer.pos( size,  size, 0.0D).tex(1.0D, 0.0D).endVertex(); // Top Right
        buffer.pos( size, -size, 0.0D).tex(1.0D, 1.0D).endVertex(); // Bottom Right
        buffer.pos(-size, -size, 0.0D).tex(0.0D, 1.0D).endVertex(); // Bottom Left
        tessellator.draw();

        GlStateManager.popMatrix();

        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableFog();
    }
}