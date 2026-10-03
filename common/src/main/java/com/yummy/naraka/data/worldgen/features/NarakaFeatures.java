package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.ReplaceBlockFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.List;

public class NarakaFeatures {
    public static final ResourceKey<Feature> PURIFIED_SOUL_LANTERN = create("purified_soul_lantern");

    public static void bootstrap(BootstrapContext<Feature> context) {
        NarakaOreFeatures.bootstrap(context);
        NarakaCaveFeatures.bootstrap(context);

        context.register(
                PURIFIED_SOUL_LANTERN,
                new ReplaceBlockFeature(
                        List.of(
                                BlockReplacement.replace(
                                        new BlockMatchTest(NarakaBlocks.TRANSPARENT_BLOCK.get()),
                                        NarakaBlocks.PURIFIED_SOUL_LANTERN.get().defaultBlockState()
                                )
                        )
                )
        );
    }

    public static ResourceKey<Feature> create(String name) {
        return ResourceKey.create(Registries.FEATURE, NarakaMod.identifier(name));
    }
}
