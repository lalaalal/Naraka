package com.yummy.naraka.mixin.server;

import com.yummy.naraka.NarakaConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.SERVER)
@Mixin(DedicatedServer.class)
public abstract class DedicatedServerMixin {
    @Inject(method = "onServerExit", at = @At("HEAD"))
    public void closeConfigChangeListener(CallbackInfo ci) {
        NarakaConfig.stop();
    }
}
