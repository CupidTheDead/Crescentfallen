package fracture.mod.objects.blocks.item;

import fracture.mod.util.interfaces.IMetaName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockVariants extends ItemBlock {

	public ItemBlockVariants(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	//Damage is used in variants to determine what an item is.
	
//	@Override
//	public String getUnlocalizedName(ItemStack stack) {
//		return super.getTranslationKey() + "_" + ((IMetaName) this.block).getSpecialName(stack);
//	}
	@Override
	public String getTranslationKey(ItemStack stack) {
	    return super.getTranslationKey() + "_" + ((IMetaName) this.block).getSpecialName(stack);
	}
}
