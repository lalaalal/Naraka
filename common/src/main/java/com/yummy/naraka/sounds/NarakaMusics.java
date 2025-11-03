package com.yummy.naraka.sounds;

import com.yummy.naraka.client.sound.BossMusicPlayer;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

public final class NarakaMusics {
    @Nullable
    private static BossMusicPlayer bossMusicPlayerInstance;

    public static final Music HEROBRINE_PHASE_1 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_1);
    public static final Music HEROBRINE_PHASE_2 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_2);
    public static final Music HEROBRINE_PHASE_3 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_3);
    public static final Music HEROBRINE_PHASE_4 = createBossMusic(NarakaSoundEvents.HEROBRINE_PHASE_4);

    public static Music createBossMusic(Holder<SoundEvent> soundEvent) {
        return new Music(soundEvent, 0, 0, true);
    }

    private static final NarakaClientboundEventPacket.Event[] HEROBRINE_MUSIC_EVENT = new NarakaClientboundEventPacket.Event[]{
            null,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_1,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_2,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_3,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_4
    };

    public static NarakaClientboundEventPacket.Event musicEventByPhase(int phase) {
        if (0 < phase && phase <= 4)
            return HEROBRINE_MUSIC_EVENT[phase];
        return NarakaClientboundEventPacket.Event.STOP_MUSIC;
    }

    public static BossMusicPlayer bossMusicPlayer() {
        if (bossMusicPlayerInstance == null) {
            MusicManager musicManager = Minecraft.getInstance().getMusicManager();
            if (musicManager instanceof BossMusicPlayer bossMusicPlayer)
                return bossMusicPlayerInstance = bossMusicPlayer;
            throw new IllegalStateException("MusicManager is not BossMusicPlayer");
        }
        return bossMusicPlayerInstance;
    }
}
