package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.sound.VolumeController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(SoundManager.class)
public abstract class SoundManagerMixin implements VolumeController {
    @Shadow @Final
    private SoundEngine soundEngine;

    @Override
    public void naraka$setVolume(SoundInstance soundInstance) {
        if (soundEngine instanceof VolumeController volumeController)
            volumeController.naraka$setVolume(soundInstance);
    }
}
