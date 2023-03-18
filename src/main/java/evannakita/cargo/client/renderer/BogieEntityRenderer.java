package evannakita.cargo.client.renderer;

import evannakita.cargo.CargoClient;
import evannakita.cargo.client.model.BogieEntityModel;
import evannakita.cargo.entity.BogieEntity;
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
import net.minecraft.util.math.Vec3d;

public class BogieEntityRenderer extends EntityRenderer<BogieEntity>{
    private static final Identifier TEXTURE = new Identifier("cargo", "textures/entity/bogie.png");
    protected final EntityModel<BogieEntity> model;

    public BogieEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 1.0f;
        this.model = new BogieEntityModel(context.getPart(CargoClient.BOGIE_LAYER));
    }

    @Override
    public void render(BogieEntity bogieEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(bogieEntity, f, g, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        long l = (long)((Entity)bogieEntity).getId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float h = (((float)(l >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float j = (((float)(l >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float k = (((float)(l >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        matrixStack.translate(h, j, k);
        double d = MathHelper.lerp((double)g, ((BogieEntity)bogieEntity).lastRenderX, ((Entity)bogieEntity).getX());
        double e = MathHelper.lerp((double)g, ((BogieEntity)bogieEntity).lastRenderY, ((Entity)bogieEntity).getY());
        double m = MathHelper.lerp((double)g, ((BogieEntity)bogieEntity).lastRenderZ, ((Entity)bogieEntity).getZ());
        Vec3d vec3d = ((BogieEntity)bogieEntity).snapPositionToTrack(d, e, m);
        float o = MathHelper.lerp(g, ((BogieEntity)bogieEntity).prevPitch, ((Entity)bogieEntity).getPitch());
        if (vec3d != null) {
            Vec3d vec3d2 = ((BogieEntity)bogieEntity).snapPositionToTrackWithOffset(d, e, m, 0.3f);
            Vec3d vec3d3 = ((BogieEntity)bogieEntity).snapPositionToTrackWithOffset(d, e, m, -0.3f);
            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }
            if (vec3d3 == null) {
                vec3d3 = vec3d;
            }
            matrixStack.translate(vec3d.x - d, (vec3d2.y + vec3d3.y) / 2.0 - e, vec3d.z - m);
            Vec3d vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
            if (vec3d4.length() != 0.0) {
                vec3d4 = vec3d4.normalize();
                f = (float)(Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / Math.PI);
                o = (float)(Math.atan(vec3d4.y) * 73.0);
            }
        }
        matrixStack.translate(0.0f, 1.4375f, 0.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-o));
        float p = (float)((BogieEntity)bogieEntity).getDamageWobbleTicks() - g;
        float q = ((BogieEntity)bogieEntity).getDamageWobbleStrength() - g;
        if (q < 0.0f) {
            q = 0.0f;
        }
        if (p > 0.0f) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(p) * p * q / 10.0f * (float)((BogieEntity)bogieEntity).getDamageWobbleSide()));
        }
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        this.model.setAngles(bogieEntity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(bogieEntity)));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
    }

    @Override
    public Identifier getTexture(BogieEntity entity) {
        return TEXTURE;
    }
}
