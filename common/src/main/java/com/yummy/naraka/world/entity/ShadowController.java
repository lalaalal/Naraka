package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.ai.skill.PunchSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ShadowController {
    private final Herobrine herobrine;
    private final List<UUID> shadowHerobrines = new ArrayList<>();
    @Nullable
    private ShadowHerobrine mainShadow;

    public ShadowController(Herobrine herobrine) {
        this.herobrine = herobrine;
    }

    public void save(CompoundTag tag) {
        NarakaNbtUtils.writeCollection(tag, "ShadowHerobrines", shadowHerobrines, NarakaEntityUtils::writeUUID, herobrine.registryAccess());
    }

    public void load(CompoundTag tag) {
        shadowHerobrines.clear();
        NarakaNbtUtils.readCollection(tag, "ShadowHerobrines", () -> shadowHerobrines, NarakaEntityUtils::readUUID, herobrine.registryAccess());
    }

    public void summonShadowHerobrine(ServerLevel level) {
        if (shadowHerobrines.size() >= NarakaConfig.COMMON.maxShadowHerobrineSpawn.getValue())
            return;
        ShadowHerobrine shadowHerobrine = new ShadowHerobrine(level, herobrine);
        BlockPos randomPos = NarakaUtils.randomBlockPos(herobrine.getRandom(), herobrine.blockPosition(), 4);
        BlockPos spawnPos = NarakaUtils.findAir(level, randomPos, Direction.UP);
        shadowHerobrine.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        getShadows(level)
                .stream().findAny()
                .ifPresent(existing -> shadowHerobrine.setHealth(existing.getHealth()));
        level.addFreshEntity(shadowHerobrine);
        shadowHerobrines.add(shadowHerobrine.getUUID());
        mainShadow = shadowHerobrine;
    }

    public void broadcastShadowHerobrineHurt(ServerLevel level, ShadowHerobrine shadowHerobrine) {
        for (ShadowHerobrine entity : getShadows(level)) {
            if (entity != shadowHerobrine)
                entity.setHealth(shadowHerobrine.getHealth());
        }
        SkillManager skillManager = herobrine.getSkillManager();
        if (shadowHerobrine.isDeadOrDying()) {
            shadowHerobrines.clear();
            skillManager.interrupt();
            if (herobrine.isHibernateMode())
                herobrine.stopHibernateMode(level);
            herobrine.startStaggering();
        }
    }

    public void switchWithShadowHerobrine(ServerLevel level) {
        if (herobrine.isDeadOrDying())
            return;
        herobrine.accumulatedHurtDamage = 0;
        herobrine.accumulatedDamageTickCount = 0;
        getShadows(level).stream()
                .findAny()
                .ifPresent(shadowHerobrine -> {
                    Vec3 originalHerobrinePosition = herobrine.position();
                    herobrine.setPos(shadowHerobrine.position());
                    shadowHerobrine.setPos(originalHerobrinePosition);
                    shadowHerobrine.playAnimation(AnimationLocations.IDLE, 40);
                });
    }

    public void killShadows(ServerLevel level) {
        getShadows(level).forEach(shadowHerobrine -> shadowHerobrine.kill(level));
    }

    public int getShadowCount() {
        return shadowHerobrines.size();
    }

    private void updateShadowsGoal(ServerLevel level) {
        for (ShadowHerobrine shadowHerobrine : getShadows(level)) {
            if (shadowHerobrine == mainShadow) {
                shadowHerobrine.stopAvoidTarget();
            } else {
                shadowHerobrine.startAvoidTarget();
            }
        }
    }

    private void updateShadowsSkill(ServerLevel level) {
        for (ShadowHerobrine shadowHerobrine : getShadows(level)) {
            Skill<?> punchSkill = shadowHerobrine.getSkillManager().getSkill(PunchSkill.LOCATION);
            if (punchSkill != null) {
                punchSkill.changeCooldown(getSkillCooldown(shadowHerobrine));
                punchSkill.setEnabled(shadowHerobrine == mainShadow);
            }
        }
    }

    private void resetShadowsSkill(ServerLevel level) {
        for (ShadowHerobrine shadowHerobrine : getShadows(level)) {
            Skill<?> punchSkill = shadowHerobrine.getSkillManager().getSkill(PunchSkill.LOCATION);
            if (punchSkill != null) {
                punchSkill.changeCooldown(PunchSkill.DEFAULT_COOLDOWN);
                punchSkill.setEnabled(true);
            }
        }
    }

    private int getSkillCooldown(ShadowHerobrine shadowHerobrine) {
        return (shadowHerobrine == mainShadow) ? 0 : PunchSkill.DEFAULT_COOLDOWN;
    }

    private void switchMainShadow(ServerLevel level) {
        shadowHerobrines.stream()
                .filter(uuid -> mainShadow == null || uuid != mainShadow.getUUID())
                .filter(uuid -> herobrine.getRandom().nextFloat() < 0.7f)
                .findAny()
                .map(uuid -> NarakaEntityUtils.findEntityByUUID(level, uuid, ShadowHerobrine.class))
                .ifPresent(shadowHerobrine -> mainShadow = shadowHerobrine);
    }

    public boolean someoneJustUsedSkill(ServerLevel level) {
        for (ShadowHerobrine shadowHerobrine : getShadows(level)) {
            Skill<?> skill = shadowHerobrine.getCurrentSkill();
            if (skill != null && skill.getCurrentTickCount() < 20)
                return true;
        }
        return false;
    }

    public void updateRolePlaying(ServerLevel level) {
        switchMainShadow(level);
        updateShadowsSkill(level);
        updateShadowsGoal(level);
    }

    public void stopRolePlaying(ServerLevel level) {
        mainShadow = null;
        resetShadowsSkill(level);
        for (ShadowHerobrine shadowHerobrine : getShadows(level))
            shadowHerobrine.stopAvoidTarget();
    }

    public Collection<ShadowHerobrine> getShadows(ServerLevel level) {
        return NarakaEntityUtils.findEntitiesByUUID(level, shadowHerobrines, ShadowHerobrine.class);
    }
}
