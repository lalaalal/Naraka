package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.yummy.naraka.client.sound.CategoryGainAccess;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin implements CategoryGainAccess {
    @Shadow
    @Final
    private Object2FloatMap<SoundSource> gainBySource;

    @Override
    public float naraka$getCategoryGain(SoundSource source) {
        return gainBySource.getOrDefault(source, 1);
    }

    @ModifyReturnValue(method = "calculateVolume(FLnet/minecraft/sounds/SoundSource;)F", at = @At("RETURN"))
    private float checkMasterGain(float original) {
        return original * this.gainBySource.getOrDefault(SoundSource.MASTER, 1);
    }
}
