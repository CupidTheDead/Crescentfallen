package fracture.mod.init;

import java.util.Random;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.EnumParticleTypes;

public class FleshPile extends BlockFalling implements IHasModel {

	public FleshPile(String name, Material material) {

		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CFMain.CrescentfallenBlocks);
		setHardness(0.8f);
		setResistance(0.5f);
		setLightLevel(0.0f);
		setLightOpacity(0);
		// setDefaultSlipperiness(slipperiness);
		setHarvestLevel("shovel", 0);
		setSoundType(blockSoundType.SLIME);
		// setBlockUnbreakable();

		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
	    // 20% chance to spawn a drip per tick
	    if (rand.nextFloat() < 0.2F) {
	        double x = pos.getX() + rand.nextDouble();
	        double y = pos.getY() - 0.05D; // Spawn just below the block
	        double z = pos.getZ() + rand.nextDouble();

	        BlockPos below = pos.down();
	        IBlockState stateBelow = worldIn.getBlockState(below);

	        // Only drip if air or non-solid below
	        if (!stateBelow.isFullCube()) {
	            worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return net.minecraft.init.Items.ROTTEN_FLESH;
	}

	@Override
	public int quantityDropped(Random random) {
		return 1 + random.nextInt(3); // Drops 1–3 rotten flesh
	}

	@Override
	public void registerModels() {
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		{
		}
	}

}
