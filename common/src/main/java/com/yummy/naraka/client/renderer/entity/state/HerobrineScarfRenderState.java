package com.yummy.naraka.client.renderer.entity.state;

import net.minecraft.world.entity.LivingEntity;

public interface HerobrineScarfRenderState {
    void naraka$extractWavingScarfRenderState(LivingEntity livingEntity, float partialTicks);

    WavingScarfRenderState naraka$getScarfRenderState();
}
