package com.yummy.naraka.world.item;

import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public interface ItemDefaultNbtProvider {
    Optional<CompoundTag> naraka$getDefaultNbt();
}
