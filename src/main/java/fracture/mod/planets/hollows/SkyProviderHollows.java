package fracture.mod.planets.hollows;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fracture.mod.CFInfo;
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
import net.minecraftforge.fml.client.FMLClientHandler;

public class SkyProviderHollows extends IRenderHandler {

    private static final ResourceLocation sunTexture = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/triopas.png");
    private static final ResourceLocation planetBody = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/helius.png");
    private static final ResourceLocation ringWorld = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/ringworld.png");
    private static final ResourceLocation skyCloudMatrix = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/skycloudmatrix.png");
    
    private static final ResourceLocation hollowsClouds = new ResourceLocation("textures/environment/clouds.png");

    public int starList;
    public int glSkyList;
    public int glSkyList2;
    private float sunSize;

    public SkyProviderHollows(IGalacticraftWorldProvider hollowsProvider) {
        this.sunSize = 0.7F * hollowsProvider.getSolarSize() * 7;

        int displayLists = GLAllocation.generateDisplayLists(3);
        this.starList = displayLists;
        this.glSkyList = displayLists + 1;
        this.glSkyList2 = displayLists + 2;

        // Bind stars to display list
        GL11.glPushMatrix();
        GL11.glNewList(this.starList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();

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
        GlStateManager.disableRescaleNormal();
        GL11.glColor3f(1F, 1F, 1F);
        
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(0, 0, 0);
        GL11.glCallList(this.glSkyList);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        
        // --- NIGHT SKY COLOR LOGIC ---
        Vec3d vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
        float f1 = (float) vec3.x;
        float f2 = (float) vec3.y;
        float f3 = (float) vec3.z;
        float f6;

        // Determine Darkness (0.0 = Noon, 1.0 = Midnight)
        float starBrightness = world.getStarBrightness(partialTicks);


        float navyR = 0.0F;
        float navyG = 0.0F;
        float navyB = 0.1F; 

        // Interpolate color towards darker blue based on darkness
        f1 = f1 * (1.0F - starBrightness) + navyR * starBrightness;
        f2 = f2 * (1.0F - starBrightness) + navyG * starBrightness;
        f3 = f3 * (1.0F - starBrightness) + navyB * starBrightness;

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

        float f18 = world.getStarBrightness(partialTicks);
        if (f18 > 0.0F) {
            GL11.glPushMatrix();
            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-19.0F, 0, 1.0F, 0);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Always full brightness
            GL11.glCallList(this.starList);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        float f7;
        float f8;
        float f9;
        float f10;

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

        f18 = 1.0F - f18;

        worldRenderer1.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        float r = f6 * f18;
        float g = f7 * f18;
        float b = f8 * f18;
        float a = afloat[3] * 2 / f18;
        worldRenderer1.pos(0.0D, 100.0D, 0.0D).color(r, g, b, a).endVertex();
        r = afloat[0] * f18;
        g = afloat[1] * f18;
        b = afloat[2] * f18;
        a = 0.0F;

        // Render sun aura
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

        // Render larger sun aura
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

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glPushMatrix();
        f7 = 0.0F;
        f8 = 0.0F;
        f9 = 0.0F;
        GL11.glTranslatef(f7, f8, f9);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);

        // Render sun
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);

        // Some blanking to conceal the stars
        f10 = this.sunSize / 3.5F;
        worldRenderer1.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer1.pos(-f10, 99.9D, -f10).endVertex();
        worldRenderer1.pos(f10, 99.9D, -f10).endVertex();
        worldRenderer1.pos(f10, 99.9D, f10).endVertex();
        worldRenderer1.pos(-f10, 99.9D, f10).endVertex();
        tessellator1.draw();

        // render sun texture
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        f10 = this.sunSize;
        mc.renderEngine.bindTexture(SkyProviderHollows.sunTexture);
        worldRenderer1.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer1.pos(-f10, 100.0D, -f10).tex(0.0D, 0.0D).endVertex();
        worldRenderer1.pos(f10, 100.0D, -f10).tex(1.0D, 0.0D).endVertex();
        worldRenderer1.pos(f10, 100.0D, f10).tex(1.0D, 1.0D).endVertex();
        worldRenderer1.pos(-f10, 100.0D, f10).tex(0.0D, 1.0D).endVertex();
        tessellator1.draw();

        GL11.glRotatef(360.0F, 1.0F, 0.0F, 0.0F);

        GL11.glPopMatrix();
        GlStateManager.enableRescaleNormal();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();

        // BEGIN RING RENDER
        Tessellator tessellator4 = Tessellator.getInstance();

        GlStateManager.pushMatrix();

        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 0.0F, 1.0F);

        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        GlStateManager.enableTexture2D();
        mc.renderEngine.bindTexture(ringWorld);

        float radius = 10F;

        // Fade effect depending on time of day
        float effCelAng1 = 360F;
        if (effCelAng1 > 0.25F)
            effCelAng1 = 1F - effCelAng1;
        effCelAng1 = 0.25F - Math.min(0.25F, effCelAng1);

        // Use fixed randomness per in-game day for consistent sky features
        long time = world.getWorldTime() + 1000L;
        int day = (int) (time / 24000L);
        Random rand = new Random(day * 0xFF);
        float angle1 = rand.nextFloat() * 360F;
        float angle2 = rand.nextFloat() * 360F;

        // Set transparency based on time
        GlStateManager.color(1F, 1F, 1F, effCelAng1);

        // Rotate the ring
        GlStateManager.rotate(angle1, 0F, 1F, 0F);
        GlStateManager.rotate(angle2, 0F, 0F, 1F);

        // Begin drawing the ring
        BufferBuilder buffer7 = tessellator4.getBuffer();
        buffer7.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        // Define Halo parameters
        int angles = 64; // Number of vertical ring segments
        float anglePer = 360F / angles; // Degrees per segment
        float uPer = 1.0F / angles; // Texture step for UVs
        float y = 2.0F; // Height of each quad in the ring(retrospectively height)

        for (int i = 0; i < angles; i++) {
            int j = (i % 2 == 0) ? i - 1 : i;
            float ang = j * anglePer;
            double xp = Math.cos(Math.toRadians(ang)) * radius;
            double zp = Math.sin(Math.toRadians(ang)) * radius;
            double yo = 0;

            float ut = (j % angles) * uPer;

            if (i % 2 == 0) {
                buffer7.pos(xp, yo + 0 + y, zp).tex(ut, 1F).endVertex();
                buffer7.pos(xp, yo + 0, zp).tex(ut, 0F).endVertex();
            } else {
                buffer7.pos(xp, yo + 0, zp).tex(ut, 0F).endVertex();
                buffer7.pos(xp, yo + 0 + y, zp).tex(ut, 1F).endVertex();
            }
        }

        tessellator4.draw();

        // Cleanup
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.popMatrix();

        // ---- RENDER SMOKE LAYER ----
        this.renderSmokeLayer(world.getTotalWorldTime(), partialTicks);
        // ----------------------------

        // ---- RENDER OBJECT START: HELIUS ----
        GL11.glPushMatrix();
        GL11.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);

        // spin slightly with sky
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 0.0F, 1.0F);

        // Position and scale the planet 
        float planetScale = 200.0F; 
        float planetDistance = 100.0F; 
        float planetDistanceZ = 110.0F; 
        float verticalOffset = -0.0F;  

        GL11.glTranslatef(0.0F, verticalOffset, -planetDistanceZ); 
        GL11.glTranslatef(planetDistance, -30.0F, -100.0F);
        
        GL11.glScalef(planetScale, planetScale, planetScale);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        mc.renderEngine.bindTexture(planetBody);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-1.0D, -1.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        buffer.pos(1.0D, -1.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        buffer.pos(1.0D, 1.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        buffer.pos(-1.0D, 1.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tess.draw();

        GL11.glPopMatrix();
        // -------------------------------------------------------------
    }
    
    private void renderStars() {
        final Random rand = new Random(10842L);
        final Tessellator var2 = Tessellator.getInstance();
        BufferBuilder worldRenderer = var2.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int starIndex = 0; starIndex < (65000); ++starIndex) {
            double var4 = rand.nextFloat() * 2.0F - 1.0F;
            double var6 = rand.nextFloat() * 2.0F - 1.0F;
            double var8 = rand.nextFloat() * 2.0F - 1.0F;
            final double var10 = 0.15F + rand.nextFloat() * 0.1F;
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;

            if (var12 < 1.0D && var12 > 0.01D) {
                var12 = 1.0D / Math.sqrt(var12);
                var4 *= var12;
                var6 *= var12;
                var8 *= var12;
                final double var14 = var4 * (rand.nextDouble() * 150D + 130D);
                final double var16 = var6 * (rand.nextDouble() * 150D + 130D);
                final double var18 = var8 * (rand.nextDouble() * 150D + 130D);
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

    private void renderSmokeLayer(long worldTime, float partialTicks) {
        GlStateManager.pushMatrix();

        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        

        GlStateManager.disableCull();          // Render both sides
        GlStateManager.depthMask(false);       // Don't write to depth buffer (allows layering)
        GlStateManager.disableAlpha();         // Disable Alpha Test so low-alpha pixels aren't discarded

        Minecraft.getMinecraft().renderEngine.bindTexture(hollowsClouds);
        
        // Dithering
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();

        // Position and Scale
        double height = 30.0D; 
        double size = 300.0D; 

        GlStateManager.translate(0.0, height, 0.0);

        // Scroll Logic
        double scrollSpeed = (double) (worldTime % 24000L) / 4000.0D;
        double scrollX = scrollSpeed + ((double) partialTicks / 4000.0D);

        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        double repeats = 4.0D; // How many times the cloud texture repeats
        int gridDivisions = 16;
        double segmentSize = (size * 2) / gridDivisions;
        double texSegment = repeats / gridDivisions;

        for (int x = 0; x < gridDivisions; x++) {
            for (int z = 0; z < gridDivisions; z++) {
                double x1 = -size + x * segmentSize;
                double z1 = -size + z * segmentSize;
                double x2 = x1 + segmentSize;
                double z2 = z1 + segmentSize;

                // Colors
                float r = 1.0F; 
                float g = 1.0F;
                float b = 1.0F;
                
                float maxAlpha = 0.4F; 

                float a1 = getCloudAlpha(x1, z1, size, maxAlpha);
                float a2 = getCloudAlpha(x1, z2, size, maxAlpha);
                float a3 = getCloudAlpha(x2, z2, size, maxAlpha);
                float a4 = getCloudAlpha(x2, z1, size, maxAlpha);

                double u1 = scrollX + (x * texSegment);
                double u2 = scrollX + ((x + 1) * texSegment);
                double v1 = z * texSegment;
                double v2 = (z + 1) * texSegment;

                buff.pos(x1, 0, z1).tex(u1, v1).color(r, g, b, a1).endVertex();
                buff.pos(x1, 0, z2).tex(u1, v2).color(r, g, b, a2).endVertex();
                buff.pos(x2, 0, z2).tex(u2, v2).color(r, g, b, a3).endVertex();
                buff.pos(x2, 0, z1).tex(u2, v1).color(r, g, b, a4).endVertex();
            }
        }

        tess.draw();



        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        
        GlStateManager.depthMask(true);
        GlStateManager.enableCull(); 
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
    private float getCloudAlpha(double x, double z, double maxSize, float maxAlpha) {
        double dist = Math.sqrt(x * x + z * z);
        // Start fading at 20% distance
        double fadeStart = maxSize * 0.2D; 
        if (dist < fadeStart) {
            return maxAlpha;
        } else if (dist >= maxSize) {
            return 0.0F;
        } else {
            double ratio = (dist - fadeStart) / (maxSize - fadeStart);
            return maxAlpha * (1.0F - (float) ratio);
        }
    }

    public float getSkyBrightness(float par1) {
        final float var2 = FMLClientHandler.instance().getClient().world.getCelestialAngle(par1);
        float var3 = 1.0F - (MathHelper.sin(var2 * Constants.twoPI) * 2.0F + 0.25F);

        if (var3 < 0.0F) {
            var3 = 0.0F;
        }

        if (var3 > 1.0F) {
            var3 = 1.0F;
        }

        return var3 * var3 * 1F;
    }

}