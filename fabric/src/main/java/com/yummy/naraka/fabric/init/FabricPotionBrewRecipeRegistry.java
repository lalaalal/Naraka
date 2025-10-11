package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.PotionBrewRecipeRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.function.Consumer;

public final class FabricPotionBrewRecipeRegistry {
    @MethodProxy(PotionBrewRecipeRegistry.class)
    public static void register(Consumer<PotionBrewing.Builder> consumer) {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(consumer::accept);
    }
}
