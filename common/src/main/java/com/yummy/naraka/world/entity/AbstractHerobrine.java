package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.goal.LookAtTargetGoal;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHerobrine extends SkillUsingMob implements StigmatizingEntity, AfterimageEntity, Enemy {
    public final boolean isShadow;

    protected int staggeringTickCount = Integer.MIN_VALUE;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
                .add(Attributes.STEP_HEIGHT, 1.7)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MAX_HEALTH, 666);
    }

    public static boolean isNotHerobrine(LivingEntity livingEntity) {
        return !livingEntity.getType().is(NarakaEntityTypeTags.HEROBRINE);
    }

    protected AbstractHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level, boolean isShadow) {
        super(entityType, level);
        this.isShadow = isShadow;
        registerAnimation(AnimationLocations.STAGGERING);
        setPersistenceRequired();
        updateAnimation(AnimationLocations.IDLE);
        skillManager.runOnSkillEnd(this::updateAnimationOnSkillEnd);
    }

    private void updateAnimationOnSkillEnd(Skill<?> skill) {
        if (!skill.hasLinkedSkill())
            setAnimation(AnimationLocations.IDLE);
    }

    private void updateStaggering() {
        if (staggeringTickCount == 0)
            stopStaggering();
        if (staggeringTickCount >= 0)
            staggeringTickCount -= 1;
    }

    protected void startStaggering() {
        int duration = NarakaConfig.COMMON.herobrineStaggeringDuration.getValue();
        this.startStaggering(true, duration);
    }

    protected void startStaggering(boolean playAnimation, int duration) {
        if (staggeringTickCount > 0)
            return;
        staggeringTickCount = Math.max(1, duration);
        skillManager.pause(true);
        if (playAnimation)
            setAnimation(AnimationLocations.STAGGERING);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.STAGGERING_PREVENT_MOVING);
    }

    protected void stopStaggering() {
        setAnimation(AnimationLocations.IDLE);
        staggeringTickCount = Integer.MIN_VALUE;
        skillManager.resume();
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.STAGGERING_PREVENT_MOVING);
    }

    public boolean isStaggering() {
        return staggeringTickCount > 0;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        GroundPathNavigation navigation = new GroundPathNavigation(this, level);
        navigation.setCanFloat(true);
        navigation.setCanWalkOverFences(true);
        navigation.setCanOpenDoors(true);
        return navigation;
    }

    @Override
    protected void registerGoals() {
        targetSelector.addGoal(1, new HurtByTargetGoal(this, Herobrine.class));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (target, serverLevel) -> !target.getType().is(NarakaEntityTypeTags.HEROBRINE)));

        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new LookAtTargetGoal(this));
        goalSelector.addGoal(3, new MoveToTargetGoal(this, 1, 64));
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        updateStaggering();
        super.customServerAiStep(serverLevel);
    }

    protected abstract Fireball createFireball(ServerLevel level);

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return false;
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        return false;
    }

    @Override
    public int getAirSupply() {
        return getMaxAirSupply();
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canStandOnFluid(FluidState fluidState) {
        return !fluidState.isEmpty();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel serverLevel, DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LIGHTNING_BOLT) || super.isInvulnerableTo(serverLevel, source);
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
