package com.yummy.naraka.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class EntityEvents {
    public static final Event<LivingDeath> LIVING_DEATH = Event.create(listeners -> (entity, source) -> {
        boolean result = false;
        for (LivingDeath listener : listeners)
            result |= listener.die(entity, source);
        return result;
    });

    public static final Event<PlayerJoin> PLAYER_JOIN = Event.create(listeners -> player -> {
        for (PlayerJoin listener : listeners)
            listener.join(player);
    });

    /**
     * On LivingEntity hurt
     */
    public static final Event<LivingHurt> LIVING_HURT = Event.forPlatform(listeners -> (entity, source, amount) -> {
        for (LivingHurt listener : listeners)
            amount = listener.modifyDamage(entity, source, amount);
        return amount;
    });

    /**
     * On LivingEntity actually accept the damage
     */
    public static final Event<LivingDamage> LIVING_DAMAGE = Event.forPlatform(listeners -> (entity, source, amount) -> {
        for (LivingDamage listener : listeners)
            amount = listener.modifyDamage(entity, source, amount);
        return amount;
    });

    public static final Event<EquipmentChange> EQUIPMENT_CHANGE = Event.create(listeners -> (livingEntity, equipmentSlot, previousStack, currentStack) -> {
        for (EquipmentChange listener : listeners)
            listener.onEquipmentChange(livingEntity, equipmentSlot, previousStack, currentStack);
    });

    public static final EntityDataChangeEvent ENTITY_DATA_CHANGE = new EntityDataChangeEvent();

    @FunctionalInterface
    public interface LivingDeath {
        /**
         * Handle on entity die
         *
         * @param entity Entity going to die
         * @param source Damage source
         * @return True if accepting death
         */
        boolean die(LivingEntity entity, DamageSource source);
    }

    @FunctionalInterface
    public interface PlayerJoin {
        void join(ServerPlayer player);
    }

    @FunctionalInterface
    public interface LivingHurt {
        float modifyDamage(LivingEntity entity, DamageSource source, float amount);
    }

    @FunctionalInterface
    public interface LivingDamage {
        float modifyDamage(LivingEntity entity, DamageSource source, float amount);
    }

    @FunctionalInterface
    public interface EquipmentChange {
        void onEquipmentChange(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack);
    }
}
