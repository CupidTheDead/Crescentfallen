package fracture.mod.init;


import fracture.mod.AddonConfig;
import fracture.mod.ModInfo;
import fracture.mod.planets.WorldProviderHollows;
import fracture.mod.planets.WorldProviderTheFracture;
import fracture.mod.planets.hollows.hollows.biome.HollowsBiomes;
import fracture.mod.planets.thefracture.thefracture.biome.TheFractureBiomes;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.CelestialBody;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.Moon;
import micdoodle8.mods.galacticraft.api.galaxies.Planet;
import micdoodle8.mods.galacticraft.api.world.AtmosphereInfo;
import micdoodle8.mods.galacticraft.api.world.EnumAtmosphericGas;
import micdoodle8.mods.galacticraft.core.dimension.TeleportTypeMoon;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedEnderman;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedZombie;
import micdoodle8.mods.galacticraft.planets.mars.dimension.TeleportTypeMars;
import micdoodle8.mods.galacticraft.planets.venus.dimension.TeleportTypeVenus;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome.SpawnListEntry;

/**
 * Class AddonCelestialBodies
 * 
 * This is where we will store the public static Fields for all of our
 * CelestalObjects: - SolarSystems - Planets - Moons - SpaceStations (aka:
 * Satellites)
 * 
 * Each field can be called from other classes directly
 * 
 */
public class AddonPlanets {

	// Planets for addonSystem1
	public static Planet planetOneS1;
	public static Planet planetTwoS1;
	//public static Moon Kona;
	
	// Planets for addonSystem2
	//public static Planet planetOneS2;

	/**
	 * this is our initialization method that will be called in the mods main class
	 * to build our celestial bodies
	 * 
	 * the order these are in are an important part of this compiling correctly
	 */
	public AddonPlanets() {
		this.createPlanets();
		this.registerPlanetTeleportTypes();
		this.registerPlanets();
		this.createMoons();
	}

	private void createPlanets() {
		
		// Solar System 1 Planets
		
		planetOneS1 = new Planet("Hollows").setParentSolarSystem(AddonSolarSystems.addonSystem1);
		planetOneS1.setTierRequired(3);
		planetOneS1.setRingColorRGB(0.1F, 0.9F, 0.6F);
		planetOneS1.setPhaseShift((float) (Math.PI * 0.5F));
		planetOneS1.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.9F, 0.9F));
		planetOneS1.setRelativeOrbitTime(1.0F);
		planetOneS1.setBodyIcon(new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/Hollows.png"));
		planetOneS1.atmosphereComponent(EnumAtmosphericGas.HYDROGEN);
		planetOneS1.setDimensionInfo(AddonConfig.Dimension.p1s1Id, WorldProviderHollows.class);
		planetOneS1.setAtmosphere(new AtmosphereInfo(false, false, false, 5.0F, 0.0F, 0.1F));
		planetOneS1.addChecklistKeys("space_suit", "equip_oxygen_suit", "equip_parachute");
		planetOneS1.setBiomeInfo(HollowsBiomes.biomes);
		
		//IMPORTANT:PLACE PLANETTWOS1 REGESTERER HERE, LOOK IN DISCORD HARDER
		
		planetTwoS1 = new Planet("Fracture").setParentSolarSystem(AddonSolarSystems.addonSystem1);
		planetTwoS1.setTierRequired(3);
		planetTwoS1.setRingColorRGB(0.1F, 0.9F, 0.6F);
		planetTwoS1.setPhaseShift((float) (Math.PI / 5.8748F));
		planetTwoS1.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(2.3F, 2.3F));
		planetTwoS1.setRelativeOrbitTime(1.6F);
		planetTwoS1.setBodyIcon(new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/fracture.png"));
		planetTwoS1.atmosphereComponent(EnumAtmosphericGas.HYDROGEN);
		planetTwoS1.setDimensionInfo(AddonConfig.Dimension.p2s1Id, WorldProviderTheFracture.class);
		planetTwoS1.setAtmosphere(new AtmosphereInfo(false, false, false, 5.0F, 0.0F, 0.1F));
		planetTwoS1.addChecklistKeys("space_suit", "equip_oxygen_suit", "equip_parachute");
		planetTwoS1.setBiomeInfo(TheFractureBiomes.biomes);
		
		
		
		
		//IMPORTANT:PLACE Solar System 2 Planets REGESTERER HERE, LOOK IN DISCORD HARDER
				
			}

	private void createMoons() {
	//	Kona = new Moon("Kona").setParentPlanet(AddonPlanets.planetTwoS1);
	//	Kona.setTierRequired(3);
	//	Kona.setRingColorRGB(0.1F, 0.9F, 0.6F);
	//	Kona.setPhaseShift((float) (Math.PI * 0.5F));
	//	Kona.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(0.9F, 0.9F));
	//	Kona.setRelativeOrbitTime(1.0F);
	//	Kona.setBodyIcon(new ResourceLocation(ModInfo.ID, "textures/gui/celestialbodies/kona.png"));
	//	Kona.atmosphereComponent(EnumAtmosphericGas.CO2);
		//errors here. Think p1s1ID is refrencing that it needs to create an ID, if not change it back
	//	Kona.setDimensionInfo(AddonConfig.Dimension.p1s1Id, WorldProviderHollows.class);
	//	Kona.setAtmosphere(new AtmosphereInfo(false, false, false, 5.0F, 0.0F, 0.1F));
	//	Kona.addChecklistKeys("space_suit", "equip_oxygen_suit", "equip_parachute");
	//	Kona.setBiomeInfo(HollowsBiomes.biomes);
		
	}
	

private static void setMobInfo(CelestialBody body) {
	body.addMobInfo(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
	body.addMobInfo(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
	body.addMobInfo(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
	body.addMobInfo(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
	body.addMobInfo(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
	}

private void registerPlanetTeleportTypes() {

	GalacticraftRegistry.registerTeleportType(WorldProviderHollows.class, new TeleportTypeMars());
	GalacticraftRegistry.registerTeleportType(WorldProviderTheFracture.class, new TeleportTypeVenus());
	
}
//}

private void registerPlanets() {

	GalaxyRegistry.registerPlanet(planetOneS1);
	GalaxyRegistry.registerPlanet(planetTwoS1);
	
	//GalaxyRegistry.registerPlanet(planetOneS2);

	}

}
	
	
	
	











	
	//private static void setMobInfo(CelestialBody body) {
	//	body.addMobInfo(new SpawnListEntry(EntityEvolvedZombie.class, 8, 2, 3));
	//	body.addMobInfo(new SpawnListEntry(EntityEvolvedSpider.class, 8, 2, 3));
	//	body.addMobInfo(new SpawnListEntry(EntityEvolvedSkeleton.class, 8, 2, 3));
	//	body.addMobInfo(new SpawnListEntry(EntityEvolvedCreeper.class, 8, 2, 3));
	//	body.addMobInfo(new SpawnListEntry(EntityEvolvedEnderman.class, 10, 1, 4));
	//}

	//private void registerPlanetTeleportTypes() {

		//GalacticraftRegistry.registerTeleportType(WorldProviderPlanetOneS1.class, new TeleportTypeMars());
		//MOON REGESTER TELEPORT TYPE  GOES HERE. REMOVE NOTE TAG TO IMPLEMENT
		//GalacticraftRegistry.registerTeleportType(WorldProviderPlanetTwoS1.class, new TeleportTypeMoon());
		
	//}

	//private void registerPlanets() {

		//GalaxyRegistry.registerPlanet(planetOneS1);
		//GalaxyRegistry.registerPlanet(planetTwoS1);
		
		//GalaxyRegistry.registerPlanet(planetOneS2);

	///}

//}