package com.yummy.naraka.damagesource;

import com.yummy.naraka.entity.Spear;
import com.yummy.naraka.entity.SpearOfLonginus;
import com.yummy.naraka.event.NarakaGameEventBus;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import javax.annotation.Nullable;

/**
 * Provides static methods for creating {@linkplain DamageSource} without level<br>
 * Don't use in client
 *
 * @author lalaalal
 */
public class NarakaDamageSources {
    private static Registry<DamageType> registry;

    /**
     * Required to access {@linkplain Registries#DAMAGE_TYPE}<br>
     * Initialized in {@linkplain NarakaGameEventBus#onServerStarted(ServerStartedEvent)}
     *
     * @param registryAccess Registry access
     * @see DamageSources
     * @see NarakaGameEventBus#onServerStarted(ServerStartedEvent)
     */
    public static void initialize(RegistryAccess registryAccess) {
        registry = registryAccess.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    private static void ensureInitialized() {
        if (registry == null)
            throw new IllegalStateException("Not initialized");
    }

    public static DamageSource source(ResourceKey<DamageType> key) {
        ensureInitialized();
        return new DamageSource(registry.getHolderOrThrow(key));
    }

    public static DamageSource source(ResourceKey<DamageType> key, @Nullable Entity causingEntity) {
        ensureInitialized();
        return new DamageSource(registry.getHolderOrThrow(key), causingEntity);
    }

    public static DamageSource source(ResourceKey<DamageType> key, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
        ensureInitialized();
        return new DamageSource(registry.getHolderOrThrow(key), directEntity, causingEntity);
    }

    public static DamageSource stigma(@Nullable Entity causingEntity) {
        return source(NarakaDamageTypes.STIGMA, causingEntity);
    }

    public static DamageSource deathCountZero(@Nullable Entity causingEntity) {
        return source(NarakaDamageTypes.DEATH_COUNT_ZERO, causingEntity);
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
}
