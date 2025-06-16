package fracture.mod.util.handlers;

import java.io.File;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler 
{
	public static Configuration config; 
	
	
	
	public static void init(File file)
	{
		config= new Configuration(file);
		
		String catagory;
		
		catagory = "IDs";
		config.addCustomCategoryComment(catagory, "Set ID's for each planet");

	}
	
	
}
