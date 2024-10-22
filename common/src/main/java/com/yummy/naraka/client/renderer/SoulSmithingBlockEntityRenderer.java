package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.SoulSmithingBlock;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class SoulSmithingBlockEntityRenderer implements BlockEntityRenderer<SoulSmithingBlockEntity> {
    private final ModelPart root;
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public SoulSmithingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        root = context.bakeLayer(NarakaModelLayers.FORGING_BLOCK);
    }

    @Override
    public void render(SoulSmithingBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();
        Direction direction = blockEntity.getBlockState().getValue(SoulSmithingBlock.FACING);
        Quaternionf quaternionf = new Quaternionf()
                .rotationY((float) -Math.toRadians(direction.toYRot() + 180));
        poseStack.rotateAround(quaternionf, 0.5f, 0.5f, 0.5f);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.FORGING_BLOCK));
        root.render(poseStack, vertexConsumer, light, overlay);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.rotateAround(quaternionf, 0.5f, 0.5f, 0.5f);
        poseStack.translate(0.5, 0.7, 0.5);
        ItemStack itemStack = blockEntity.getForgingItem();
        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, light, overlay, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        poseStack.popPose();
    }
}
