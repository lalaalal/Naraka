package com.yummy.naraka.world.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record OrePillarConfiguration(
        Holder<Block> baseBlock,
        HolderSet<Block> oreCandidates,
        int maxHeight,
        IntProvider heightProvider,
        IntProvider radiusProvider,
        float orePlaceChance,
        float spreadChance,
        boolean useSingleOre
) implements FeatureConfiguration {
    public static final Codec<OrePillarConfiguration> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryFixedCodec.create(Registries.BLOCK).fieldOf("base_block").forGetter(OrePillarConfiguration::baseBlock),
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("ore_candidates").forGetter(OrePillarConfiguration::oreCandidates),
                    Codec.INT.fieldOf("max_height").forGetter(OrePillarConfiguration::maxHeight),
                    IntProvider.CODEC.fieldOf("height_provider").forGetter(OrePillarConfiguration::heightProvider),
                    IntProvider.CODEC.fieldOf("radius_provider").forGetter(OrePillarConfiguration::radiusProvider),
                    Codec.FLOAT.fieldOf("ore_place_chance").forGetter(OrePillarConfiguration::orePlaceChance),
                    Codec.FLOAT.fieldOf("spread_chance").forGetter(OrePillarConfiguration::spreadChance),
                    Codec.BOOL.fieldOf("use_single_ore").forGetter(OrePillarConfiguration::useSingleOre)
            ).apply(instance, OrePillarConfiguration::new)
    );

    public int sampleHeight(RandomSource random) {
        return heightProvider.sample(random);
    }

    public int sampleRadius(RandomSource random) {
        return radiusProvider.sample(random);
    }
}
