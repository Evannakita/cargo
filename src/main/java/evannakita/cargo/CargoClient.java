package evannakita.cargo;

import evannakita.cargo.client.model.BicycleEntityModel;
import evannakita.cargo.client.model.BogieEntityModel;
import evannakita.cargo.client.renderer.BicycleEntityRenderer;
import evannakita.cargo.client.renderer.BogieEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class CargoClient implements ClientModInitializer {

    public static final EntityModelLayer BICYCLE_LAYER = new EntityModelLayer(new Identifier("cargo", "bicycle"), "main");
    public static final EntityModelLayer BOGIE_LAYER = new EntityModelLayer(new Identifier("cargo", "bogie"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Cargo.BICYCLE, (context) -> new BicycleEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(BICYCLE_LAYER, BicycleEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(Cargo.BOGIE, (context) -> new BogieEntityRenderer(context));
        EntityModelLayerRegistry.registerModelLayer(BOGIE_LAYER, BogieEntityModel::getTexturedModelData);

        BlockRenderLayerMap.INSTANCE.putBlock(Cargo.TRACK_WITH_WHEELS, RenderLayer.getCutout());
    }
}
