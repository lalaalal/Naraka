package com.yummy.naraka.world.item.armortrim;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;

public class NarakaTrimPatterns {
    public static final ResourceKey<TrimPattern> PURIFIED_SOUL_SILENCE = create("purified_soul_silence");

    public static void bootstrap(BootstrapContext<TrimPattern> context) {
        TrimPatterns.register(context, NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, PURIFIED_SOUL_SILENCE);
    }

    private static ResourceKey<TrimPattern> create(String name) {
        return ResourceKey.create(Registries.TRIM_PATTERN, NarakaMod.location(name));
    }
}
