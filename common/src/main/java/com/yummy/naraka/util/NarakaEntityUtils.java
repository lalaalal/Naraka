package com.yummy.naraka.util;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncPlayerMovementPacket;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class NarakaEntityUtils {

    @Nullable
    public static <T> T findEntityByUUID(Level level, UUID uuid, Class<T> type) {
        Entity entity = level.getEntity(uuid);
        if (type.isInstance(entity))
            return type.cast(entity);
        return null;
    }

    public static <T> Collection<T> findEntitiesByUUID(Level level, Collection<UUID> uuids, Class<T> type) {
        return uuids.stream()
                .map(uuid -> findEntityByUUID(level, uuid, type))
                .filter(Objects::nonNull)
                .toList();
    }

    public static Vec3 getDirectionNormalVector(Entity from, Entity to) {
        return getDirectionNormalVector(from.position(), to.position());
    }

    public static Vec3 getDirectionNormalVector(Vec3 from, Vec3 to) {
        return to.subtract(from).normalize();
    }

    public static boolean disableAndHurtShield(LivingEntity livingEntity, int cooldown, int damage) {
        if (NarakaAttributeModifiers.hasAttributeModifier(livingEntity, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.STUN_PREVENT_MOVING))
            return false;
        if (livingEntity instanceof Player player && livingEntity.isBlocking() && livingEntity.level() instanceof ServerLevel level) {
            ItemStack usedItem = player.getItemBlockingWith();
            if (usedItem != null) {
                EquipmentSlot slot = player.getEquipmentSlotForItem(usedItem);
                usedItem.hurtAndBreak(damage, player, slot);
                BlocksAttacks blocksAttacks = usedItem.get(DataComponents.BLOCKS_ATTACKS);
                if (blocksAttacks != null) {
                    blocksAttacks.disable(level, player, cooldown / 20f, usedItem);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isDamageable(LivingEntity livingEntity) {
        if (livingEntity instanceof Player player)
            return isDamageablePlayer(player);
        return !livingEntity.isInvulnerable();
    }

    public static boolean isDamageablePlayer(Player player) {
        return !(player.isCreative() || player.isSpectator());
    }

    public static FallingBlockEntity createFloatingBlock(Level level, BlockPos pos, BlockState state, Vec3 movement) {
        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
        fallingBlockEntity.disableDrop();
        fallingBlockEntity.setDeltaMovement(movement);
        return fallingBlockEntity;
    }

    public static void sendPlayerMovement(ServerPlayer player, Vec3 movement) {
        NetworkManager.clientbound().send(player, new SyncPlayerMovementPacket(movement));
    }
}
