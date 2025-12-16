package com.yummy.naraka.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class BossMusicSoundInstance extends AbstractSoundInstance {
    public BossMusicSoundInstance(SoundEvent soundEvent) {
        super(soundEvent, SoundSource.MUSIC, RandomSource.create());
        looping = true;
    }

    public BossMusicSoundInstance(Identifier location) {
        super(location, SoundSource.MUSIC, RandomSource.create());
        looping = true;
    }

    public void decreaseVolume(float interval) {
        this.volume = Math.max(this.volume - interval, 0);
    }

    public void increaseVolume(float interval) {
        this.volume += interval;
    }
}
