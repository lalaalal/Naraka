package com.yummy.naraka;

import com.yummy.naraka.event.NarakaGameEventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Utils
 *
 * @author lalaalal
 */
public class NarakaUtil {
    private static final float EASE_OUT_SLOPE = 1.13837021f;

    private static MinecraftServer server;
    private static final Map<UUID, Entity> cache = new HashMap<>();

    /**
     * Store {@linkplain MinecraftServer}
     *
     * @param server Minecraft server
     * @see NarakaGameEventBus#onServerStarted(ServerStartedEvent)
     */
    public static void initialize(MinecraftServer server) {
        NarakaUtil.server = server;
    }

    /**
     * Get LocalPlayer if Minecraft instance exists
     * 
     * @return Local player
     */
    public static @Nullable LocalPlayer getClientPlayer() {
        try {
            return Minecraft.getInstance().player;
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public static void writeUUIDs(CompoundTag compoundTag, String name, Collection<UUID> uuids) {
        CompoundTag listTag = new CompoundTag();
        listTag.putInt("size", uuids.size());
        int index = 0;
        for (UUID uuid : uuids) {
            listTag.putUUID(String.valueOf(index), uuid);

            index += 1;
        }
        compoundTag.put(name, listTag);
    }

    public static @Nullable List<UUID> readUUIDs(CompoundTag compoundTag, String name) {
        if (!compoundTag.contains(name))
            return null;
        CompoundTag listTag = compoundTag.getCompound(name);
        int size = listTag.getInt("size");
        List<UUID> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            UUID uuid = listTag.getUUID(String.valueOf(index));
            list.add(uuid);
        }

        return list;
    }

    public static @Nullable Entity findEntityByUUID(ServerLevel serverLevel, UUID uuid) {
        if (cache.containsKey(uuid))
            return cache.get(uuid);
        for (Entity entity : serverLevel.getAllEntities()) {
            if (entity.getUUID().equals(uuid)) {
                cache.put(uuid, entity);
                return entity;
            }
        }
        return null;
    }

    public static @Nullable <T> T findEntityByUUID(ServerLevel serverLevel, UUID uuid, Class<T> type) {
        Entity entity = findEntityByUUID(serverLevel, uuid);
        if (type.isInstance(entity))
            return type.cast(entity);
        return null;
    }

    /**
     * Search entity using UUID from all levels
     *
     * @param uuid Entity UUID
     * @return Entity matching UUID null if absent
     */
    public static @Nullable Entity findEntityByUUID(UUID uuid) {
        for (ServerLevel serverLevel : server.getAllLevels()) {
            Entity entity = findEntityByUUID(serverLevel, uuid);
            if (entity != null)
                return entity;
        }
        return null;
    }

    /**
     * Search entity using UUID from all levels
     *
     * @param uuid Entity UUID
     * @param type Type to check
     * @param <T>  Type wanted
     * @return Cast to type if type is correct
     * @see NarakaUtil#findEntityByUUID(ServerLevel, UUID, Class)
     */
    public static @Nullable <T> T findEntityByUUID(UUID uuid, Class<T> type) {
        for (ServerLevel serverLevel : server.getAllLevels()) {
            T entity = findEntityByUUID(serverLevel, uuid, type);
            if (entity != null)
                return entity;
        }
        return null;
    }

    public static float easeInOut(float delta, float from, float to) {
        float range = to - from;
        float normal = (-Mth.cos(delta * Mth.PI) + 1) * 0.5f;
        return normal * range + from;
    }

    public static float easeIn(float delta, float from, float to) {
        float range = to - from;
        float normal = 1 - easeOut(1 - delta, 0, 1);
        return normal * range + from;
    }

    public static float easeOut(float delta, float from, float to) {
        float range = to - from;
        if (delta < 0.7419f)
            return EASE_OUT_SLOPE * delta * range + from;
        return easeInOut(delta, from, to);
    }

    public static float degree(float radian) {
        return radian * 57.29577951308232f;
    }

    public static float radian(float degree) {
        return degree * 0.017453292519943295f;
    }

    public static float wrapRadian(float radian) {
        float result = radian % Mth.TWO_PI;
        if (result >= Mth.PI) {
            result -= Mth.TWO_PI;
        }

        if (result < -Mth.PI) {
            result += Mth.TWO_PI;
        }

        return result;
    }

    public static PartPose addOffsetOnly(PartPose base, PartPose pose) {
        return PartPose.offsetAndRotation(
                pose.x + base.x,
                pose.y + base.y,
                pose.z + base.z,
                base.xRot, base.yRot, base.zRot
        );
    }

    public static PartPose rotationOnly(PartPose pose) {
        return PartPose.rotation(pose.xRot, pose.yRot, pose.zRot);
    }
}
