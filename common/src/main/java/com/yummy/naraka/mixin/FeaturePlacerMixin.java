package com.yummy.naraka.mixin;

import com.yummy.naraka.world.structure.protection.NoFeatureArea;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.placement.FeaturePlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FeaturePlacer.class)
public abstract class FeaturePlacerMixin {
    @Shadow
    @Final
    private WorldGenLevel level;

    @Inject(method = "place(Lnet/minecraft/world/level/levelgen/placement/PlacedFeature;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("HEAD"), cancellable = true)
    private void checkProtection(PlacedFeature placedFeature, RandomSource random, BlockPos origin, boolean biomeCheck, CallbackInfoReturnable<Boolean> cir) {
        if (NoFeatureArea.isProtectedArea(level, origin)) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }
}
