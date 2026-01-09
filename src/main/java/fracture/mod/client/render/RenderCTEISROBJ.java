//package fracture.mod.client.render;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.block.model.IBakedModel;
//import net.minecraft.client.renderer.texture.TextureMap;
//import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.obj.OBJLoader;
//import net.minecraftforge.common.model.TRSRTransformation;
//
//import java.util.HashMap;
//import java.util.Map;
//
//		Class used to render OBJs 
//		Currently not in use
//
//public class RenderCTEISROBJ extends TileEntityItemStackRenderer {
//	
//	//Custom TEISR system that displays objs
//
//    // CACHE
//    private static final Map<Item, IBakedModel> modelCache = new HashMap<>();
//
//    @Override
//    public void renderByItem(ItemStack itemStack) {
//        IBakedModel bakedModel = getModel(itemStack);
//
//        if (bakedModel != null) {
//            GlStateManager.pushMatrix();
//            
//            // Texture Bind
//            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//            
//            // GUI Lighting Fix(not working)
//            GlStateManager.enableRescaleNormal();
//            GlStateManager.enableAlpha();
//            RenderHelper.enableStandardItemLighting();
//
//            // Offcenter adjustment
//            GlStateManager.translate(0.5, 0.5, 0.5);
//
//            // Render
//            Minecraft.getMinecraft().getRenderItem().renderItem(itemStack, bakedModel);
//
//            GlStateManager.popMatrix();
//        }
//    }
//
//    // Helper method to find or load the correct model for the specific gun
//    private IBakedModel getModel(ItemStack stack) {
//        Item item = stack.getItem();
//
//        // Check if gun model is already loaded
//        if (modelCache.containsKey(item)) {
//            return modelCache.get(item);
//        }
//
//        // 2. If not, load it dynamically based on the Registry Name
//        try {
//            ResourceLocation regName = item.getRegistryName();
//            if (regName == null) return null;
//
//            // Useing toString() and split() to avoid method errors
//            // regName.toString() gives "fracture:old_earth_rifle"
//            String[] parts = regName.toString().split(":"); 
//            String domain = parts[0]; // "fracture"
//            String path = parts[1];   // "old_earth_rifle"
//
//            // Dynamic path: "fracture:models/item/gun_name.obj"
//            ResourceLocation modelLoc = new ResourceLocation(
//                domain, 
//                "models/item/" + path + ".obj"
//            );
//
//            IModel rawModel = OBJLoader.INSTANCE.loadModel(modelLoc);
//            
//            IBakedModel baked = rawModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
//                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
//
//            // Store in cache
//            modelCache.put(item, baked);
//            return baked;
//
//        } catch (Exception e) {
//            System.err.println("Failed to load custom gun model for: " + item.getRegistryName());
//            e.printStackTrace();
//            modelCache.put(item, null); 
//            return null;
//        }
//    }
//}