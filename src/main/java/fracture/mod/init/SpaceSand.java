package fracture.mod.init;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SpaceSand extends BlockFalling implements IHasModel
{
	//public SpaceStone(Material materialIn) {
	public SpaceSand(String name, Material material) 
	{

		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CFMain.CrescentfallenBlocks);
		setHardness(0.5f);
		setResistance(0.5f);
		setLightLevel(0.0f);
		setLightOpacity(2222);
		//setDefaultSlipperiness(slipperiness);
		setHarvestLevel("shovel", 0);
		setSoundType(blockSoundType.SAND);
		//setBlockUnbreakable();
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	//@Override
	//public Item getItemDropped(IBlockState state, Random rand, int fortune) 
	//{
	//	return ItemInit.itemhere;
	//}
	//Above statement is used if you want a block to drop an item

	//@Override
	//public void onStartFalling(World worldIn, BlockPos pos, IBlockState fallingState) {
	    // Called when the block begins to fall
	//}
	
	@Override
	public void onEndFalling(World worldIn, BlockPos pos, IBlockState fallState, IBlockState hitState) {
	    super.onEndFalling(worldIn, pos, fallState, hitState);

	    if (worldIn.isRemote) {
	        for (int i = 0; i < 5; ++i) {
	            double offsetX = worldIn.rand.nextDouble();
	            double offsetZ = worldIn.rand.nextDouble();
	            double velocityX = (worldIn.rand.nextDouble() - 0.5D) * 0.1D;
	            double velocityY = 0.1D;
	            double velocityZ = (worldIn.rand.nextDouble() - 0.5D) * 0.1D;

	            worldIn.spawnParticle(
	                EnumParticleTypes.BLOCK_DUST,
	                pos.getX() + offsetX,
	                pos.getY(),
	                pos.getZ() + offsetZ,
	                velocityX,
	                velocityY,
	                velocityZ,
	                Block.getStateId(fallState) // particle appearance matches this block
	            );
	        }
	    }
	}
	
	@Override
	public void registerModels()
	{
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
	}

}
