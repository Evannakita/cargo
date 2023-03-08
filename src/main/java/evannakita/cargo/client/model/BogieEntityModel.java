package evannakita.cargo.client.model;

import evannakita.cargo.entity.BogieEntity;
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

// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class BogieEntityModel extends EntityModel<BogieEntity> {
	private final ModelPart bb_main;
	public BogieEntityModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(1, 0).cuboid(-10.0F, -11.0F, 4.0F, 2.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-8.0F, -12.0F, 3.0F, 0.0F, 10.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-8.0F, -12.0F, -13.0F, 0.0F, 10.0F, 10.0F, new Dilation(0.0F))
		.uv(1, 0).cuboid(-10.0F, -11.0F, -12.0F, 2.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 26).cuboid(-12.0F, -8.0F, -9.0F, 24.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 26).cuboid(-12.0F, -8.0F, 7.0F, 24.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(-12.0F, -9.0F, -3.0F, 24.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		bb_main.addChild("frame_r1", ModelPartBuilder.create().uv(0, 18).cuboid(22.0F, -3.5F, -11.0F, 0.0F, 8.0F, 22.0F, new Dilation(0.0F))
		.uv(0, 18).cuboid(0.0F, -3.5F, -11.0F, 0.0F, 8.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(11.0F, -8.5F, 0.0F, 0.0F, 3.1416F, 0.0F));

		bb_main.addChild("wheel_r1", ModelPartBuilder.create().uv(1, 0).cuboid(-10.0F, -11.0F, 12.0F, 2.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-8.0F, -12.0F, 11.0F, 0.0F, 10.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 6).cuboid(-8.0F, -12.0F, -5.0F, 0.0F, 10.0F, 10.0F, new Dilation(0.0F))
		.uv(1, 0).cuboid(-10.0F, -11.0F, -4.0F, 2.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 8.0F, 0.0F, 3.1416F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	@Override
	public void setAngles(BogieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}