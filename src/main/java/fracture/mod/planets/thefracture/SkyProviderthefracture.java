package fracture.mod.planets.thefracture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import fracture.mod.CFInfo;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
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

public class SkyProviderthefracture extends IRenderHandler {

    private static final ResourceLocation sunTexture = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/triopas.png");
    private static final ResourceLocation konaFractureSky = new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/kona.png");
    private static final ResourceLocation smokeTexture = new ResourceLocation("textures/environment/clouds.png");

    public int starList;
    public int glSkyList;
    public int glSkyList2;
    private float sunSize;
    
    // Meteor Logic
    private List<OrbitingMeteor> meteors = new ArrayList<>();
    private Random rand = new Random();
    private long lastFrameTime = System.nanoTime();

    public SkyProviderthefracture(IGalacticraftWorldProvider fractureProvider) {
        this.sunSize = 6.5F * fractureProvider.getSolarSize() * 7;

        int displayLists = GLAllocation.generateDisplayLists(3);
        this.starList = displayLists;
        this.glSkyList = displayLists + 1;
        this.glSkyList2 = displayLists + 2;

        // 1. COMPILE STAR LIST
        GL11.glPushMatrix();
        GL11.glNewList(this.starList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();

        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();

        // 2. COMPILE SKY BOX (UP)
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

        // 3. COMPILE SKY BOX (DOWN)
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
        long currentFrameTime = System.nanoTime();
        float deltaTime = (currentFrameTime - lastFrameTime) / 1_000_000_000.0F;
        lastFrameTime = currentFrameTime;

        GlStateManager.pushMatrix();
        try {
            GlStateManager.disableTexture2D();
            GlStateManager.disableRescaleNormal();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            GlStateManager.color(1F, 1F, 1F);

            // --- 1. RENDER DARK BACKGROUND ---
            GlStateManager.color(0, 0, 0);
            GL11.glCallList(this.glSkyList);
            
            Vec3d vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
            float f1 = (float) vec3.x;
            float f2 = (float) vec3.y;
            float f3 = (float) vec3.z;

            if (mc.gameSettings.anaglyph) {
                float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                f1 = f4;
                f2 = f5;
                f3 = f6;
            }

            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GlStateManager.color(f1, f2, f3);
            GL11.glCallList(this.glSkyList);

            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            RenderHelper.disableStandardItemLighting();

            // --- 2. RENDER STARS ---
            float starBrightness = world.getStarBrightness(partialTicks);
            // MODIFICATION: Reduced minimum brightness to 0.05F (very dim during day)
            float renderStarBrightness = Math.max(starBrightness, 0.05F);
            
            if (renderStarBrightness > 0.0F) {
                GlStateManager.pushMatrix();
                long worldTime = world.getTotalWorldTime();
                float baseRotation = (worldTime % 24000L) / 24000.0F * 360.0F;
                float cumulativeRotation = (worldTime / 24000.0F) * 360.0F;
                float starRotation = baseRotation + cumulativeRotation * 0.01F;
                float wobbleX = (float) Math.sin(worldTime / 2000.0) * 39.0F;
                float wobbleZ = (float) Math.cos(worldTime / 2500.0) * 50.0F;

                GlStateManager.rotate(starRotation, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(wobbleX, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(wobbleZ, 0.0F, 0.0F, 1.0F);

                GlStateManager.color(1.0F, 1.0F, 1.0F, renderStarBrightness); 
                GL11.glCallList(this.starList);
                GlStateManager.popMatrix();
            }

            // --- 3. RENDER SUN & AURA ---
            GlStateManager.pushMatrix();
            // ROTATION
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);

            // SETUP STATES
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableCull();

            float[] afloat = new float[4];
            afloat[0] = 255 / 255.0F; 
            afloat[1] = 194 / 255.0F; 
            afloat[2] = 180 / 255.0F; 
            afloat[3] = 0.3F;         

            float sunBright = 1.0F - starBrightness;
            float sunAlpha = 0.6F; 

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder worldRenderer = tessellator.getBuffer();
            
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
            worldRenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            
            double auraDistance = 100.0D;
            
            // Center of Aura with translucency
            worldRenderer.pos(0.0D, auraDistance, 0.0D).color(afloat[0], afloat[1], afloat[2], afloat[3] * 2 * sunBright * sunAlpha).endVertex();
            
            // Edges of Aura
            float auraSize = 45.0F;
            
            worldRenderer.pos(-auraSize, auraDistance, -auraSize).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos(0, auraDistance, (double) -auraSize * 1.5F).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos(auraSize, auraDistance, -auraSize).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos((double) auraSize * 1.5F, auraDistance, 0).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos(auraSize, auraDistance, auraSize).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos(0, auraDistance, (double) auraSize * 1.5F).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos(-auraSize, auraDistance, auraSize).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos((double) -auraSize * 1.5F, auraDistance, 0).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            worldRenderer.pos(-auraSize, auraDistance, -auraSize).color(1.0F, 0.3F, 0.1F, 0.0F).endVertex();
            
            tessellator.draw();
            GlStateManager.shadeModel(GL11.GL_FLAT);

            // RENDER MAIN SUN TEXTURE with translucency
            GlStateManager.enableTexture2D(); 
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, sunAlpha);
            
            double sunBodyDistance = 95.0D; 
            float sSize = this.sunSize; 
            
            mc.renderEngine.bindTexture(SkyProviderthefracture.sunTexture);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-sSize, sunBodyDistance, -sSize).tex(0.0D, 0.0D).endVertex();
            worldRenderer.pos(sSize, sunBodyDistance, -sSize).tex(1.0D, 0.0D).endVertex();
            worldRenderer.pos(sSize, sunBodyDistance, sSize).tex(1.0D, 1.0D).endVertex();
            worldRenderer.pos(-sSize, sunBodyDistance, sSize).tex(0.0D, 1.0D).endVertex();
            tessellator.draw();
            
            GlStateManager.enableCull(); 
            GlStateManager.popMatrix(); 
            
            // --- METEORS ---
            this.updateAndRenderMeteors(deltaTime);
            
            // --- SMOKE CLOUD LAYER ---
            this.renderSmokeLayer(world.getTotalWorldTime(), partialTicks);

            // --- 4. RENDER CUSTOM PLANET (KONA) ---
            GlStateManager.pushMatrix();
            GlStateManager.rotate(34.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(200F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 0.0F, 1.0F);

            float planetScale = 7.0F;
            float planetDistance = 5.0F;
            float planetDistanceZ = 110.0F;
            float verticalOffset = -17.0F;

            GlStateManager.translate(0.0F, verticalOffset, -planetDistanceZ);
            GlStateManager.translate(planetDistance, -30.0F, -100.0F);
            GlStateManager.scale(planetScale, planetScale, planetScale);

            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.renderEngine.bindTexture(konaFractureSky);

            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-1.0D, -1.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
            worldRenderer.pos(1.0D, -1.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
            worldRenderer.pos(1.0D, 1.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
            worldRenderer.pos(-1.0D, 1.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();

        } finally {
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
            GlStateManager.popMatrix(); 
        }
    }

    private void renderStars() {
        final Random rand = new Random(10842L);
        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < 65000; ++i) {
            double x = rand.nextFloat() * 2.0F - 1.0F;
            double y = rand.nextFloat() * 2.0F - 1.0F;
            double z = rand.nextFloat() * 2.0F - 1.0F;
            final double size = 0.15F + rand.nextFloat() * 0.1F;
            double mag = x * x + y * y + z * z;

            if (mag < 1.0D && mag > 0.01D) {
                mag = 1.0D / Math.sqrt(mag);
                x *= mag;
                y *= mag;
                z *= mag;

                float temp = rand.nextFloat();
                float r, g, b;

                if (temp < 0.33F) {
                    float t = temp / 0.33F;
                    r = 1.0F; g = 0.2F + t * 0.5F; b = 0.1F + t * 0.1F;
                } else if (temp < 0.66F) {
                    float t = (temp - 0.33F) / 0.33F;
                    r = 1.0F; g = 0.7F + t * 0.3F; b = 0.2F - t * 0.1F;
                } else {
                    float t = (temp - 0.66F) / 0.34F;
                    r = 1.0F; g = 1.0F; b = 0.1F + t * 0.9F;
                }

                double dist = rand.nextDouble();
                double xOffset = x * dist * 5.0D;
                double yOffset = y * dist * 5.0D;
                double zOffset = z * dist * 5.0D;
                final double px = x * (rand.nextDouble() * 150D + 130D) + xOffset;
                final double py = y * (rand.nextDouble() * 150D + 130D) + yOffset;
                final double pz = z * (rand.nextDouble() * 150D + 130D) + zOffset;

                boolean pulsar = rand.nextDouble() < 0.01;
                float alpha = pulsar ? 1.0F : 0.6F + rand.nextFloat() * 0.4F;

                final double azimuth = Math.atan2(x, z);
                final double sinAz = Math.sin(azimuth);
                final double cosAz = Math.cos(azimuth);
                final double inclination = Math.atan2(Math.sqrt(x * x + z * z), y);
                final double sinIncl = Math.sin(inclination);
                final double cosIncl = Math.cos(inclination);
                final double angle = rand.nextDouble() * Math.PI * 2.0D;
                final double sinAng = Math.sin(angle);
                final double cosAng = Math.cos(angle);

                for (int j = 0; j < 4; ++j) {
                    final double dx = ((j & 2) - 1) * size;
                    final double dz = ((j + 1 & 2) - 1) * size;
                    final double vx = dx * cosAng - dz * sinAng;
                    final double vz = dz * cosAng + dx * sinAng;
                    final double vy = vx * sinIncl;
                    final double rx = -vx * cosIncl;
                    final double fx = rx * sinAz - vz * cosAz;
                    final double fz = vz * sinAz + rx * cosAz;
                    buffer.pos(px + fx, py + vy, pz + fz).color(r, g, b, alpha).endVertex();
                }
            }
        }
        tessellator.draw();
    }
    
    private void renderSmokeLayer(long worldTime, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        // Standard blending
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        
        double scrollSpeed = worldTime % 24000L / 12000.0D; 
        double scrollX = scrollSpeed + (partialTicks / 12000.0D);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(smokeTexture);
        
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        
        double height = 40.0D; 
        double size = 200.0D;
        
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F); 
        GlStateManager.translate(0.0, -height, 0.0);
        
        // MODIFICATION: Render smoke in a grid to fade out edges (dither)
        buff.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        
        double repeats = 4.0D;
        int gridDivisions = 16;
        double segmentSize = (size * 2) / gridDivisions;
        double texSegment = repeats / gridDivisions;
        
        for (int x = 0; x < gridDivisions; x++) {
            for (int z = 0; z < gridDivisions; z++) {
                double x1 = -size + x * segmentSize;
                double z1 = -size + z * segmentSize;
                double x2 = x1 + segmentSize;
                double z2 = z1 + segmentSize;
                
                // Calculate distance from center (0,0) for each vertex
                // We use the center point of the quad to approximate alpha for the whole quad
                // Or calculate per vertex for smoother gradient
                
                // Colors
                float r = 0.2F;
                float g = 0.15F;
                float b = 0.25F;
                float maxAlpha = 0.4F;
                
                // Helper to get alpha based on distance
                // At 0 dist, alpha = maxAlpha. At 'size' dist, alpha = 0.
                
                float a1 = getCloudAlpha(x1, z1, size, maxAlpha);
                float a2 = getCloudAlpha(x1, z2, size, maxAlpha);
                float a3 = getCloudAlpha(x2, z2, size, maxAlpha);
                float a4 = getCloudAlpha(x2, z1, size, maxAlpha);
                
                double u1 = scrollX + (x * texSegment);
                double u2 = scrollX + ((x+1) * texSegment);
                double v1 = z * texSegment;
                double v2 = (z+1) * texSegment;

                buff.pos(x1, 0, z1).tex(u1, v1).color(r, g, b, a1).endVertex();
                buff.pos(x1, 0, z2).tex(u1, v2).color(r, g, b, a2).endVertex();
                buff.pos(x2, 0, z2).tex(u2, v2).color(r, g, b, a3).endVertex();
                buff.pos(x2, 0, z1).tex(u2, v1).color(r, g, b, a4).endVertex();
            }
        }

        tess.draw();
        
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
    private float getCloudAlpha(double x, double z, double maxSize, float maxAlpha) {
        double dist = Math.sqrt(x*x + z*z);
        // Start fading at 50% distance, fade to 0 at 100% distance
        double fadeStart = maxSize * 0.5D;
        if (dist < fadeStart) {
            return maxAlpha;
        } else if (dist >= maxSize) {
            return 0.0F;
        } else {
            double ratio = (dist - fadeStart) / (maxSize - fadeStart);
            return maxAlpha * (1.0F - (float)ratio);
        }
    }
    
    private static class OrbitingMeteor {
        float angle;      
        float speed;      
        float radius;     
        float tiltX, tiltZ;
        float r, g, b;    
        float size;
        
        // MODIFICATION: Lifespan logic
        float age = 0;
        float maxAge;
        
        public OrbitingMeteor() {
            Random r = new Random();
            this.angle = r.nextFloat() * (float)Math.PI * 2;
            this.speed = 0.5F + r.nextFloat() * 0.5F; 
            this.radius = 80.0F + r.nextFloat() * 10.0F; 
            
            this.tiltX = r.nextFloat() * 360.0F;
            this.tiltZ = r.nextFloat() * 360.0F;
            
            this.r = 1.0F;
            this.g = 0.5F + r.nextFloat() * 0.5F;
            this.b = 0.2F;
            this.size = 2.0F + r.nextFloat() * 3.0F;
            
            // MODIFICATION: Reduced max age to 1.0-2.5 seconds
            this.maxAge = 1.0F + r.nextFloat() * 1.5F;
        }
    }

    private void updateAndRenderMeteors(float deltaTime) {
        if (rand.nextFloat() < 0.005F) {
            meteors.add(new OrbitingMeteor());
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GlStateManager.disableDepth(); 

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buff = tess.getBuffer();
        
        buff.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        Iterator<OrbitingMeteor> it = meteors.iterator();
        while (it.hasNext()) {
            OrbitingMeteor m = it.next();
            
            m.angle += m.speed * deltaTime;
            m.age += deltaTime;
            
            // MODIFICATION: Remove if too old
            if (m.age > m.maxAge) {
                it.remove();
                continue;
            }
            
            // Calculate Fade based on Age (Fade In / Fade Out)
            float lifeRatio = m.age / m.maxAge;
            // Sine wave fade: Starts at 0, goes to 1, ends at 0
            float lifeAlpha = (float) Math.sin(lifeRatio * Math.PI);

            int trailSegments = 5;
            float trailLength = 0.3F; 
            
            for(int i = 0; i < trailSegments; i++) {
                float a1 = m.angle - (i * (trailLength/trailSegments));
                float a2 = m.angle - ((i+1) * (trailLength/trailSegments));
                
                float alpha1 = (1.0F - ((float)i / trailSegments)) * lifeAlpha;
                float alpha2 = (1.0F - ((float)(i+1) / trailSegments)) * lifeAlpha;

                Vec3d p1 = getOrbitPos(m.radius, a1, m.tiltX, m.tiltZ);
                Vec3d p2 = getOrbitPos(m.radius, a2, m.tiltX, m.tiltZ);
                
                buff.pos(p1.x, p1.y, p1.z).color(m.r, m.g, m.b, alpha1).endVertex();
                buff.pos(p2.x, p2.y, p2.z).color(m.r, m.g, m.b, alpha2).endVertex();
            }
        }
        
        tess.draw();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
    
    private Vec3d getOrbitPos(float radius, float angle, float tiltX, float tiltZ) {
        float x = (float)Math.cos(angle) * radius;
        float y = (float)Math.sin(angle) * radius;
        float z = 0;
        
        float radZ = (float)Math.toRadians(tiltZ);
        float x2 = x * (float)Math.cos(radZ) - y * (float)Math.sin(radZ);
        float y2 = x * (float)Math.sin(radZ) + y * (float)Math.cos(radZ);
        
        float radX = (float)Math.toRadians(tiltX);
        float y3 = y2 * (float)Math.cos(radX) - z * (float)Math.sin(radX);
        float z3 = y2 * (float)Math.sin(radX) + z * (float)Math.cos(radX);
        
        return new Vec3d(x2, y3, z3);
    }
}