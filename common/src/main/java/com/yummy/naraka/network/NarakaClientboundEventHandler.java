package com.yummy.naraka.network;

import com.yummy.naraka.client.gui.screen.AnimationControlScreen;
import com.yummy.naraka.client.gui.screen.SkillControlScreen;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class NarakaClientboundEventHandler {
    private static final NarakaClientboundEventPacket.Event[] HEROBRINE_MUSIC_EVENT = new NarakaClientboundEventPacket.Event[]{
            null,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_1,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_2,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_3,
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_4
    };

    private static final Music[] HEROBRINE_MUSIC = new Music[]{
            null,
            NarakaMusics.HEROBRINE_PHASE_1,
            NarakaMusics.HEROBRINE_PHASE_2,
            NarakaMusics.HEROBRINE_PHASE_3,
            NarakaMusics.HEROBRINE_PHASE_4
    };

    public static NarakaClientboundEventPacket.Event musicEventByPhase(int phase) {
        if (0 < phase && phase <= 4)
            return HEROBRINE_MUSIC_EVENT[phase];
        return NarakaClientboundEventPacket.Event.STOP_MUSIC;
    }

    public static void handleEntityEvent(NarakaClientboundEntityEventPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            Level level = context.level();
            Entity entity = level.getEntity(packet.entityId());
            if (entity != null)
                packet.event().handle(entity);
        });
    }

    public static void handleEvent(NarakaClientboundEventPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            for (NarakaClientboundEventPacket.Event event : packet.events())
                event.handle();
        });
    }

    static void updateHerobrineMusic(final int phase) {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();
        MusicManager musicManager = minecraft.getMusicManager();

        if (0 < phase && phase <= 4) {
            soundManager.stop();
            musicManager.stopPlaying();
            musicManager.startPlaying(HEROBRINE_MUSIC[phase]);
        }
    }

    static void stopHerobrineMusic() {
        Minecraft minecraft = Minecraft.getInstance();
        MusicManager musicManager = minecraft.getMusicManager();

        musicManager.stopPlaying();
    }

    static void showSkillControlScreen(Entity entity) {
        if (entity instanceof SkillUsingMob mob)
            Minecraft.getInstance().setScreen(new SkillControlScreen(mob));
    }

    static void showAnimationControlScreen(Entity entity) {
        if (entity instanceof SkillUsingMob mob)
            Minecraft.getInstance().setScreen(new AnimationControlScreen(mob));
    }

    static void startHerobrineSky() {
        NarakaConfig.CLIENT.renderHerobrineSky.set(true);
    }

    static void stopHerobrineSky() {
        NarakaConfig.CLIENT.renderHerobrineSky.set(false);
    }
}
