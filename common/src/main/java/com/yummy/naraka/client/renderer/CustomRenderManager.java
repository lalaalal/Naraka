package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class CustomRenderManager {
    private static final Map<Item, CustomItemRenderer> CUSTOM_RENDERERS = new HashMap<>();
    private static final Map<Block, RenderType> CUSTOM_BLOCK_RENDER_TYPES = new HashMap<>();

    public static void register(ItemLike item, CustomItemRenderer customItemRenderer) {
        CUSTOM_RENDERERS.put(item.asItem(), customItemRenderer);
    }

    public static void register(RenderType renderType, Block... blocks) {
        for (Block block : blocks)
            CUSTOM_BLOCK_RENDER_TYPES.put(block, renderType);
    }

    public static boolean hasCustomRenderer(ItemStack stack) {
        return CUSTOM_RENDERERS.containsKey(stack.getItem());
    }

    @Deprecated
    public static boolean hasCustomRenderType(Block block) {
        return CUSTOM_BLOCK_RENDER_TYPES.containsKey(block);
    }

    public static CustomItemRenderer getCustomRenderer(ItemStack stack) {
        CustomItemRenderer itemRenderer = CUSTOM_RENDERERS.get(stack.getItem());
        if (itemRenderer == null)
            throw new IllegalArgumentException("Item " + stack.getItem() + " is not registered");
        return itemRenderer;
    }

    public static Map<Block, RenderType> getCustomBlockRenderTypes() {
        return Map.copyOf(CUSTOM_BLOCK_RENDER_TYPES);
    }

    @Deprecated
    public static RenderType getCustomRenderType(Block block) {
        RenderType renderType = CUSTOM_BLOCK_RENDER_TYPES.get(block);
        if (renderType == null)
            throw new IllegalArgumentException("Block " + block + " is not registered");
        return renderType;
    }

    public interface CustomItemRenderer {
        boolean shouldRenderCustom(ItemStack stack, ItemDisplayContext context);

        void render(ItemStack stack, ItemDisplayContext context, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay);
    }
}
