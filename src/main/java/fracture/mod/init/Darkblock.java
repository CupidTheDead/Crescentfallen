package fracture.mod.init;

import java.util.Random;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Darkblock extends Block implements IHasModel {

	// public SpaceStone(Material materialIn) {
	public Darkblock(String name, Material material) {

		super(material);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(CFMain.CrescentfallenBlocks);
		setHardness(3.0f);
		setResistance(3.0f);
		setLightLevel(0.0f);
		setLightOpacity(0);
		// setDefaultSlipperiness(slipperiness);
		setHarvestLevel("pickaxe", 1);
		setSoundType(blockSoundType.STONE);
		// setBlockUnbreakable();

		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
	    return Items.COAL;
	}
	@Override
	public int damageDropped(IBlockState state) {
	    return 1; // 1 = charcoal
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		EntityPlayer entity = Minecraft.getMinecraft().player;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int i = x;
		int j = y;
		int k = z;
		if (true)
			for (int l = 0; l < 4; ++l) {
				double d0 = (i + random.nextFloat());
				double d1 = (j + random.nextFloat());
				double d2 = (k + random.nextFloat());
				int i1 = random.nextInt(2) * 2 - 1;
				double d3 = (random.nextFloat() - 0.5D) * 0.5D;
				double d4 = (random.nextFloat() - 0.5D) * 0.5D;
				double d5 = (random.nextFloat() - 0.5D) * 0.5D;
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
			}

	}
	
	@Override
	public void registerModels() {
		CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		{
		}
	}

}
