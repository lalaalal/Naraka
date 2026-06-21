package com.yummy.naraka.client.renderer;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @see com.yummy.naraka.mixin.client.ItemModelResolverMixin
 */
public class ItemRenderRegistry {
    private static final Map<Item, RenderType> ITEM_RENDER_TYPES = new HashMap<>();
    private static final Set<Item> ANIMATED_ITEMS = new HashSet<>();

    public static void registerRenderType(Supplier<Item> item, RenderType renderType) {
        ITEM_RENDER_TYPES.put(item.get(), renderType);
        registerAnimatedItem(item);
    }

    public static void registerAnimatedItem(Supplier<Item> item) {
        ANIMATED_ITEMS.add(item.get());
    }

    public static boolean shouldBeAnimated(Item item) {
        return ANIMATED_ITEMS.contains(item);
    }

    public static boolean hasRenderTypeOverride(ItemStack itemStack) {
        return ITEM_RENDER_TYPES.containsKey(itemStack.getItem());
    }

    public static RenderType getRenderType(ItemStack itemStack) {
        return ITEM_RENDER_TYPES.get(itemStack.getItem());
    }
}
