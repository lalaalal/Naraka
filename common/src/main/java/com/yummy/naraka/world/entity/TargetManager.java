package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TargetManager {
    private static final Codec<List<EntityReference<LivingEntity>>> TARGETS_CODEC = EntityReference.<LivingEntity>codec().listOf();

    private final List<EntityReference<LivingEntity>> targets = new ArrayList<>();
    private final Set<LivingEntity> cache = new HashSet<>();
    private final Set<LivingEntity> playerOrBosses = new HashSet<>();
    private int maxWatchedPlayerOrBosses = 0;

    public TargetManager() {

    }

    public static boolean isPlayerOrBoss(LivingEntity target) {
        return target.is(ConventionalTags.Entities.BOSSES) || target.is(EntityTypeIds.PLAYER);
    }

    public void tryAddTarget(@Nullable LivingEntity target) {
        if (target != null) {
            targets.add(EntityReference.of(target));
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
            LivingEntity target = reference.getEntity(level, LivingEntity.class);
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

    public void save(ValueOutput output) {
        output.store("Targets", TARGETS_CODEC, targets);
    }

    public void read(ValueInput input, Level level) {
        targets.clear();
        cache.clear();
        playerOrBosses.clear();
        input.read("Targets", TARGETS_CODEC).ifPresent(targets::addAll);
        targets.removeIf(target -> {
            LivingEntity livingEntity = target.getEntity(level, LivingEntity.class);
            if (livingEntity == null)
                return true;
            cache.add(livingEntity);
            if (isPlayerOrBoss(livingEntity))
                playerOrBosses.add(livingEntity);
            return false;
        });
    }
}
