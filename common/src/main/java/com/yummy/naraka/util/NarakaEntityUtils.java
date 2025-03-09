package com.yummy.naraka.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class NarakaEntityUtils {
    @Nullable
    public static <T> T findEntityByUUID(ServerLevel serverLevel, UUID uuid, Class<T> type) {
        Entity entity = serverLevel.getEntity(uuid);
        if (type.isInstance(entity))
            return type.cast(entity);
        return null;
    }

    public static Vec3 getDirectionNormalVector(Entity from, Entity to) {
        return getDirectionNormalVector(from.position(), to.position());
    }

    public static Vec3 getDirectionNormalVector(Vec3 from, Vec3 to) {
        return to.subtract(from).normalize();
    }

    public static boolean disableAndHurtShield(LivingEntity livingEntity, int cooldown, int damage) {
        if (livingEntity instanceof Player player && livingEntity.isBlocking()) {
            player.getCooldowns().addCooldown(Items.SHIELD, cooldown);
            player.stopUsingItem();
            player.level().broadcastEntityEvent(livingEntity, (byte) 30);

            InteractionHand hand = player.getUsedItemHand();
            ItemStack shield = player.getItemInHand(hand);
            EquipmentSlot slot = player.getEquipmentSlotForItem(shield);
            shield.hurtAndBreak(damage, player, slot);

            return true;
        }

        return false;
    }
}
