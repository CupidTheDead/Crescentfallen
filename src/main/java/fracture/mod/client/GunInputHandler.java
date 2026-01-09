//package fracture.mod.client; // Make sure this matches your package structure
//
//import fracture.mod.objects.items.ItemCustomGun;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;
//import net.minecraftforge.fml.relauncher.Side;
//import org.lwjgl.input.Mouse;
//
//@Mod.EventBusSubscriber(modid = "fracture", value = Side.CLIENT)
//public class GunInputHandler {
//
//    @SubscribeEvent
//    public static void onMouseClick(MouseInputEvent event) {
//        // "0" is the Left Mouse Button (Fire). "1" is Right (Aim).
//        int button = Mouse.getEventButton();
//        boolean isDown = Mouse.getEventButtonState();
//
//        // Check if Left Button was JUST pressed down
//        if (button == 0 && isDown) {
//            EntityPlayer player = Minecraft.getMinecraft().player;
//            if (player != null && !player.getHeldItemMainhand().isEmpty()) {
//                // Check if the player is holding YOUR gun
//                if (player.getHeldItemMainhand().getItem() instanceof ItemCustomGun) {
//                    
//                    System.out.println("DEBUG: Left Click detected via InputHandler!"); 
//                    
//                    // Update the Map to trigger the animation in ItemCustomGun
//                    ItemCustomGun.LAST_SHOT_MAP.put(player.getUniqueID(), System.currentTimeMillis());
//                }
//            }
//        }
//    }
//}