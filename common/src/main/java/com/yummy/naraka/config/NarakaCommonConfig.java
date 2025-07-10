package com.yummy.naraka.config;

public class NarakaCommonConfig extends StaticConfiguration {
    public final ConfigValue<Boolean> showTestCreativeModeTab;
    public final ConfigValue<Integer> herobrineTakingStigmaTick;
    public final ConfigValue<Float> herobrineHurtLimitReduce;
    public final ConfigValue<Integer> maxShadowHerobrineSpawn;
    public final ConfigValue<Float> fasterLiquidSwimmingSpeed;
    public final ConfigValue<Integer> narakaFireballTargetTracingLevel;
    public final ConfigValue<Boolean> disableHerobrineDestroyingStructure;
    public final ConfigValue<Boolean> despawnHerobrineWhenTargetIsDead;

    public final ConfigValue<Boolean> breakComboWhenSkillDisabled;
    public final ConfigValue<Boolean> alwaysCombo;
    public final ConfigValue<Boolean> disableStigma;

    public NarakaCommonConfig() {
        super("naraka-common", PropertiesConfigFile::new);

        this.showTestCreativeModeTab = define("show_test_creative_mode_tab", false)
                .comment("Need to restart game");
        this.herobrineTakingStigmaTick = define("herobrine_taking_stigma_tick", 1200);
        this.herobrineHurtLimitReduce = define("herobrine_hurt_limit_reduce", 2.0f);
        this.maxShadowHerobrineSpawn = define("max_shadow_herobrine_spawn", 3);
        this.fasterLiquidSwimmingSpeed = define("faster_liquid_swimming_speed", 5f);
        this.narakaFireballTargetTracingLevel = define("naraka_fireball_target_tracing_level", 1)
                .comment("0 : No tracing")
                .comment("1 : Can rotate movement")
                .comment("2 : Can reduce speed (can turn back)");
        this.disableHerobrineDestroyingStructure = define("disable_herobrine_destroying_structure", false);
        this.despawnHerobrineWhenTargetIsDead = define("despawn_herobrine_when_target_is_dead", true);

        this.breakComboWhenSkillDisabled = define("break_combo_when_skill_disabled", false);
        this.alwaysCombo = define("always_combo", false);
        this.disableStigma = define("disable_stigma", false);
    }
}
