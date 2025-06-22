package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.ai.skill.FlickerSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class ShadowController {
    private final Herobrine herobrine;
    private final List<UUID> shadowHerobrines = new ArrayList<>();
    private int flickerStack = 0;

    public ShadowController(Herobrine herobrine) {
        this.herobrine = herobrine;
    }

    public void save(CompoundTag tag) {
        NarakaNbtUtils.writeCollection(tag, "ShadowHerobrines", shadowHerobrines, NarakaNbtUtils::writeUUID, herobrine.registryAccess());
    }

    public void load(CompoundTag tag) {
        shadowHerobrines.clear();
        NarakaNbtUtils.readCollection(tag, "ShadowHerobrines", () -> shadowHerobrines, NarakaNbtUtils::readUUID, herobrine.registryAccess());
    }

    public void summonShadowHerobrine(ServerLevel level) {
        if (shadowHerobrines.size() >= NarakaConfig.COMMON.maxShadowHerobrineSpawn.getValue())
            return;
        ShadowHerobrine shadowHerobrine = new ShadowHerobrine(level, herobrine);
        shadowHerobrine.setPos(herobrine.position());
        getShadows(level)
                .stream().findAny()
                .ifPresent(existing -> shadowHerobrine.setHealth(existing.getHealth()));
        level.addFreshEntity(shadowHerobrine);
        shadowHerobrines.add(shadowHerobrine.getUUID());
        if (herobrine.isHibernateMode())
            shadowHerobrine.useFlicker();
        shadowHerobrine.playStaticAnimation(AnimationLocations.SHADOW_SUMMONED, 80, true);
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
            if (herobrine.isHibernateMode()) {
                herobrine.stopHibernateMode(level);
                herobrine.startStaggering(AnimationLocations.STIGMATIZE_ENTITIES_END, 100, -1);
            } else {
                herobrine.startStaggering();
            }
        }
    }

    public void increaseFlickerStack() {
        flickerStack += 1;
    }

    public void increaseFlickerStack(int amount) {
        flickerStack += amount;
    }

    public void consumeFlickerStack(ServerLevel level) {
        consumeFlickerStack(level, List.of());
    }

    public void consumeFlickerStack(ServerLevel level, List<ShadowHerobrine> excludes) {
        if (flickerStack > 0) {
            Optional<ShadowHerobrine> shadowHerobrine = selectShadowHerobrine(level, excludes);
            shadowHerobrine.ifPresent(shadow -> {
                if (!shadow.isPlayingStaticAnimation()) {
                    shadow.useSkill(FlickerSkill.LOCATION);
                    flickerStack -= 1;
                }
            });
        }
    }

    public Optional<ShadowHerobrine> selectShadowHerobrine(ServerLevel level, List<ShadowHerobrine> excludes) {
        ShadowHerobrine[] shadows = getShadows(level).stream()
                .filter(shadowHerobrine -> !excludes.contains(shadowHerobrine))
                .toArray(ShadowHerobrine[]::new);
        if (shadows.length == 0)
            return Optional.empty();
        int randomIndex = level.random.nextInt(shadows.length);

        return Optional.of(shadows[randomIndex]);
    }

    public void killShadows(ServerLevel level) {
        getShadows(level).forEach(shadowHerobrine -> shadowHerobrine.kill(level));
    }

    public int getShadowCount() {
        return shadowHerobrines.size();
    }

    public boolean someoneJustUsedSkill(ServerLevel level) {
        for (ShadowHerobrine shadowHerobrine : getShadows(level)) {
            Optional<Skill<?>> skill = shadowHerobrine.getCurrentSkill();
            if (skill.isPresent() && skill.get().getCurrentTickCount() < 20)
                return true;
        }
        return false;
    }

    public void activateFlickerSkill(ServerLevel level) {
        getShadows(level).forEach(ShadowHerobrine::useFlicker);
    }

    public void deactivateFlickerSkill(ServerLevel level) {
        getShadows(level).forEach(ShadowHerobrine::usePunchOnly);
    }

    public Collection<ShadowHerobrine> getShadows(ServerLevel level) {
        return NarakaEntityUtils.findEntitiesByUUID(level, shadowHerobrines, ShadowHerobrine.class);
    }
}
