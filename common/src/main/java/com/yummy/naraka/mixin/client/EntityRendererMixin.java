package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.entity.state.PurifiedSoulFlameRenderState;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Inject(method = "extractRenderState", at = @At("RETURN"))
    private void setDisplayPurifiedSoulFlame(T entity, S reusedState, float partialTick, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity && reusedState instanceof PurifiedSoulFlameRenderState purifiedSoulFlameRenderState) {
            boolean displayPurifiedSoulFlame = EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0;
            purifiedSoulFlameRenderState.naraka$setDisplayPurifiedSoulFlame(displayPurifiedSoulFlame);
        }
    }
}
