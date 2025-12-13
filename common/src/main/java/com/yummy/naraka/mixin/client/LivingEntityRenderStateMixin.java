package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.entity.state.HerobrineScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.PurifiedSoulFlameRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public abstract class LivingEntityRenderStateMixin implements PurifiedSoulFlameRenderState, HerobrineScarfRenderState {
    @Unique
    private boolean naraka$displayNarkaFlame;
    @Unique
    private final WavingScarfRenderState naraka$wavingScarfRenderState = new WavingScarfRenderState();

    @Override
    public boolean naraka$displayPurifiedSoulFlame() {
        return naraka$displayNarkaFlame;
    }

    @Override
    public void naraka$setDisplayPurifiedSoulFlame(boolean flame) {
        this.naraka$displayNarkaFlame = flame;
    }

    @Override
    public void naraka$extractWavingScarfRenderState(LivingEntity livingEntity, float partialTicks) {
        this.naraka$wavingScarfRenderState.extract(livingEntity, partialTicks);
    }

    @Override
    public WavingScarfRenderState naraka$getScarfRenderState() {
        return naraka$wavingScarfRenderState;
    }
}
