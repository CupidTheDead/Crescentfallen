package fracture.mod.init;

import net.minecraft.block.Block;

import java.util.Random;

import fracture.mod.Main;
import fracture.mod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpaceGrass extends Block implements IHasModel 
{
	
	public SpaceGrass(String name, Material material) 
	{

		super(Material.GRASS);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(fracture.mod.Main.CrescentfallenBlocks);
		setHardness(0.6f);
		setResistance(0.6f);
		setLightLevel(0.0f);
		setLightOpacity(0);
		//setDefaultSlipperiness(slipperiness);
		setHarvestLevel("shovel", 0);
		setSoundType(blockSoundType.PLANT);
		//setBlockUnbreakable();
		
		BlockInit.BLOCKS.add(this);
		ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory"); {}
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
	net.minecraftforge.common.IPlantable plantable) {
		return true;
	}
	
	
	
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
				double d0 = (i + 0.5) + (random.nextFloat() - 0.5) * 0.5D * 20;
				double d1 = ((j + 0.7) + (random.nextFloat() - 0.5) * 0.5D) + 0.5;
				double d2 = (k + 0.5) + (random.nextFloat() - 0.5) * 0.5D * 20;
				world.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, d0, d1, d2, 0, 0, 0);
			}
	}
}
	
