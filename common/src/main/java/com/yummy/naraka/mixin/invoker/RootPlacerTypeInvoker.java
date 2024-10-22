package com.yummy.naraka.mixin.invoker;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RootPlacerType.class)
public interface RootPlacerTypeInvoker {
    @Invoker("<init>")
    static <T extends RootPlacer> RootPlacerType<T> create(MapCodec<T> codec) {
        throw new AssertionError();
    }
}
