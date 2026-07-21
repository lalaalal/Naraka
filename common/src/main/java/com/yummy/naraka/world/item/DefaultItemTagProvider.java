package com.yummy.naraka.world.item;

import net.minecraft.nbt.CompoundTag;

import java.util.Optional;

public interface DefaultItemTagProvider {
    Optional<CompoundTag> naraka$getDefaultTag();
}
