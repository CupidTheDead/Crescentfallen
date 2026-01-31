package fracture.mod.world.epchanges;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class CfGanymedeChunkgen implements IChunkGenerator {
    private final World world;
    private final Random rand;
    // Replace
    private final IBlockState STONE = Blocks.STONE.getDefaultState(); 
    private final IBlockState AIR = Blocks.AIR.getDefaultState();

    public CfGanymedeChunkgen(World world, long seed) {
        this.world = world;
        this.rand = new Random(seed);
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer primer = new ChunkPrimer();
        
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                for (int k = 0; k < 60; ++k) { 
                   primer.setBlockState(i, k, j, STONE);
                }
            }
        }

        // Replace blocks
        this.replaceBiomeBlocks(x, z, primer, this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16));

        Chunk chunk = new Chunk(this.world, primer, x, z);
        chunk.generateSkylightMap();
        return chunk;
    }

    public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, Biome[] biomesIn) {
        if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(this, x, z, primer, this.world)) return;

        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                // Biome replacement logic wip
                
                // Delete bedrock
            	// set to air
            	// generates as ganymede stone
                primer.setBlockState(i, 0, j, STONE); 

            }
        }
    }

    @Override
    public void populate(int x, int z) {}
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;}
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.world.getBiome(pos).getSpawnableList(creatureType);}
    @Nullable @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;}
    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;}
    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {}
}