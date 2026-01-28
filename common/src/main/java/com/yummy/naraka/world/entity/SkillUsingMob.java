package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAnimationPacket;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Function;

public abstract class SkillUsingMob extends PathfinderMob {
    protected final SkillManager skillManager = new SkillManager(this);
    protected final Map<ResourceLocation, AnimationSelector> animationSelectors = new HashMap<>();
    protected final Map<ResourceLocation, AnimationState> animations = new HashMap<>();
    protected ResourceLocation currentAnimation = NarakaMod.location("empty");

    protected int animationTickLeft = Integer.MIN_VALUE;
    protected Runnable animationTickListener = () -> {
    };
    protected final List<ServerPlayer> players = new ArrayList<>();

    protected SkillUsingMob(EntityType<? extends SkillUsingMob> entityType, Level level) {
        super(entityType, level);

        skillManager.runOnSkillStart(this::setAnimation);
    }

    public boolean isUsingSkill() {
        return skillManager.getCurrentSkill() != null;
    }

    public AnimationState getAnimationState(ResourceLocation animationLocation) {
        if (animations.containsKey(animationLocation))
            return animations.get(animationLocation);
        return new AnimationState();
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Set<ResourceLocation> getAnimations() {
        return animations.keySet();
    }

    public void useSkill(ResourceLocation location) {
        Skill<?> skill = skillManager.getSkill(location);
        if (skill != null)
            skillManager.setCurrentSkillIfAbsence(skill);
    }

    public Optional<Skill<?>> getCurrentSkill() {
        return Optional.ofNullable(skillManager.getCurrentSkill());
    }

    public void registerAnimation(ResourceLocation animationSetIdentifiers, List<ResourceLocation> animationIdentifiers) {
        this.animationSelectors.put(animationSetIdentifiers, AnimationSelector.of(random, animationIdentifiers));
        for (ResourceLocation animationId : animationIdentifiers)
            animations.put(animationId, new AnimationState());
    }

    public void registerAnimation(ResourceLocation animationIdentifier) {
        this.animationSelectors.put(animationIdentifier, AnimationSelector.simple(animationIdentifier));
        this.animations.put(animationIdentifier, new AnimationState());
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

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        players.add(serverPlayer);
        NetworkManager.clientbound().send(serverPlayer, new SyncAnimationPacket(this, currentAnimation));
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        players.remove(serverPlayer);
    }

    public List<ServerPlayer> players() {
        return players;
    }

    /**
     * For server update
     *
     * @param animationSetIdentifier Animation
     */
    public void setAnimation(ResourceLocation animationSetIdentifier) {
        NarakaMod.LOGGER.debug("{} : Setting animation {}", this, animationSetIdentifier);
        if (!isPlayingStaticAnimation()) {
            currentAnimation = animationSelectors.getOrDefault(animationSetIdentifier, AnimationSelector.EMPTY)
                    .select();
            if (level() instanceof ServerLevel serverLevel) {
                NarakaMod.LOGGER.debug("{} : Selecting animation ({}) from animation set ({})", this, currentAnimation, animationSetIdentifier);
                NarakaMod.LOGGER.debug("{} : Sending animation ({}) sync packet", this, currentAnimation);
                SyncAnimationPacket payload = new SyncAnimationPacket(this, currentAnimation);
                NetworkManager.clientbound().send(serverLevel.players(), payload);
            }
        }
    }

    private void setAnimation(Skill<?> skill) {
        this.setAnimation(skill.location);
    }

    /**
     * For client update
     *
     * @param animationIdentifier Animation
     */
    public void updateAnimation(ResourceLocation animationIdentifier) {
        NarakaMod.LOGGER.debug("{} : Updating animation {}", this, animationIdentifier);

        if (!animations.containsKey(animationIdentifier))
            NarakaMod.LOGGER.debug("{} is not registered animation for {}", animationIdentifier, this);
        animations.forEach((identifier, state) -> {
            if (animationIdentifier.equals(identifier))
                state.start(tickCount);
            else
                state.stop();
        });

        currentAnimation = animationIdentifier;
    }

    public ResourceLocation getCurrentAnimation() {
        return currentAnimation;
    }

    private void updateAnimationTick() {
        if (animationTickLeft == 0)
            stopStaticAnimation();
        if (animationTickLeft >= 0) {
            animationTickListener.run();
            animationTickLeft -= 1;
        }
    }

    public boolean isPlayingStaticAnimation() {
        return animationTickLeft > 0;
    }

    public void playStaticAnimation(ResourceLocation animation, int duration) {
        playStaticAnimation(animation, duration, true);
    }

    public void playStaticAnimation(ResourceLocation animation, int duration, boolean interruptSkill) {
        playStaticAnimation(animation, duration, interruptSkill, false);
    }

    public void playStaticAnimation(ResourceLocation animation, int duration, boolean interruptSkill, boolean force) {
        if (force)
            stopStaticAnimation();
        if (animationTickLeft > 0)
            return;
        setAnimation(animation);
        animationTickLeft = Math.max(1, duration);
        skillManager.pause(interruptSkill);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.ANIMATION_PREVENT_MOVING);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.FLYING_SPEED, NarakaAttributeModifiers.ANIMATION_PREVENT_MOVING);
    }

    public void stopStaticAnimation() {
        if (animationTickLeft < 0)
            return;
        animationTickLeft = Integer.MIN_VALUE;
        animationTickListener = () -> {
        };
        skillManager.resume();
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.ANIMATION_PREVENT_MOVING);
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.FLYING_SPEED, NarakaAttributeModifiers.ANIMATION_PREVENT_MOVING);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        skillManager.interrupt();
    }

    @Override
    protected void customServerAiStep() {
        updateAnimationTick();
        if (level() instanceof ServerLevel level)
            skillManager.tick(level);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag output) {
        super.addAdditionalSaveData(output);
        getCurrentSkill().ifPresent(skill -> {
            NarakaNbtUtils.store(output, "CurrentSkill", ResourceLocation.CODEC, skill.location);
        });
    }

    @Override
    public void readAdditionalSaveData(CompoundTag input) {
        super.readAdditionalSaveData(input);
        NarakaNbtUtils.read(input, "CurrentSkill", ResourceLocation.CODEC).ifPresent(
                this::useSkill
        );
    }

    @FunctionalInterface
    protected interface AnimationSelector {
        AnimationSelector EMPTY = () -> NarakaMod.location("animation", "empty");

        static AnimationSelector of(final RandomSource random, final List<ResourceLocation> animationLocations) {
            if (animationLocations.isEmpty())
                return EMPTY;
            if (animationLocations.size() == 1)
                return simple(animationLocations.getFirst());
            return random(random, animationLocations);
        }

        static AnimationSelector simple(final ResourceLocation animationLocation) {
            return () -> animationLocation;
        }

        static AnimationSelector random(final RandomSource random, final List<ResourceLocation> animationLocations) {
            return () -> {
                int randomIndex = random.nextInt(animationLocations.size());
                return animationLocations.get(randomIndex);
            };
        }

        ResourceLocation select();
    }
}
