package com.yummy.naraka.mixin.accessor;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreativeModeTabs.class)
public interface CreativeModeTabsAccessor {
    @Accessor("BUILDING_BLOCKS")
    static ResourceKey<CreativeModeTab> buildingBlocks() {
        throw new AssertionError();
    }

    @Accessor("NATURAL_BLOCKS")
    static ResourceKey<CreativeModeTab> naturalBlocks() {
        throw new AssertionError();
    }

    @Accessor("FOOD_AND_DRINKS")
    static ResourceKey<CreativeModeTab> foodAndDrinks() {
        throw new AssertionError();
    }

    @Accessor("INGREDIENTS")
    static ResourceKey<CreativeModeTab> ingredients() {
        throw new AssertionError();
    }

    @Accessor("SPAWN_EGGS")
    static ResourceKey<CreativeModeTab> spawnEggs() {
        throw new AssertionError();
    }
}
