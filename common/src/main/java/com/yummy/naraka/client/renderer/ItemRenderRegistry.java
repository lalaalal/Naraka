package com.yummy.naraka.client.renderer;

import com.yummy.naraka.client.NarakaTextures;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @see com.yummy.naraka.mixin.client.ItemModelResolverMixin
 */
public class ItemRenderRegistry {
    private static final Map<Item, ConditionalRenderType> ITEM_RENDER_TYPES = new HashMap<>();
    private static final Set<Item> ANIMATED_ITEMS = new HashSet<>();

    public static void registerRenderType(Supplier<? extends Item> item, RenderType renderType, Predicate<ItemStack> predicate) {
        ITEM_RENDER_TYPES.put(item.get(), new ConditionalRenderType(renderType, predicate));
    }

    public static void registerRenderType(Supplier<? extends Item> item, RenderType renderType) {
        ITEM_RENDER_TYPES.put(item.get(), new ConditionalRenderType(renderType, _ -> true));
    }

    public static void registerAnimatedItem(Supplier<? extends Item> item) {
        ANIMATED_ITEMS.add(item.get());
    }

    public static boolean shouldBeAnimated(Item item) {
        return ANIMATED_ITEMS.contains(item);
    }

    public static boolean hasRenderTypeOverride(ItemStack itemStack) {
        return ITEM_RENDER_TYPES.getOrDefault(itemStack.getItem(), ConditionalRenderType.DEFAULT)
                .canApply(itemStack);
    }

    public static RenderType getRenderType(ItemStack itemStack) {
        return ITEM_RENDER_TYPES.getOrDefault(itemStack.getItem(), ConditionalRenderType.DEFAULT)
                .renderType();
    }

    private record ConditionalRenderType(RenderType renderType, Predicate<ItemStack> predicate) {
        public static final ConditionalRenderType DEFAULT = new ConditionalRenderType(RenderTypes.itemCutout(NarakaTextures.LOCATION_ITEMS), _ -> false);

        public boolean canApply(ItemStack itemStack) {
            return predicate.test(itemStack);
        }
    }
}
