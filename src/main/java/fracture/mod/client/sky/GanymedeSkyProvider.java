package fracture.mod.client.sky;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IRenderHandler;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class GanymedeSkyProvider extends IRenderHandler {

    private static final ResourceLocation SUN_TEXTURE = new ResourceLocation("galacticraftcore", "textures/gui/planets/atmosphericsun.png");
    private static final ResourceLocation PARENT_PLANET_TEXTURE = new ResourceLocation("galacticraftcore", "textures/gui/celestialbodies/jupiter.png");
    private static final ResourceLocation SKY_MATRIX_TEXTURE = new ResourceLocation("fracture", "textures/gui/celestialbodies/skylayer.png");


    private int starList;
    private final float sunSize = 17.5F;

    public GanymedeSkyProvider() {
        this.starList = GLAllocation.generateDisplayLists(1);
        GL11.glPushMatrix();
        GL11.glNewList(this.starList, GL11.GL_COMPILE);
        this.renderStarsInternal(); 
        GL11.glEndList();
        GL11.glPopMatrix();
    }

    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {

        GlStateManager.disableTexture2D();
        GlStateManager.disableFog();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);

        for (int k = 0; k < 6; ++k) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);

            buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);

            // Skylayer1 black
            buffer.pos(-100.0D, 100.0D, -100.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();
            buffer.pos(-100.0D, 100.0D, 100.0D).color(0.0F, 0.0F, 0.0F, 1.0F).endVertex();

            // Horizon purple
            buffer.pos(100.0D, 100.0D, 100.0D).color(0.4F, 0.0F, 0.6F, 1.0F).endVertex();
            buffer.pos(100.0D, 100.0D, -100.0D).color(0.4F, 0.0F, 0.6F, 1.0F).endVertex();

            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();


        // render internal skybox
        this.renderSkySphere(world, partialTicks, mc);
        
        // Stars
        float starBrightness = world.getStarBrightness(partialTicks);
        if (starBrightness > 0.0F) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-19.0F, 0, 1.0F, 0);
            
            GlStateManager.color(starBrightness, starBrightness, starBrightness, starBrightness);
            GlStateManager.callList(this.starList);
            GlStateManager.popMatrix();
        }

        // sun
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);

        // sun aura
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        
        float[] auraColor = new float[] { 255/255F, 194/255F, 180/255F, 0.3F };
        float f18 = 1.0F - starBrightness; 

        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(0.0D, 100.0D, 0.0D).color(auraColor[0] * f18, auraColor[1] * f18, auraColor[2] * f18, auraColor[3] * 2 / f18).endVertex();
        
        float sunAuraSize = 20.0F;
        buffer.pos(-sunAuraSize, 100.0D, -sunAuraSize).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(0, 100.0D, -sunAuraSize * 1.5F).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(sunAuraSize, 100.0D, -sunAuraSize).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(sunAuraSize * 1.5F, 100.0D, 0).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(sunAuraSize, 100.0D, sunAuraSize).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(0, 100.0D, sunAuraSize * 1.5F).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(-sunAuraSize, 100.0D, sunAuraSize).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(-sunAuraSize * 1.5F, 100.0D, 0).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        buffer.pos(-sunAuraSize, 100.0D, -sunAuraSize).color(auraColor[0], auraColor[1], auraColor[2], 0.0F).endVertex();
        tessellator.draw();
        
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        

        GlStateManager.disableTexture2D();
        GlStateManager.color(0.0F, 0.0F, 0.0F, 1.0F);
        float oldblock = this.sunSize / 3.5F;
        buffer.begin(7, DefaultVertexFormats.POSITION);
        buffer.pos(-oldblock, 99.9D, -oldblock).endVertex();
        buffer.pos(oldblock, 99.9D, -oldblock).endVertex();
        buffer.pos(oldblock, 99.9D, oldblock).endVertex();
        buffer.pos(-oldblock, 99.9D, oldblock).endVertex();
        tessellator.draw();

        // Sun Texture
        GlStateManager.enableTexture2D();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(SUN_TEXTURE);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-this.sunSize, 100.0D, -this.sunSize).tex(0.0D, 0.0D).endVertex();
        buffer.pos(this.sunSize, 100.0D, -this.sunSize).tex(1.0D, 0.0D).endVertex();
        buffer.pos(this.sunSize, 100.0D, this.sunSize).tex(1.0D, 1.0D).endVertex();
        buffer.pos(-this.sunSize, 100.0D, this.sunSize).tex(0.0D, 1.0D).endVertex();
        tessellator.draw();


        // Jupiter sky object
        float planetSize = (this.sunSize * 1.5F) * 0.35F;
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
        GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200F, 1.0F, 0.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1F);
        
        mc.renderEngine.bindTexture(PARENT_PLANET_TEXTURE);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-planetSize, -100.0D, planetSize).tex(0, 1).endVertex();
        buffer.pos(planetSize, -100.0D, planetSize).tex(1, 1).endVertex();
        buffer.pos(planetSize, -100.0D, -planetSize).tex(1, 0).endVertex();
        buffer.pos(-planetSize, -100.0D, -planetSize).tex(0, 0).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix(); 

        // Meteors
        this.renderMeteors(world, partialTicks);

        // Cleanup
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    private void renderSkySphere(WorldClient world, float partialTicks, Minecraft mc) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableFog();
        mc.renderEngine.bindTexture(SKY_MATRIX_TEXTURE);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha(); 

        // Sphere settings
        // Note: turn back upwards
        float radius = 180F;  
        int latBands = 32;
        int longBands = 32;

        // Rotation
        float celestialAngle = world.getCelestialAngle(partialTicks) * 360.0F;
        GlStateManager.rotate(90F, 1F, 0F, 0F); // Tilt axis
        GlStateManager.rotate(celestialAngle, 0F, 1F, 0F); // Spin with time(change this)
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();


        for (int lat = 0; lat < latBands; lat++) {
            float theta1 = (float) (Math.PI * lat / latBands);
            float theta2 = (float) (Math.PI * (lat + 1) / latBands);

            buffer.begin(GL11.GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_TEX);
            for (int lon = 0; lon <= longBands; lon++) {
                float phi = (float) (2 * Math.PI * lon / longBands);
                float cosPhi = (float) Math.cos(phi);
                float sinPhi = (float) Math.sin(phi);

                float x1 = (float) (Math.sin(theta1) * cosPhi);
                float y1 = (float) Math.cos(theta1);
                float z1 = (float) (Math.sin(theta1) * sinPhi);

                float x2 = (float) (Math.sin(theta2) * cosPhi);
                float y2 = (float) Math.cos(theta2);
                float z2 = (float) (Math.sin(theta2) * sinPhi);

                // Note: v coordinate is often inverted in Minecraft skies depending on texture orientation.
                // If it looks upside down, change lat/latBands to 1.0 - (lat/latBands)
                buffer.pos(x1 * radius, y1 * radius, z1 * radius).tex((float) lon / longBands, (float) lat / latBands).endVertex();
                buffer.pos(x2 * radius, y2 * radius, z2 * radius).tex((float) lon / longBands, (float) (lat + 1) / latBands).endVertex();
            }
            tessellator.draw();
        }


        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
    }

    private void renderStarsInternal() {
        Random rand = new Random(10842L);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 6000; ++i) {
            double d0 = rand.nextFloat() * 2.0F - 1.0F;
            double d1 = rand.nextFloat() * 2.0F - 1.0F;
            double d2 = rand.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + rand.nextFloat() * 0.1F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = rand.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = ((j & 2) - 1) * d3;
                    double d19 = ((j + 1 & 2) - 1) * d3;
                    double d20 = d18 * d16 - d19 * d15;
                    double d21 = d19 * d16 + d18 * d15;
                    double d22 = d20 * d12 + d17 * d13;
                    double d23 = d17 * d12 - d20 * d13;
                    double d24 = d23 * d9 - d21 * d10;
                    double d25 = d21 * d9 + d23 * d10;
                    buffer.pos(d5 + d24, d6 + d22, d7 + d25).endVertex();
                }
            }
        }
        tessellator.draw();
    }

    private void renderMeteors(WorldClient world, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        
        GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F); 
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        Random rand = new Random(432L); 
        long time = world.getTotalWorldTime();
        
        for (int i = 0; i < 15; i++) {
            float rotationX = rand.nextFloat() * 360F;
            float rotationY = rand.nextFloat() * 360F;
            float speed = 0.5F + rand.nextFloat(); 
            
            float progress = ((time + (i * 500)) * speed % 400) / 400F;
            
            if (progress < 0.8F) {
                GlStateManager.pushMatrix();
                
                GlStateManager.rotate(rotationX, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(rotationY, 0.0F, 1.0F, 0.0F);
                
                float angle = -60F + (progress * 120F);
                GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
                
                buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                
                double width = 0.4D;
                double length = 5.0D + rand.nextDouble() * 5.0D; 
                double height = 100.0D; 
                
                buffer.pos(-width, height, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
                buffer.pos(width, height, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
                
                buffer.pos(width, height, -length).color(1.0F, 1.0F, 1.0F, 0.0F).endVertex();
                buffer.pos(-width, height, -length).color(1.0F, 1.0F, 1.0F, 0.0F).endVertex();
                
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }
        
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}