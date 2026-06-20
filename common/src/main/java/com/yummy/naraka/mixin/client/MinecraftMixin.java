package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.event.ClientEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "close", at = @At("HEAD"))
    public void onMinecraftStopping(CallbackInfo ci) {
        ClientEvents.CLIENT_STOPPING.invoker().run(Minecraft.getInstance());
    }
}
