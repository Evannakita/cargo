package evannakita.cargo.client.renderer;

import evannakita.cargo.Cargo;
import evannakita.cargo.CargoClient;
import evannakita.cargo.client.model.BicycleEntityModel;
import evannakita.cargo.entity.BicycleEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class BicycleEntityRenderer extends EntityRenderer<BicycleEntity>{
    private static final Identifier TEXTURE = new Identifier(Cargo.MOD_ID, "textures/entity/bicycle.png");
    protected final EntityModel<BicycleEntity> model;

    public BicycleEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.7f;
        this.model = new BicycleEntityModel(context.getPart(CargoClient.BICYCLE_LAYER));
    }

    @Override
    public void render(BicycleEntity BicycleEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(BicycleEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        long l = (long)((Entity)BicycleEntity).getId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float h = (((float)(l >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float j = (((float)(l >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float k = (((float)(l >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        matrixStack.translate(h, j, k);
        float o = MathHelper.lerp(g, ((BicycleEntity)BicycleEntity).prevPitch, ((Entity)BicycleEntity).getPitch());
        matrixStack.translate(0.0f, 0.375f, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-o));
        float p = (float)((BicycleEntity)BicycleEntity).getDamageWobbleTicks() - g;
        float q = ((BicycleEntity)BicycleEntity).getDamageWobbleStrength() - g;
        if (q < 0.0f) {
            q = 0.0f;
        }
        if (p > 0.0f) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(p) * p * q / 10.0f * (float)((BicycleEntity)BicycleEntity).getDamageWobbleSide()));
        }
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        this.model.setAngles(BicycleEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(BicycleEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(BicycleEntity entity) {
        return TEXTURE;
    }
}
