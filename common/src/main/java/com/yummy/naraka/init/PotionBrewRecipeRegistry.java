package com.yummy.naraka.init;


import com.yummy.naraka.service.NarakaServices;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.function.Consumer;

public abstract class PotionBrewRecipeRegistry {
    public static void register(Consumer<PotionBrewing.Builder> consumer) {
        NarakaServices.POTION_BREWING_REGISTRY.register(consumer);
    }

    public interface Registrar {
        void register(Consumer<PotionBrewing.Builder> consumer);
    }
}
