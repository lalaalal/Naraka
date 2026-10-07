package com.yummy.naraka.advancements;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.advancements.AdvancementProvider;

import java.util.List;

public class NarakaAdvancementProvider {
    public static SingleRegistryBootstrap<Advancement> create() {
        return new AdvancementProvider(List.of(
                NarakaAdvancements::new
        ));
    }
}
