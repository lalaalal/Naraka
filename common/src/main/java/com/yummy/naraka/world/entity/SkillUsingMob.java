package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAnimationPacket;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.resources.Identifier;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.*;
import java.util.function.Function;

public abstract class SkillUsingMob extends PathfinderMob {
    protected final SkillManager skillManager = new SkillManager(this);
    protected final Map<Identifier, AnimationSelector> animationSelectors = new HashMap<>();
    protected final Map<Identifier, AnimationState> animations = new HashMap<>();
    protected Identifier currentAnimation = NarakaMod.identifier("empty");

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

    public AnimationState getAnimationState(Identifier animationIdentifier) {
        if (animations.containsKey(animationIdentifier))
            return animations.get(animationIdentifier);
        return new AnimationState();
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Set<Identifier> getAnimations() {
        return animations.keySet();
    }

    public void useSkill(Identifier location) {
        Skill<?> skill = skillManager.getSkill(location);
        if (skill != null)
            skillManager.setCurrentSkillIfAbsence(skill);
    }

    public Optional<Skill<?>> getCurrentSkill() {
        return Optional.ofNullable(skillManager.getCurrentSkill());
    }

    public void registerAnimation(Identifier animationSetIdentifiers, List<Identifier> animationIdentifiers) {
        this.animationSelectors.put(animationSetIdentifiers, AnimationSelector.of(random, animationIdentifiers));
        for (Identifier animationId : animationIdentifiers)
            animations.put(animationId, new AnimationState());
    }

    public void registerAnimation(Identifier animationIdentifier) {
        this.animationSelectors.put(animationIdentifier, AnimationSelector.simple(animationIdentifier));
        this.animations.put(animationIdentifier, new AnimationState());
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(int priority, S skill, Identifier... animationLocations) {
        this.skillManager.addSkill(priority, skill);
        registerAnimation(skill.identifier, List.of(animationLocations));

        return skill;
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(S skill, Identifier... animationLocations) {
        return registerSkill(Integer.MAX_VALUE, skill, animationLocations);
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(int priority, T mob, Function<T, S> factory, Identifier... animationLocations) {
        return registerSkill(priority, factory.apply(mob), animationLocations);
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(T mob, Function<T, S> factory, Identifier... animationLocations) {
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
    public void setAnimation(Identifier animationSetIdentifier) {
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
        this.setAnimation(skill.identifier);
    }

    /**
     * For client update
     *
     * @param animationIdentifier Animation
     */
    public void updateAnimation(Identifier animationIdentifier) {
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

    public Identifier getCurrentAnimation() {
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

    public void playStaticAnimation(Identifier animation, int duration) {
        playStaticAnimation(animation, duration, true);
    }

    public void playStaticAnimation(Identifier animation, int duration, boolean interruptSkill) {
        playStaticAnimation(animation, duration, interruptSkill, false);
    }

    public void playStaticAnimation(Identifier animation, int duration, boolean interruptSkill, boolean force) {
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
    protected void customServerAiStep(ServerLevel level) {
        updateAnimationTick();
        skillManager.tick(level);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        getCurrentSkill().ifPresent(skill -> {
            output.store("CurrentSkill", Identifier.CODEC, skill.identifier);
        });
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("CurrentSkill", Identifier.CODEC).ifPresent(
                this::useSkill
        );
    }

    @FunctionalInterface
    protected interface AnimationSelector {
        AnimationSelector EMPTY = () -> NarakaMod.identifier("animation", "empty");

        static AnimationSelector of(final RandomSource random, final List<Identifier> animationLocations) {
            if (animationLocations.isEmpty())
                return EMPTY;
            if (animationLocations.size() == 1)
                return simple(animationLocations.getFirst());
            return random(random, animationLocations);
        }

        static AnimationSelector simple(final Identifier animationLocation) {
            return () -> animationLocation;
        }

        static AnimationSelector random(final RandomSource random, final List<Identifier> animationLocations) {
            return () -> {
                int randomIndex = random.nextInt(animationLocations.size());
                return animationLocations.get(randomIndex);
            };
        }

        Identifier select();
    }
}
