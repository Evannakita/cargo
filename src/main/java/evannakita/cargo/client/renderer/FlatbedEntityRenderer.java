package evannakita.cargo.client.renderer;

import evannakita.cargo.Cargo;
import evannakita.cargo.CargoClient;
import evannakita.cargo.client.model.FlatbedEntityModel;
import evannakita.cargo.entity.FlatbedEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FlatbedEntityRenderer extends EntityRenderer<FlatbedEntity>{
    private static final Identifier TEXTURE = new Identifier(Cargo.MOD_ID, "textures/entity/container.png");
    protected final FlatbedEntityModel model;

    public FlatbedEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 1.0f;
        this.model = new FlatbedEntityModel(context.getPart(CargoClient.FLATBED_LAYER));
    }

    @Override
    public void render(FlatbedEntity flatbedEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        BlockPattern blockPattern = flatbedEntity.getBlockPattern();
        if (this.model.blockPattern != blockPattern) {
            this.model.updateModel(blockPattern);
        }
        super.render(flatbedEntity, f, g, matrixStack, vertexConsumerProvider, i);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(flatbedEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public Identifier getTexture(FlatbedEntity entity) {
        return TEXTURE;
    }
}
