package fracture.mod.init;

import fracture.mod.CFMain;
import fracture.mod.util.IHasModel;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * SpaceMushroom behaves like a vanilla mushroom (BlockMushroom),
 * but emits light so it is visible in caves.
 */
public class SpaceMushroom extends BlockMushroom implements IHasModel {

    public SpaceMushroom(String name) {
        super();

        setTranslationKey(name);
        setRegistryName(name);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        // Light level: 0.6 - 1.0 (0.0 = none). 0.7 is a visible glow but not blinding.
        setLightLevel(0.7F);
        setCreativeTab(CFMain.CrescentfallenBlocks);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void registerModels() {
        CFMain.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
