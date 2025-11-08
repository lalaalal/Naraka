package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class NarakaDimensionTypes {
    public static final ResourceKey<DimensionType> NARAKA = create("naraka");
    public static final ResourceLocation NARAKA_EFFECT = NARAKA.location();

    public static void bootstrap(BootstrapContext<DimensionType> context) {
        context.register(NARAKA, new DimensionType(
                OptionalLong.of(18000),
                true,
                false,
                false,
                false,
                1.0,
                false,
                false,
                0,
                256,
                128,
                BlockTags.INFINIBURN_OVERWORLD,
                NARAKA_EFFECT,
                1,
                new DimensionType.MonsterSettings(false, false, ConstantInt.ZERO, 0)
        ));
    }

    private static ResourceKey<DimensionType> create(String name) {
        return ResourceKey.create(Registries.DIMENSION_TYPE, NarakaMod.location(name));
    }
}
