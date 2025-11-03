package com.yummy.naraka.network;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.gui.screen.AnimationControlScreen;
import com.yummy.naraka.client.gui.screen.SkillControlScreen;
import com.yummy.naraka.client.sound.BossMusicPlayer;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicInfo;
import net.minecraft.sounds.Music;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
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
            NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY, NarakaClientboundEventHandler::stopHerobrineSky,
            NarakaClientboundEventPacket.Event.START_WHITE_SCREEN, NarakaClientboundEventHandler::startWhiteScreen,
            NarakaClientboundEventPacket.Event.STOP_WHITE_FOG, NarakaClientboundEventHandler::stopWhiteScreen,
            NarakaClientboundEventPacket.Event.SHAKE_CAMERA, NarakaClientboundEventHandler::shakeCamera
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

    private static void updateHerobrineMusic(final int phase) {
        BossMusicPlayer bossMusicPlayer = NarakaMusics.bossMusicPlayer();
        if (0 < phase && phase <= 4)
            bossMusicPlayer.naraka$playBossMusic(new MusicInfo(HEROBRINE_MUSIC[phase]));
    }

    private static void stopHerobrineMusic() {
        BossMusicPlayer bossMusicPlayer = NarakaMusics.bossMusicPlayer();
        bossMusicPlayer.naraka$stopBossMusic();
    }

    private static void showSkillControlScreen(Entity entity) {
        if (entity instanceof SkillUsingMob mob)
            Minecraft.getInstance().setScreen(new SkillControlScreen(mob));
    }

    private static void showAnimationControlScreen(Entity entity) {
        if (entity instanceof SkillUsingMob mob)
            Minecraft.getInstance().setScreen(new AnimationControlScreen(mob));
    }

    private static void startHerobrineSky() {
        NarakaClientContext.ENABLE_HEROBRINE_SKY.set(true);
    }

    private static void stopHerobrineSky() {
        NarakaClientContext.ENABLE_HEROBRINE_SKY.set(false);
    }

    private static void startWhiteScreen() {
        NarakaClientContext.ENABLE_WHITE_SCREEN.set(true);
    }

    private static void stopWhiteScreen() {
        NarakaClientContext.ENABLE_WHITE_SCREEN.set(false);
    }

    private static void shakeCamera() {
        NarakaClientContext.CAMERA_SHAKE_TICK.set(10);
    }
}
