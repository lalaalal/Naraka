package com.yummy.naraka.mixin;

import com.yummy.naraka.world.trunkplacers.TrunkPlacerPatches;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static net.minecraft.world.level.levelgen.feature.TreeFeature.validTreePos;

@Mixin(TrunkPlacer.class)
public abstract class TrunkPlacerMixin {
    @Inject(method = "placeLog(Lnet/minecraft/world/level/LevelSimulatedReader;Ljava/util/function/BiConsumer;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;Ljava/util/function/Function;)Z", at = @At("HEAD"), cancellable = true)
    protected void placeLog(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration, Function<BlockState, BlockState> function, CallbackInfoReturnable<Boolean> cir) {
        BlockState state = treeConfiguration.trunkProvider.getState(randomSource, blockPos);
        if (TrunkPlacerPatches.isRegistered(state.getBlock())) {
            Function<BlockState, BlockState> patched = TrunkPlacerPatches.get(state.getBlock(), function);
            boolean result = validTreePos(levelSimulatedReader, blockPos);
            biConsumer.accept(blockPos, patched.apply(state));
            cir.cancel();
            cir.setReturnValue(result);
        }
    }
}
