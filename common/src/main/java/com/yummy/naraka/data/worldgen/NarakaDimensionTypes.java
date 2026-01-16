package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TimelineTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.timeline.Timeline;

public class NarakaDimensionTypes {
    public static final ResourceKey<DimensionType> NARAKA = create("naraka");

    public static void bootstrap(BootstrapContext<DimensionType> context) {
        HolderGetter<Timeline> timelines = context.lookup(Registries.TIMELINE);
        context.register(NARAKA, new DimensionType(
                true,
                true,
                false,
                1.0,
                0,
                256,
                128,
                BlockTags.INFINIBURN_OVERWORLD,
                1,
                new DimensionType.MonsterSettings(ConstantInt.ZERO, 0),
                DimensionType.Skybox.NONE,
                DimensionType.CardinalLightType.DEFAULT,
                EnvironmentAttributeMap.builder()
                        .set(EnvironmentAttributes.CLOUD_HEIGHT, 56f)
                        .set(EnvironmentAttributes.MOON_ANGLE, 0f)
                        .set(EnvironmentAttributes.SUN_ANGLE, 180f)
                        .set(EnvironmentAttributes.BED_RULE, BedRule.EXPLODES)
                        .set(EnvironmentAttributes.CAN_START_RAID, false)
                        .set(EnvironmentAttributes.CAN_PILLAGER_PATROL_SPAWN, false)
                        .set(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, false)
                        .set(EnvironmentAttributes.NETHER_PORTAL_SPAWNS_PIGLINS, false)
                        .set(EnvironmentAttributes.PIGLINS_ZOMBIFY, false)
                        .build(),
                timelines.getOrThrow(TimelineTags.UNIVERSAL)
        ));
    }

    private static ResourceKey<DimensionType> create(String name) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, NarakaMod.identifier(name));
    }
}
