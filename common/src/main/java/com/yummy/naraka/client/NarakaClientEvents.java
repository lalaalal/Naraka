package com.yummy.naraka.client;

import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.config.Configuration;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        ClientEvents.TICK_PRE.register(NarakaClientEvents::onClientTick);
        ClientEvents.TICK_POST.register(NarakaClientEvents::updateMusicVolume);
        ClientEvents.CLIENT_STOPPING.register(NarakaClientEvents::onClientStopping);
        ClientEvents.CAMERA_SETUP.register(NarakaClientEvents::shakeCamera);
        ClientEvents.LOGIN.register(NarakaClientEvents::onClientLogin);
    }

    private static void shakeCamera(ClientEvents.CameraSetup.Context context, BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick) {
        int cameraShakeTick = NarakaClientContext.CAMERA_SHAKE_TICK.getValue();
        if (cameraShakeTick > 0) {
            float ageInTicks = entity.tickCount + partialTick;
            float dy = Mth.sin(ageInTicks * NarakaConfig.CLIENT.cameraShakingSpeed.getValue()) * cameraShakeTick * NarakaConfig.CLIENT.cameraShakingStrength.getValue();
            context.move(0, dy, 0);
        }
    }

    private static void onClientLogin() {
        NarakaClientContext.initialize();
        EntityDataHelper.clear();
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.RAINBOW_COLOR.updateColor();
        WhiteFogRenderHelper.tick();
        contextTick(NarakaClientContext.CAMERA_SHAKE_TICK);
        contextTick(NarakaClientContext.POST_EFFECT_TICK);
        contextTick(NarakaClientContext.MUTE_MUSIC_TICK);
    }

    private static void updateMusicVolume(Minecraft minecraft) {
        float originalVolume = NarakaClientContext.MUSIC_VOLUME.getValue();
        SoundManager soundManager = minecraft.getSoundManager();
        if (NarakaClientContext.MUTE_MUSIC_TICK.getValue() > 0) {
            float newVolume = Math.max(originalVolume - 0.1f, 0);
            soundManager.updateSourceVolume(SoundSource.MUSIC, newVolume);
            NarakaClientContext.MUSIC_VOLUME.set(newVolume);
        } else if (originalVolume < 1) {
            float newVolume = Math.min(originalVolume + 0.02f, 1);
            soundManager.updateSourceVolume(SoundSource.MUSIC, newVolume);
            NarakaClientContext.MUSIC_VOLUME.set(newVolume);
        }
    }

    private static void contextTick(Configuration.ConfigValue<Integer> context) {
        int tickCount = context.getValue();
        if (tickCount > 0)
            context.set(tickCount - 1);
    }

    private static void onClientStopping(Minecraft minecraft) {
        NarakaConfig.stopWatching();
    }
}
