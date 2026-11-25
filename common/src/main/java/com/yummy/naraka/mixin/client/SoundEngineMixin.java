package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.sound.CategoryGainAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin implements CategoryGainAccess {
    @Unique
    private final Map<SoundSource, Float> naraka$customVolumes = new HashMap<>();

    @Override
    public float naraka$getCategoryGain(SoundSource source) {
        return naraka$customVolumes.computeIfAbsent(source, key -> 1f);
    }

    @Inject(method = "updateCategoryVolume", at = @At("HEAD"))
    private void updateCustomVolume(SoundSource category, float volume, CallbackInfo ci) {
        naraka$customVolumes.put(category, volume);
    }

    @ModifyReturnValue(method = "calculateVolume(FLnet/minecraft/sounds/SoundSource;)F", at = @At("RETURN"))
    private float checkMasterGain(float original, @Local(argsOnly = true) SoundSource source) {
        return original * naraka$customVolumes.computeIfAbsent(source, key -> 1f)
                * naraka$customVolumes.computeIfAbsent(SoundSource.MASTER, key -> 1f);
    }
}
