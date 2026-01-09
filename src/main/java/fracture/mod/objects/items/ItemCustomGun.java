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

    // --- TRIGGER  ---
    // Detects the click and writes the "LastFiredTime" to the item
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        // Run standard CGM firing logic first (spawns bullets, ammo check)
        ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);

        // If the click was valid (SUCCESS) or processed (PASS), update the timestamp
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

    // --- ANIMATION LOOP ---
    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        try {
            // Safe Player Retrieval (Critical for 1.12.2 stability)
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
            if(tag != null) {
                 long now = System.currentTimeMillis();

                 // --- RELOAD LOGIC ---
                 if(tag.hasKey("LastReloadTime")) {
                     long reloadStart = tag.getLong("LastReloadTime");
                     if (now - reloadStart < this.reloadDuration) {
                         event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_reload", false));
                         return PlayState.CONTINUE;
                     }
                 }

                 // --- FIRE LOGIC ---
                 if(tag.hasKey("LastFiredTime")) {
                     long lastFired = tag.getLong("LastFiredTime");
                     long timeSinceFire = now - lastFired;
                     
                     // Read the gun property correctly
                     boolean isAuto = this.cfGun.general.auto;

                     if (isAuto) {
                         // AUTOMATIC: Loop if firing happened recently (200ms)
                         if (timeSinceFire < 200) {
                             event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_fire", true));
                             return PlayState.CONTINUE;
                         }
                     } else {
                         // SEMI-AUTO: Play once. 
                         // 500ms is usually perfect for a recoil animation to finish.
                         if (timeSinceFire < 500) {
                             event.getController().setAnimation(new AnimationBuilder().addAnimation("animation_fire", false));
                             return PlayState.CONTINUE;
                         }
                     }
                 }
            }

            // --- IDLE LOGIC ---
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