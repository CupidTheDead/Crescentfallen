package fracture.mod.util.handlers;

import fracture.mod.CFInfo; 
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles dive states and sliding.
 * The @EventBusSubscriber annotation automatically registers this class 
 * to the Event Bus, assuming the methods are static.
 */

//Currently debugging this class. Print lines will cause studders.

@Mod.EventBusSubscriber(modid = CFInfo.ID) 
public class DiveHandler {

    // ================================
    // CONFIG VALUES
    // ================================
    private static final double DASH_STRENGTH = 1.1D;
    private static final double LEAP_STRENGTH = 0.5D;
    private static final float SATURATION_DRAIN = 2.0F;
    private static final int COOLDOWN_TICKS = 325;
    
    private static final double SLIDE_DECAY = 0.96D; 
    private static final double STOP_THRESHOLD = 0.05D; 

    private enum DiveState {
        NONE,
        DIVING,
        SLIDING
    }

    // ================================
    // PERFORM DIVE
    // ================================
    public static void performDive(EntityPlayerMP player, float forward, float strafe) {
        System.out.println("DEBUG: performDive called for " + player.getName());

        int diveCount = player.getEntityData().getInteger("diveCount");

        // 1. Play Sound
        player.world.playSound(null, player.posX, player.posY, player.posZ,
            net.minecraft.init.SoundEvents.ENTITY_ENDERDRAGON_FLAP,
            net.minecraft.util.SoundCategory.PLAYERS, 0.3F, 1.0F);

        // 2. Cooldown UI
        if (diveCount == 0) {
            player.getEntityData().setInteger("diveCooldown", COOLDOWN_TICKS);
        }

        // 3. Drain Saturation
        player.getFoodStats().setFoodSaturationLevel(
            Math.max(0, player.getFoodStats().getSaturationLevel() - SATURATION_DRAIN)
        );

        // 4. Calculate Velocity
        if (forward != 0 || strafe != 0) {
            float yaw = player.rotationYaw;
            double motionX = (strafe * Math.cos(Math.toRadians(yaw)) - forward * Math.sin(Math.toRadians(yaw)));
            double motionZ = (forward * Math.cos(Math.toRadians(yaw)) + strafe * Math.sin(Math.toRadians(yaw)));

            Vec3d moveVec = new Vec3d(motionX, 0, motionZ);
            if (moveVec.lengthSquared() > 1e-6) {
                moveVec = moveVec.normalize();
                player.motionX = moveVec.x * DASH_STRENGTH;
                player.motionZ = moveVec.z * DASH_STRENGTH;
                player.motionY += LEAP_STRENGTH;
                player.velocityChanged = true; 
                System.out.println("DEBUG: Velocity Applied: " + player.motionX + ", " + player.motionZ);
            }
        }

        tryStepUp(player);

        player.getEntityData().setInteger("diveCount", diveCount + 1);
        
        setState(player, DiveState.DIVING);
        System.out.println("DEBUG: State set to DIVING");
    }

    // ================================
    // UPDATE LOOP
    // ================================
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        // Run ONLY on Server
        if (event.getEntityLiving().world.isRemote) return;
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;
        
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        DiveState state = getState(player);

        // DEBUG: verify the loop is running at all
        // if (player.ticksExisted % 20 == 0) System.out.println("DEBUG: Tick check for " + player.getName() + " State: " + state);

        // DIVING -> SLIDING
        if (state == DiveState.DIVING) {
            if (player.onGround) {
                System.out.println("DEBUG: Hit Ground! Switching to SLIDING.");
                setState(player, DiveState.SLIDING);
            }
        }

        // SLIDING
        if (state == DiveState.SLIDING) {
            
            if (!player.onGround) {
                System.out.println("DEBUG: Lost ground contact! Resetting to NONE.");
                setState(player, DiveState.NONE);
                return;
            }

            // Apply friction
            player.motionX *= SLIDE_DECAY;
            player.motionZ *= SLIDE_DECAY;
            player.velocityChanged = true; 

            double currentSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);
            System.out.println("DEBUG: Sliding... Speed=" + currentSpeed);

            if (currentSpeed < STOP_THRESHOLD) {
                System.out.println("DEBUG: Too slow. Stopping slide.");
                player.motionX = 0;
                player.motionZ = 0;
                player.velocityChanged = true;
                player.getEntityData().setInteger("diveCount", 0);
                setState(player, DiveState.NONE);
            }

            // Particles
            if (currentSpeed > 0.1 && player.world instanceof WorldServer) {
                WorldServer worldServer = (WorldServer) player.world;
                worldServer.spawnParticle(
                    EnumParticleTypes.BLOCK_DUST,
                    player.posX, player.posY + 0.1, player.posZ, 
                    3, 0.2, 0.0, 0.2, 0.05, 
                    Block.getStateId(Blocks.DIRT.getDefaultState())
                );
            }
        }
    }
    
    // ================================
    // HELPERS
    // ================================
    private static void tryStepUp(EntityPlayerMP player) {
        Vec3d motion = new Vec3d(player.motionX, 0, player.motionZ);
        if (motion.lengthSquared() < 1e-6) return;
        Vec3d dir = motion.normalize();
        BlockPos posAhead = new BlockPos(player.posX + dir.x, player.posY, player.posZ + dir.z);
        IBlockState frontBlock = player.world.getBlockState(posAhead);
        IBlockState aboveFront = player.world.getBlockState(posAhead.up());

        if (frontBlock.getMaterial().isSolid() && aboveFront.getMaterial().isReplaceable()) {
            player.setPositionAndUpdate(posAhead.getX() + 0.5, posAhead.getY() + 1.01, posAhead.getZ() + 0.5);
            player.motionY = 0.2; 
        }
    }

    private static DiveState getState(EntityPlayer player) {
        String s = player.getEntityData().getString("diveState");
        if (s.isEmpty()) return DiveState.NONE; 
        try {
            return DiveState.valueOf(s);
        } catch (Exception e) {
            return DiveState.NONE;
        }
    }

    private static void setState(EntityPlayer player, DiveState state) {
        player.getEntityData().setString("diveState", state.name());
    }
}