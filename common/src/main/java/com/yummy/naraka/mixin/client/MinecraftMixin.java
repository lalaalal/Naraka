package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.world.entity.data.StunHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Inject(method = "destroy", at = @At("HEAD"))
    public void onMinecraftStopping(CallbackInfo ci) {
        ClientEvents.CLIENT_STOPPING.invoker().run(Minecraft.getInstance());
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    public void preventItemUseOnStun(CallbackInfo ci) {
        if (player != null && StunHelper.isStun(player))
            ci.cancel();
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    public void preventAttackOnStun(CallbackInfoReturnable<Boolean> cir) {
        if (player != null && StunHelper.isStun(player))
            cir.cancel();
    }
}
