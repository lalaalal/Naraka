package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.PotionBrewRecipeRegistry;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.alchemy.PotionBrewing;

import java.util.function.Consumer;

public final class FabricPotionBrewRecipeRegistry implements PotionBrewRecipeRegistry.Registrar {
    @Override
    public void register(Consumer<PotionBrewing.Builder> consumer) {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(consumer::accept);
    }
}
