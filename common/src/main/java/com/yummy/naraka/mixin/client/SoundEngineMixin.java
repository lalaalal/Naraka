package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.sound.VolumeController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Options;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin implements VolumeController {
    @Shadow private boolean loaded;

    @Shadow @Final private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Shadow @Final private Options options;

    @Override
    public void naraka$setVolume(SoundInstance soundInstance) {
        float maxVolume = options.getSoundSourceVolume(soundInstance.getSource());
        float volume = Mth.lerp(soundInstance.getVolume(), 0, maxVolume);
        if (loaded) {
            ChannelAccess.ChannelHandle handle = instanceToChannel.get(soundInstance);
            if (handle != null)
                handle.execute(channel -> channel.setVolume(volume));
        }
    }
}
