package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAnimationPacket;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
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
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class SkillUsingMob extends PathfinderMob {
    protected final SkillManager skillManager = new SkillManager(this);
    protected final Map<Identifier, AnimationController> animationControllers = new HashMap<>();
    protected Identifier currentAnimation = NarakaMod.location("empty");

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

    public void forEachAnimations(BiConsumer<Identifier, AnimationState> consumer) {
        for (AnimationController controller : animationControllers.values())
            controller.update(consumer);
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Set<Identifier> getAnimations() {
        return animationControllers.keySet();
    }

    public void useSkill(Identifier location) {
        Skill<?> skill = skillManager.getSkill(location);
        if (skill != null)
            skillManager.setCurrentSkillIfAbsence(skill);
    }

    public Optional<Skill<?>> getCurrentSkill() {
        return Optional.ofNullable(skillManager.getCurrentSkill());
    }

    public void registerAnimation(Identifier animationSetLocation, List<Identifier> animationLocations) {
        this.animationControllers.put(animationSetLocation, AnimationController.of(random, animationLocations));
    }

    public void registerAnimation(Identifier animationLocation) {
        this.animationControllers.put(animationLocation, AnimationController.simple(animationLocation));
    }

    public <T extends SkillUsingMob, S extends Skill<T>> S registerSkill(int priority, S skill, Identifier... animationLocations) {
        this.skillManager.addSkill(priority, skill);
        registerAnimation(skill.location, List.of(animationLocations));

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
     * @param animationLocation Animation
     */
    public void setAnimation(Identifier animationLocation) {
        NarakaMod.LOGGER.debug("{} : Setting animation {}", this, animationLocation);
        if (!isPlayingStaticAnimation()) {
            currentAnimation = animationLocation;
            if (level() instanceof ServerLevel serverLevel) {
                NarakaMod.LOGGER.debug("{} : Sending animation ({}) sync packet", this, animationLocation);
                SyncAnimationPacket payload = new SyncAnimationPacket(this, animationLocation);
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
     * @param animationLocation Animation
     */
    public void updateAnimation(Identifier animationLocation) {
        NarakaMod.LOGGER.debug("{} : Updating animation {}", this, animationLocation);
        animationControllers.forEach((location, animationController) -> {
            if (animationLocation.equals(location))
                animationController.start(tickCount);
            else
                animationController.stop();
        });
        currentAnimation = animationLocation;
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
        NetworkManager.clientbound().send(level.players(), ClientboundEntityPositionSyncPacket.of(this));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        getCurrentSkill().ifPresent(skill -> {
            output.store("CurrentSkill", Identifier.CODEC, skill.location);
        });
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.read("CurrentSkill", Identifier.CODEC).ifPresent(
                this::useSkill
        );
    }

    protected static abstract class AnimationController {
        private static final AnimationController EMPTY = new AnimationController(List.of()) {
            @Override
            protected Identifier select() {
                return NarakaMod.location("animation", "empty");
            }
        };

        static AnimationController of(final RandomSource random, final List<Identifier> animationLocations) {
            if (animationLocations.isEmpty())
                return EMPTY;
            if (animationLocations.size() == 1)
                return simple(animationLocations.getFirst());
            return random(random, animationLocations);
        }

        static AnimationController simple(final Identifier animationLocation) {
            return new AnimationController(List.of(animationLocation)) {
                @Override
                protected Identifier select() {
                    return animationLocation;
                }
            };
        }

        static AnimationController random(final RandomSource random, final List<Identifier> animationLocations) {
            return new AnimationController(animationLocations) {
                @Override
                protected Identifier select() {
                    int randomIndex = random.nextInt(animationLocations.size());
                    return animationLocations.get(randomIndex);
                }
            };
        }

        protected final Map<Identifier, AnimationState> animationStates;

        public AnimationController(List<Identifier> animationLocations) {
            this.animationStates = new HashMap<>();
            for (Identifier animationLocation : animationLocations)
                animationStates.put(animationLocation, new AnimationState());
        }

        protected abstract Identifier select();

        public void start(int tickCount) {
            final Identifier selected = select();
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

        public void update(BiConsumer<Identifier, AnimationState> consumer) {
            animationStates.forEach(consumer);
        }
    }
}
