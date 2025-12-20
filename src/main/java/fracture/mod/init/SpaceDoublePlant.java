package fracture.mod.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.IStringSerializable;
import javax.annotation.Nullable;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;

import java.util.Random;

public class SpaceDoublePlant extends BlockBush implements IHasModel {

	public static final PropertyEnum<Half> HALF = PropertyEnum.create("half", Half.class);

	public SpaceDoublePlant(String name, Material material) {
		super(material);
		this.setTranslationKey(name);
		this.setRegistryName(name);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(CFMain.CrescentfallenBlocks);
		this.setHardness(0.0F);
		this.setLightOpacity(0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, Half.LOWER));

		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (!super.canPlaceBlockAt(worldIn, pos))
			return false; // base checks

		return pos.getY() < worldIn.getHeight() - 1 && worldIn.isAirBlock(pos.up());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state,
			@Nullable net.minecraft.entity.EntityLivingBase placer, net.minecraft.item.ItemStack stack) {

		if (!worldIn.isAirBlock(pos.up()) && !worldIn.getBlockState(pos.up()).getMaterial().isReplaceable()) {

			worldIn.setBlockToAir(pos);
			return;
		}

		// Place lower at pos and upper at pos.up()
		worldIn.setBlockState(pos, this.getDefaultState().withProperty(HALF, Half.LOWER), 2);
		worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, Half.UPPER), 2);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		Half half = state.getValue(HALF);
		BlockPos other = (half == Half.UPPER) ? pos.down() : pos.up();
		IBlockState otherState = worldIn.getBlockState(other);

		if (otherState.getBlock() == this && otherState.getValue(HALF) != half) {

			worldIn.setBlockToAir(other);
		}

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		Half half = state.getValue(HALF);
		if (half == Half.UPPER) {

			IBlockState below = worldIn.getBlockState(pos.down());
			if (below.getBlock() != this || below.getValue(HALF) != Half.LOWER) {

				worldIn.setBlockToAir(pos);
			}
		} else {

			IBlockState above = worldIn.getBlockState(pos.up());
			boolean upperOk = (above.getBlock() == this && above.getValue(HALF) == Half.UPPER);

			boolean soilOk = worldIn.getBlockState(pos.down()).getBlock()
					.canSustainPlant(worldIn.getBlockState(pos.down()), worldIn, pos.down(), EnumFacing.UP, this);
			if (!upperOk && !soilOk) {

				worldIn.setBlockToAir(pos);
				if (!worldIn.isRemote) {
					spawnAsEntity(worldIn, pos, new net.minecraft.item.ItemStack(this));
				}
			}
		}

		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {

		return (state.getValue(HALF) == Half.UPPER) ? Item.getItemFromBlock(Blocks.AIR) : Item.getItemFromBlock(this);
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 1.0D, 0.9D);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(HALF, meta == 1 ? Half.UPPER : Half.LOWER);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(HALF) == Half.UPPER) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HALF);
	}

	@Override
	public void registerModels() {
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

	@Override
	public EnumOffsetType getOffsetType() {
		return EnumOffsetType.XZ;
	}

	public static enum Half implements IStringSerializable {
		LOWER("lower"), UPPER("upper");

		private final String name;

		Half(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}
