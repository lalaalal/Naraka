package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.PotionBrewRecipeRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.function.Consumer;

public final class FabricPotionBrewRecipeRegistry {
    @MethodProxy(PotionBrewRecipeRegistry.class)
    public static void register(Consumer<PotionBrewing.Builder> consumer) {
        FabricPotionBrewingBuilder.BUILD.register(consumer::accept);
    }
}
