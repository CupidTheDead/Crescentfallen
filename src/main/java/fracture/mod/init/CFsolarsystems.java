package fracture.mod.init;

import fracture.mod.CFInfo;
import micdoodle8.mods.galacticraft.api.galaxies.GalaxyRegistry;
import micdoodle8.mods.galacticraft.api.galaxies.SolarSystem;
import micdoodle8.mods.galacticraft.api.galaxies.Star;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.util.ResourceLocation;

public class CFsolarsystems {

	public static SolarSystem starTriopas;
	public static SolarSystem nebulaCore;

	public CFsolarsystems() {
		this.createSolarSystems();
		this.registerSolarSystems();
	}

	private void createSolarSystems() {

		starTriopas = new SolarSystem("Triopas", "milky_way").setMapPosition(new Vector3(1.2F, 0.3f, 0.0f));
		Star iStar = new Star("triopas").setParentSolarSystem(starTriopas);
		iStar.setTierRequired(-1);

		iStar.setBodyIcon(new ResourceLocation(CFInfo.ID, "textures/gui/celestialbodies/triopas.png"));

		starTriopas.setMainStar(iStar);

		nebulaCore = new SolarSystem("Artemis 1", "milky_way").setMapPosition(new Vector3(-1.0F, 1.0f, 0.0f));
		Star jStar = new Star("artemis1").setParentSolarSystem(nebulaCore);
		jStar.setTierRequired(-1);
		jStar.setBodyIcon(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/sun.png"));
		nebulaCore.setMainStar(jStar);
	}

	private void registerSolarSystems() {

		GalaxyRegistry.registerSolarSystem(starTriopas);
		GalaxyRegistry.registerSolarSystem(nebulaCore);

	}

}
