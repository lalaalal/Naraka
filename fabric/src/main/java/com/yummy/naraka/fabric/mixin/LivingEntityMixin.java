package com.yummy.naraka.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.event.EntityEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "HEAD"), argsOnly = true, name = "dmg")
    public float handleHurtEvent(float dmg, @Local(argsOnly = true, name = "source") DamageSource source) {
        return EntityEvents.LIVING_HURT.invoker().modifyDamage(naraka$self(), source, dmg);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getCombatTracker()Lnet/minecraft/world/damagesource/CombatTracker;"), argsOnly = true, name = "dmg")
    public float handleDamageEvent(float dmg, @Local(argsOnly = true, name = "source") DamageSource source) {
        return EntityEvents.LIVING_DAMAGE.invoker().modifyDamage(naraka$self(), source, dmg);
    }

    @Unique
    private LivingEntity naraka$self() {
        return (LivingEntity) (Object) this;
    }
}
