package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class SoulStabilizerSpecialRenderer implements SpecialModelRenderer<SoulStabilizerSpecialRenderer.SoulContainer> {
    private final SoulStabilizerBlockEntity blockEntity = new SoulStabilizerBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_STABILIZER.get().defaultBlockState());
    private final ModelPart bottle;
    private final ModelPart liquid;

    public SoulStabilizerSpecialRenderer(ModelPart bottle, ModelPart liquid) {
        this.bottle = bottle;
        this.liquid = liquid;
    }

    @Override
    public SoulContainer extractArgument(ItemStack stack) {
        blockEntity.reset();
        NarakaItemUtils.loadBlockEntity(stack, blockEntity, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
        return SoulContainer.from(blockEntity);
    }

    @Override
    public void submit(@Nullable SoulContainer soulContainer, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean bl, int color) {
        poseStack.pushPose();
        poseStack.scale(2.8f, 2.8f, 2.8f);
        poseStack.translate(-0.32, 0, -0.32);
        RenderType bottleRenderType = RenderType.entityCutout(NarakaTextures.SOUL_STABILIZER);
        submitNodeCollector.submitModelPart(bottle, poseStack, bottleRenderType, light, overlay, null, -1, null);
        poseStack.popPose();

        if (soulContainer == null || soulContainer.type == SoulType.NONE)
            return;

        float soulRatio = (float) soulContainer.amount / SoulStabilizerBlockEntity.getCapacity();
        int liquidColor = ARGB.color(0x99, soulContainer.type.getColor());

        poseStack.pushPose();
        poseStack.scale(2.8f, soulRatio * 2.8f, 2.8f);
        poseStack.translate(-0.32, 0, -0.32);
        RenderType liquidRenderType = RenderType.entityTranslucent(SoulStabilizerBlockEntityRenderer.WATER_OVERLAY);
        submitNodeCollector.submitModelPart(liquid, poseStack, liquidRenderType, light, overlay, null, liquidColor, null);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> output) {
        PoseStack poseStack = new PoseStack();
        bottle.getExtentsForGui(poseStack, output);
        liquid.getExtentsForGui(poseStack, output);
    }

    @Environment(EnvType.CLIENT)
    public record SoulContainer(SoulType type, int amount) {
        public static final SoulContainer EMPTY = new SoulContainer(SoulType.NONE, 0);

        public static SoulContainer from(SoulStabilizerBlockEntity blockEntity) {
            return new SoulContainer(blockEntity.getSoulType(), blockEntity.getSouls());
        }
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<SoulStabilizerSpecialRenderer.Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(BakingContext context) {
            ModelPart root = context.entityModelSet().bakeLayer(NarakaModelLayers.SOUL_STABILIZER);
            return new SoulStabilizerSpecialRenderer(root.getChild("bottle"), root.getChild("liquid"));
        }

        @Override
        public MapCodec<Unbaked> type() {
            return CODEC;
        }
    }
}
