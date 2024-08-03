package com.yummy.naraka.mixin;

import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public abstract class FeatureMixin<FC extends FeatureConfiguration> {
    @Inject(method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
    protected void checkStructure(FC config, WorldGenLevel level, ChunkGenerator generator, RandomSource pRandom, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (StructureProtector.checkProtected(pos)) {
            cir.cancel();
            cir.setReturnValue(false);
        }
    }
}
