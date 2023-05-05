package evannakita.cargo.client.model;

import java.util.Arrays;

import evannakita.cargo.ModBlocks;
import evannakita.cargo.entity.FlatbedEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
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
import net.minecraft.predicate.block.BlockStatePredicate;

public class FlatbedEntityModel extends EntityModel<FlatbedEntity> {
	private ModelPart root;
	public BlockPattern blockPattern;

	public FlatbedEntityModel(ModelPart root) {
		this.root = root;
	}

	public static TexturedModelData getTexturedModelData() {
		return getTexturedModelData(
			BlockPatternBuilder.start()
				.aisle("#")
				.where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.CONTAINER)))
				.build()
		);
	}

	public static TexturedModelData getTexturedModelData(BlockPattern blockPattern) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		for (int depth = 0; depth < blockPattern.getDepth(); depth++) {
			for (int height = 0; height < blockPattern.getHeight(); height++) {
				for (int width = 0; width < blockPattern.getWidth(); width++) {			
					float d = depth;
					float h = height;
					float w = width;
					modelPartData.addChild(
						Arrays.asList(depth, height, width).toString(),
						ModelPartBuilder.create()
							.uv(0, 0)
							.cuboid(d*16.0F, h*16.0F, w*16.0F, 16.0F, 16.0F, 16.0F, new Dilation(0.0F)),
						ModelTransform.pivot(0.0F, 24.0F, 0.0F)
					);
				}
			}
		}
		return TexturedModelData.of(modelData, 16, 16);
	}

	public void updateModel(BlockPattern blockPattern) {
		this.blockPattern = blockPattern;
		this.root = getTexturedModelData(blockPattern).createModel();
	}

	@Override
	public void setAngles(FlatbedEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}