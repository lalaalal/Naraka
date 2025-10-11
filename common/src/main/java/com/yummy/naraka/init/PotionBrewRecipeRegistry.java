package com.yummy.naraka.init;


import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.function.Consumer;

public abstract class PotionBrewRecipeRegistry {
    public static void register(Consumer<PotionBrewing.Builder> consumer) {
        MethodInvoker.invoke(PotionBrewRecipeRegistry.class, "register", consumer);
    }
}
