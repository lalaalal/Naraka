package com.yummy.naraka.world.entity;

import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.client.animation.herobrine.HerobrinePunchAnimation;
import com.yummy.naraka.client.animation.herobrine.HerobrineSkillAnimation;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.goal.LookAtTargetGoal;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHerobrine extends SkillUsingMob implements StigmatizingEntity, AfterimageEntity, Enemy {
    public static final int MAX_WEAKNESS_TICK = 40;
    public static final String WEAKNESS_ANIMATION = "weakness";

    public final AnimationState punchAnimationState1 = animationState(HerobrinePunchAnimation.PUNCH_1);
    public final AnimationState punchAnimationState2 = animationState(HerobrinePunchAnimation.PUNCH_2);
    public final AnimationState punchAnimationState3 = animationState(HerobrinePunchAnimation.PUNCH_3);
    public final AnimationState rushAnimationState = animationState(HerobrineSkillAnimation.RUSH);
    public final AnimationState throwFireballAnimationState = animationState(HerobrineSkillAnimation.THROW_NARAKA_FIREBALL);
    public final AnimationState stigmatizeEntitiesAnimationState = new AnimationState();
    public final AnimationState blockingSkillAnimationState = animationState(HerobrineAnimation.BLOCKING);
    public final AnimationState weaknessAnimationState = new AnimationState();

    protected final PunchSkill<AbstractHerobrine> punchSkill = registerSkill(this, PunchSkill::new, punchAnimationState1, punchAnimationState2, punchAnimationState3);
    protected final DashSkill<AbstractHerobrine> dashSkill = registerSkill(this, DashSkill::new);
    protected final DashAroundSkill<AbstractHerobrine> dashAroundSkill = registerSkill(this, DashAroundSkill::new);
    protected final RushSkill<AbstractHerobrine> rushSkill = registerSkill(new RushSkill<>(this, AbstractHerobrine::isNotHerobrine), rushAnimationState);
    protected final StigmatizeEntitiesSkill<AbstractHerobrine> stigmatizeEntitiesSkill = registerSkill(this, StigmatizeEntitiesSkill::new, stigmatizeEntitiesAnimationState);
    protected final ThrowFireballSkill throwFireballSkill = registerSkill(new ThrowFireballSkill(this, this::createFireball), throwFireballAnimationState);
    protected final BlockingSkill blockingSkill = registerSkill(this, BlockingSkill::new, blockingSkillAnimationState);

    public final boolean isShadow;

    protected int weaknessTickCount = Integer.MAX_VALUE;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
                .add(Attributes.STEP_HEIGHT, 1.7)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.ARMOR_TOUGHNESS, 6)
                .add(Attributes.MAX_HEALTH, 666);
    }

    protected static boolean isNotHerobrine(LivingEntity livingEntity) {
        return !livingEntity.getType().is(NarakaEntityTypeTags.HEROBRINE);
    }

    protected AbstractHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level, boolean isShadow) {
        super(entityType, level);
        this.isShadow = isShadow;
        registerAnimation(WEAKNESS_ANIMATION, weaknessAnimationState);
        setPersistenceRequired();
    }

    private void updateWeakness() {
        if (weaknessTickCount == MAX_WEAKNESS_TICK)
            stopWeakness();
        if (weaknessTickCount <= MAX_WEAKNESS_TICK)
            weaknessTickCount += 1;
    }

    protected void startWeakness() {
        setAnimation(WEAKNESS_ANIMATION);
        weaknessTickCount = 0;
        skillManager.pause(true);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
    }

    protected void stopWeakness() {
        setAnimation("idle");
        weaknessTickCount = Integer.MAX_VALUE;
        skillManager.resume();
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
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
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));

        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new MoveToTargetGoal(this, 1, 64));
        goalSelector.addGoal(3, new LookAtTargetGoal(this));
    }

    @Override
    protected void customServerAiStep() {
        updateWeakness();
        super.customServerAiStep();
    }

    protected abstract Fireball createFireball();

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
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || source.is(DamageTypes.LIGHTNING_BOLT) || super.isInvulnerableTo(source);
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
