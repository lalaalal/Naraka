package com.yummy.naraka.network;

import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.sounds.NarakaSoundEvents;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class NarakaClientboundEventHandler {
    private static @Nullable SoundInstance currentPlayingMusic = null;

    private static final NarakaClientboundEventPacket.Event[] HEROBRINE_MUSIC_EVENT = new NarakaClientboundEventPacket.Event[]{
            null,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_1,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_2,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_3,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_4
    };

    private static final SoundEvent[] HEROBRINE_MUSIC = new SoundEvent[]{
            null,
            NarakaSoundEvents.HEROBRINE_PHASE_1.value(),
            NarakaSoundEvents.HEROBRINE_PHASE_2.value(),
            NarakaSoundEvents.HEROBRINE_PHASE_3.value(),
            NarakaSoundEvents.HEROBRINE_PHASE_4.value()
    };

    public static NarakaClientboundEventPacket.Event getEventByPhase(int phase) {
        if (0 < phase && phase <= 4)
            return HEROBRINE_MUSIC_EVENT[phase];
        return NarakaClientboundEventPacket.Event.STOP_MUSIC;
    }

    public static void handleEntityEvent(NarakaClientboundEntityEventPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().execute(() -> {
            Level level = context.getPlayer().level();
            Entity entity = level.getEntity(packet.entityId());
            if (entity != null)
                packet.event().handle(entity);
        });
    }

    public static void handleEvent(NarakaClientboundEventPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().execute(() -> packet.event().handle());
    }

    static void updateHerobrineMusic(final int phase) {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();

        if (0 < phase && phase <= 4) {
            if (currentPlayingMusic != null)
                soundManager.stop(currentPlayingMusic);
            currentPlayingMusic = NarakaMusics.bossMusic(HEROBRINE_MUSIC[phase]);
            soundManager.play(currentPlayingMusic);
        }
    }

    static void stopHerobrineMusic() {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();

        if (currentPlayingMusic != null) {
            soundManager.stop(currentPlayingMusic);
            currentPlayingMusic = null;
        }
    }
}
