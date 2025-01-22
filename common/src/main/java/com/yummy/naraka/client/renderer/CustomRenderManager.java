package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.util.Color;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class CustomRenderManager {
    private static final Map<Item, CustomItemRenderer> CUSTOM_RENDERERS = new HashMap<>();
    private static final Map<Block, RenderType> CUSTOM_BLOCK_RENDER_TYPES = new HashMap<>();
    private static final Map<Item, Color> COLORED_ITEMS = new HashMap<>();
    private static final Set<Item> RAINBOW_ITEMS = new HashSet<>();

    public static void register(ItemLike item, CustomItemRenderer customItemRenderer) {
        CUSTOM_RENDERERS.put(item.asItem(), customItemRenderer);
    }

    public static void register(RenderType renderType, Block... blocks) {
        for (Block block : blocks)
            CUSTOM_BLOCK_RENDER_TYPES.put(block, renderType);
    }

    public static void renderRainbow(Item item) {
        RAINBOW_ITEMS.add(item);
    }

    public static void renderColored(Item item, Color color) {
        COLORED_ITEMS.put(item, color);
    }

    public static void restoreColor(Item item) {
        COLORED_ITEMS.remove(item);
    }

    public static boolean shouldRenderRainbow(ItemStack itemStack) {
        return RAINBOW_ITEMS.contains(itemStack.getItem());
    }

    public static boolean shouldRenderColored(ItemStack itemStack) {
        return COLORED_ITEMS.containsKey(itemStack.getItem());
    }

    public static boolean hasCustomRenderer(ItemStack stack) {
        return CUSTOM_RENDERERS.containsKey(stack.getItem());
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

    public static Color getItemColor(ItemStack stack) {
        return COLORED_ITEMS.get(stack.getItem());
    }

    public interface CustomItemRenderer {
        boolean shouldRenderCustom(ItemStack stack, ItemDisplayContext context);

        void render(ItemStack stack, ItemDisplayContext context, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay);
    }
}
