package com.yummy.naraka.config;

import com.yummy.naraka.util.Color;

public class NarakaClientConfig extends StaticConfiguration {
    public final ConfigValue<Boolean> showReinforcementValue = define("show_reinforcement_value", false);
    public final ConfigValue<Color> afterimageColor = define("afterimage_color", Color.of(0x000000));
    public final ConfigValue<Color> shadowHerobrineColor = define("shadow_herobrine_color", Color.of(0x000000));
    public final ConfigValue<Boolean> alwaysDisplayHerobrineScarf = define("always_display_herobrine_scarf", false);
    public final ConfigValue<Float> herobrineScarfDefaultRotation = define("herobrine_scarf_default_rotation", 70.0f);
    public final ConfigValue<Integer> herobrineScarfPartitionNumber = define("herobrine_scarf_partition_number", 32)
            .comment("Divide scarf with given number")
            .comment("Bigger value, short wave cycle")
            .comment("!! Editing while playing game may cause crash");

    public final ConfigValue<Integer> oreSeeThroughRange = define("ore_see_through_range", 20);
    public final ConfigValue<Boolean> disableOreSeeThrough = define("disable_ore_see_through", false);
    public final ConfigValue<Integer> herobrineSkyCloudSpeed = define("herobrine_sky_cloud_speed", 500);

    public final ConfigValue<Float> cameraShakingSpeed = define("camera_shaking_speed", 4f);
    public final ConfigValue<Float> cameraShakingStrength = define("camera_shaking_strength", 0.025f);
    public final ConfigValue<Boolean> disableWorldOpenExperimentalWarning = define("disable_world_open_experimental_warning", false);

    public NarakaClientConfig() {
        super("naraka-client", PropertiesConfigFile::new);
    }
}
