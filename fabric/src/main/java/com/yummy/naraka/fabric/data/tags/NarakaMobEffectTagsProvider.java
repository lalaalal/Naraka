package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.NarakaMobEffectTags;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

import java.util.concurrent.CompletableFuture;

public class NarakaMobEffectTagsProvider extends FabricTagProvider<MobEffect> {
    public NarakaMobEffectTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.MOB_EFFECT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(NarakaMobEffectTags.CHALLENGERS_BLESSING)
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_AMETHYST.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_COPPER.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_DIAMOND.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_EMERALD.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_GOLD.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_LAPIS.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_NECTARIUM.unwrapKey().orElseThrow())
                .add(NarakaMobEffects.CHALLENGERS_BLESSING_REDSTONE.unwrapKey().orElseThrow());
    }
}
