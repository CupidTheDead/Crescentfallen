package fracture.mod.init;

	import java.util.Random;
	import micdoodle8.mods.galacticraft.api.vector.Vector3;
	import micdoodle8.mods.galacticraft.api.world.ITeleportType;
	import micdoodle8.mods.galacticraft.core.entities.EntityLander;
	import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
	import micdoodle8.mods.galacticraft.core.util.CompatibilityManager;
	import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
	import micdoodle8.mods.galacticraft.core.util.GCLog;
	import net.minecraft.entity.Entity;
	import net.minecraft.entity.player.EntityPlayerMP;
	import net.minecraft.util.math.BlockPos;
	import net.minecraft.world.World;
	import net.minecraft.world.WorldServer;
	import net.minecraft.world.biome.Biome;

	//Note: currently not functioning, needs to be fixed
	
	public class TeleportTypeCF implements ITeleportType {
		//changed to basic random y spawn algorithm (400-500)
	  protected double playerSpawnY = 400 + (Math.random() * 500);
	  
	  public boolean useParachute() {
	    return true;
	  }
	  
	  public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player) {
	    if (player != null) {
	      GCPlayerStats stats = GCPlayerStats.get((Entity)player);
	      double x = stats.getCoordsTeleportedFromX();
	      double z = stats.getCoordsTeleportedFromZ();
	      //changed to getbiome 
	      Biome biome = world.getBiome(new BlockPos(x, 0.0D, z));
	      //if (BiomeUtils.isOceanBiome(biome))
	        //return null; 
	      int limit = ConfigManagerCore.otherPlanetWorldBorders - 2;
	      if (limit > 20) {
	        if (x > limit) {
	          z *= limit / x;
	          x = limit;
	        } else if (x < -limit) {
	          z *= -limit / x;
	          x = -limit;
	        } 
	        if (z > limit) {
	          x *= limit / z;
	          z = limit;
	        } else if (z < -limit) {
	          x *= -limit / z;
	          z = -limit;
	        } 
	      } 
	      return new Vector3(x, this.playerSpawnY, z);
	    } 
	    return null;
	  }
	  
	  
	  //THIS FUCTION WAS CHANGED
	    @Override
	    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity) {
	        return new Vector3(entity.posX, this.playerSpawnY, entity.posZ);
	    }
	  //
	    
	    
	  public Vector3 getParaChestSpawnLocation(WorldServer world, EntityPlayerMP player, Random rand) {
	    return null;
	  }
	  
	  public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket) {
	        GCPlayerStats stats = GCPlayerStats.get(player);

	        if (!ridingAutoRocket && !ConfigManagerCore.disableLander && stats.getTeleportCooldown() <= 0) {
	            if (player.capabilities.isFlying) {
	                player.capabilities.isFlying = false;
	            }

	            EntityLander lander = new EntityLander(player);
	            lander.setPosition(player.posX, player.posY, player.posZ);

	            if (!newWorld.isRemote) {
	                boolean previous = CompatibilityManager.forceLoadChunks((WorldServer) newWorld);
	                lander.forceSpawn = true;
	                newWorld.spawnEntity(lander);
	                newWorld.updateEntityWithOptionalForce(lander, true);
	                player.startRiding(lander);
	                CompatibilityManager.forceLoadChunksEnd((WorldServer) newWorld, previous);
	                //GCLog.debug("Entering lander at : " + player.posX + "," + player.posZ + " lander spawn at: " + lander.posX + "," + lander.posZ);
	            }
	            stats.setTeleportCooldown(10);
	    } 
	  }
	  
	  public void setupAdventureSpawn(EntityPlayerMP player) {}
	}
