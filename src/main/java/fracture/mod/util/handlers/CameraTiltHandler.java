package fracture.mod.util.handlers;

import fracture.mod.CFMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

	//CAMERA TILT TESTING
	// Note: make this only trigger when wearing spacesuit in future

public class CameraTiltHandler {

    private float lastTilt = 0F;

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!CFMain.enableCameraTilt) return; // config toggle

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        if (player == null) return;

        // Disable in Creative
        if (player.capabilities.isCreativeMode) {
            lastTilt = 0F;
            event.setRoll(0F);
            return;
        }

        boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
        boolean right = mc.gameSettings.keyBindRight.isKeyDown();

        float targetTilt = 0.0F;

        // Base tilt while strafing
        if (left) targetTilt = -0.6F;  // roll left
        if (right) targetTilt = 0.6F;  // roll right

        //~0.3f in booth directions is not jarring but noticeable
        
        // Extra tilt when diving (using NBT flag set in DivePacket / movement handler)
        //Not functional
        if (player.getEntityData().getBoolean("justDived")) {
            if (left) targetTilt = -1.6F;
            if (right) targetTilt = 1.6F;
        }

        // Interpolation
        float smoothing = 0.1F; // smaller = smoother
        lastTilt += (targetTilt - lastTilt) * smoothing;

        event.setRoll(lastTilt);
    }
}