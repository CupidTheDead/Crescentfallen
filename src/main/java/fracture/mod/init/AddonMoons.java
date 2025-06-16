
package fracture.mod.init;

import fracture.mod.AddonConfig;
import fracture.mod.ModInfo;
import fracture.mod.planets.WorldProviderHollows;
import fracture.mod.planets.WorldProviderTheFracture;
import fracture.mod.planets.hollows.hollows.biome.HollowsBiomes;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome.SpawnListEntry;


	public class AddonMoons extends Moon {


			public AddonMoons(String moonName) {
				super(moonName);
		// TODO Auto-generated constructor stub
	}
			
		public static Moon Kona;
	
			public static void init() {
				initializeMoons();
				registerMoons();
	}
	//public AddonMoons() {

		
		//{
		
		//this.createMoons(); {
		
		private void createMoons() {
			Kona = new Moon("Kona").setParentPlanet(AddonPlanets.planetTwoS1);
			//Kona = new Moon("Kona").setParentPlanet(AddonPlanets.planetTwoS1);
			Kona.setTierRequired(3);
			Kona.setRingColorRGB(0.1F, 0.9F, 0.6F);
			Kona.setPhaseShift((float) (Math.PI * 0.5F));
			Kona.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.9F, 0.9F));
			Kona.setRelativeOrbitTime(1.0F);
			Kona.setBodyIcon(new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/kona.png"));
			Kona.atmosphereComponent(EnumAtmosphericGas.CO2);
			Kona.setDimensionInfo(AddonConfig.Dimension.idMoon, WorldProviderHollows.class);
			Kona.setAtmosphere(new AtmosphereInfo(false, false, false, 5.0F, 0.0F, 0.1F));
			Kona.addChecklistKeys("space_suit", "equip_oxygen_suit", "equip_parachute");
			Kona.setBiomeInfo(HollowsBiomes.biomes);
				
			
		}
		
		
		
		private static void setMobInfo(CelestialBody body) {
			body.addMobInfo(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
			body.addMobInfo(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
			body.addMobInfo(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
			body.addMobInfo(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
			body.addMobInfo(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
			}

		private void registerPlanetTeleportTypes() {


			GalacticraftRegistry.registerTeleportType(WorldProviderTheFracture.class, new TeleportTypeMoon());
			
		}
		//}
		
		
		
		
		private static void initializeMoons() {
		
			
			 {
				Kona = new Moon("Kona").setParentPlanet(AddonPlanets.planetTwoS1);
				//Kona = new Moon("Kona").setParentPlanet(AddonPlanets.planetTwoS1);
				Kona.setTierRequired(3);
				Kona.setRingColorRGB(0.1F, 0.9F, 0.6F);
				Kona.setPhaseShift((float) (Math.PI * 0.5F));
				Kona.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.9F, 0.9F));
				Kona.setRelativeOrbitTime(1.0F);
				Kona.setBodyIcon(new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/kona.png"));
				Kona.atmosphereComponent(EnumAtmosphericGas.CO2);
				Kona.setDimensionInfo(AddonConfig.Dimension.idMoon, WorldProviderHollows.class);
				Kona.setAtmosphere(new AtmosphereInfo(false, false, false, 5.0F, 0.0F, 0.1F));
				Kona.addChecklistKeys("space_suit", "equip_oxygen_suit", "equip_parachute");
				Kona.setBiomeInfo(HollowsBiomes.biomes);
				Kona.addMobInfo(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
				Kona.addMobInfo(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
				Kona.addMobInfo(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
				Kona.addMobInfo(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
				Kona.addMobInfo(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
			 }
			
			}
			 
			 
			 
			 
			 
			 
			 private static void registerMoons() {
					 {
						GalaxyRegistry.registerMoon(Kona);
						GalacticraftRegistry.registerTeleportType(WorldProviderHollows.class, new TeleportTypeMoon());
						GalacticraftRegistry.registerRocketGui(WorldProviderHollows.class, new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/rocket_gui.png"));
					}

			//private void registermoon() {
			//GalaxyRegistry.registerMoon(Kona);
			//GalaxyRegistry.registerPlanet(planetOneS2);

			}

		}
			
	
