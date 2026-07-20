package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.event.ClientEvents;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientHandshakePacketListenerImplMixin {
    @Inject(method = "handleGameProfile", at = @At("TAIL"))
    public void onLoginFinished(CallbackInfo ci) {
        ClientEvents.LOGIN.invoker().run();
    }
}
