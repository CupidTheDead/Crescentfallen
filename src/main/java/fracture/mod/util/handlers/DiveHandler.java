package fracture.mod.util.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles dive states and sliding.
 */
public class DiveHandler {

    // ================================
    // CONFIG VALUES (tweak here)
    // ================================
    private static final double DASH_STRENGTH = 1.1D;       // forward dash magnitude (XZ)
    private static final double LEAP_STRENGTH = 0.5D; 		// upward boost
    private static final float SATURATION_DRAIN = 2.0F;     // saturation drained per dive
    private static final int COOLDOWN_TICKS = 325;          // optional cooldown marker for HUD
    private static final double SLIDE_FRICTION = 0.985;     // friction multiplier while sliding (close to 1 = ice-like)
    private static final int SLIDE_TICKS_MAX = 4000;         // max ticks sliding will last (if not cancelled earlier)
    private static final double SLIDE_MIN_SPEED_SQ = 0.10;  // squared speed threshold to end slide

    private enum DiveState {
        NONE,
        DIVING,
        SLIDING
    }


//         // If the player presses any movement key, continue the slide
//         Minecraft mc = Minecraft.getMinecraft();
//         if (mc.player != null) {
//             boolean moved = mc.gameSettings.keyBindForward.isKeyDown()
//                          || mc.gameSettings.keyBindBack.isKeyDown()
//                          || mc.gameSettings.keyBindLeft.isKeyDown()
//                          || mc.gameSettings.keyBindRight.isKeyDown()
//                          || mc.gameSettings.keyBindJump.isKeyDown()
//                          || mc.gameSettings.keyBindSneak.isKeyDown();
//
//             if (moved) {
//                 setState(player, DiveState.SLIDING);
//                 player.getEntityData().setInteger("diveCount", 0);
//             }
//         }
//
//         // Stop sliding naturally when nearly stopped
//         if (player.motionX * player.motionX + player.motionZ * player.motionZ < 0.01) {
//             setState(player, DiveState.NONE);
//             player.getEntityData().setInteger("diveCount", 0);
//         }
//     }
 

    // ================================
    // PERFORM DIVE (called from packet handler)
    // ================================
    public static void performDive(EntityPlayerMP player, float forward, float strafe) {
        int diveCount = player.getEntityData().getInteger("diveCount");

        // Play sound (server will relay to clients)
        player.world.playSound(
            null,
            player.posX, player.posY, player.posZ,
            net.minecraft.init.SoundEvents.ENTITY_ENDERDRAGON_FLAP,
            net.minecraft.util.SoundCategory.PLAYERS,
            0.3F, 1.0F 
        );

        // Cooldown marker for HUD (only on first dive for display)
        if (diveCount == 0) {
            player.getEntityData().setInteger("diveCooldown", COOLDOWN_TICKS);
        }

        // Drain saturation
        player.getFoodStats().setFoodSaturationLevel(
            Math.max(0, player.getFoodStats().getSaturationLevel() - SATURATION_DRAIN)
        );

        // Movement direction required to dash
        if (forward != 0 || strafe != 0) {
            float yaw = player.rotationYaw;

            // Compute movement vector from player input + yaw
            double motionX = (strafe * Math.cos(Math.toRadians(yaw)) - forward * Math.sin(Math.toRadians(yaw)));
            double motionZ = (forward * Math.cos(Math.toRadians(yaw)) + strafe * Math.sin(Math.toRadians(yaw)));

            Vec3d moveVec = new Vec3d(motionX, 0, motionZ);
            if (moveVec.lengthSquared() <= 1e-6) return; // no movement
            moveVec = moveVec.normalize();

            // Instant directional override: set exact momentum in X/Z
            player.motionX = moveVec.x * DASH_STRENGTH;
            player.motionZ = moveVec.z * DASH_STRENGTH;

            // Vertical boost for the dive
            player.motionY += LEAP_STRENGTH;

            player.velocityChanged = true;
        }

        // Try to step up on low blocks to preserve momentum (smooth)
        tryStepUp(player);

        // Increment dive counter and set state to DIVING
        player.getEntityData().setInteger("diveCount", diveCount + 1);
        setState(player, DiveState.DIVING);
    }

    
    // ================================
    // GROUND SLIDE 
    // ================================
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

        DiveState state = getState(player);

//        // Reset dive count if touching ground and NOT sliding
//        if (player.onGround && state != DiveState.SLIDING) {
//            player.getEntityData().setInteger("diveCount", 0);
//            setState(player, DiveState.NONE);
//        }

        // Handle sliding state
        if (state == DiveState.SLIDING) {

            // Apply friction
            //.motionX *= SLIDE_FRICTION;
            //player.motionZ *= SLIDE_FRICTION;

            // Spawn particles client-side
            if (player.world.isRemote) {
                for (int i = 0; i < 4; i++) {
                    player.world.spawnParticle(
                        EnumParticleTypes.BLOCK_DUST,
                        player.posX, player.posY + 0.05, player.posZ,
                        (player.world.rand.nextDouble() - 0.5) * 0.1,
                        0.01,
                        (player.world.rand.nextDouble() - 0.5) * 0.1,
                        Block.getStateId(Blocks.DIRT.getDefaultState())
                    );
                }
            }  
        }
        
        
        
    }
    
    
    
    // ================================
    // HELPERS
    // ================================
    private static void tryStepUp(EntityPlayerMP player) {
        Vec3d motion = new Vec3d(player.motionX, 0, player.motionZ);
        if (motion.lengthSquared() < 1e-6) return; // no movement
        Vec3d dir = motion.normalize();
        BlockPos posAhead = new BlockPos(player.posX + dir.x, player.posY, player.posZ + dir.z);

        IBlockState frontBlock = player.world.getBlockState(posAhead);
        IBlockState aboveFront = player.world.getBlockState(posAhead.up());

        if (!frontBlock.getMaterial().isReplaceable() && aboveFront.getMaterial().isReplaceable()) {
            player.setPositionAndUpdate(
                posAhead.getX() + 0.5,
                posAhead.getY() + 1.01,
                posAhead.getZ() + 0.5
            );
            player.motionY = 0.2; 
        }
    }

    private static DiveState getState(EntityPlayerMP player) {
        String s = player.getEntityData().getString("diveState");
        try {
            return DiveState.valueOf(s);
        } catch (Exception e) {
            return DiveState.NONE;
        }
    }

    private static void setState(EntityPlayerMP player, DiveState state) {
        player.getEntityData().setString("diveState", state.name());
    }
}