package evannakita.cargo;

import evannakita.cargo.client.model.BicycleEntityModel;
import evannakita.cargo.client.model.BogieEntityModel;
import evannakita.cargo.client.model.FlatbedEntityModel;
import evannakita.cargo.client.renderer.BicycleEntityRenderer;
import evannakita.cargo.client.renderer.BogieEntityRenderer;
import evannakita.cargo.client.renderer.FlatbedEntityRenderer;
import evannakita.cargo.screen.FireboxScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CargoClient implements ClientModInitializer {

    public static final EntityModelLayer BICYCLE_LAYER = new EntityModelLayer(new Identifier(Cargo.MOD_ID, "bicycle"), "main");
    public static final EntityModelLayer BOGIE_LAYER = new EntityModelLayer(new Identifier(Cargo.MOD_ID, "bogie"), "main");
    public static final EntityModelLayer FLATBED_LAYER = new EntityModelLayer(new Identifier(Cargo.MOD_ID, "flatbed"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Cargo.BICYCLE, (context) -> new BicycleEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(BICYCLE_LAYER, BicycleEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(Cargo.BOGIE, (context) -> new BogieEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(BOGIE_LAYER, BogieEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(Cargo.FLATBED, (context) -> new FlatbedEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(FLATBED_LAYER, FlatbedEntityModel::getTexturedModelData);

        HandledScreens.register(Cargo.BOXCAR_SCREEN_HANDLER, Generic3x3ContainerScreen::new);
        HandledScreens.register(Cargo.FIREBOX_SCREEN_HANDLER, FireboxScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GONDOLA, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TANK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TANK_HATCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TRACK_WITH_WHEELS, RenderLayer.getCutout());
    }
}
