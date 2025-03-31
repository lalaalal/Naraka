package com.yummy.naraka.mixin;

import com.yummy.naraka.event.EntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class, Player.class, ServerPlayer.class})
public abstract class LivingDeathInvoker {
    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    public void invokeLivingDeathEvents(DamageSource damageSource, CallbackInfo ci) {
        if (!EntityEvents.LIVING_DEATH.invoker().die(naraka$living(), damageSource))
            ci.cancel();
    }

    @Unique
    protected LivingEntity naraka$living() {
        return (LivingEntity) (Object) this;
    }
}
