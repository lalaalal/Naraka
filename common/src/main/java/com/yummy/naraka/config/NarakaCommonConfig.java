package com.yummy.naraka.config;

public class NarakaCommonConfig extends StaticConfiguration {
    public final ConfigValue<Boolean> showTestCreativeModeTab = define("show_test_creative_mode_tab", false)
            .comment("Need to restart game");
    public final ConfigValue<Integer> herobrineTakingStigmaTick = define("herobrine_taking_stigma_tick", 1200);
    public final ConfigValue<Float> herobrineHurtLimitReduce = define("herobrine_hurt_limit_reduce", 2.0f);
    public final ConfigValue<Integer> maxShadowHerobrineSpawn = define("max_shadow_herobrine_spawn", 3);
    public final ConfigValue<Float> fasterLiquidSwimmingSpeed = define("faster_liquid_swimming_speed", 5f);
    public final ConfigValue<Integer> narakaFireballTargetTracingLevel = define("naraka_fireball_target_tracing_level", 1)
            .comment("0 : No tracing")
            .comment("1 : Can rotate movement")
            .comment("2 : Always trace")
            .comment("3 : Can reduce speed (can turn back)");
    public final ConfigValue<Boolean> disableHerobrineDestroyingStructure = define("disable_herobrine_destroying_structure", false);
    public final ConfigValue<Boolean> despawnHerobrineWhenTargetIsDead = define("despawn_herobrine_when_target_is_dead", true);

    public final ConfigValue<Boolean> breakComboWhenSkillDisabled = define("break_combo_when_skill_disabled", false);
    public final ConfigValue<Boolean> alwaysCombo = define("always_combo", false);
    public final ConfigValue<Boolean> disableStigma = define("disable_stigma", false);

    public NarakaCommonConfig() {
        super("naraka-common", PropertiesConfigFile::new);
    }
}
