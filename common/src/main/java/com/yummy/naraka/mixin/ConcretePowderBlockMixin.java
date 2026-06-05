package com.yummy.naraka.mixin;

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
    @Inject(method = "shouldSolidify", at = @At("HEAD"), cancellable = true)
    private static void preventSolidifyInHerobrineBiome(BlockGetter level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof LevelReader levelReader && levelReader.getBiome(pos).is(NarakaBiomes.HEROBRINE)) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "touchesLiquid", at = @At("HEAD"), cancellable = true)
    private static void preserveStateInHerobrineBiome(BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (level instanceof LevelReader levelReader && levelReader.getBiome(pos).is(NarakaBiomes.HEROBRINE)) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }
}
