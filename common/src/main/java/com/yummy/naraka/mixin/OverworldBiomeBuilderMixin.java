package com.yummy.naraka.mixin;

import com.mojang.datafixers.util.Pair;
import com.yummy.naraka.NarakaConfig;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {
    @Shadow
    @Final
    private Climate.Parameter FULL_RANGE;

    @Shadow
    protected abstract void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consumer, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter depth, float weirdness, ResourceKey<Biome> key);

    @Inject(method = "addUndergroundBiomes", at = @At("RETURN"))
    private void modifyUndergroundBiomes(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> consume, CallbackInfo ci) {
        if (!NarakaMod.isDataGeneration && NarakaMod.isModLoaded && NarakaConfig.GENERATE_PILLAR_CAVES)
            addUndergroundBiome(
                    consume,
                    this.FULL_RANGE,
                    this.FULL_RANGE,
                    Climate.Parameter.span(0.8F, 1.0F),
                    this.FULL_RANGE,
                    this.FULL_RANGE,
                    0.0F,
                    NarakaBiomes.PILLAR_CAVE
            );
    }
}
