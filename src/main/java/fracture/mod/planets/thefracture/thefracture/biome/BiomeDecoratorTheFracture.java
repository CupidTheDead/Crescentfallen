package fracture.mod.planets.thefracture.thefracture.biome;

import fracture.mod.planets.thefracture.thefracture.biome.gen.feature.WorldGenTheFractureburns;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.BiomeDecoratorSpace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiomeDecoratorTheFracture extends BiomeDecoratorSpace {

    private World currentWorld;


    public BiomeDecoratorTheFracture() {
    }

    @Override
    protected void setCurrentWorld(World world) {
        this.currentWorld = world;
    }

    @Override
    protected World getCurrentWorld() {
        return this.currentWorld;
    }

    @Override
    protected void decorate() {
    	
//        super.decorate(this.getCurrentWorld(), rand,HERE );
//
//        
//     // For example, if these are fields or arguments somewhere
//        int chunkX = this.posX; // or however your class stores it
//        int chunkZ = this.posZ;
//
//        // Convert chunk coords to world coords
//        BlockPos chunkStart = new BlockPos(chunkX * 16, 0, chunkZ * 16);
//        
//        super.decorate(this.getCurrentWorld(), rand, chunkStart);
//
//            BlockPos chunkPos = new BlockPos(this.posX * 16, 0, this.posZ * 16); // use actual chunkX and chunkZ
//            //super.decorate(this.getCurrentWorld(), rand, chunkPos);
//
//            if (rand.nextInt(4) == 0) {
//                BlockPos genPos = chunkPos.add(rand.nextInt(16), 0, rand.nextInt(16));
//                new WorldGenTheFractureburns(4).generate(this.currentWorld, this.rand, genPos);
//            
            
            
            
    	
        }
    }
