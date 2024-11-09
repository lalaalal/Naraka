package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.carver.NarakaWorldCarvers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

public class NarakaConfiguredWorldCarvers {
    public static final ResourceKey<ConfiguredWorldCarver<?>> PILLAR_CAVE = create("pillar_cave");

    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        context.register(
                PILLAR_CAVE,
                NarakaWorldCarvers.PILLAR_CAVE.get().configured(
                        new CarverConfiguration(
                                0.2f,
                                UniformHeight.of(VerticalAnchor.aboveBottom(8), VerticalAnchor.absolute(47)),
                                UniformFloat.of(0.1F, 0.9F),
                                VerticalAnchor.aboveBottom(8),
                                CarverDebugSettings.of(false, Blocks.OAK_BUTTON.defaultBlockState()),
                                blocks.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES)
                        )
                )
        );
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, NarakaMod.location(name));
    }
}
