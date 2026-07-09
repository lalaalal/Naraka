package com.yummy.naraka.forge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.item.PickRangeModifiable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Shadow
    @Final
    protected ServerPlayer player;

    @ModifyExpressionValue(method = "handleBlockBreakAction", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;canReach(Lnet/minecraft/core/BlockPos;D)Z"))
    private boolean modifyMaxInteractionDistance(boolean original, @Local(argsOnly = true) BlockPos pos) {
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
            return player.getEyePosition().distanceToSqr(Vec3.atCenterOf(pos)) < pickRangeModifiable.getPickRange() * pickRangeModifiable.getPickRange();
        return original;
    }
}
