package fracture.mod.util.handlers;

import fracture.mod.CFInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

/**
 * Handles dive states, sliding, and dive general movement system logic.
 */
@Mod.EventBusSubscriber(modid = CFInfo.ID)
public class DiveHandler {


    // CONFIG VALUES
    private static final double DASH_STRENGTH = 1.1D;
    private static final double LEAP_STRENGTH = 0.5D;
    private static final float SATURATION_DRAIN = 2.0F;
    private static final int COOLDOWN_TICKS = 20; // Reduced cooldown (1 second)

    // Slide Configs
    private static final float SLIDE_START_SPEED = 1.2F;
    private static final float SLIDE_DECAY_RATE = 0.04F;
    private static final float SLIDE_HITBOX_HEIGHT = 0.6F;
    private static final float NORMAL_HITBOX_HEIGHT = 1.8F;

    private static Method setSizeMethod;

    static {
        try {
            setSizeMethod = ReflectionHelper.findMethod(Entity.class, "setSize", "func_70105_a", float.class, float.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum DiveState {
        NONE,
        DIVING,
        SLIDING
    }


    // PERFORM DIVE

    public static void performDive(EntityPlayerMP player, float forward, float strafe) {
        // Checks if cooldown is active
        if (player.getEntityData().getInteger("diveCooldown") > 0) {
            return;
        }

        // Player can dive if touched ground/water since last dive
        // default to true if the tag doesn't exist so players can dive on first login
        boolean canDive = player.getEntityData().hasKey("canDive") ? player.getEntityData().getBoolean("canDive") : true;
        
        // If we are airborne and haven't reset, deny the dive
        if (!canDive && !player.onGround && !player.isInWater() && !player.isInLava()) {
            return;
        }

        int diveCount = player.getEntityData().getInteger("diveCount");

        // Play Sound
        player.world.playSound(null, player.posX, player.posY, player.posZ,
                net.minecraft.init.SoundEvents.ENTITY_ENDERDRAGON_FLAP,
                net.minecraft.util.SoundCategory.PLAYERS, 0.3F, 1.0F);

        // Set Cooldown & Drain Saturation
        player.getEntityData().setInteger("diveCooldown", COOLDOWN_TICKS);
        
        player.getFoodStats().setFoodSaturationLevel(
                Math.max(0, player.getFoodStats().getSaturationLevel() - SATURATION_DRAIN)
        );

        // Calculate Velocity
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

                player.getEntityData().setDouble("lastDiveDirX", moveVec.x);
                player.getEntityData().setDouble("lastDiveDirZ", moveVec.z);
            }
        }

        // Update State & Disable further diving until landing
        player.getEntityData().setInteger("diveCount", diveCount + 1);
        player.getEntityData().setBoolean("canDive", false); // DISABLE DIVE
        
        updatePlayerSize(player, 0.6F, SLIDE_HITBOX_HEIGHT);
        setState(player, DiveState.DIVING);
    }

    // UPDATE LOOP
    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().world.isRemote) return;
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        DiveState state = getState(player);

        // Ground/Liquid Check
        // If player touches ground OR liquid, allow diving again
        if (player.onGround || player.isInWater() || player.isInLava()) {
            if (!player.getEntityData().getBoolean("canDive")) {
                player.getEntityData().setBoolean("canDive", true);
            }
        }

        // Cooldown Tick
        int cd = player.getEntityData().getInteger("diveCooldown");
        if (cd > 0) {
            player.getEntityData().setInteger("diveCooldown", cd - 1);
        }


     // STATE DIVING

        if (state == DiveState.DIVING) {
            updatePlayerSize(player, 0.6F, SLIDE_HITBOX_HEIGHT);

            if (!player.onGround && (Math.abs(player.motionX) > 0.01 || Math.abs(player.motionZ) > 0.01)) {
                 Vec3d currentMotion = new Vec3d(player.motionX, 0, player.motionZ).normalize();
                 player.getEntityData().setDouble("lastDiveDirX", currentMotion.x);
                 player.getEntityData().setDouble("lastDiveDirZ", currentMotion.z);
            }

            if (player.onGround) {
                double dx = player.posX - player.prevPosX;
                double dz = player.posZ - player.prevPosZ;
                Vec3d slideDir = new Vec3d(dx, 0, dz);

                if (slideDir.lengthSquared() < 1.0E-4D) {
                    double lastX = player.getEntityData().getDouble("lastDiveDirX");
                    double lastZ = player.getEntityData().getDouble("lastDiveDirZ");
                    
                    if (Math.abs(lastX) > 0.01 || Math.abs(lastZ) > 0.01) {
                         slideDir = new Vec3d(lastX, 0, lastZ);
                    } else {
                        float yaw = player.rotationYaw;
                        slideDir = new Vec3d(-Math.sin(Math.toRadians(yaw)), 0, Math.cos(Math.toRadians(yaw)));
                    }
                }

                slideDir = slideDir.normalize();

                player.getEntityData().setDouble("slideDirX", slideDir.x);
                player.getEntityData().setDouble("slideDirZ", slideDir.z);
                player.getEntityData().setFloat("slideSpeed", SLIDE_START_SPEED);

                setState(player, DiveState.SLIDING);
            }
        }

     // STATE SLIDING
        
        if (state == DiveState.SLIDING) {
            updatePlayerSize(player, 0.6F, SLIDE_HITBOX_HEIGHT);

            if (player.isSneaking()) {
                stopSliding(player);
                return;
            }

            double dirX = player.getEntityData().getDouble("slideDirX");
            double dirZ = player.getEntityData().getDouble("slideDirZ");
            float speed = player.getEntityData().getFloat("slideSpeed");

            if (speed <= 0) {
                stopSliding(player);
                return;
            }

            if (player.collidedHorizontally) {
                stopSliding(player);
                return;
            }

            player.motionX = dirX * speed;
            player.motionZ = dirZ * speed;
            player.velocityChanged = true;

            speed -= SLIDE_DECAY_RATE;
            player.getEntityData().setFloat("slideSpeed", speed);

            spawnSlideParticles(player);
        }
    }

    private static void stopSliding(EntityPlayer player) {
        player.motionX = 0;
        player.motionZ = 0;
        player.velocityChanged = true;
        updatePlayerSize(player, 0.6F, NORMAL_HITBOX_HEIGHT);
        player.getEntityData().setInteger("diveCount", 0);
        setState(player, DiveState.NONE);
    }

    private static void updatePlayerSize(EntityPlayer player, float width, float height) {
        if (player.height == height && player.width == width) return; 
        try {
            if (setSizeMethod != null) setSizeMethod.invoke(player, width, height);
        } catch (Exception e) {}
    }

    private static void spawnSlideParticles(EntityPlayer player) {
        if (player.world instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) player.world;
            worldServer.spawnParticle(
                EnumParticleTypes.BLOCK_DUST,
                player.posX, player.posY + 0.1, player.posZ, 
                3, 0.2, 0.0, 0.2, 0.05, 
                Block.getStateId(Blocks.DIRT.getDefaultState())
            );
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