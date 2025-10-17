package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.sound.BossMusicPlayer;
import com.yummy.naraka.client.sound.BossMusicSoundInstance;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(MusicManager.class)
public abstract class MusicManagerMixin implements BossMusicPlayer {
    @Shadow @Nullable
    private SoundInstance currentMusic;
    @Shadow @Final
    private Minecraft minecraft;
    @Shadow
    private int nextSongDelay;

    @Shadow
    public abstract void stopPlaying();

    @Unique @Nullable
    private BossMusicSoundInstance naraka$bossMusic;
    @Unique @Nullable
    private BossMusicSoundInstance naraka$nextMusic;
    @Unique
    private boolean naraka$stopBossMusic = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickBossMusic(CallbackInfo ci) {
        if (naraka$bossMusic != null) {
            if (naraka$stopBossMusic || naraka$nextMusic != null) {
                naraka$bossMusic.decreaseVolume(0.02f);
            }
            if (naraka$bossMusic.getVolume() == 0) {
                naraka$bossMusic = null;
                stopPlaying();
                if (naraka$nextMusic != null) {
                    naraka$playBossMusicInstance(naraka$nextMusic);
                    naraka$nextMusic = null;
                }
            }
        }
    }

    @Override
    public void naraka$playBossMusic(Music music) {
        BossMusicSoundInstance bossMusicSoundInstance = new BossMusicSoundInstance(music.getEvent().value());
        if (naraka$bossMusic == null) {
            stopPlaying();
            naraka$playBossMusicInstance(bossMusicSoundInstance);
        } else {
            naraka$nextMusic = bossMusicSoundInstance;
        }
    }

    @Override
    public void naraka$stopBossMusic() {
        naraka$nextMusic = null;
        naraka$stopBossMusic = true;
    }

    @Unique
    private void naraka$playBossMusicInstance(BossMusicSoundInstance bossMusicSoundInstance) {
        naraka$bossMusic = bossMusicSoundInstance;
        currentMusic = naraka$bossMusic;
        naraka$stopBossMusic = false;
        this.minecraft.getSoundManager().play(bossMusicSoundInstance);
        this.nextSongDelay = Integer.MAX_VALUE;
    }
}
