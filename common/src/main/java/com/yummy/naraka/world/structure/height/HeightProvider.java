package com.yummy.naraka.world.structure.height;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.structure.Structure;

public abstract class HeightProvider {
    public static final Codec<HeightProvider> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryFixedCodec.create(NarakaRegistries.Keys.HEIGHT_PROVIDER_TYPE).fieldOf("type").forGetter(HeightProvider::getType),
                    IntProvider.CODEC.fieldOf("sampler").forGetter(provider -> provider.sampler)
            ).apply(instance, HeightProvider::create)
    );
    protected final IntProvider sampler;

    private static HeightProvider create(Holder<HeightProviderType> type, IntProvider sampler) {
        return type.value().create(sampler);
    }

    protected HeightProvider(IntProvider sampler) {
        this.sampler = sampler;
    }

    public abstract int getHeight(Structure.GenerationContext context);

    public abstract Holder<HeightProviderType> getType();
}
