package com.yummy.naraka.mixin.invoker;

import net.minecraft.world.level.block.MangroveRootsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MangroveRootsBlock.class)
public interface MangroveRootsBlockInvoker {
    @Invoker("<init>")
    static MangroveRootsBlock create(BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }
}
