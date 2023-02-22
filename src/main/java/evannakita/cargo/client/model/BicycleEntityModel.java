package evannakita.cargo.client.model;

import evannakita.cargo.entity.BicycleEntity;
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
public class BicycleEntityModel extends EntityModel<BicycleEntity> {
	private final ModelPart bb_main;
	public BicycleEntityModel(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bb_main = modelPartData.addChild("bb_main", ModelPartBuilder.create().uv(40, 54).cuboid(-2.5F, -8.5F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
		.uv(26, 0).cuboid(-2.0F, -7.0F, -1.0F, 4.0F, 2.0F, 14.0F, new Dilation(0.0F))
		.uv(8, 10).cuboid(3.0F, -5.0F, -1.0F, 0.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(4, 4).cuboid(3.0F, -1.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
		.uv(8, 6).cuboid(-3.0F, -11.0F, -1.0F, 0.0F, 4.0F, 2.0F, new Dilation(0.0F))
		.uv(4, 0).cuboid(-7.0F, -11.0F, -2.0F, 4.0F, 0.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 40).cuboid(-1.0F, -12.0F, -18.0F, 2.0F, 12.0F, 12.0F, new Dilation(0.0F))
		.uv(28, 28).cuboid(-1.0F, -12.0F, 6.0F, 2.0F, 12.0F, 12.0F, new Dilation(0.0F))
		.uv(28, 52).cuboid(-2.0F, -24.0F, -13.0F, 4.0F, 19.0F, 2.0F, new Dilation(0.0F))
		.uv(44, 26).cuboid(-6.0F, -24.0F, -15.0F, 12.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-1.0F, -20.0F, -1.0F, 2.0F, 15.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 20).cuboid(-3.0F, -7.0F, -1.0F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(48, 0).cuboid(-2.5F, -8.5F, 9.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
		.uv(42, 38).cuboid(-1.0F, -19.0F, -13.0F, 2.0F, 2.0F, 14.0F, new Dilation(0.0F))
		.uv(36, 16).cuboid(-2.0F, -22.0F, -3.0F, 4.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		bb_main.addChild("cube_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -1.0F, -17.0F, 4.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.0F, 12.0F, -0.7854F, 0.0F, 0.0F));

		bb_main.addChild("cube_r2", ModelPartBuilder.create().uv(0, 20).cuboid(-1.0F, -1.0F, -17.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.0F, 0.0F, -0.7854F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void setAngles(BicycleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bb_main.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}