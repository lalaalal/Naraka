package com.yummy.naraka.world.damagesource;

import com.yummy.naraka.world.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

public class NarakaDamageSources {
    public static DamageSource source(ResourceKey<DamageType> key, RegistryAccess registryAccess) {
        Registry<DamageType> damageTypes = registryAccess.lookupOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(damageTypes.getOrThrow(key));
    }

    public static DamageSource source(ResourceKey<DamageType> key, Entity causingEntity) {
        Registry<DamageType> damageTypes = causingEntity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(damageTypes.getOrThrow(key), causingEntity);
    }

    public static DamageSource source(ResourceKey<DamageType> key, Entity directEntity, @Nullable Entity causingEntity) {
        Registry<DamageType> damageTypes = directEntity.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE);
        return new DamageSource(damageTypes.getOrThrow(key), directEntity, causingEntity);
    }

    public static DamageSource fixed(Entity causingEntity) {
        return source(NarakaDamageTypes.MOB_ATTACK_FIXED, causingEntity);
    }

    public static DamageSource projectileFixed(Projectile projectile, @Nullable Entity cause) {
        return source(NarakaDamageTypes.PROJECTILE_FIXED, projectile, cause);
    }

    public static DamageSource stigma(Entity causingEntity) {
        return source(NarakaDamageTypes.STIGMA, causingEntity);
    }

    public static DamageSource stigmaConsume(Entity causingEntity) {
        return source(NarakaDamageTypes.STIGMA_CONSUME, causingEntity);
    }

    public static DamageSource spear(Spear spear) {
        Entity owner = spear.getOwner();
        if (owner == null)
            return source(NarakaDamageTypes.SPEAR, spear);
        return source(NarakaDamageTypes.SPEAR, spear, spear.getOwner());
    }

    public static DamageSource spear(LivingEntity livingEntity) {
        return source(NarakaDamageTypes.SPEAR, livingEntity);
    }

    public static DamageSource longinus(SpearOfLonginus spearOfLonginus) {
        Entity owner = spearOfLonginus.getOwner();
        if (owner == null)
            return source(NarakaDamageTypes.SPEAR_OF_LONGINUS, spearOfLonginus);
        return source(NarakaDamageTypes.SPEAR_OF_LONGINUS, spearOfLonginus, spearOfLonginus.getOwner());
    }

    public static DamageSource longinus(LivingEntity livingEntity) {
        return source(NarakaDamageTypes.SPEAR_OF_LONGINUS, livingEntity);
    }

    public static DamageSource stardust(Stardust stardust) {
        return source(NarakaDamageTypes.STARDUST, stardust, stardust.getOwner());
    }

    public static DamageSource narakaFireball(NarakaFireball fireball) {
        return source(NarakaDamageTypes.NARAKA_FIREBALL, fireball, fireball.getOwner());
    }

    public static DamageSource pickaxeSlash(PickaxeSlash pickaxeSlash) {
        return source(NarakaDamageTypes.PICKAXE_SLASH, pickaxeSlash, pickaxeSlash.getOwner());
    }

    public static DamageSource purifiedSoulFire(RegistryAccess registryAccess) {
        return source(NarakaDamageTypes.PURIFIED_SOUL_FIRE, registryAccess);
    }
}
