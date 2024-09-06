package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    public boolean isInLiquid(boolean original) {
        if (NarakaItemUtils.canApplyReinforcementEffect(this, EquipmentSlot.LEGS, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING))
            return original || isInLiquid();
        return original;
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    public boolean isUnderLiquid(boolean original) {
        if (NarakaItemUtils.canApplyReinforcementEffect(this, EquipmentSlot.LEGS, NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING))
            return original || isEyeInFluid(FluidTags.LAVA);
        return original;
    }
}
