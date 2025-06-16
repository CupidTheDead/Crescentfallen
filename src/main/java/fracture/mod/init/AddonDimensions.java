package fracture.mod.init;


import fracture.mod.AddonConfig.Dimension;
import fracture.mod.planets.WorldProviderHollows;
import fracture.mod.planets.WorldProviderTheFracture;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

	public class AddonDimensions {
	
		public static DimensionType dimPlanetOneS1;
		public static DimensionType dimPlanetTwoS1;
		
	//public static final DimensionType dimPlanetTwoS1 = DimensionType.register("AddonDimensions.dimPlanetTwoS1", "_addondimensions.dimplanettwoS1", -36, WorldProviderTheFracture.class, false);
		//public static DimensionType dimPlanetOneS2;
		//private static DimensionType dimPlanetTwoS1;

	public static void init() {
		//Dimension resolved to addonconfig, may not be correct. If it doesent work,
		//try resolving it to dim from default forge library
		
		AddonDimensions.dimPlanetOneS1 = WorldUtil.getDimensionTypeById(Dimension.p1s1Id);
		//AddonDimensions.dimPlanetOneS1 = WorldUtil.getDimensionTypeById(-650);
	
		//TESTING 5/22/25
		AddonDimensions.dimPlanetTwoS1 = WorldUtil.getDimensionTypeById(Dimension.p2s1Id);
		//AddonDimensions.dimPlanetOneS2 = WorldUtil.getDimensionTypeById(Dimension.p1s2Id);

		
		
		}
	
	
	public static void registerDimensions() {
		
		DimensionManager.registerDimension(-39, dimPlanetTwoS1);
		
	}
	
}