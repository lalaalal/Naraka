package com.yummy.naraka.mixin.client;

import com.yummy.naraka.sounds.NarakaMusics;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(SimpleSoundInstance.class)
public abstract class SimpleSoundInstanceMixin extends AbstractSoundInstance {
    protected SimpleSoundInstanceMixin(SoundEvent soundEvent, SoundSource source, RandomSource random) {
        super(soundEvent, source, random);
    }

    protected SimpleSoundInstanceMixin(ResourceLocation location, SoundSource source, RandomSource random) {
        super(location, source, random);
    }

    @Override
    public boolean isLooping() {
        return NarakaMusics.isLoopingMusic(this) || super.isLooping();
    }
}
