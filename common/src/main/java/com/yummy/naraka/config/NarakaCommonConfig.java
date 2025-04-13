package com.yummy.naraka.config;

public class NarakaCommonConfig extends StaticConfiguration {
    public final ConfigValue<Boolean> generatePillarCaves;
    public final ConfigValue<Boolean> showTestCreativeModeTab;
    public final ConfigValue<Integer> herobrineTakingStigmaTick;
    public final ConfigValue<Integer> herobrineStaggeringDuration;
    public final ConfigValue<Double> herobrineHurtLimitCalculationRatioModifier;
    public final ConfigValue<Double> herobrineMaxHurtCountCalculationModifier;
    public final ConfigValue<Integer> maxShadowHerobrineSpawn;
    public final ConfigValue<Float> fasterLiquidSwimmingSpeed;
    public final ConfigValue<Integer> narakaFireballTargetTracingLevel;

    public NarakaCommonConfig() {
        super("naraka-common", PropertiesConfigFile::new);

        this.generatePillarCaves = define("generate_pillar_caves", false);
        this.showTestCreativeModeTab = define("show_test_creative_mode_tab", false)
                .comment("Need to restart game");
        this.herobrineTakingStigmaTick = define("herobrine_taking_stigma_tick", 1200);
        this.herobrineStaggeringDuration = define("herobrine_staggering_duration", 90)
                .comment("In tick");
        this.herobrineHurtLimitCalculationRatioModifier = define("herobrine_hurt_limit_calculation_ratio_modifier", 1.0)
                .comment("Bigger value, bigger hurt limit");
        this.herobrineMaxHurtCountCalculationModifier = define("herobrine_max_hurt_count_calculation_modifier", 1.0)
                .comment("Bigger value, bigger max hurt count");
        this.maxShadowHerobrineSpawn = define("max_shadow_herobrine_spawn", 3);
        this.fasterLiquidSwimmingSpeed = define("faster_liquid_swimming_speed", 5f);
        this.narakaFireballTargetTracingLevel = define("naraka_fireball_target_tracing_level", 1)
                .comment("0 : No tracing")
                .comment("1 : Can rotate movement")
                .comment("2 : Can reduce speed (can turn back)");
    }
}
