package evannakita.cargo.client.renderer;

import evannakita.cargo.Cargo;
import evannakita.cargo.CargoClient;
import evannakita.cargo.client.model.BoxcarEntityModel;
import evannakita.cargo.entity.BoxcarEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BoxcarEntityRenderer extends EntityRenderer<BoxcarEntity>{
    private static final Identifier TEXTURE = new Identifier(Cargo.MOD_ID, "textures/entity/boxcar_hull.png");
    protected final EntityModel<BoxcarEntity> model;

    public BoxcarEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 1.0f;
        this.model = new BoxcarEntityModel(context.getPart(CargoClient.BOXCAR_LAYER));
    }

    @Override
    public void render(BoxcarEntity boxcarEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(boxcarEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(BoxcarEntity entity) {
        return TEXTURE;
    }
}
