package fracture.mod.util.handlers;

import fracture.mod.objects.items.ItemCustomGun;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class GunEventHandler {

    // Tracker for just clicked and holding down
    private boolean wasClicking = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        // Safety Checks
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.currentScreen != null) {
            wasClicking = false; // Reset if menu opens
            return;
        }

        // Check button state (0 = Left click)
        boolean isClicking = Mouse.isButtonDown(0);

        // Determine action
        if (isClicking) {
            if (!wasClicking) {
                // This triggers SEMI-AUTO fire
                handleFire(false); 
            } else {
                // This triggers FULL-AUTO fire
                handleFire(true);
            }
        }

        // Update Tracker for next tick
        wasClicking = isClicking;
    }

    // RELOAD - Kept separate as KeyInput is reliable
//Note: update this to mirror mrcrayfishes gun.reload key
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemCustomGun) {
                setTag(mc.player.getHeldItemMainhand(), "LastReloadTime");
            }
        }
    }
//Note: Extend this to only play if gun is empty
    
    // Helper Logic
    private void handleFire(boolean isHolding) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack stack = mc.player.getHeldItemMainhand();

        // Check if holding gun
        if (stack.getItem() instanceof ItemCustomGun) {
            ItemCustomGun gunItem = (ItemCustomGun) stack.getItem();
            
            // If button is held down, update only if the gun is automatic
            if (isHolding && !gunItem.getGun().general.auto) {
                return; 
            }

            // Update the tag
            setTag(stack, "LastFiredTime");
            
            // System.out.println("Fracture Debug: Gun Fired! (Holding: " + isHolding + ")");
            
            // Cancel reload if player fires
            if(stack.hasTagCompound()) {
                stack.getTagCompound().removeTag("LastReloadTime");
            }
        }
    }

    private void setTag(ItemStack stack, String key) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setLong(key, System.currentTimeMillis());
    }
}