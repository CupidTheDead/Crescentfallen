package fracture.mod.util.handlers;

import net.minecraft.util.IStringSerializable;

public class EnumHandler 
{
	public static enum EnumType implements IStringSerializable
	{
		//METALS
		copper(0, "copper"),
		//IRON(1, "iron"),
		TIN(1, "tin");
		//URANIUM(3, "uranium"),
		//ALUMINUM(4, "aluminum"),
		//GOLD(5, "goblin"),
		
		//GEMS
		//DIAMOND(6, "diamond"),
		//TANZ(7, "tanz"),
		//EMERALD(8, "emerald"),
		//COAL(9, "coal"),
		//PLUTONIUM(10, "pluto"),
		
		//OTHER(h stands for happy, or seritonium, o stands for oil)
		//HSHALE(11, "hshale"),
	//	OSHALE(12, "oshale"),
		//CENT(13, "centurium");
		
		private static final EnumType[] META_LOOKUP = new EnumType[values().length];
		private final int meta;
		private final String name, unlocalizedName;
		
		private EnumType(int meta, String name) 
		{
			this(meta, name, name);
		}
		
		
		private EnumType(int meta, String name, String unlocalizedName) 
		{
			this.meta = meta;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			
			
			
		}
		@Override
		public String getName() 
		{
			return this.name;			
		}
		public int getMeta()
		{
			return this.meta;
		}
		public String getUnlocalizedName()
		{
			return this.unlocalizedName;
		}		
		@Override
		public String toString()
		{
			return this.name();
		}
		
		
		public static EnumType byMetadata(int meta)
		{
			return META_LOOKUP[meta];
		}
		
		static 
		{
			for(EnumType enumtype : values())
			{
				META_LOOKUP[enumtype.getMeta()]= enumtype;
			}
		}
	}
}
