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
	
	
	
	
	
	
	//..this is where renderstars used to be..
	
	
	
	
	

	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		
		
		
		
		
		
		//
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GlStateManager.disableRescaleNormal();
        GL11.glColor3f(1F, 1F, 1F);
        //final Tessellator var23 = Tessellator.getInstance();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(0, 0, 0);
        GL11.glCallList(this.glSkyList);
        
        
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        
        
        
        
        
        
        
        
        
        
       
        
        
        
//        // Helius in the sky
//        GL11.glPushMatrix();
//
//        // Get vertical position difference from horizon
//        double playerHorizon = mc.player.getPositionEyes(partialTicks).y - world.getHorizon();
//
//        // Slight rotation to make it tilt
//        GL11.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);
//
//        // Spin with sky
//        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 0.0F, 1.0F);
//
//        // Position and scale the planet
//        float planetScale = 150.0F;
//        float planetDistance = 110.0F;
//
//        // Apply horizon-based Y positioning
//        GL11.glTranslatef(planetDistance, (float) playerHorizon - 40.0F, -100.0F);
//        GL11.glScalef(planetScale, planetScale, planetScale);
//
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        mc.renderEngine.bindTexture(planetBody);
//        Tessellator tess = Tessellator.getInstance();
//        BufferBuilder buffer = tess.getBuffer();
//
//        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//        buffer.pos(-1.0D, -1.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
//        buffer.pos(1.0D, -1.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
//        buffer.pos(1.0D, 1.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
//        buffer.pos(-1.0D, 1.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
//        tess.draw();
//
//        GL11.glPopMatrix();
//
//        System.out.println("[SkyProvider] Rendered Helius at distance " + planetDistance + ", scale " + planetScale + ", Y offset from horizon: " + playerHorizon);
//        
//        
        
        
        


        
        
        
        
        
        
        
        
        
        
        
        
        
        //REMOVE THIS IF NON FUCTIONAL
        
        

//        GlStateManager.disableFog();
//        GlStateManager.disableAlpha();
//        GlStateManager.enableBlend();
//        mc.getTextureManager().bindTexture(new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/deepintheheart.png"));
//
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder buffer3 = tessellator.getBuffer();
//        buffer3.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//
//  float size = 100.0F; // Size of the cube
//
//     // Front face
//     buffer3.pos(-size, -size, -size).tex(0, 0).endVertex();
//     buffer3.pos(-size,  size, -size).tex(0, 1).endVertex();
//     buffer3.pos( size,  size, -size).tex(1, 1).endVertex();
//     buffer3.pos( size, -size, -size).tex(1, 0).endVertex();
//
//     // Back face
//     buffer3.pos( size, -size, size).tex(0, 0).endVertex();
//     buffer3.pos( size,  size, size).tex(0, 1).endVertex();
//     buffer3.pos(-size,  size, size).tex(1, 1).endVertex();
//     buffer3.pos(-size, -size, size).tex(1, 0).endVertex();
//
//     // Left face
//     buffer3.pos(-size, -size, size).tex(0, 0).endVertex();
//     buffer3.pos(-size,  size, size).tex(0, 1).endVertex();
//     buffer3.pos(-size,  size, -size).tex(1, 1).endVertex();
//     buffer3.pos(-size, -size, -size).tex(1, 0).endVertex();
//
//     // Right face
//     buffer3.pos(size, -size, -size).tex(0, 0).endVertex();
//     buffer3.pos(size,  size, -size).tex(0, 1).endVertex();
//     buffer3.pos(size,  size, size).tex(1, 1).endVertex();
//     buffer3.pos(size, -size, size).tex(1, 0).endVertex();
//
//     // Top face
//     buffer3.pos(-size, size, -size).tex(0, 0).endVertex();
//     buffer3.pos(-size, size, size).tex(0, 1).endVertex();
//     buffer3.pos( size, size, size).tex(1, 1).endVertex();
//     buffer3.pos( size, size, -size).tex(1, 0).endVertex();
//
//     // Bottom face
//     buffer3.pos(-size, -size, size).tex(0, 0).endVertex();
//     buffer3.pos(-size, -size, -size).tex(0, 1).endVertex();
//     buffer3.pos( size, -size, -size).tex(1, 1).endVertex();
//     buffer3.pos( size, -size, size).tex(1, 0).endVertex();
//
//        tessellator.draw();
//
//        GlStateManager.enableAlpha();
//        GlStateManager.disableBlend();
//        
//    	System.out.println("CUBE_RENDER_PROCESS");;       
//        
//        
//        
//     //..   
        
        
        
        
        
        
        
        
        
        
        
        
        
        //
        
        
        
        
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		Vec3d vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
		float f1 = (float) vec3.x;
		float f2 = (float) vec3.y;
		float f3 = (float) vec3.z;
		float f6;
		
		//
		
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//		Vec3d vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
//		float f1 = (float) vec3.x;
//		float f2 = (float) vec3.y;
//		float f3 = (float) vec3.z;
//		float f6;

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
		//ANNOTATED THIS OUT 6/30/25
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

		
		
		
		
		
		
		


//		if (f18 > 0.0F) {
//			GL11.glPushMatrix();
//			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//			GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//			GL11.glRotatef(-19.0F, 0, 1.0F, 0);
//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F); // Always full brightness
//			GL11.glCallList(this.starList);
//			GL11.glPopMatrix();
//			}	
			
			
//			GL11.glPushMatrix();
//			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
//			GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
//			GL11.glRotatef(-19.0F, 0, 1.0F, 0);
//			GL11.glColor4f(f18, f18, f18, f18);
//			GL11.glCallList(this.starList);
//			GL11.glPopMatrix();
	
		
		
		
		
		
		
		
		
		
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		
		
		
		
		
		
		
		
		
		

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
		//there is an error where a layer of the sun is not following the sun
		//GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		//GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
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
		
		//render sun texture
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
		
		
		
		
		
		
		
		
	
		
		

		
		
		
		
		
		
		 //BEGIN
        
         //Required variables
	     // float celAng;                        // Celestial angle [0.0, 1.0]
	     // float y0 = ...;                      // Base Y offset for the ring
	     // float y = ...;                       // Height of the rainbow ring
	     // float uPer = ...;                    // UV horizontal scale per angle segment
	     // int angles = ...;                    // Number of ring segments
	     // float anglePer = 360F / angles;     // Degrees between segments
	     // ResourceLocation ringWorld = ...;   // Your rainbow texture
	     // Minecraft mc = Minecraft.getMinecraft();
	     // World world = mc.world;
	      Tessellator tessellator4 = Tessellator.getInstance();

	     GlStateManager.pushMatrix();
	     
	     //testing..
	     GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 0.0F, 1.0F);

	     GlStateManager.enableBlend();
	     GlStateManager.disableDepth();
	     GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);


	     
	     Tessellator tess6 = Tessellator.getInstance();
	     BufferBuilder buffer6 = tess.getBuffer();
	     
	     
	     
	     GlStateManager.enableTexture2D();
	     mc.renderEngine.bindTexture(ringWorld);

	     float radius = 10F;

	     // Fade effect depending on time of day
	     float effCelAng1 = 360F;
	     //ABOVE LINE WAS CHANGED FROM celAng
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
	     int angles = 64;                // Number of vertical ring segments
	     float anglePer = 360F / angles; // Degrees per segment
	     float uPer = 1.0F / angles;     // Texture step for UVs
	     float y = 2.0F;                 // Height of each quad in the ring(retrospectively height)

	     
	     
	     
	     for (int i = 0; i < angles; i++) {
	         int j = (i % 2 == 0) ? i - 1 : i;
	         float ang = j * anglePer;
	         double xp = Math.cos(Math.toRadians(ang)) * radius;
	         double zp = Math.sin(Math.toRadians(ang)) * radius;
	         double yo = 0;

	         //float ut = ang * uPer;
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
	     
//			float f17;
//			f17 = 20F;
//			
//			
//			
//			// === Rainbow
//			GlStateManager.pushMatrix();
//			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
//			mc.renderEngine.bindTexture(ringWorld);
//			f17 = 10F;
//			float effCelAng1 = celAng;
//			if(effCelAng1 > 0.25F)
//				effCelAng1 = 1F - effCelAng1;
//			effCelAng1 = 0.25F - Math.min(0.25F, effCelAng1);
	//
//			long time = world.getWorldTime() + 1000;
//			int day = (int) (time / 24000L);
//			Random rand = new Random(day * 0xFF);
//			float angle1 = rand.nextFloat() * 360F;
//			float angle2 = rand.nextFloat() * 360F;
//			GlStateManager.color(1F, 1F, 1F, effCelAng1 * (1));
//			GlStateManager.rotate(angle1, 0F, 1F, 0F);
//			GlStateManager.rotate(angle2, 0F, 0F, 1F);
	//
//			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//			for(int i = 0; i < angles; i++) {
//				int j = i;
//				if(i % 2 == 0)
//					j--;
	//
//				float ang = j * anglePer;
//				double xp = Math.cos(ang * Math.PI / 180F) * f17;
//				double zp = Math.sin(ang * Math.PI / 180F) * f17;
//				double yo = 0;
	//
//				float ut = ang * uPer;
//				if(i % 2 == 0) {
//					tessellator.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
//					tessellator.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
//				} else {
//					tessellator.getBuffer().pos(xp, yo + y0, zp).tex(ut, 0).endVertex();
//					tessellator.getBuffer().pos(xp, yo + y0 + y, zp).tex(ut, 1F).endVertex();
//				}
	//
//			}
//			tessellator.draw();
//			GlStateManager.popMatrix();
//			GlStateManager.color(1F, 1F, 1F, 1F);
//			GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
	        
	        
	        
	        
	        
	        
	        
			
			//float f18 = world.getStarBrightness(partialTicks);

	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	       
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        //END
	        
	     
	     
	    //THIS WORKS PROPERLY
		//render object start, Helius
	     
        GL11.glPushMatrix();
        GL11.glRotatef(-30.0F, 0.0F, 1.0F, 0.0F);


       //spin slightly with sky
        GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 0.0F, 0.0F, 1.0F);

        // Position and scale the planet
        float planetScale = 200.0F; // adjust to make object bigger or smaller
        float planetDistance = 100.0F; // adjusts objects distance from center point
        float planetDistanceZ = 110.0F; // increase to push object farther back in the sky
        float verticalOffset = -0.0F;  // controls up/down

        GL11.glTranslatef(0.0F, verticalOffset, -planetDistanceZ); // method to push object on Z axis

        GL11.glTranslatef(planetDistance, -30.0F, -100.0F);
        GL11.glScalef(planetScale, planetScale, planetScale);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        mc.renderEngine.bindTexture(planetBody);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(-1.0D, -1.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        buffer.pos(1.0D, -1.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        buffer.pos(1.0D, 1.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        buffer.pos(-1.0D, 1.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tess.draw();

        GL11.glPopMatrix();
        
        //render object end, Helius
        
        //System.out.println("[SkyProvider] Rendered Helius at " + planetDistanceZ + " units distance and scale " + planetScale);
    
		
		
	     
	     
        
        
        
        
        
        
        
        
	     
        
        
        
        
        
        
        //BEGIN
        
     // Render rotated inner sky sphere
        GL11.glPushMatrix();

        // Disable depth and fog so it always renders on top of background
        GlStateManager.disableDepth();
        GL11.glDisable(GL11.GL_FOG);

        // Texture settings
        mc.renderEngine.bindTexture(skyCloudMatrix);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        // Sphere settings
        float radius1 = 180F;
        int latBands = 32;
        int longBands = 32;

        // Apply rotations
        float celestialAngle = world.getCelestialAngle(partialTicks) * 360.0F;
        GL11.glRotatef(90F, 1F, 0F, 0F); // Tilt the sphere to the side (rotate around X)
        GL11.glRotatef(celestialAngle, 0F, 1F, 0F); // Rotate with sun/stars around Y

        // Initialize tessellator
        //Tessellator tess = Tessellator.getInstance();
       // BufferBuilder buffer = tess.getBuffer();

        // Render sphere using triangle strips
        for (int lat = 0; lat <= latBands; lat++) {
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

                buffer.pos(x1 * radius1, y1 * radius1, z1 * radius1).tex((float) lon / longBands, (float) lat / latBands).endVertex();
                buffer.pos(x2 * radius1, y2 * radius1, z2 * radius1).tex((float) lon / longBands, (float) (lat + 1) / latBands).endVertex();
            }
            tess.draw();
        }

        // Restore GL state
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.enableDepth();
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();

        //System.out.println("[SkyProvider] Rendered side-facing, spinning inner sky sphere.");
        //END
        
//		
//	   //Render red cloud layer
//	        GL11.glPushMatrix();
//	        // Position above player
//	        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
//	        //..
//	        GL11.glDisable(GL11.GL_TEXTURE_2D);
//	        GL11.glEnable(GL11.GL_BLEND);
//	        GL11.glDisable(GL11.GL_ALPHA_TEST);
//	        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//	        GL11.glShadeModel(GL11.GL_SMOOTH);
//	        
//	        
//	        
//
//	        Tessellator tess2 = Tessellator.getInstance();
//	        BufferBuilder buffer2 = tess2.getBuffer();
//	        //tess2 and buffer2 are products of overlapping fields. 
//	        //star render was moved above render because I wanted to test 
//	        //if it would make the sky object work.
//
//	        //slow scroll movement
//	        double scroll = (world.getTotalWorldTime() % 4000L) / 4000.0D * 60.0D;
//	        GL11.glTranslatef((float) -scroll, 0.0F, 0.0F);
//
//	        float cloudHeight = 120.0F;
//	        float cloudRadius = 180.0F;
//	        float cloudAlpha = 0.4F;
//	        
//	        buffer2.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
//	        buffer2.pos(0.0D, cloudHeight, 0.0D).color(1.0F, 0.2F, 0.2F, cloudAlpha).endVertex(); // center (red)
//
//	            for (int i = 0; i <= 16; i++) {
//	                double angle = i * Math.PI * 2.0 / 16.0;
//	                double x = Math.cos(angle) * cloudRadius;
//	                double z = Math.sin(angle) * cloudRadius;
//	                buffer2.pos(x, cloudHeight, z).color(9.0F, 0.0F, 0.0F, 0.0F).endVertex(); // fade out
//	            //changed 1 to 9
//	            
//	        }
//
//	        tess2.draw();
//
//	        GL11.glShadeModel(GL11.GL_FLAT);
//	        GL11.glDisable(GL11.GL_BLEND);
//	        GL11.glEnable(GL11.GL_ALPHA_TEST);
//	        GL11.glEnable(GL11.GL_TEXTURE_2D);
//	        GL11.glPopMatrix();
//	        
//	        
//	        System.out.println("[SkyProviderHollows] Rendered red cloud layer at height " + cloudHeight + " with radius " + cloudRadius);
//
//	        
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

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