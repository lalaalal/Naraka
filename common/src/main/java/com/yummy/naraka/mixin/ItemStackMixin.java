package com.yummy.naraka.mixin;

import com.yummy.naraka.world.item.ItemDefaultNbtProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract void setTag(@Nullable CompoundTag compoundTag);

    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;I)V", at = @At("RETURN"))
    private void storeDefaultNbt(ItemLike item, int count, CallbackInfo ci) {
        if (item.asItem() instanceof ItemDefaultNbtProvider nbtProvider)
            nbtProvider.naraka$getDefaultNbt().ifPresent(this::setTag);
    }
}
