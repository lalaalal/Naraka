package com.yummy.naraka.sounds;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public final class NarakaMusics {
    public static SoundInstance bossMusic(SoundEvent soundEvent) {
        return new SimpleSoundInstance(
                soundEvent.getLocation(), SoundSource.MUSIC, 1, 1, SoundInstance.createUnseededRandom(), true, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true
        );
    }
}
