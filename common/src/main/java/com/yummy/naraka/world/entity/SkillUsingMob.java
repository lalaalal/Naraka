package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SkillUsingMob extends PathfinderMob implements AfterimageEntity {
    public static final EntityDataAccessor<List<Afterimage>> AFTERIMAGES = SynchedEntityData.defineId(SkillUsingMob.class, NarakaEntityDataSerializers.AFTERIMAGES);
    public static final EntityDataAccessor<String> CURRENT_SKILL = SynchedEntityData.defineId(SkillUsingMob.class, EntityDataSerializers.STRING);

    protected final SkillManager skillManager = new SkillManager(random);
    protected final Map<String, AnimationController> animationStates = new HashMap<>();

    protected SkillUsingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        skillManager.runOnSkillStart(skill -> entityData.set(CURRENT_SKILL, skill.name));
        skillManager.runOnSkillEnd(skill -> entityData.set(CURRENT_SKILL, "idle"));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AFTERIMAGES, new ArrayList<>())
                .define(CURRENT_SKILL, "idle");
    }

    public boolean isUsingSkill() {
        return skillManager.getCurrentSkill() != null;
    }

    @Nullable
    public Skill getCurrentSkill() {
        return skillManager.getCurrentSkill();
    }

    public void registerSkill(Skill skill, AnimationState animationState) {
        skillManager.addSkill(skill);
        animationStates.put(skill.name, AnimationController.simple(animationState));
    }

    public void registerSkill(Skill skill, AnimationState... animationStates) {
        this.skillManager.addSkill(skill);
        this.animationStates.put(skill.name, AnimationController.random(random, animationStates));
    }

    public double getAttackDamage() {
        AttributeInstance instance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (instance == null)
            return 1;
        return instance.getValue();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (level().isClientSide && dataAccessor == CURRENT_SKILL) {
            String currentSkillName = entityData.get(CURRENT_SKILL);
            animationStates.forEach((name, animationController) -> {
                if (currentSkillName.equals(name))
                    animationController.start(tickCount);
                else
                    animationController.stop();
            });
        }
    }

    @Override
    protected void customServerAiStep() {
        skillManager.tick();
    }

    @Override
    public void tick() {
        super.tick();
        List<Afterimage> afterimages = new ArrayList<>(getAfterimages());
        afterimages.removeIf(Afterimage::tick);
        this.entityData.set(AFTERIMAGES, afterimages);
    }

    @Override
    public void createAfterimage() {
        List<Afterimage> afterimages = new ArrayList<>(getAfterimages());
        if (!level().isClientSide) {
            afterimages.add(new Afterimage(this.position(), 12));
            this.entityData.set(AFTERIMAGES, afterimages, true);
        }
    }

    @Override
    public List<Afterimage> getAfterimages() {
        return this.entityData.get(AFTERIMAGES);
    }

    public interface AnimationController {
        static AnimationController simple(AnimationState animationState) {
            return new AnimationController() {
                @Override
                public void start(int tickCount) {
                    animationState.start(tickCount);
                }

                @Override
                public void stop() {
                    animationState.stop();
                }
            };
        }

        static AnimationController random(RandomSource random, AnimationState... animationStates) {
            return new AnimationController() {
                @Override
                public void start(int tickCount) {
                    int selected = random.nextInt(animationStates.length);
                    for (int index = 0; index < animationStates.length; index++) {
                        if (index == selected)
                            animationStates[index].start(tickCount);
                        else
                            animationStates[index].stop();
                    }
                }

                @Override
                public void stop() {
                    for (AnimationState animationState : animationStates)
                        animationState.stop();
                }
            };
        }

        void start(int tickCount);

        void stop();
    }
}
