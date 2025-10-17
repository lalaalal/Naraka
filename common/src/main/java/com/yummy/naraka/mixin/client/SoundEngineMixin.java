package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.sound.VolumeController;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin implements VolumeController {
    @Shadow private boolean loaded;

    @Shadow @Final private Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel;

    @Override
    public void naraka$setVolume(SoundInstance soundInstance) {
        if (loaded) {
            ChannelAccess.ChannelHandle handle = instanceToChannel.get(soundInstance);
            if (handle != null)
                handle.execute(channel -> channel.setVolume(soundInstance.getVolume()));
        }
    }
}
