package com.yummy.naraka.mixin.client;

import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public abstract class ClientLevelDataMixin {
    @Inject(method = "getDayTime", at = @At("HEAD"), cancellable = true)
    public void modifyDayTime(CallbackInfoReturnable<Long> cir) {
        if (NarakaConfig.CLIENT.renderHerobrineSky.getValue()) {
            cir.cancel();
            cir.setReturnValue(13000L);
        }
    }
}
