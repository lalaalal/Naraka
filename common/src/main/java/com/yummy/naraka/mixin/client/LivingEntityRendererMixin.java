package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.entity.state.HerobrineScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.PurifiedSoulFlameRenderState;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void setDisplayPurifiedSoulFlame(T entity, S reusedState, float partialTick, CallbackInfo ci) {
        if (reusedState instanceof PurifiedSoulFlameRenderState purifiedSoulFlameRenderState) {
            boolean displayPurifiedSoulFlame = EntityDataHelper.getRawEntityData(entity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0;
            purifiedSoulFlameRenderState.naraka$setDisplayPurifiedSoulFlame(displayPurifiedSoulFlame);
        }
        if (reusedState instanceof HerobrineScarfRenderState herobrineScarfRenderState) {
            herobrineScarfRenderState.naraka$extractWavingScarfRenderState(entity, partialTick);
        }
    }
}
