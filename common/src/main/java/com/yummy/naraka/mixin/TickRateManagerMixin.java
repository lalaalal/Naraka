package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TickRateManager.class)
public abstract class TickRateManagerMixin {
    @ModifyReturnValue(method = "isEntityFrozen", at = @At("RETURN"))
    public boolean checkKeepUnfrozen(boolean original, @Local(argsOnly = true) Entity entity) {
        if (EntityDataHelper.getRawEntityData(entity, NarakaEntityDataTypes.KEEP_UNFROZEN.get()))
            return false;
        return original;
    }
}
