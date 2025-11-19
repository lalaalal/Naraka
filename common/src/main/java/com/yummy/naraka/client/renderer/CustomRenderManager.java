package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.util.ComponentStyles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Environment(EnvType.CLIENT)
public class CustomRenderManager {
    private static final Map<Item, CustomItemRenderer> CUSTOM_RENDERERS = new HashMap<>();
    private static final Map<Item, Supplier<Color>> COLORED_ITEMS = new HashMap<>();
    private static final Map<Item, UnaryOperator<RenderType>> CUSTOM_RENDER_TYPES = new HashMap<>();

    public static void register(ItemLike item, CustomItemRenderer customItemRenderer) {
        CUSTOM_RENDERERS.put(item.asItem(), customItemRenderer);
    }

    public static void registerCustomRenderType(ItemLike item, UnaryOperator<RenderType> renderType) {
        CUSTOM_RENDER_TYPES.put(item.asItem(), renderType);
    }

    public static void registerRainbow(Item item) {
        COLORED_ITEMS.put(item, () -> ComponentStyles.RAINBOW_COLOR.getCurrentColor().withAlpha(0xff));
    }

    public static void renderColored(Item item, Supplier<Color> color) {
        COLORED_ITEMS.put(item, color);
    }

    public static void restoreColor(Item item) {
        COLORED_ITEMS.remove(item);
    }

    public static boolean shouldRenderColored(ItemStack itemStack) {
        return COLORED_ITEMS.containsKey(itemStack.getItem());
    }

    public static boolean hasCustomRenderer(ItemStack stack) {
        return CUSTOM_RENDERERS.containsKey(stack.getItem());
    }

    public static boolean hasCustomRenderType(ItemStack stack) {
        return CUSTOM_RENDER_TYPES.containsKey(stack.getItem());
    }

    public static CustomItemRenderer getCustomRenderer(ItemStack stack) {
        CustomItemRenderer itemRenderer = CUSTOM_RENDERERS.get(stack.getItem());
        if (itemRenderer == null)
            throw new IllegalArgumentException("Item " + stack.getItem() + " is not registered");
        return itemRenderer;
    }

    public static Color getItemColor(ItemStack stack) {
        return COLORED_ITEMS.get(stack.getItem()).get();
    }

    public static RenderType getCustomRenderType(ItemStack stack, RenderType defaultType) {
        return CUSTOM_RENDER_TYPES.get(stack.getItem())
                .apply(defaultType);
    }

    public interface CustomItemRenderer {
        default boolean applyTransform() {
            return true;
        }

        boolean shouldRenderCustom(ItemStack stack, ItemDisplayContext context);

        void render(ItemStack stack, ItemDisplayContext context, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay);
    }
}
