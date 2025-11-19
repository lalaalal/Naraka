package com.yummy.naraka.client.renderer;

import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @see com.yummy.naraka.mixin.client.FeatureRenderDispatcherMixin
 * @see com.yummy.naraka.mixin.client.ItemModelResolverMixin
 * @see com.yummy.naraka.mixin.client.ItemStackRenderStateMixin
 * @see com.yummy.naraka.mixin.client.LayerRenderStateMixin
 * @see com.yummy.naraka.mixin.client.SubmitNodeCollectionMixin
 */
@Environment(EnvType.CLIENT)
public class ItemRenderRegistry {
    private static final Map<Item, Supplier<Color>> ITEM_COLORS = new HashMap<>();
    private static final Map<ItemStackRenderState, Integer> TEMPORARY_COLORS = new HashMap<>();
    private static final Map<Item, Consumer<ItemRenderTypeSetter>> ITEM_RENDER_TYPES = new HashMap<>();

    public static void registerColor(Supplier<Item> item, Supplier<Color> color) {
        ITEM_COLORS.put(item.get(), color);
    }

    public static void registerRenderType(Supplier<Item> item, Consumer<ItemRenderTypeSetter> renderTypeSetter) {
        ITEM_RENDER_TYPES.put(item.get(), renderTypeSetter);
    }

    public static boolean hasColorOverride(ItemStack itemStack) {
        return ITEM_COLORS.containsKey(itemStack.getItem());
    }

    public static boolean hasRenderTypeOverride(ItemStack itemStack) {
        return ITEM_RENDER_TYPES.containsKey(itemStack.getItem());
    }

    public static void setTemporaryColor(ItemStackRenderState renderState, int color) {
        TEMPORARY_COLORS.put(renderState, color);
    }

    public static int getTemporaryColor(ItemStackRenderState renderState) {
        return TEMPORARY_COLORS.get(renderState);
    }

    public static boolean hasTemporaryColor(ItemStackRenderState renderState) {
        return TEMPORARY_COLORS.containsKey(renderState);
    }

    public static int getColor(ItemStack itemStack) {
        return ITEM_COLORS.get(itemStack.getItem()).get()
                .withAlpha(0xff).pack();
    }

    public static void setRenderType(ItemStack itemStack, ItemRenderTypeSetter itemRenderTypeSetter) {
        ITEM_RENDER_TYPES.get(itemStack.getItem())
                .accept(itemRenderTypeSetter);
    }

    public static void clearTemporary() {
        TEMPORARY_COLORS.clear();
    }
}
