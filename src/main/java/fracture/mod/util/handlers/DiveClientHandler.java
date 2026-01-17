//package fracture.mod.util.handlers;
//
//import fracture.mod.util.Reference;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityPlayerSP;
//import net.minecraftforge.client.event.EntityViewRenderEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.relauncher.Side;
//
//@Mod.EventBusSubscriber(value = Side.CLIENT, modid = Reference.MODID)
//public class DiveClientHandler {
//
//    private static float currentZoom = 1.0F;
//
//    @SubscribeEvent
//    public static void onFOVModifier(EntityViewRenderEvent.FOVModifier event) {
//        EntityPlayerSP player = Minecraft.getMinecraft().player;
//        if (player == null) return;
//
//        double speed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
//        float target = 1.0F;
//
//        // Trigger zoom if on ground and moving reasonably fast
//        if (speed > 0.2 && player.onGround) {
//            target = 0.95F; // Very subtle zoom (5%)
//        }
//
//        // Slower, smoother transition
//        currentZoom += (target - currentZoom) * 0.05F;
//
//        event.setFOV(event.getFOV() * currentZoom);
//    }
//}