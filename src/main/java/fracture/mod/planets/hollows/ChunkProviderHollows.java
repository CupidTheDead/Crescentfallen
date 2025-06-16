package fracture.mod.planets.hollows;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fracture.mod.world.chunk.ChunkProviderBase;
import fracture.mod.world.chunk.ChunkProviderHollowsTemp;
import fracture.mod.init.BlockInit;
import fracture.mod.planets.hollows.hollows.biome.BiomeDecoratorHollows;
import fracture.mod.world.MapGenAddonCaveGen;
import fracture.mod.world.MapGenAddonRavinGen;
import micdoodle8.mods.galacticraft.api.prefab.world.gen.MapGenBaseMeta;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.state.IBlockState;
//import micdoodle8.mods.galacticraft.api.world.ChunkProviderBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;

public class ChunkProviderHollows extends ChunkProviderHollowsTemp {

    private final BiomeDecoratorHollows decorator = new BiomeDecoratorHollows();
    private final MapGenAddonRavinGen ravineGenerator = new MapGenAddonRavinGen();
    private final MapGenAddonCaveGen caveGenerator = new MapGenAddonCaveGen(BlockInit.STONE_KONA.getDefaultState(), Blocks.LAVA.getDefaultState(),
            Sets.newHashSet(BlockInit.STONE_KONA, BlockInit.STONE_KONA));

    public ChunkProviderHollows(World par1World, long seed, boolean mapFeaturesEnabled) {
        	super(par1World, seed, mapFeaturesEnabled);
        	//this.stoneBlock = Blocks.STONE.getDefaultState();
        	//this.waterBlock =Blocks.WATER.getDefaultState();
        
	        // this.stoneBlock = BlockInit.STONE_FRACTURE.getDefaultState();
	        //this.waterBlock = GCBlocks.crudeOil.getDefaultState();
        
        	//this.stoneBlock = Blocks.STONE.getDefaultState();
        	//this.waterBlock =Blocks.WATER.getDefaultState();
        
    }

    
    protected List<MapGenBaseMeta> getWorldGenerators() {
        List<MapGenBaseMeta> generators = Lists.newArrayList();
        generators.add(this.caveGenerator);
        return generators;
    }

    
    public int getCraterProbability() {
        return 2000;
    }

    
    public void onChunkProvide(int cX, int cZ, ChunkPrimer primer) {
        this.ravineGenerator.generate(this.world, cX, cZ, primer);
    }

    
    public void onPopulate(int cX, int cZ) {

    }

    @Override
    public void recreateStructures(Chunk chunk, int x, int z) {
    }

    
    protected void decoratePlanet(World world, Random rand, int x, int z) {
        this.decorator.decorate(this.world, rand, x, z);
    }
}

    //@Override
    //protected IBlockState getTopBlock() {
    // TODO Auto-generated method stub
    //return null;
    
//	}

    //@Override
    //protected IBlockState getSubBlock() {
    //TODO Auto-generated method stub
    //return null;
    
//	}
