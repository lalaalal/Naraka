package com.yummy.naraka.util;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncPlayerMovementPacket;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NarakaEntityUtils {
    @Nullable
    public static <T> T findEntityByUUID(Level level, UUID uuid, Class<T> type) {
        if (level instanceof ServerLevel serverLevel) {
            Entity entity = serverLevel.getEntity(uuid);
            if (type.isInstance(entity))
                return type.cast(entity);
        }
        return null;
    }

    public static <T> Collection<T> findEntitiesByUUID(ServerLevel serverLevel, Collection<UUID> uuids, Class<T> type) {
        return uuids.stream()
                .map(uuid -> findEntityByUUID(serverLevel, uuid, type))
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
        if (livingEntity instanceof Player player && livingEntity.isBlocking()) {
            InteractionHand hand = player.getUsedItemHand();
            ItemStack usedItem = player.getItemInHand(hand);
            EquipmentSlot slot = LivingEntity.getEquipmentSlotForItem(usedItem);
            usedItem.hurtAndBreak(damage, player, entity -> entity.broadcastBreakEvent(slot));
            player.getCooldowns().addCooldown(usedItem.getItem(), cooldown);
            player.stopUsingItem();
            player.level().broadcastEntityEvent(livingEntity, (byte) 30);
            return true;
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

    public static FallingBlockEntity createFloatingBlock(ServerLevel level, List<ServerPlayer> players, BlockPos pos, BlockState state, Vec3 movement) {
        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level, pos, state);
        fallingBlockEntity.disableDrop();
        fallingBlockEntity.setDeltaMovement(movement);
        ClientboundSetEntityMotionPacket packet = new ClientboundSetEntityMotionPacket(fallingBlockEntity);
        players.forEach(player -> player.connection.send(packet));
        return fallingBlockEntity;
    }

    public static void sendPlayerMovement(ServerPlayer player, Vec3 movement) {
        NetworkManager.clientbound().send(player, new SyncPlayerMovementPacket(movement));
    }
}
