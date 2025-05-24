package com.yummy.naraka.network;

import com.yummy.naraka.client.gui.screen.AnimationControlScreen;
import com.yummy.naraka.client.gui.screen.SkillControlScreen;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicInfo;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Consumer;

public class NarakaClientboundEventHandler {
    private static final Map<NarakaClientboundEntityEventPacket.Event, Consumer<Entity>> ENTITY_EVENT_MAP = Map.of(
            NarakaClientboundEntityEventPacket.Event.SHOW_SKILL_CONTROL_SCREEN, NarakaClientboundEventHandler::showSkillControlScreen,
            NarakaClientboundEntityEventPacket.Event.SHOW_ANIMATION_CONTROL_SCREEN, NarakaClientboundEventHandler::showAnimationControlScreen
    );

    private static final Map<NarakaClientboundEventPacket.Event, Runnable> EVENT_MAP = Map.of(
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_1, () -> NarakaClientboundEventHandler.updateHerobrineMusic(1),
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_2, () -> NarakaClientboundEventHandler.updateHerobrineMusic(2),
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_3, () -> NarakaClientboundEventHandler.updateHerobrineMusic(3),
            NarakaClientboundEventPacket.Event.PLAY_HEROBRINE_PHASE_4, () -> NarakaClientboundEventHandler.updateHerobrineMusic(4),
            NarakaClientboundEventPacket.Event.STOP_MUSIC, NarakaClientboundEventHandler::stopHerobrineMusic,
            NarakaClientboundEventPacket.Event.START_HEROBRINE_SKY, NarakaClientboundEventHandler::startHerobrineSky,
            NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY, NarakaClientboundEventHandler::stopHerobrineSky
    );

    private static final Music[] HEROBRINE_MUSIC = new Music[]{
            null,
            NarakaMusics.HEROBRINE_PHASE_1,
            NarakaMusics.HEROBRINE_PHASE_2,
            NarakaMusics.HEROBRINE_PHASE_3,
            NarakaMusics.HEROBRINE_PHASE_4
    };

    public static void handleEntityEvent(NarakaClientboundEntityEventPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            Level level = context.level();
            Entity entity = level.getEntity(packet.entityId());
            if (entity != null)
                ENTITY_EVENT_MAP.getOrDefault(packet.event(), e -> {
                }).accept(entity);
        });
    }

    public static void handleEvent(NarakaClientboundEventPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            for (NarakaClientboundEventPacket.Event event : packet.events())
                EVENT_MAP.getOrDefault(event, () -> {
                }).run();
        });
    }

    static void updateHerobrineMusic(final int phase) {
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();
        MusicManager musicManager = minecraft.getMusicManager();

        if (0 < phase && phase <= 4) {
            soundManager.stop();
            musicManager.stopPlaying();
            musicManager.startPlaying(new MusicInfo(HEROBRINE_MUSIC[phase]));
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
