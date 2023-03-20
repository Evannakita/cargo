package evannakita.cargo.client.model;

import evannakita.cargo.entity.BoxcarEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class BoxcarEntityModel extends EntityModel<BoxcarEntity> {
	private final ModelPart root;

	public BoxcarEntityModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		for (int i = 0; i < 3; i++) {
			float f = i;
			modelPartData.addChild(
				Integer.toString(i),
				ModelPartBuilder.create()
					.uv(0, 0).cuboid(0.0F, f*16.0F, 0.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)),
				ModelTransform.pivot(0.0F, 24.0F, 0.0F)
			);
		}
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	@Override
	public void setAngles(BoxcarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}