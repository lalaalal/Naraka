package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;

public class TargetManager {
    private static final Codec<List<UUID>> TARGETS_CODEC = UUIDUtil.CODEC.listOf();

    private final List<UUID> targets = new ArrayList<>();
    private final Set<LivingEntity> cache = new HashSet<>();
    private final Set<LivingEntity> playerOrBosses = new HashSet<>();
    private int maxWatchedPlayerOrBosses = 0;

    public TargetManager() {

    }

    public static boolean isPlayerOrBoss(LivingEntity target) {
        return target.getType().is(ConventionalTags.Entities.BOSSES) || target instanceof Player;
    }

    public void tryAddTarget(@Nullable LivingEntity target) {
        if (target != null) {
            targets.add(target.getUUID());
            cache.add(target);
            if (isPlayerOrBoss(target)) {
                playerOrBosses.add(target);
                maxWatchedPlayerOrBosses = Math.max(playerOrBosses.size(), maxWatchedPlayerOrBosses);
            }
        }
    }

    public Set<LivingEntity> getAllTargets() {
        return cache;
    }

    public Set<LivingEntity> getPlayerOrBosses() {
        return playerOrBosses;
    }

    private boolean isInvalidTarget(LivingEntity target) {
        return target.isDeadOrDying() || !NarakaEntityUtils.isDamageable(target);
    }

    public void update(Level level) {
        targets.removeIf(reference -> {
            LivingEntity target = NarakaEntityUtils.findEntityByUUID(level, reference, LivingEntity.class);
            if (target == null)
                return true;
            return isInvalidTarget(target);
        });
        cache.removeIf(this::isInvalidTarget);
        playerOrBosses.removeIf(this::isInvalidTarget);
    }

    public boolean allTargetsAreDisappeared() {
        return maxWatchedPlayerOrBosses > 0 && playerOrBosses.isEmpty();
    }

    public void save(CompoundTag output) {
        NarakaNbtUtils.store(output, "Targets", TARGETS_CODEC, targets);
    }

    public void read(CompoundTag input, Level level) {
        targets.clear();
        cache.clear();
        playerOrBosses.clear();
        NarakaNbtUtils.read(input, "Targets", TARGETS_CODEC).ifPresent(targets::addAll);
        targets.removeIf(target -> {
            LivingEntity livingEntity = NarakaEntityUtils.findEntityByUUID(level, target, LivingEntity.class);
            if (livingEntity == null)
                return true;
            cache.add(livingEntity);
            if (isPlayerOrBoss(livingEntity))
                playerOrBosses.add(livingEntity);
            return false;
        });
    }
}
