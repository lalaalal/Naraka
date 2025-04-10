package com.yummy.naraka.config;

public class NarakaCommonConfig extends Configuration {
    public final ConfigValue<Boolean> generatePillarCaves;
    public final ConfigValue<Boolean> showTestCreativeModeTab;
    public final ConfigValue<Integer> herobrineTakingStigmaTick;
    public final ConfigValue<Double> herobrineHurtLimitCalculationRatioModifier;
    public final ConfigValue<Double> herobrineMaxHurtCountCalculationModifier;
    public final ConfigValue<Integer> maxShadowHerobrineSpawn;

    public NarakaCommonConfig() {
        super("naraka-common", PropertiesConfigFile::new);

        this.generatePillarCaves = define("generate_pillar_caves", false);
        this.showTestCreativeModeTab = define("show_test_creative_mode_tab", false);
        this.herobrineTakingStigmaTick = define("herobrine_taking_stigma_tick", 1200);
        this.herobrineHurtLimitCalculationRatioModifier = define("herobrine_hurt_limit_calculation_ratio_modifier", 1.0)
                .append("Bigger value, bigger hurt limit");
        this.herobrineMaxHurtCountCalculationModifier = define("herobrine_max_hurt_count_calculation_modifier", 1.0)
                .append("Bigger value, bigger max hurt count");
        this.maxShadowHerobrineSpawn = define("max_shadow_herobrine_spawn", 3);
    }
}
