package com.yummy.naraka.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sounds.Music;

@Environment(EnvType.CLIENT)
public interface BossMusicPlayer {
    void naraka$playBossMusic(Music music);

    void naraka$stopBossMusic();
}
