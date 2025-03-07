package com.yummy.naraka.world.entity;

import com.yummy.naraka.network.SyncAnimationPayload;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import dev.architectury.networking.NetworkManager;
import net.minecraft.server.level.ServerLevel;
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

public abstract class SkillUsingMob extends PathfinderMob {
    protected final SkillManager skillManager = new SkillManager(random);
    protected final Map<String, AnimationController> animationStates = new HashMap<>();
    protected final List<Afterimage> afterimages = new ArrayList<>();

    protected SkillUsingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        skillManager.runOnSkillStart(this::setAnimation);
        skillManager.runOnSkillEnd(skill -> setAnimation("idle"));
    }

    public boolean isUsingSkill() {
        return skillManager.getCurrentSkill() != null;
    }

    @Nullable
    public Skill<?> getCurrentSkill() {
        return skillManager.getCurrentSkill();
    }

    public void registerSkill(Skill<?> skill, AnimationState... animationStates) {
        this.skillManager.addSkill(skill);
        this.animationStates.put(skill.name, AnimationController.of(random, animationStates));
    }

    @Override
    public void tick() {
        super.tick();
        afterimages.removeIf(Afterimage::tick);
    }

    public double getAttackDamage() {
        AttributeInstance instance = getAttribute(Attributes.ATTACK_DAMAGE);
        if (instance == null)
            return 1;
        return instance.getValue();
    }

    protected void setAnimation(String name) {
        if (level() instanceof ServerLevel serverLevel) {
            SyncAnimationPayload payload = new SyncAnimationPayload(this, name);
            NetworkManager.sendToPlayers(serverLevel.players(), payload);
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

    public interface AnimationController {
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
