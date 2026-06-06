package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ConcretePowderBlock.class)
public abstract class ConcretePowderBlockMixin {
    @Inject(method = "shouldSolidify(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private static void preventSolidifyInHerobrineBiome(BlockGetter level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof LevelReader levelReader && levelReader.getBiome(pos).is(NarakaBiomes.HEROBRINE)) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(
            method = {"touchesLiquid(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z", "touchesLiquid(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"},
            require = 1,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isFaceSturdy(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z")
    )
    private static boolean preserveStateInHerobrineBiome(boolean original, @Local(argsOnly = true) BlockGetter level, @Local(argsOnly = true) BlockPos pos) {
        if (level instanceof LevelReader levelReader && levelReader.getBiome(pos).is(NarakaBiomes.HEROBRINE))
            return true;
        return original;
    }
}
