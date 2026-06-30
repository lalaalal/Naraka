package com.yummy.naraka.network;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaPostEffects;
import com.yummy.naraka.client.gui.screen.AnimationControlScreen;
import com.yummy.naraka.client.gui.screen.SkillControlScreen;
import com.yummy.naraka.client.sound.BossMusicPlayer;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Consumer;

public class NarakaClientboundEventHandler {
    private static final Map<NarakaClientboundEntityEventPacket.Event, Consumer<Entity>> ENTITY_EVENT_MAP = Map.of(
            NarakaClientboundEntityEventPacket.Event.SHOW_SKILL_CONTROL_SCREEN, NarakaClientboundEventHandler::showSkillControlScreen,
            NarakaClientboundEntityEventPacket.Event.SHOW_ANIMATION_CONTROL_SCREEN, NarakaClientboundEventHandler::showAnimationControlScreen,
            NarakaClientboundEntityEventPacket.Event.PLAY_HEROBRINE_PHASE_1, (entity) -> NarakaClientboundEventHandler.updateHerobrineMusic(entity, 1),
            NarakaClientboundEntityEventPacket.Event.PLAY_HEROBRINE_PHASE_2, (entity) -> NarakaClientboundEventHandler.updateHerobrineMusic(entity, 2),
            NarakaClientboundEntityEventPacket.Event.PLAY_HEROBRINE_PHASE_3, (entity) -> NarakaClientboundEventHandler.updateHerobrineMusic(entity, 3),
            NarakaClientboundEntityEventPacket.Event.PLAY_HEROBRINE_PHASE_4, (entity) -> NarakaClientboundEventHandler.updateHerobrineMusic(entity, 4),
            NarakaClientboundEntityEventPacket.Event.STOP_BOSS_MUSIC, NarakaClientboundEventHandler::stopHerobrineMusic
    );

    private static final Map<NarakaClientboundEventPacket.Event, Runnable> EVENT_MAP = Map.of(
            NarakaClientboundEventPacket.Event.START_HEROBRINE_SKY, NarakaClientboundEventHandler::startHerobrineSky,
            NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY, NarakaClientboundEventHandler::stopHerobrineSky,
            NarakaClientboundEventPacket.Event.START_WHITE_SCREEN, NarakaClientboundEventHandler::startWhiteScreen,
            NarakaClientboundEventPacket.Event.STOP_WHITE_FOG, NarakaClientboundEventHandler::stopWhiteScreen,
            NarakaClientboundEventPacket.Event.SHAKE_CAMERA, NarakaClientboundEventHandler::shakeCamera,
            NarakaClientboundEventPacket.Event.MONOCHROME_EFFECT, NarakaClientboundEventHandler::monochrome,
            NarakaClientboundEventPacket.Event.RYOIKI_GRAY_EFFECT, NarakaClientboundEventHandler::ryoikiGrayEffect,
            NarakaClientboundEventPacket.Event.RYOIKI_GREEN_EFFECT, NarakaClientboundEventHandler::ryoikiGreenEffect,
            NarakaClientboundEventPacket.Event.MUTE_MUSIC_CATEGORY, NarakaClientboundEventHandler::muteMusicCategory
    );

    public static final Music[] HEROBRINE_MUSIC = new Music[]{
            NarakaMusics.HEROBRINE_PHASE_1,
            NarakaMusics.HEROBRINE_PHASE_1,
            NarakaMusics.HEROBRINE_PHASE_2,
            NarakaMusics.HEROBRINE_PHASE_3,
            NarakaMusics.HEROBRINE_PHASE_4
    };

    public static void handleEntityEvent(NarakaClientboundEntityEventPacket packet, NetworkManager.Context context) {
        Level level = context.level();
        Entity entity = level.getEntity(packet.entityId());
        Minecraft.getInstance().execute(() -> {
            if (entity != null)
                ENTITY_EVENT_MAP.getOrDefault(packet.event(), e -> {
                }).accept(entity);
        });
    }

    public static void handleEvent(NarakaClientboundEventPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            for (NarakaClientboundEventPacket.Event event : packet.events())
                event.run();
        });
    }

    public static void updateHerobrineMusic(Entity entity, final int phase) {
        BossMusicPlayer bossMusicPlayer = NarakaMusics.bossMusicPlayer();
        if (0 < phase && phase <= 4) {
            bossMusicPlayer.naraka$playBossMusic(HEROBRINE_MUSIC[phase]);
            NarakaClientContext.HEROBRINE_MUSIC_SOURCES.getValue()
                    .add(entity.getUUID());
        }
    }

    public static void stopHerobrineMusic(Entity entity) {
        BossMusicPlayer bossMusicPlayer = NarakaMusics.bossMusicPlayer();
        NarakaClientContext.HEROBRINE_MUSIC_SOURCES.getValue()
                .remove(entity.getUUID());
        if (NarakaClientContext.HEROBRINE_MUSIC_SOURCES.getValue().isEmpty())
            bossMusicPlayer.naraka$stopBossMusic();
    }

    public static void showSkillControlScreen(Entity entity) {
        if (entity instanceof SkillUsingMob mob)
            Minecraft.getInstance().setScreen(new SkillControlScreen(mob));
    }

    public static void showAnimationControlScreen(Entity entity) {
        if (entity instanceof SkillUsingMob mob)
            Minecraft.getInstance().setScreen(new AnimationControlScreen(mob));
    }

    public static void startHerobrineSky() {
        NarakaClientContext.ENABLE_HEROBRINE_SKY.set(true);
    }

    public static void stopHerobrineSky() {
        NarakaClientContext.ENABLE_HEROBRINE_SKY.set(false);
    }

    public static void startWhiteScreen() {
        NarakaClientContext.ENABLE_WHITE_SCREEN.set(true);
    }

    public static void stopWhiteScreen() {
        NarakaClientContext.ENABLE_WHITE_SCREEN.set(false);
    }

    public static void shakeCamera() {
        NarakaClientContext.CAMERA_SHAKE_TICK.set(10);
    }

    public static void monochrome() {
        NarakaClientContext.POST_EFFECT.set(NarakaPostEffects.MONOCHROME);
        NarakaClientContext.POST_EFFECT_TICK.set(10);
    }

    public static void ryoikiGrayEffect() {
        NarakaClientContext.POST_EFFECT.set(NarakaPostEffects.RYOIKI_GRAY);
        NarakaClientContext.POST_EFFECT_TICK.set(50);
    }

    public static void ryoikiGreenEffect() {
        NarakaClientContext.POST_EFFECT.set(NarakaPostEffects.RYOIKI_GREEN);
        NarakaClientContext.POST_EFFECT_TICK.set(50);
    }

    public static void muteMusicCategory() {
        NarakaClientContext.MUTE_MUSIC_TICK.set(60);
    }

    public static void freezeTick() {
        NarakaClientContext.TICK_FROZEN.set(true);
        NarakaClientContext.FROZEN_PARTIAL_TICK.set(Minecraft.getInstance().getFrameTime());
    }

    public static void unfreezeTick() {
        NarakaClientContext.TICK_FROZEN.set(false);
    }
}
