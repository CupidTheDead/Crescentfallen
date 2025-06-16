package fracture.mod.planets;

import java.util.LinkedList;
import java.util.List;

import fracture.mod.AddonConfig;
import fracture.mod.init.AddonDimensions;
import fracture.mod.init.AddonPlanets;
import fracture.mod.init.BlockInit;
import fracture.mod.planets.thefracture.ChunkProviderthefracture;
import fracture.mod.planets.thefracture.SkyObjectTestTheFracture;
import fracture.mod.planets.thefracture.SkyProviderthefracture;
import fracture.mod.planets.thefracture.thefracture.biome.BiomeProviderTheFracture;
import fracture.mod.world.chunk.ChunkProviderBase;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeAdaptive;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.WorldProviderSpace;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.ISolarLevel;
import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;
import scala.tools.nsc.interactive.Problem;

public class WorldProviderTheFracture extends WorldProviderSpace implements IGalacticraftWorldProvider, ISolarLevel {
			//added implement for IGalacticraftWorldProvider

	
	
	
	
	//this is 6/10/25
	
	
	
	//   protected void renderSky()
	 //   {
	 //       this.setSkyRenderer(new SkyProviderthefracture(this.getSolarSize()));
	//    }
	
	   
	   //the sky provider isent even refrenced here, or anywhere..
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	//public WorldProviderTheFracture() {
	//	   this.setSkyRenderer(new SkyObjectTestTheFracture());
	//	}

	    	//ABOVE IS NEW CODE!! 6/3/2025
	   // this is the Problem. its trying to render this code serverside, but it needs to be registered clientside.
	   //you have created a test instance named 'skyobjecttestthefracture'
	   //remember to replace the code for skyprovider the fracture with your own.
	    //after you tried to put a box around yourself that followed the player and hung in the sky(and failed),
	    //your ultiment goal was to create a box in the sky, which when the code you wrote for that did not work,
	    //you moved on to trying to get a flat texture in the sky.
	    //when that didnt work, you tried to get any change whatsoever out of the sky_provider.
	    //no luck. dont even know if its set up correctly. it should be, ive quadruple checked and it
	    //seems right? none of this shit works.
	    
	    //there are several other contained edits in client proxy, skyproviderthefracture, skyobjecttestthefracture.
	    
	    //https://github.com/MJRLegends/ExtraPlanets/blob/dev_1.12.2/src/main/java/com/mjr/extraplanets/planets/Saturn/WorldProviderSaturn.java
	    //https://github.com/SteveKunG/MorePlanets/blob/b1874a7773f70af8cecff47b1bd434f47ac9a90d/src/main/java/stevekung/mods/moreplanets/core/event/WorldTickEventHandler.java#L49
	    //https://github.com/Quatroctus/Blockstate-Generator/tree/master/BlockStateGen/src/dev/anime/blockstate/generator
	    //https://forums.minecraftforge.net/topic/74316-using-custom-sky-graphics-sunmoon-textures-sizes-positions-etc/
	    
	
	@Override
	public Vector3 getFogColor() {
		return new Vector3(.5, .2, .0);
	}
	

	@Override
	public Vector3 getSkyColor() {
		return new Vector3(.5, .2, .1);
	}

	@Override
	public boolean canRainOrSnow() {
		return false;
	}

	@Override
	public void updateWeather() {
		super.updateWeather();
	}

	@Override
	public boolean hasSunset() {
		return true;
	}

	@Override
	public long getDayLength() {
		return 24000L;
	}

	@Override
	public boolean shouldForceRespawn() {
		return true;
	}

	//@Override
	//public Class<? extends IChunkGenerator> getChunkProviderClass() {
		//return ChunkProviderthefracture.class;
        //return (Class<? extends IChunkGenerator>) ChunkProviderBase.class;
    //}
	
    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass() {
    	return ChunkProviderthefracture.class;
    	//if it doesn't work, change getChunkProviderClass to createChunkGenerator
	}

	@Override
	public Class<? extends BiomeProvider> getBiomeProviderClass() {
		BiomeAdaptive.setBodyMultiBiome(AddonPlanets.planetTwoS1);
		return BiomeProviderTheFracture.class;
	}

	@Override
	public int getAverageGroundLevel() {
		return 63;
	}

	@Override
	public boolean canCoordinateBeSpawn(int var1, int var2) {
		return true;
	}

	@Override
	public float getGravity() {
		return 0.000F;
	}

	@Override
	public int getHeight() {
		return 800;
	}

	@Override
	public double getMeteorFrequency() {
		return 0.0D;
	}

	@Override
	public double getFuelUsageMultiplier() {
		return 1.0D;
	}

	@Override
	public boolean canSpaceshipTierPass(int tier) {
		return tier >=3;
		//return tier >= AddonConfig.addon_planet_settings.planetTwoTier;
	}

	@Override
	public float getFallDamageModifier() {
		return 0.10F;
	}

	@Override
	public float getSoundVolReductionAmount() {
		return 1.0F;
	}

	@Override
	public CelestialBody getCelestialBody() {
		return AddonPlanets.planetTwoS1;
	}

	@Override
	public boolean hasBreathableAtmosphere() {
		return true;
	}

	@Override
	public float getThermalLevelModifier() {
		return 0.0F;
	}

	@Override
	public float getWindLevel() {
		return 0.0F;
	}

	@Override
	public double getSolarEnergyMultiplier() {
		return 2.0F;
	}

	@Override
	public boolean shouldDisablePrecipitation() {
		return true;
	}

	@Override
	public boolean shouldCorrodeArmor() {
		return true;
	}

	@Override
	public DimensionType getDimensionType() {
		return AddonDimensions.dimPlanetTwoS1;
	}

	@Override
	public boolean isDaytime() {
		final float a = this.world.getCelestialAngle(0F);
		return a < 0.42F || a > 0.58F;
	}

	@Override
	public int getDungeonSpacing() {
		return 0;
	}

	@Override
	public ResourceLocation getDungeonChestType() {
		return null;
	}

	@Override
	public List<Block> getSurfaceBlocks() {
		List<Block> list = new LinkedList<>();
		  list.add(BlockInit.SURFACE_FRACTURE);
		  list.add(BlockInit.STONE_FRACTURE);
		  list.add(BlockInit.DRIED_DIRT);
		  list.add(Blocks.GRAVEL);
		 list.add(MarsBlocks.marsBlock);
		 list.add(Blocks.STONE);
		 list.add(Blocks.COBBLESTONE_WALL);
		 list.add(Blocks.MOSSY_COBBLESTONE);
		 list.add(Blocks.OBSIDIAN);
		 list.add(Blocks.MYCELIUM);
		 //...
		
		return list;
	}
}

