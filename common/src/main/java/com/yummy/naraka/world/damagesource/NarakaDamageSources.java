package com.yummy.naraka.world.damagesource;

import com.yummy.naraka.world.entity.Spear;
import com.yummy.naraka.world.entity.SpearOfLonginus;
import com.yummy.naraka.world.entity.Stardust;
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

/**
 * Provides static methods for creating {@linkplain DamageSource} without level<br>
 * Don't use in client
 *
 * @author lalaalal
 */
public class NarakaDamageSources {
    @Nullable
    private static Registry<DamageType> registry;

    public static void initialize(RegistryAccess registryAccess) {
        registry = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    private static void ensureInitialized(@Nullable Registry<DamageType> registry) {
        if (registry == null)
            throw new IllegalStateException("NarakaDamageSources is not initialized");
    }

    public static DamageSource source(ResourceKey<DamageType> key) {
        ensureInitialized(registry);
        return new DamageSource(registry.getHolderOrThrow(key));
    }

    public static DamageSource source(ResourceKey<DamageType> key, Entity causingEntity) {
        ensureInitialized(registry);
        return new DamageSource(registry.getHolderOrThrow(key), causingEntity);
    }

    public static DamageSource source(ResourceKey<DamageType> key, Entity directEntity, @Nullable Entity causingEntity) {
        ensureInitialized(registry);
        return new DamageSource(registry.getHolderOrThrow(key), directEntity, causingEntity);
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
}
