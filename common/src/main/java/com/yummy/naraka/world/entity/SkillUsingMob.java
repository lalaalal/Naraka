package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class SkillUsingMob extends PathfinderMob {
    protected final SkillManager skillManager = new SkillManager(random);
    protected final Map<ResourceLocation, AnimationController> animationStates = new HashMap<>();
    protected ResourceLocation currentAnimation = NarakaMod.location("empty");

    protected SkillUsingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        skillManager.runOnSkillStart(this::setAnimation);
    }

    public boolean isUsingSkill() {
        return skillManager.getCurrentSkill() != null;
    }

    public void forEachAnimations(BiConsumer<ResourceLocation, AnimationState> consumer) {
        for (AnimationController controller : animationStates.values())
            controller.update(consumer);
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Set<ResourceLocation> getAnimations() {
        return animationStates.keySet();
    }

    public void useSkill(ResourceLocation location) {
        Skill<?> skill = skillManager.getSkill(location);
        if (skill != null)
            skillManager.setCurrentSkillIfAbsence(skill);
    }

    @Nullable
    public Skill<?> getCurrentSkill() {
        return skillManager.getCurrentSkill();
    }

    public void registerAnimation(ResourceLocation animationSetLocation, List<ResourceLocation> animationLocations) {
        this.animationStates.put(animationSetLocation, AnimationController.of(random, animationLocations));
    }

    public void registerAnimation(ResourceLocation animationLocation) {
        this.animationStates.put(animationLocation, AnimationController.simple(animationLocation));
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(int priority, S skill, ResourceLocation... animationLocations) {
        this.skillManager.addSkill(priority, skill);
        registerAnimation(skill.location, List.of(animationLocations));

        return skill;
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(S skill, ResourceLocation... animationLocations) {
        return registerSkill(Integer.MAX_VALUE, skill, animationLocations);
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(int priority, T mob, Function<T, S> factory, ResourceLocation... animationLocations) {
        return registerSkill(priority, factory.apply(mob), animationLocations);
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(T mob, Function<T, S> factory, ResourceLocation... animationLocations) {
        return registerSkill(Integer.MAX_VALUE, factory.apply(mob), animationLocations);
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

    /**
     * For server update
     *
     * @param animationLocation Animation
     */
    public void setAnimation(ResourceLocation animationLocation) {
        currentAnimation = animationLocation;
        if (level() instanceof ServerLevel serverLevel) {
            SyncAnimationPayload payload = new SyncAnimationPayload(this, animationLocation);
            NetworkManager.sendToClient(serverLevel.players(), payload);
        }
    }

    private void setAnimation(Skill<?> skill) {
        this.setAnimation(skill.location);
    }

    /**
     * For client update
     *
     * @param animationLocation Animation
     */
    public void updateAnimation(ResourceLocation animationLocation) {
        animationStates.forEach((location, animationController) -> {
            if (animationLocation.equals(location))
                animationController.start(tickCount);
            else
                animationController.stop();
        });
        currentAnimation = animationLocation;
    }

    public ResourceLocation getCurrentAnimation() {
        return currentAnimation;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        skillManager.tick(level);
    }

    protected static abstract class AnimationController {
        private static final AnimationController EMPTY = new AnimationController(List.of()) {
            @Override
            protected ResourceLocation select() {
                return NarakaMod.location("empty");
            }
        };

        static AnimationController of(final RandomSource random, final List<ResourceLocation> animationLocations) {
            if (animationLocations.isEmpty())
                return EMPTY;
            if (animationLocations.size() == 1)
                return simple(animationLocations.getFirst());
            return random(random, animationLocations);
        }

        static AnimationController simple(final ResourceLocation animationLocation) {
            return new AnimationController(List.of(animationLocation)) {
                @Override
                protected ResourceLocation select() {
                    return animationLocation;
                }
            };
        }

        static AnimationController random(final RandomSource random, final List<ResourceLocation> animationLocations) {
            return new AnimationController(animationLocations) {
                @Override
                protected ResourceLocation select() {
                    int randomIndex = random.nextInt(animationLocations.size());
                    return animationLocations.get(randomIndex);
                }
            };
        }

        protected final Map<ResourceLocation, AnimationState> animationStates;

        public AnimationController(List<ResourceLocation> animationLocations) {
            this.animationStates = new HashMap<>();
            for (ResourceLocation animationLocation : animationLocations)
                animationStates.put(animationLocation, new AnimationState());
        }

        protected abstract ResourceLocation select();

        public void start(int tickCount) {
            final ResourceLocation selected = select();
            animationStates.forEach((animationLocation, animationState) -> {
                if (animationLocation.equals(selected))
                    animationState.start(tickCount);
                else
                    animationState.stop();
            });
        }

        public void stop() {
            for (AnimationState animationState : animationStates.values())
                animationState.stop();
        }

        public void update(BiConsumer<ResourceLocation, AnimationState> consumer) {
            animationStates.forEach(consumer);
        }
    }
}
