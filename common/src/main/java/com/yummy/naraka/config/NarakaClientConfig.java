package com.yummy.naraka.config;

import com.yummy.naraka.util.Color;

public class NarakaClientConfig extends StaticConfiguration {
    public final ConfigValue<Boolean> showReinforcementValue;
    public final ConfigValue<Color> afterimageColor;
    public final ConfigValue<Color> shadowHerobrineColor;
    public final ConfigValue<Boolean> alwaysDisplayHerobrineScarf;
    public final ConfigValue<Float> herobrineScarfDefaultRotation;
    public final ConfigValue<Integer> herobrineScarfPartitionNumber;
    public final ConfigValue<Float> herobrineScarfWaveSpeed;
    public final ConfigValue<Float> herobrineScarfWaveMaxAngle;
    public final ConfigValue<Float> herobrineScarfWaveCycleModifier;
    public final ConfigValue<Integer> oreSeeThroughRange;
    public final ConfigValue<Boolean> disableOreSeeThrough;
    public final ConfigValue<Integer> herobrineSkyCloudSpeed;

    public NarakaClientConfig() {
        super("naraka-client", PropertiesConfigFile::new);

        this.showReinforcementValue = define("show_reinforcement_value", false);
        this.afterimageColor = define("afterimage_color", Color.of(0x000000));
        this.shadowHerobrineColor = define("shadow_herobrine_color", Color.of(0x000000));

        this.alwaysDisplayHerobrineScarf = define("always_display_herobrine_scarf", false);
        this.herobrineScarfDefaultRotation = define("herobrine_scarf_default_rotation", 70.0f);
        this.herobrineScarfPartitionNumber = define("herobrine_scarf_partition_number", 32)
                .comment("Divide scarf with given number")
                .comment("Bigger value, short wave cycle")
                .comment("!! Editing while playing game may cause crash");
        this.herobrineScarfWaveSpeed = define("herobrine_scarf_wave_speed", 0.2f);
        this.herobrineScarfWaveMaxAngle = define("herobrine_scarf_wave_max_angle", 22.5f);
        this.herobrineScarfWaveCycleModifier = define("herobrine_scarf_wave_cycle_modifier", 1.0f)
                .comment("Bigger value, short wave cycle");

        this.oreSeeThroughRange = define("ore_see_through_range", 20);
        this.disableOreSeeThrough = define("disable_ore_see_through", false);
        this.herobrineSkyCloudSpeed = define("herobrine_sky_cloud_speed", 500);
    }
}
