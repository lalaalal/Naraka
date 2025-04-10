package com.yummy.naraka.config;

import com.yummy.naraka.util.Color;

public class NarakaClientConfig extends Configuration {
    public final ConfigValue<Boolean> showReinforcementValue;
    public final ConfigValue<Boolean> disableNonShaderLonginusRendering;
    public final ConfigValue<Color> afterimageColor;
    public final ConfigValue<Color> shadowHerobrineColor;
    public final ConfigValue<Boolean> alwaysDisplayHerobrineScarf;
    public final ConfigValue<Float> herobrineScarfDefaultRotation;
    public final ConfigValue<Integer> herobrineScarfPartitionNumber;
    public final ConfigValue<Float> herobrineScarfWaveSpeed;
    public final ConfigValue<Float> herobrineScarfWaveMaxAngle;
    public final ConfigValue<Float> herobrineScarfWaveCycleModifier;

    public NarakaClientConfig() {
        super("naraka-client", PropertiesConfigFile::new);

        this.showReinforcementValue = define("show_reinforcement_value", false);
        this.disableNonShaderLonginusRendering = define("disable_non_shader_longinus_rendering", false);
        this.afterimageColor = define("afterimage_color", Color.of(0x7e00ff));
        this.shadowHerobrineColor = define("shadow_herobrine_color", Color.of(0x0000ff));

        this.alwaysDisplayHerobrineScarf = define("always_display_herobrine_scarf", false);
        this.herobrineScarfDefaultRotation = define("herobrine_scarf_default_rotation", 70.0f);

        this.herobrineScarfPartitionNumber = define("herobrine_scarf_partition_number", 16)
                .append("Divide scarf with given number")
                .append("Bigger value, short wave cycle")
                .append("!! Editing while playing game may cause crash");
        this.herobrineScarfWaveSpeed = define("herobrine_scarf_wave_speed", 0.2f);
        this.herobrineScarfWaveMaxAngle = define("herobrine_scarf_wave_max_angle", 22.5f);
        this.herobrineScarfWaveCycleModifier = define("herobrine_scarf_wave_cycle_modifier", 1.0f)
                .append("Bigger value, short wave cycle");
    }
}
