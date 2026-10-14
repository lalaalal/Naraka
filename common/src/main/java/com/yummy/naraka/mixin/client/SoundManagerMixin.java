package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.sound.CategoryGainAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundManager.class)
public abstract class SoundManagerMixin implements CategoryGainAccess {
    @Shadow
    @Final
    private SoundEngine soundEngine;

    @Override
    public float naraka$getCategoryGain(SoundSource source) {
        return ((CategoryGainAccess) soundEngine).naraka$getCategoryGain(source);
    }
}
