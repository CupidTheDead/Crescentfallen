package fracture.mod.init;

import java.util.ArrayList;
import java.util.List;
import fracture.mod.util.IHasModel;
import fracture.mod.objects.blocks.BlockBase;
import fracture.mod.objects.blocks.BlockOres;
//import fracture.mod.objects.blocks.BlockOres;
//import fracture.mod.objects.blocks.BlockOres;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockInit 
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	//--------------------------------MATERIAL
	
	public static final Block BLOCK_CENTURIUM = new Blockofcent("block_centurium", Material.IRON);
	public static final Block BLOCK_TERRAMINIUM = new Blockofmetal("block_terraminium", Material.IRON);
	public static final Block BLOCK_SERITONIUM = new Blockofmetal("block_seritonium", Material.IRON);

	//--------------------------------TURF
	
	public static final Block DRIED_DIRT = new SpaceDirt("dried_dirt", Material.GROUND);
	public static final Block DREAMYARD_DIRT = new SpaceDirt("dreamyard_dirt", Material.GROUND);
	//public static final Block SAND_FRACTURE_BROKEN = new SpaceDirt("sand_fracture_broken", Material.GROUND);
	
	
	//public static final Block GRAVEL_FRACTURE = new SpaceSand("gravel_fracture", Material.SAND);
	//public static final Block GRAVEL_DREAMYARD = new SpaceSand("gravel_dreamyard", Material.SAND);
	//public static final Block GRAVEL_KONA = new SpaceSand("gravel_kona", Material.SAND);
	//public static final Block SAND_FRACTURE = new SpaceSand("sand_fracture", Material.SAND);
	//public static final Block SAND_FRACTURE_HOT = new SpaceSand("sand_fracture_hot", Material.SAND);
	//public static final Block SALT_DEPOSIT = new SpaceSand("salt_deposit", Material.SAND);
	
	//--------------------------------STONE
	
	//the fracture/destruction point
	public static final Block STONE_FRACTURE = new SpaceStone("stone_fracture", Material.ROCK);
	public static final Block ASTROID_FRACTURE = new SpaceStone("astroid_fracture", Material.ROCK);
	public static final Block SURFACE_FRACTURE = new SpaceStone("surface_fracture", Material.ROCK);
	public static final Block BURNT_STONE_FRACTURE = new SpaceStone("burnt_stone_fracture", Material.ROCK);
	public static final Block COBBLESTONE_FRACTURE = new SpaceStone("cobblestone_fracture", Material.ROCK);
	public static final Block PEAK_SEDIMENT_FRACTURE = new SpaceStone("peak_sediment_fracture", Material.ROCK);
	public static final Block STONE_SUBSURFACE_FRACTURE = new SpaceStone("stone_subsurface_fracture", Material.ROCK);
	
	//kona
	public static final Block STONE_KONA = new SpaceStone("stone_kona", Material.ROCK);
	public static final Block STONE_SUBSURFACE_KONA = new SpaceStone("stone_subsurface_kona", Material.ROCK);
	public static final Block SURFACE_KONA = new SpaceStone("surface_kona", Material.ROCK);
	
	//hollows
	
	//dreamyard
	public static final Block DREAMSTONE = new SpaceStone("dreamstone", Material.ROCK);
	
	//--------------------------------MISC
	
	public static final Block FLESH_PILE = new FleshPile("flesh_pile", Material.GROUND);
	public static final Block TRASH_PILE = new Trash("trash_pile", Material.GROUND);
	public static final Block OVERGRASS_FULL = new SpaceGrass("overgrass_full", Material.GROUND);
	public static final Block OVERGRASS_BLOCK = new SpaceGrass("overgrass_block", Material.GROUND);
	
	//--------------------------------ORES
	
	//orebasic
	public static final Block ORE_OVERWORLD = new BlockOres("ore_overworld", "overworld");
	//public static final Block ORE_FRACTURE = new BlockOres("ore_fracture", "addonDimensions.dimplanettwos1");
	//public static final Block ORE_KONA = new BlockOres("ore_fracture", "addonDimensions.dimmoonones1");
	//public static final Block ORE_HOLLOWS = new BlockOres("ore_hollows", "addonDimensions.dimplanetones1");

	//public static final Block ORE_DREAMYARD = new BlockOres("ore_fracture_uranium", Material.ROCK);
	
	//the fracture
	public static final Block ORE_FRACTURE_URANIUM = new SpaceOre2("ore_fracture_uranium", Material.ROCK);
	public static final Block ORE_FRACTURE_IRON = new SpaceOre1("ore_fracture_iron", Material.ROCK);
	public static final Block ORE_FRACTURE_TIN = new SpaceOre1("ore_fracture_tin", Material.ROCK);
	public static final Block ORE_FRACTURE_COPPER = new SpaceOre1("ore_fracture_copper", Material.ROCK);
	
	//kona
	public static final Block ORE_KONA_COPPER = new SpaceOre1("ore_kona_copper", Material.ROCK);
	public static final Block ORE_KONA_IRON = new SpaceOre1("ore_kona_iron", Material.ROCK);
	public static final Block ORE_KONA_TIN = new SpaceOre1("ore_kona_tin", Material.ROCK);
	
	//hollows
	//public static final Block ORE_HOLLOWS_TERRAMINIUM = new SpaceOreIce("ore_hollows_terraminium", Material.ICE);
	//public static final Block ORE_HOLLOWS_PLATINUM = new SpaceOre2("ore_hollows_platinum", Material.ROCK);
	//public static final Block ORE_HOLLOWS_ANTIGRAVITY = new SpaceOreGravity("ore_hollows_antigravity", Material.ROCK);
	
	//dreamyard
	//public static final Block ORE_DREAMYARD_IRON = new SpaceOre1("ore_fracture_uranium", Material.ROCK);
	//public static final Block ORE_DREAMYARD_GOLD = new SpaceOre0("ore_fracture_iron", Material.ROCK);
	//public static final Block ORE_DREAMYARD_URANIUM = new SpaceOre2("ore_fracture_tin", Material.ROCK);
	//public static final Block ORE_DREAMYARD_COPPER = new SpaceOre1("ore_fracture_copper", Material.ROCK);
	//public static final Block ORE_DREAMYARD_TIN = new SpaceOre1("ore_fracture_copper", Material.ROCK);
	//public static final Block ORE_DREAMYARD_ALUMINUM = new SpaceOre1("ore_fracture_copper", Material.ROCK);
	
	//gems
	//public static final Block ORE_DREAMYARD_EMERALD = new SpaceOreEmerald("ore_dreamyard_emerald", Material.ROCK);
	//public static final Block ORE_FRACTURE_EMERALD = new SpaceOreEmerald("ore_fracture_emerald", Material.ROCK);
	//public static final Block ORE_FRACTURE_COAL = new SpaceOreCoal("ore_fracture_coal", Material.ROCK);
	//public static final Block ORE_DREAMYARD_COAL = new SpaceOreCoal("ore_dreamyard_coal", Material.ROCK);
	public static final Block ORE_KONA_TANZANITE = new SpaceOreTanz("ore_kona_tanzanite", Material.ROCK);
	//public static final Block ORE_DREAMYARD_DIAMOND = new SpaceOreDiamond("ore_dreamyard_diamond", Material.ROCK);
	//public static final Block ORE_FRACTURE_DIAMOND = new SpaceOreDiamond("ore_fracture_diamond", Material.ROCK);
	//public static final Block ORE_DREAMYARD_PLUTO = new SpaceOrePlutonium("ore_dreamyard_pluto", Material.ROCK);
	
	//misc
	//public static final Block ORE_CENTURIUM = new SpaceOreCent("ore_centurium", Material.IRON);
	//public static final Block OIL_SHALE = new OilShale("oil_shale", Material.ROCK);
	//public static final Block SERITONIN_SHALE = new SerotoninShale("oil_shale", Material.ROCK);
	
	//--------------------------------MACHINES
	
	//public static final Block tester = new BlockBase("tester", Material.ROCK);

}
