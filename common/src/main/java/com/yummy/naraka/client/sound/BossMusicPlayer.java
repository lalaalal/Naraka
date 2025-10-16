package com.yummy.naraka.client.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sounds.MusicInfo;

@Environment(EnvType.CLIENT)
public interface BossMusicPlayer {
    void naraka$playBossMusic(MusicInfo musicInfo);

    void naraka$stopBossMusic();
}
