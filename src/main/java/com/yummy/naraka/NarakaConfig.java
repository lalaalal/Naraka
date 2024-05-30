package com.yummy.naraka;

import com.yummy.naraka.attachment.DeathCountHelper;
import com.yummy.naraka.attachment.StigmaHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Mod configuration<br>
 */
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NarakaConfig {
    private static final Pair<NarakaConfig, ModConfigSpec> PAIR = new ModConfigSpec.Builder()
            .configure(NarakaConfig::new);

    public static final ModConfigSpec SPEC = PAIR.getRight();
    public final ConfigValue<Integer> MAX_DEATH_COUNT;
    public final ConfigValue<Integer> MAX_STIGMA;
    public final ConfigValue<Integer> PAUSE_DURATION_BY_STIGMA;
    public final ConfigValue<Integer> KEEP_STIGMA_DURATION;
    private NarakaConfig(ModConfigSpec.Builder builder) {
        MAX_DEATH_COUNT = builder.defineInRange("death_count.max", 5, 1, Integer.MAX_VALUE);

        MAX_STIGMA = builder.defineInRange("stigma.max", 3, 1, Integer.MAX_VALUE);
        PAUSE_DURATION_BY_STIGMA = builder
                .comment("Ticks to pause entity")
                .defineInRange("stigma.pause_duration", 20 * 5, 1, Integer.MAX_VALUE);
        KEEP_STIGMA_DURATION = builder
                .comment("Ticks to keep stigma. Stigma will be decreased after configured tick")
                .defineInRange("stigma.keep_duration", 20 * 60, 1, Integer.MAX_VALUE);
    }

    static NarakaConfig getInstance() {
        return PAIR.getLeft();
    }

    @SubscribeEvent
    public static void loadConfig(ModConfigEvent.Loading event) {
        DeathCountHelper.loadConfig();
        StigmaHelper.loadConfig();
    }

    @SubscribeEvent
    public static void reloadConfig(ModConfigEvent.Reloading event) {
        DeathCountHelper.loadConfig();
        StigmaHelper.loadConfig();
    }
}