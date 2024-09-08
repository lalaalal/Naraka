package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.mixin.LivingEntityMixin;
import com.yummy.naraka.util.NarakaItemUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends LivingEntityMixin {
    @Shadow
    public abstract boolean isUnderWater();

    public LocalPlayerMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Needed to avoid setting sprinting false with lava swimming
     */
    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isInWater()Z"))
    public boolean isInLiquid(boolean original) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(living()))
            return original || isInLiquid();
        return original;
    }

    @ModifyExpressionValue(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUnderWater()Z"))
    public boolean isUnderLiquid(boolean original) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(living()))
            return original || isUnderLiquid();
        return original;
    }
}
