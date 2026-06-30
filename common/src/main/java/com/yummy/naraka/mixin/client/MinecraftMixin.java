package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.event.ClientEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "destroy", at = @At("HEAD"))
    public void onMinecraftStopping(CallbackInfo ci) {
        ClientEvents.CLIENT_STOPPING.invoker().run(Minecraft.getInstance());
    }

    @ModifyArg(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;render(FJZ)V"))
    public float replacePartialTick(float partialTicks) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            return NarakaClientContext.FROZEN_PARTIAL_TICK.getValue();
        return partialTicks;
    }
}
