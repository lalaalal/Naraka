package com.yummy.naraka.network.handler;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaClientServices;
import com.yummy.naraka.client.sound.CategoryGainAccess;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.packet.SoundControlPacket;
import com.yummy.naraka.util.JobManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;

public class SoundControlHandler {
    public static void handle(SoundControlPacket packet, NetworkManager.Context context) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            CategoryGainAccess categoryGainAccess = (CategoryGainAccess) minecraft.getSoundManager();
            float startVolume = categoryGainAccess.naraka$getCategoryGain(packet.source());
            NarakaClientServices.CLIENT_JOB_MANAGER.register(NarakaMod.identifier("volume." + packet.source().getName()), new SoundControlJob(packet, startVolume));
        });
    }

    private static class SoundControlJob implements JobManager.Job<Minecraft> {
        private int tickCount = 0;
        private final float startVolume;
        private final SoundControlPacket packet;

        private SoundControlJob(SoundControlPacket packet, float startVolume) {
            this.packet = packet;
            this.startVolume = startVolume;
        }

        @Override
        public void tick(Minecraft instance) {
            SoundManager soundManager = instance.getSoundManager();
            float delta = Math.clamp(tickCount / (float) packet.duration(), 0, 1);
            float currentVolume = packet.interpolate(delta, startVolume);
            soundManager.updateCategoryVolume(packet.source(), currentVolume);
            tickCount += 1;
        }

        @Override
        public boolean finished() {
            return tickCount > packet.duration();
        }
    }
}
