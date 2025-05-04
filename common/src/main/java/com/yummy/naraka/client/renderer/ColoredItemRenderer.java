package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.mixin.client.ItemModelResolverMixin;
import com.yummy.naraka.mixin.client.ItemRendererMixin;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @see ItemRendererMixin
 * @see ItemModelResolverMixin#storeItemRenderContext(ItemStackRenderState, ItemStack, ItemDisplayContext, boolean, Level, LivingEntity, int, CallbackInfo)
 */
@Environment(EnvType.CLIENT)
public class ColoredItemRenderer {
    private static final Map<Item, Supplier<Color>> ITEM_COLORS = new HashMap<>();
    private static ItemStack currentRenderingItem = ItemStack.EMPTY;

    public static void register(Supplier<Item> item, Supplier<Color> color) {
        ITEM_COLORS.put(item.get(), color);
    }

    public static void setCurrentRenderingItem(ItemStack itemStack) {
        currentRenderingItem = itemStack;
    }

    public static boolean shouldRenderAsColored() {
        return ITEM_COLORS.containsKey(currentRenderingItem.getItem());
    }

    public static void renderColored(BakedModel model, int packedLight, int packedOverlay, PoseStack poseStack, VertexConsumer vertexConsumer) {
        Color color = ITEM_COLORS.get(currentRenderingItem.getItem())
                .get().withAlpha(0xff);
        renderModelLists(model, packedLight, packedOverlay, poseStack, vertexConsumer, color);
    }

    private static void renderModelLists(BakedModel model, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer vertexConsumer, Color color) {
        RandomSource randomSource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            renderColoredQuadList(poseStack, vertexConsumer, model.getQuads(null, direction, randomSource), combinedLight, combinedOverlay, color);
        }

        randomSource.setSeed(42L);
        renderColoredQuadList(poseStack, vertexConsumer, model.getQuads(null, null, randomSource), combinedLight, combinedOverlay, color);
    }

    private static void renderColoredQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, int combinedLight, int combinedOverlay, Color color) {
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad bakedQuad : quads)
            buffer.putBulkData(pose, bakedQuad, color.red01(), color.green01(), color.blue01(), color.alpha01(), combinedLight, combinedOverlay);
    }
}
