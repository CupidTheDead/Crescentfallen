package fracture.mod.init;

import net.minecraft.block.Block;
import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Trash extends Block implements IHasModel {

	public Trash(String name, Material material) {

		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CFMain.CrescentfallenBlocks);
		setHardness(0.8f);
		setResistance(0.5f);
		setLightLevel(0.0f);
		setLightOpacity(0);
		// setDefaultSlipperiness(slipperiness);
		setHarvestLevel("pickaxe", 1);
		setSoundType(blockSoundType.GROUND);
		// setBlockUnbreakable();

		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, net.minecraft.world.World worldIn,
			net.minecraft.util.math.BlockPos pos, java.util.Random rand) {
		// 20% chance per tick to spawn a drip
		if (rand.nextFloat() < 0.2F) {
			double x = (double) pos.getX() + rand.nextFloat();
			double y = (double) pos.getY();
			double z = (double) pos.getZ() + rand.nextFloat();

			BlockPos below = pos.down();
			IBlockState stateBelow = worldIn.getBlockState(below);

			// Only drip if block below is non-solid or transparent
			if (!stateBelow.isOpaqueCube()) {
				worldIn.spawnParticle(net.minecraft.util.EnumParticleTypes.DRIP_LAVA,
						x, y - 0.05D, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void registerModels() {
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		{
		}
	}

}
