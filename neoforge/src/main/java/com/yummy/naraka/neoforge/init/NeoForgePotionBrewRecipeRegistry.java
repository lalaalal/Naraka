package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.init.PotionBrewRecipeRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

import java.util.function.Consumer;

public final class NeoForgePotionBrewRecipeRegistry implements NarakaEventBus {
    @MethodProxy(PotionBrewRecipeRegistry.class)
    public static void register(Consumer<PotionBrewing.Builder> consumer) {
        NEOFORGE_BUS.addListener(RegisterBrewingRecipesEvent.class, event -> {
            consumer.accept(event.getBuilder());
        });
    }
}
