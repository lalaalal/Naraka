package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.blockentity.SoulStabilizerBlockEntityRenderer;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SoulStabilizerSpecialRenderer implements NoDataSpecialModelRenderer {
    private final SoulStabilizerBlockEntity blockEntity = new SoulStabilizerBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_STABILIZER.get().defaultBlockState());
    private final ModelPart bottle;
    private final ModelPart liquid;

    public SoulStabilizerSpecialRenderer(ModelPart bottle, ModelPart liquid) {
        this.bottle = bottle;
        this.liquid = liquid;
    }

    @Override
    public Void extractArgument(ItemStack stack) {
        NarakaItemUtils.loadBlockEntity(stack, blockEntity, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
        return null;
    }

    @Override
    public void render(ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean hasFoilType) {
        poseStack.pushPose();
        poseStack.scale(2.8f, 2.8f, 2.8f);
        poseStack.translate(-0.32, 0, -0.32);
        VertexConsumer bottleBuffer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.SOUL_STABILIZER));
        bottle.render(poseStack, bottleBuffer, packedLight, packedOverlay);
        poseStack.popPose();

        if (blockEntity.getSoulType() == SoulType.NONE)
            return;

        float soulRatio = (float) blockEntity.getSouls() / SoulStabilizerBlockEntity.CAPACITY;
        int color = ARGB.color(0x99, blockEntity.getSoulType().getColor());

        poseStack.pushPose();
        poseStack.scale(2.8f, soulRatio * 2.8f, 2.8f);
        poseStack.translate(-0.32, 0, -0.32);
        VertexConsumer liquidBuffer = bufferSource.getBuffer(RenderType.entityTranslucent(SoulStabilizerBlockEntityRenderer.WATER_OVERLAY));
        liquid.render(poseStack, liquidBuffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<SoulStabilizerSpecialRenderer.Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            ModelPart root = modelSet.bakeLayer(NarakaModelLayers.SOUL_STABILIZER);
            return new SoulStabilizerSpecialRenderer(root.getChild("bottle"), root.getChild("liquid"));
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
