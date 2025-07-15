package com.yummy.naraka.client;

import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.ComponentStyles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        ClientEvents.TICK_PRE.register(NarakaClientEvents::onClientTick);
        ClientEvents.CLIENT_STOPPING.register(NarakaClientEvents::onClientStopping);
        ClientEvents.CAMERA_SETUP.register(NarakaClientEvents::shakeCamera);
    }

    private static void shakeCamera(ClientEvents.CameraSetup.Context context, BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick) {
        int cameraShakeTick = NarakaClientContext.CAMERA_SHAKE_TICK.getValue();
        if (cameraShakeTick > 0) {
            float ageInTicks = entity.tickCount + partialTick;
            float dy = Mth.sin(ageInTicks * NarakaConfig.CLIENT.cameraShakingSpeed.getValue()) * cameraShakeTick * NarakaConfig.CLIENT.cameraShakingStrength.getValue();
            context.move(0, dy, 0);
        }
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.tick();
        WhiteFogRenderHelper.tick();
        cameraTick();
    }

    private static void cameraTick() {
        int cameraShakeTick = NarakaClientContext.CAMERA_SHAKE_TICK.getValue();
        if (cameraShakeTick > 0)
            NarakaClientContext.CAMERA_SHAKE_TICK.set(cameraShakeTick - 1);
    }

    private static void onClientStopping(Minecraft minecraft) {
        NarakaConfig.stopWatching();
    }
}
