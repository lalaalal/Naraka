package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @ModifyExpressionValue(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z"))
    public boolean allowSpectatorFlyingSpeedForFlyingReinforcementEffect(boolean original) {
        return original || (minecraft.player != null
                && minecraft.player.getAbilities().flying
                && minecraft.player.isSprinting()
                && NarakaItemUtils.canApplyReinforcementEffect(minecraft.player, EquipmentSlot.CHEST, NarakaReinforcementEffects.FLYING));
    }
}
