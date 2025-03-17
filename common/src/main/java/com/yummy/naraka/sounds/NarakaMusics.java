package com.yummy.naraka.sounds;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

import java.util.HashSet;
import java.util.Set;

public final class NarakaMusics {
    private static final Set<ResourceLocation> LOOPING_MUSICS = new HashSet<>();

    public static final Music HEROBRINE_PHASE_1 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_1);
    public static final Music HEROBRINE_PHASE_2 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_2);
    public static final Music HEROBRINE_PHASE_3 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_3);
    public static final Music HEROBRINE_PHASE_4 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_4);

    public static boolean isLoopingMusic(SoundInstance soundInstance) {
        return LOOPING_MUSICS.contains(soundInstance.getLocation());
    }

    public static Music createBossMusic(Holder<SoundEvent> soundEvent) {
        Music music = new Music(soundEvent, 0, 0, true);
        LOOPING_MUSICS.add(soundEvent.value().getLocation());
        return music;
    }
}
