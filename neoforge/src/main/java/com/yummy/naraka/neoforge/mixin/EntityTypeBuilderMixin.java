package com.yummy.naraka.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.Builder.class)
public abstract class EntityTypeBuilderMixin {
    @Unique
    private boolean naraka$keyIsNull = false;

    @Inject(method = "build", at = @At("HEAD"))
    public void onBuild(String key, CallbackInfoReturnable<EntityType<?>> cir) {
        this.naraka$keyIsNull = key == null;
    }

    @ModifyExpressionValue(method = "build", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/EntityType$Builder;serialize:Z"))
    public boolean avoidFetchChoiceType(boolean original) {
        if (naraka$keyIsNull)
            return false;
        return original;
    }
}
