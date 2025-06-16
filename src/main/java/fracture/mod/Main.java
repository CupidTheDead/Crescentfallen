package fracture.mod;

import fracture.mod.init.AddonDimensions;
import fracture.mod.init.AddonPlanets;
import fracture.mod.init.AddonSolarSystems;
import fracture.mod.planets.WorldProviderHollows;
import fracture.mod.planets.WorldProviderTheFracture;
import fracture.mod.proxy.Proxy;
import fracture.mod.tabs.CrescentfallenBlockstab;
//import fracture.mod.tabs.CresentfallenTab;
import fracture.mod.tabs.CrescentfallenTabitems;
import fracture.mod.util.Reference;

//NEW
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;


@Mod(modid = Reference.MODID, name = Reference.MODNAME, version = Reference.VERSION)
public class Main {
	
	public static final CrescentfallenBlockstab CrescentfallenBlocks = new CrescentfallenBlockstab("crescentfallenblocks");
	public static final CreativeTabs Crescentfallenitems = new CrescentfallenTabitems("crescentfallenitems");
	
	
	@Instance
	public static Main instance;
	
	@SidedProxy(
			clientSide = "fracture.mod.proxy.ClientProxy",
			serverSide = "Reference.COMMON")
	
	public static Proxy proxy;
	
		//this is where the working 'system' code was previously
		//if needed put it back here
		//see tutorial for details
		//edit 6/10/25. issue is fixed.
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		//Call this in the preInit (make sure you register any blocks or items first)
		new AddonSolarSystems();
		new AddonPlanets();
		//new AddonDimensions();
			
		proxy.preInit(event);
		//GalacticraftCore.satelliteSpaceStation = (Satellite) new Satellite("spacestation.fracture").setParentBody(AddonPlanets.planetTwoS1).setRelativeSize(0.2667F).setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(9F, 9F)).setRelativeOrbitTime(1 / 0.05F);
		//non functional; fix later.
		
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		//Call this in the preInit (make sure you register any blocks or items first)
		new AddonSolarSystems();
		new AddonPlanets();
		//new AddonDimensions();
		
	}	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Register addon dimensions used by planets/moons/etc.. in postInit

		AddonDimensions.init();
		proxy  .postInit(event);
		
		GalacticraftRegistry.registerDimension("AddonDimensions.dimPlanetOneS1", "_addonDimensions.dimplanetones1,", AddonConfig.Dimension.p1s1Id, WorldProviderHollows.class, false);
		AddonDimensions.dimPlanetOneS1 = WorldUtil.getDimensionTypeById(AddonConfig.Dimension.p1s1Id);
	
		GalacticraftRegistry.registerDimension("AddonDimensions.dimPlanetTwoS1", "_addonDimensions.dimplanettwos1,", AddonConfig.Dimension.p2s1Id, WorldProviderTheFracture.class, false);
				
		AddonDimensions.dimPlanetTwoS1 = WorldUtil.getDimensionTypeById(AddonConfig.Dimension.p2s1Id);
		
		//Not yet defined
		//GalacticraftRegistry.getTeleportTypeForDimension(AddonConfig.Dimension.p2s1Id <WorldProviderPlanetTwoS1.class>);
		
	}
	public static World getWorld () {
		return proxy.getWorld();
	}
}
