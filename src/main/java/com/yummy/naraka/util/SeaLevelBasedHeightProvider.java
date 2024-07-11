package com.yummy.naraka.util;

import net.minecraft.core.Holder;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SeaLevelBasedHeightProvider extends HeightProvider {
    public static final SeaLevelBasedHeightProvider EXACT = new SeaLevelBasedHeightProvider(ConstantInt.ZERO);

    public SeaLevelBasedHeightProvider(IntProvider sampler) {
        super(sampler);
    }

    @Override
    public int getHeight(Structure.GenerationContext context) {
        int seaLevel = context.chunkGenerator().getSeaLevel();
        return seaLevel + sampler.sample(context.random());
    }

    @Override
    public Holder<HeightProviderType> getType() {
        return NarakaHeightProviders.SEA_LEVEL_BASED;
    }
}
