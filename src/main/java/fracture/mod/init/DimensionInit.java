package fracture.mod.init;

import fracture.mod.planets.WorldProviderHollows;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DimensionInit 
{
//public static final DimensionType AddonDimensions.dimPlanetOneS1 = DimensionType.register
	public static final DimensionType dimPlanetOneS1 = DimensionType.register("AddonDimensions.dimPlanetOneS1", "_addondimensions.dimplanetoneS1", -650, WorldProviderHollows.class, false);

	public static void registerDimensions()
	{
		DimensionManager.registerDimension(-36, dimPlanetOneS1);
	}
}
