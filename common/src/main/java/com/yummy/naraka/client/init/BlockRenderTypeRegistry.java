package com.yummy.naraka.client.init;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class BlockRenderTypeRegistry {
    private static final Map<Block, RenderType> CUSTOM_BLOCK_RENDER_TYPES = new HashMap<>();

    public static void register(RenderType renderType, Block... blocks) {
        for (Block block : blocks)
            CUSTOM_BLOCK_RENDER_TYPES.put(block, renderType);
    }

    public static Map<Block, RenderType> getCustomBlockRenderTypes() {
        return Map.copyOf(CUSTOM_BLOCK_RENDER_TYPES);
    }

    @Deprecated
    public interface CustomItemRenderer {
        default boolean applyTransform() {
            return true;
        }

        boolean shouldRenderCustom(ItemStack stack, ItemDisplayContext context);

        void render(ItemStack stack, ItemDisplayContext context, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay);
    }
}
