package com.yummy.naraka;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Mod configuration
 */
public class NarakaConfig {
    private static final Pair<NarakaConfig, ModConfigSpec> PAIR = new ModConfigSpec.Builder()
            .configure(NarakaConfig::new);

    public static final ModConfigSpec SPEC = PAIR.getRight();
    public final ConfigValue<Integer> maxDeathCount;
    public final ConfigValue<Integer> maxStigma;
    public final ConfigValue<Integer> pauseDurationByStigma;
    public final ConfigValue<Integer> keepStigmaDuration;

    public final ConfigValue<Double> nectariumComposeChance;

    public final ConfigValue<Integer> soulCraftingRequiredFuel;
    public final ConfigValue<Integer> soulCraftingTime;

    private NarakaConfig(ModConfigSpec.Builder builder) {
        maxDeathCount = builder.defineInRange("death_count.max", 5, 1, Integer.MAX_VALUE);
        maxStigma = builder.defineInRange("stigma.max", 3, 1, Integer.MAX_VALUE);
        pauseDurationByStigma = builder
                .comment("Ticks to pause entity")
                .defineInRange("stigma.pause_duration", 20 * 5, 1, Integer.MAX_VALUE);
        keepStigmaDuration = builder
                .comment("Ticks to keep stigma. Stigma will be decreased after configured tick")
                .defineInRange("stigma.keep_duration", 20 * 60, 1, Integer.MAX_VALUE);

        nectariumComposeChance = builder
                .comment("Nectarium composing chance for honey block")
                .defineInRange("nectarium_compose_chance", 0.5, 0.01, 1);

        soulCraftingRequiredFuel = builder
                .comment("Required fuel for soul crafting")
                .defineInRange("soul_crafting.required_fuel", 10, 1, Integer.MAX_VALUE);
        soulCraftingTime = builder
                .comment("Soul crafting time")
                .defineInRange("soul_crafting.crafting_time", 1200, 1, Integer.MAX_VALUE);
    }

    static NarakaConfig getInstance() {
        return PAIR.getLeft();
    }
}