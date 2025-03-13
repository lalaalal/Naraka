package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NarakaEntityUtils {
    private static final Map<UUID, Entity> CACHED_ENTITIES = new HashMap<>();

    @Nullable
    public static Entity findEntityBuUUID(ServerLevel serverLevel, UUID uuid) {
        if (CACHED_ENTITIES.containsKey(uuid))
            return CACHED_ENTITIES.get(uuid);
        return serverLevel.getEntity(uuid);
    }

    @Nullable
    public static <T> T findEntityByUUID(ServerLevel serverLevel, UUID uuid, Class<T> type) {
        Entity entity = findEntityBuUUID(serverLevel, uuid);
        if (entity != null)
            CACHED_ENTITIES.put(uuid, entity);
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

    public static void updatePositionForUpStep(Level level, Entity entity, Vec3 delta, double stepHeight) {
        BlockPos pos = NarakaUtils.pos(entity.position().add(delta.normalize().scale(0.5)));
        BlockState state = level.getBlockState(pos);
        VoxelShape shape = state.getCollisionShape(level, pos);
        double height = shape.max(Direction.Axis.Y);
        if (!shape.isEmpty() && height <= stepHeight && entity.getY() - entity.blockPosition().getY() < height)
            entity.setPos(entity.getX(), entity.getY() + height, entity.getZ());
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
