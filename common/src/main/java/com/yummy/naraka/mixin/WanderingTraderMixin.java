package com.yummy.naraka.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin {
    @Inject(method = "updateTrades", at = @At(value = "RETURN"))
    protected void addNarakaItemsToTrades(ServerLevel level, CallbackInfo ci) {

    }
}
