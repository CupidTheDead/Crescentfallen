package fracture.mod.objects.items;

import com.mrcrayfish.guns.item.ItemGun;
import com.mrcrayfish.guns.object.Gun;
import fracture.mod.CFMain;
import fracture.mod.init.ItemInit;
import fracture.mod.client.render.HybridGunRenderer;
import fracture.mod.util.IHasModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class ItemCustomGun extends ItemGun implements IHasModel, IAnimatable {

    private final Gun cfGun;
    private final int reloadDuration; 
    
    public AnimationFactory factory = new AnimationFactory(this);

    public ItemCustomGun(Gun gun, String name, int reloadDuration) {
        super(new ResourceLocation("fracture", name));
        
        this.cfGun = gun;
        this.reloadDuration = reloadDuration; 
        
        setTranslationKey(name);
        setCreativeTab(CFMain.CrescentfallenGuns);
        this.setTileEntityItemStackRenderer(new HybridGunRenderer());
        ItemInit.ITEMS.add(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
        if (worldIn.isRemote && (result.getType() == EnumActionResult.SUCCESS || result.getType() == EnumActionResult.PASS)) {
            ItemStack stack = playerIn.getHeldItem(handIn);
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            stack.getTagCompound().setLong("LastFiredTime", System.currentTimeMillis());
        }
        return result;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 1, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        try {
            EntityPlayer player = null;
            List<Entity> extraData = event.getExtraDataOfType(Entity.class);
            if (!extraData.isEmpty() && extraData.get(0) instanceof EntityPlayer) {
                player = (EntityPlayer) extraData.get(0);
            }
            if (player == null) player = Minecraft.getMinecraft().player;
            if (player == null) return PlayState.STOP;

            ItemStack stack = player.getHeldItemMainhand(); 
            if (stack.getItem() != this) return PlayState.STOP;

            NBTTagCompound tag = stack.getTagCompound();
            long now = System.currentTimeMillis();

            if(tag != null) {
                 // --- RELOAD ANIMATION ---
                 if(tag.hasKey("LastReloadTime")) {
                     long reloadStart = tag.getLong("LastReloadTime");
                     if (now - reloadStart < this.reloadDuration) {
                         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_reload", false));
                         return PlayState.CONTINUE;
                     }
                 }

                 // --- FIRE ANIMATION ---
                 if(tag.hasKey("LastFiredTime")) {
                     long lastFired = tag.getLong("LastFiredTime");
                     long timeSinceFire = now - lastFired;
                     boolean isAuto = this.cfGun.general.auto;

                     if (isAuto) {
                         if (timeSinceFire < 200) {
                             event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_fire", true));
                             return PlayState.CONTINUE;
                         }
                     } else {
                         if (timeSinceFire < 500) {
                             event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_fire", false));
                             return PlayState.CONTINUE;
                         }
                     }
                 }
            }
            
            // --- WALK ANIMATION WIP---
            if (player.moveForward != 0 || player.moveStrafing != 0) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_walk", true));
                return PlayState.CONTINUE;
            }

            // --- IDLE ANIMATION WIP---
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_idle", true));
            return PlayState.CONTINUE;

        } catch (Exception e) {
            e.printStackTrace();
            return PlayState.STOP;
        }
    }

    @Override
    public AnimationFactory getFactory() { return this.factory; }
    @Override
    public Gun getGun() { return this.cfGun; }
    @Override
    public void registerModels() { CFMain.proxy.registerItemRenderer(this, 0, "inventory"); }
    @Override
    public int getMaxItemUseDuration(ItemStack stack) { return 72000; }
    @Override
    public EnumAction getItemUseAction(ItemStack stack) { return EnumAction.NONE; }
}