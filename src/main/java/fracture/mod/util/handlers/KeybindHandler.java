package fracture.mod.util.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.entity.EntityPlayerSP;
//import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import fracture.mod.CFMain;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;


public class KeybindHandler {
    public static KeyBinding diveKey;

    public static void register() {
        diveKey = new KeyBinding("key.dive", Keyboard.KEY_LCONTROL, "key.categories.movement");
        ClientRegistry.registerKeyBinding(diveKey);
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Get the Minecraft instance
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;
        EntityPlayerSP player = mc.player;

        // Dive key pressed -> send DivePacket
        if (diveKey.isPressed()) {
            float forward = player.moveForward;
            float strafe = player.moveStrafing;
            CFMain.NETWORK.sendToServer(new DivePacket(forward, strafe));
        }

        // If any movement key is pressed, tell the server to cancel slide
        boolean moved = mc.gameSettings.keyBindForward.isKeyDown()
                     || mc.gameSettings.keyBindBack.isKeyDown()
                     || mc.gameSettings.keyBindLeft.isKeyDown()
                     || mc.gameSettings.keyBindRight.isKeyDown()
                     || mc.gameSettings.keyBindJump.isKeyDown()
                     || mc.gameSettings.keyBindSneak.isKeyDown();

        if (moved) {
            CFMain.NETWORK.sendToServer(new SlideCancelPacket());
        }
    }
}