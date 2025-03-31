package com.yummy.naraka.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.Builder.class)
public abstract class EntityTypeBuilderMixin<T extends Entity> {
    @Unique
    private boolean naraka$isNaraka = false;

    @Inject(method = "build", at = @At("HEAD"))
    public void check(String key, CallbackInfoReturnable<EntityType<T>> cir) {
        naraka$isNaraka = key.startsWith("naraka:");
    }

    @ModifyExpressionValue(method = "build", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntityType$Builder;serialize:Z"))
    public boolean ignoreNaraka(boolean original) {
        if (naraka$isNaraka)
            return false;
        return original;
    }
}
