package fracture.mod.client.render;

import fracture.mod.objects.items.ItemCustomGun;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelCustomGun extends AnimatedGeoModel<ItemCustomGun> {

	//Dynamic resource finders for Hybrid geckolib renderer
	
    @Override
    public ResourceLocation getModelLocation(ItemCustomGun object) {
        // Finds .geo.json file based on the item name
        // Example: fracture:geo/old_earth_rifle.geo.json
        return new ResourceLocation("fracture", "geo/" + object.getRegistryName().getPath() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ItemCustomGun object) {
        // Example: fracture:textures/items/old_earth_rifle.png
        return new ResourceLocation("fracture", "textures/items/" + object.getRegistryName().getPath() + ".png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ItemCustomGun object) {
        // Example: fracture:animations/old_earth_rifle.animation.json
        return new ResourceLocation("fracture", "animations/" + object.getRegistryName().getPath() + ".animation.json");
    }
}