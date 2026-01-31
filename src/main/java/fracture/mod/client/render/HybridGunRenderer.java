package fracture.mod.client.render;

import fracture.mod.objects.items.ItemCustomGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

//Wip, not functioning

public class HybridGunRenderer extends GeoItemRenderer<ItemCustomGun> {

    private int debugTimer = 0;

    public HybridGunRenderer() {
        super(new ModelCustomGun());
    }

    @Override
    public void renderByItem(ItemStack itemStack) {
        AbstractClientPlayer player = Minecraft.getMinecraft().player;
        if (player == null) {
            super.renderByItem(itemStack);
            return;
        }

        // Run debug logs only once every 100 frames to avoid lag
        boolean doDebug = (debugTimer++ % 100 == 0);

        GlStateManager.pushMatrix();

        // --- 1. GLOBAL POSITIONING ---
        // Center the rig in the hand
        GlStateManager.translate(0.5, 0.5, 0.5);


        GlStateManager.scale(0.0625F, 0.0625F, 0.0625F); 
        

        ItemCustomGun animatable = (ItemCustomGun) itemStack.getItem();
        GeoModel model = this.getGeoModelProvider().getModel(this.getGeoModelProvider().getModelLocation(animatable));
        

        if (doDebug) {
            System.out.println("--- [FRACTURE DEBUG] START ---");
            if (model.topLevelBones.isEmpty()) {
                System.out.println("CRITICAL: Model loaded but has NO bones! Check JSON Identifier.");
            } else {

                checkBonePivot(model, "Gun");
            }
        }


        Minecraft.getMinecraft().renderEngine.bindTexture(this.getGeoModelProvider().getTextureLocation(animatable));


        setBoneVisibility(model, "Arms", false);
        setBoneVisibility(model, "Gun", true);
        

        this.render(model, animatable, 0, 0, 0, 0, 0);


        if(model.topLevelBones != null) {
            for(GeoBone groupBone : model.topLevelBones) {
                if(groupBone.name.equals("Arms")) {
                    for(GeoBone childBone : groupBone.childBones) {
                        if(childBone.name.equals("hr")) {
                            renderVanillaArm(player, groupBone, childBone, EnumHandSide.RIGHT);
                        }
                        else if(childBone.name.equals("hl")) {
                            renderVanillaArm(player, groupBone, childBone, EnumHandSide.LEFT);
                        }
                    }
                }
            }
        }


        setBoneVisibility(model, "Arms", true);
        setBoneVisibility(model, "Gun", true);

        GlStateManager.popMatrix();
    }

    private void renderVanillaArm(AbstractClientPlayer player, GeoBone parent, GeoBone bone, EnumHandSide side) {
        GlStateManager.pushMatrix();
        

        GlStateManager.translate(parent.rotationPointX, parent.rotationPointY, parent.rotationPointZ);
        GlStateManager.translate(parent.getPositionX(), parent.getPositionY(), parent.getPositionZ());
        GlStateManager.rotate(parent.getRotationZ(), 0, 0, 1);
        GlStateManager.rotate(parent.getRotationY(), 0, 1, 0);
        GlStateManager.rotate(parent.getRotationX(), 1, 0, 0);


        GlStateManager.translate(bone.rotationPointX, bone.rotationPointY, bone.rotationPointZ);
        GlStateManager.translate(bone.getPositionX(), bone.getPositionY(), bone.getPositionZ());
        GlStateManager.rotate(bone.getRotationZ(), 0, 0, 1);
        GlStateManager.rotate(bone.getRotationY(), 0, 1, 0);
        GlStateManager.rotate(bone.getRotationX(), 1, 0, 0);
        

        GlStateManager.scale(16.0F, 16.0F, 16.0F); 
        RenderPlayer renderPlayer = Minecraft.getMinecraft().getRenderManager().getSkinMap().get(player.getSkinType());
        Minecraft.getMinecraft().renderEngine.bindTexture(player.getLocationSkin());

        GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F); 
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F); 

        if (side == EnumHandSide.RIGHT) {
             renderPlayer.renderRightArm(player);
        } else {
             renderPlayer.renderLeftArm(player);
        }

        GlStateManager.popMatrix();
    }

    private void setBoneVisibility(GeoModel model, String boneName, boolean visible) {
        if(model.topLevelBones != null) {
            for (GeoBone bone : model.topLevelBones) {
                recursiveVisibility(bone, boneName, visible);
            }
        }
    }

    private void recursiveVisibility(GeoBone bone, String targetName, boolean visible) {
        if (bone.name.equals(targetName)) {
            bone.isHidden = !visible;
        }
        if (bone.childBones != null) {
            for (GeoBone child : bone.childBones) {
                recursiveVisibility(child, targetName, visible);
            }
        }
    }


    private void checkBonePivot(GeoModel model, String targetName) {
        for (GeoBone bone : model.topLevelBones) {
            if (bone.name.equals(targetName)) {
                System.out.println("   [DEBUG] Bone '" + targetName + "' Pivot: " + 
                    bone.rotationPointX + ", " + 
                    bone.rotationPointY + ", " + 
                    bone.rotationPointZ);
                return;
            }
        }
        System.out.println("   [DEBUG] Could NOT find bone: " + targetName);
    }
}