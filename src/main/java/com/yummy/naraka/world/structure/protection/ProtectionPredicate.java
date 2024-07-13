package com.yummy.naraka.world.structure.protection;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public interface ProtectionPredicate {
    boolean shouldProtect(BoundingBox box, Vec3i pos);
}
