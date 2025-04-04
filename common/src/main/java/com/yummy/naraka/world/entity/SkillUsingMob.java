package com.yummy.naraka.world.entity;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAnimationPayload;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class SkillUsingMob extends PathfinderMob {
    protected final SkillManager skillManager = new SkillManager(random);
    protected final Map<String, AnimationController> animationStates = new HashMap<>();
    protected final Map<AnimationState, ResourceLocation> animationDefinitions = new HashMap<>();

    protected SkillUsingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        skillManager.runOnSkillStart(this::setAnimation);
        skillManager.runOnSkillEnd(skill -> setAnimation("idle"));
    }

    public boolean isUsingSkill() {
        return skillManager.getCurrentSkill() != null;
    }

    protected AnimationState animationState(ResourceLocation animation) {
        AnimationState animationState = new AnimationState();
        animationDefinitions.put(animationState, animation);
        return animationState;
    }

    public void forEachAnimations(BiConsumer<AnimationState, ResourceLocation> consumer) {
        animationDefinitions.forEach(consumer);
    }

    @Nullable
    public Skill<?> getCurrentSkill() {
        return skillManager.getCurrentSkill();
    }

    public void registerAnimation(String name, AnimationState... animationStates) {
        this.animationStates.put(name, AnimationController.of(random, animationStates));
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(S skill, AnimationState... animationStates) {
        this.skillManager.addSkill(skill);
        registerAnimation(skill.name, animationStates);

        return skill;
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(T mob, Function<T, S> factory, AnimationState... animationStates) {
        return registerSkill(factory.apply(mob), animationStates);
    }

    public float getAttackDamage() {
        AttributeInstance instance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (instance == null)
            return 1;
        return (float) instance.getValue();
    }

    public DamageSource getDefaultDamageSource() {
        return damageSources().mobAttack(this);
    }

    public void setAnimation(String name) {
        if (level() instanceof ServerLevel serverLevel) {
            SyncAnimationPayload payload = new SyncAnimationPayload(this, name);
            NetworkManager.sendToClient(serverLevel.players(), payload);
        }
    }

    private void setAnimation(Skill<?> skill) {
        this.setAnimation(skill.name);
    }

    public void updateAnimation(String animationName) {
        animationStates.forEach((name, animationController) -> {
            if (animationName.equals(name))
                animationController.start(tickCount);
            else
                animationController.stop();
        });
    }

    @Override
    protected void customServerAiStep() {
        skillManager.tick();
    }

    protected interface AnimationController {
        static AnimationController of(final RandomSource random, final AnimationState... animationStates) {
            if (animationStates.length == 0)
                return empty();
            if (animationStates.length == 1)
                return simple(animationStates[0]);
            return random(random, animationStates);
        }

        static AnimationController empty() {
            return new AnimationController() {
                @Override
                public void start(int tickCount) {
                }

                @Override
                public void stop() {
                }
            };
        }

        static AnimationController simple(final AnimationState animationState) {
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

        static AnimationController random(final RandomSource random, final AnimationState... animationStates) {
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
