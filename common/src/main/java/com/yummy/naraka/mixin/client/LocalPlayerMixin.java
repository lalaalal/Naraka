package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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

    @ModifyReturnValue(method = "isUnderWater", at = @At(value = "RETURN"))
    public boolean isUnderLiquid(boolean original) {
        if (NarakaItemUtils.canApplyFasterLiquidSwimming(naraka$living()))
            return original || naraka$isUnderLiquid();
        return original;
    }
}
