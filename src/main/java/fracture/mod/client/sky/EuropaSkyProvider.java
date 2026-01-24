package fracture.mod.client.sky;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.IRenderHandler;

public class EuropaSkyProvider extends IRenderHandler {

    private static final ResourceLocation jupiterTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/jupiter.png");
    private static final ResourceLocation sunTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/planets/atmosphericsun.png");

    public int starList;        
    public int starListTwinkle; 
    public int glSkyList;
    public int glSkyList2;
    private float sunSize;

    public EuropaSkyProvider(IGalacticraftWorldProvider europaProvider) {
        this.sunSize = 17.5F * europaProvider.getSolarSize();
        
        int displayLists = GLAllocation.generateDisplayLists(4);
        this.starList = displayLists;
        this.starListTwinkle = displayLists + 1;
        this.glSkyList = displayLists + 2;
        this.glSkyList2 = displayLists + 3;


        GL11.glPushMatrix();
        GL11.glNewList(this.starList, GL11.GL_COMPILE);
        this.renderStars(true); 
        GL11.glEndList();
        GL11.glPopMatrix();


        GL11.glPushMatrix();
        GL11.glNewList(this.starListTwinkle, GL11.GL_COMPILE);
        this.renderStars(false); // false = generate twinkling stars
        GL11.glEndList();
        GL11.glPopMatrix();

        // Skybox
        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();

        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        final byte byte2 = 64;
        final int i = 256 / byte2 + 2;
        float f = 16F;
        for (int j = -byte2 * i; j <= byte2 * i; j += byte2) {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2) {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                worldRenderer.pos(j, f, l).endVertex();
                worldRenderer.pos(j + byte2, f, l).endVertex();
                worldRenderer.pos(j + byte2, f, l + byte2).endVertex();
                worldRenderer.pos(j, f, l + byte2).endVertex();
                tessellator.draw();
            }
        }
        GL11.glEndList();

        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        for (int k = -byte2 * i; k <= byte2 * i; k += byte2) {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2) {
                worldRenderer.pos(k + byte2, f, i1).endVertex();
                worldRenderer.pos(k, f, i1).endVertex();
                worldRenderer.pos(k, f, i1 + byte2).endVertex();
                worldRenderer.pos(k + byte2, f, i1 + byte2).endVertex();
            }
        }
        tessellator.draw();
        GL11.glEndList();
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        
        float starBrightnessForSky = world.getStarBrightness(partialTicks);
        float skyBrightness = 1.0F - starBrightnessForSky;


        float targetR = 0.12F;
        float targetG = 0.04F;
        float targetB = 0.35F;

        float f1 = targetR * skyBrightness; 
        float f2 = targetG * skyBrightness;
        float f3 = targetB * skyBrightness;

        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        f2 = MathHelper.clamp(f2, 0.0F, 1.0F);
        f3 = MathHelper.clamp(f3, 0.0F, 1.0F);


        float f6;

        if (mc.gameSettings.anaglyph) {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        GL11.glColor3f(f1, f2, f3);
        Tessellator tessellator1 = Tessellator.getInstance();
        BufferBuilder worldRenderer1 = tessellator1.getBuffer();
        
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG); 
        GL11.glColor3f(f1, f2, f3);
        GL11.glCallList(this.glSkyList);
        
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();

        float f7;
        float f8;
        float f9;
        float f10;

        float time = world.getTotalWorldTime() + partialTicks;
        float starBrightness = world.getStarBrightness(partialTicks);

        // STARS 
        if (starBrightness > 0.0F) {
            GL11.glPushMatrix();
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-19.0F, 0, 1.0F, 0);

            GL11.glColor4f(starBrightness, starBrightness, starBrightness, starBrightness);
            GL11.glCallList(this.starList);

            float flicker = (MathHelper.sin(time * 0.15F) + MathHelper.cos(time * 0.65F)) * 0.5F; 
            float twinkleAlpha = starBrightness * (0.6F + 0.4F * flicker); 

            GL11.glColor4f(starBrightness, starBrightness, starBrightness, twinkleAlpha);
            GL11.glCallList(this.starListTwinkle);

            GL11.glPopMatrix();
        }

        // SUN AURA AND SUN BODY 
        float[] afloat = new float[4];
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glPushMatrix();
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        
        afloat[0] = 255 / 255.0F;
        afloat[1] = 194 / 255.0F;
        afloat[2] = 180 / 255.0F;
        afloat[3] = 0.3F;
        f6 = afloat[0];
        f7 = afloat[1];
        f8 = afloat[2];
        float f11;

        if (mc.gameSettings.anaglyph) {
            f9 = (f6 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
            f10 = (f6 * 30.0F + f7 * 70.0F) / 100.0F;
            f11 = (f6 * 30.0F + f8 * 70.0F) / 100.0F;
            f6 = f9;
            f7 = f10;
            f8 = f11;
        }

        float f18 = 1.0F - starBrightness; 

        // INNER AURA 
        worldRenderer1.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        float r = f6 * f18;
        float g = f7 * f18;
        float b = f8 * f18;
        float a = afloat[3] * 2 / f18; 
        
        if(a > 1.0f) a = 1.0f;
        
        worldRenderer1.pos(0.0D, 100.0D, 0.0D).color(r, g, b, a).endVertex();
        r = afloat[0] * f18;
        g = afloat[1] * f18;
        b = afloat[2] * f18;
        a = 0.0F;

        f10 = 20.0F; 
        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos(0, 100.0D, (double) -f10 * 1.5F).color(r, g, b, a).endVertex();
        worldRenderer1.pos(f10, 100.0D, -f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos((double) f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
        worldRenderer1.pos(f10, 100.0D, f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos(0, 100.0D, (double) f10 * 1.5F).color(r, g, b, a).endVertex();
        worldRenderer1.pos(-f10, 100.0D, f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos((double) -f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
        tessellator1.draw();

        // OUTER AURA
        worldRenderer1.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        r = f6 * f18;
        g = f7 * f18;
        b = f8 * f18;
        a = afloat[3] * f18;
        worldRenderer1.pos(0.0D, 100.0D, 0.0D).color(r, g, b, a).endVertex();
        r = afloat[0] * f18;
        g = afloat[1] * f18;
        b = afloat[2] * f18;
        a = 0.0F;

        f10 = 40.0F; 
        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos(0, 100.0D, (double) -f10 * 1.5F).color(r, g, b, a).endVertex();
        worldRenderer1.pos(f10, 100.0D, -f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos((double) f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
        worldRenderer1.pos(f10, 100.0D, f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos(0, 100.0D, (double) f10 * 1.5F).color(r, g, b, a).endVertex();
        worldRenderer1.pos(-f10, 100.0D, f10).color(r, g, b, a).endVertex();
        worldRenderer1.pos((double) -f10 * 1.5F, 100.0D, 0).color(r, g, b, a).endVertex();
        worldRenderer1.pos(-f10, 100.0D, -f10).color(r, g, b, a).endVertex();
        tessellator1.draw();
        
        GL11.glPopMatrix();
        GL11.glShadeModel(GL11.GL_FLAT);

        // SUN TEXTURE
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        
        GL11.glPushMatrix();
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        f10 = this.sunSize / 3.5F; 
        worldRenderer1.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer1.pos(-f10, 99.9D, -f10).endVertex();
        worldRenderer1.pos(f10, 99.9D, -f10).endVertex();
        worldRenderer1.pos(f10, 99.9D, f10).endVertex();
        worldRenderer1.pos(-f10, 99.9D, f10).endVertex();
        tessellator1.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        f10 = this.sunSize;
        mc.renderEngine.bindTexture(sunTexture);
        worldRenderer1.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer1.pos(-f10, 100.0D, -f10).tex(0.0D, 0.0D).endVertex();
        worldRenderer1.pos(f10, 100.0D, -f10).tex(1.0D, 0.0D).endVertex();
        worldRenderer1.pos(f10, 100.0D, f10).tex(1.0D, 1.0D).endVertex();
        worldRenderer1.pos(-f10, 100.0D, f10).tex(0.0D, 1.0D).endVertex();
        tessellator1.draw();
        
        GL11.glPopMatrix(); 

        // JUPITER
        GL11.glPushMatrix();
        float jupiterSize = sunSize * 3.5F; 
        GL11.glScalef(0.6F, 0.6F, 0.6F);
        GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(200F, 1.0F, 0.0F, 0.0F); 

        mc.renderEngine.bindTexture(jupiterTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); 
        worldRenderer1.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer1.pos(-jupiterSize, -100.0D, jupiterSize).tex(0, 1).endVertex();
        worldRenderer1.pos(jupiterSize, -100.0D, jupiterSize).tex(1, 1).endVertex();
        worldRenderer1.pos(jupiterSize, -100.0D, -jupiterSize).tex(1, 0).endVertex();
        worldRenderer1.pos(-jupiterSize, -100.0D, -jupiterSize).tex(0, 0).endVertex();
        tessellator1.draw();
        GL11.glPopMatrix();

        // AURORA
        this.renderAurora(tessellator1, time);

        // CLEANUP
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.0F, 0.0F, 0.0F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_ALPHA_TEST); 
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    private void renderAurora(Tessellator tessellator, float time) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        
        GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F); 
        
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        
        float width = 30.0F; 
        float height = 110.0F; 
        float length = 300.0F; 
        
        int segments = 400; 
        
        for (int i = 0; i <= segments; i++) {
            float t = (float)i / (float)segments; 
            float posOffset = (t - 0.5F) * length; 

            float wave1 = MathHelper.sin(t * 10.0F + time * 0.02F) * 5.0F;
            float wave2 = MathHelper.cos(t * 15.0F - time * 0.02F) * 5.0F;
            
            float hue = (t * 5.0F + time * 0.01F) % 1.0F;
            
            float r = 0.2F + 0.6F * MathHelper.sin(hue * 6.28F);
            float g = 0.2F + 0.6F * MathHelper.sin(hue * 6.28F + 2.0F);
            float b = 0.6F + 0.4F * MathHelper.sin(hue * 6.28F + 4.0F);
            
            r = MathHelper.clamp(r, 0.0F, 1.0F);
            g = MathHelper.clamp(g, 0.0F, 1.0F);
            b = MathHelper.clamp(b, 0.0F, 1.0F);

            float alphaBase = 1.0F - Math.abs((t - 0.5F) * 2.0F);
            float alpha = alphaBase * 0.6F + 0.1F * MathHelper.sin(time * 0.1F);
            if(alpha < 0) alpha = 0;

            worldRenderer.pos(posOffset, height + wave1, -width + wave2)
                .color(r, g, b, 0.0F).endVertex(); 
                
            worldRenderer.pos(posOffset, height + wave1, 0 + wave2)
                .color(r, g, b, alpha).endVertex(); 
                
            worldRenderer.pos(posOffset, height + wave1, width + wave2)
                .color(r, g, b, 0.0F).endVertex(); 
        }
        
        tessellator.draw();
        
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private void renderStars(boolean generateStatic) {
        final Random rand = new Random(10842L); 
        final Tessellator var2 = Tessellator.getInstance();
        BufferBuilder worldRenderer = var2.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int starIndex = 0; starIndex < 12000; ++starIndex) {
            double var4 = rand.nextFloat() * 2.0F - 1.0F;
            double var6 = rand.nextFloat() * 2.0F - 1.0F;
            double var8 = rand.nextFloat() * 2.0F - 1.0F;
            final double var10 = 0.08F + rand.nextFloat() * 0.08F; 
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;

            if (var12 < 1.0D && var12 > 0.01D) {
                boolean isTwinkler = rand.nextFloat() < 0.28F;

                if (generateStatic && isTwinkler) continue;

                if (!generateStatic && !isTwinkler) continue;

                var12 = 1.0D / Math.sqrt(var12);
                var4 *= var12;
                var6 *= var12;
                var8 *= var12;
                final double var14 = var4 * 100.0D;
                final double var16 = var6 * 100.0D;
                final double var18 = var8 * 100.0D;
                final double var20 = Math.atan2(var4, var8);
                final double var22 = Math.sin(var20);
                final double var24 = Math.cos(var20);
                final double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
                final double var28 = Math.sin(var26);
                final double var30 = Math.cos(var26);
                final double var32 = rand.nextDouble() * Math.PI * 2.0D;
                final double var34 = Math.sin(var32);
                final double var36 = Math.cos(var32);

                for (int var38 = 0; var38 < 4; ++var38) {
                    final double var39 = 0.0D;
                    final double var41 = ((var38 & 2) - 1) * var10;
                    final double var43 = ((var38 + 1 & 2) - 1) * var10;
                    final double var47 = var41 * var36 - var43 * var34;
                    final double var49 = var43 * var36 + var41 * var34;
                    final double var53 = var47 * var28 + var39 * var30;
                    final double var55 = var39 * var28 - var47 * var30;
                    final double var57 = var55 * var22 - var49 * var24;
                    final double var61 = var49 * var22 + var55 * var24;
                    worldRenderer.pos(var14 + var57, var16 + var53, var18 + var61).endVertex();
                }
            }
        }
        var2.draw();
    }
}