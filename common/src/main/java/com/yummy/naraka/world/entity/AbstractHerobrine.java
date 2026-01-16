package com.yummy.naraka.world.entity;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.ai.goal.LookAtTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractHerobrine extends SkillUsingMob implements StigmatizingEntity, AfterimageEntity, Enemy {
    protected static final EntityDataAccessor<Boolean> FINAL_MODEL = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DISPLAY_SCARF = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DISPLAY_EYE = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> DISPLAY_PICKAXE = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> ALPHA = SynchedEntityData.defineId(AbstractHerobrine.class, EntityDataSerializers.INT);

    public final boolean isShadow;
    private final ScarfWavingData scarfWavingData = new ScarfWavingData();

    private float eyeAlpha = 1;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
                .add(Attributes.STEP_HEIGHT, 1.25)
                .add(Attributes.MOVEMENT_SPEED, 0.17f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.SAFE_FALL_DISTANCE, 256)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0)
                .add(Attributes.JUMP_STRENGTH, 0)
                .add(Attributes.FLYING_SPEED, 0.5f)
                .add(Attributes.MAX_HEALTH, 666);
    }

    public static boolean isNotHerobrine(LivingEntity livingEntity) {
        return !livingEntity.getType().is(NarakaEntityTypeTags.HEROBRINE);
    }

    protected AbstractHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level, boolean isShadow) {
        super(entityType, level);
        this.isShadow = isShadow;
        registerAnimation(HerobrineAnimationIdentifiers.STAGGERING);
        registerAnimation(HerobrineAnimationIdentifiers.IDLE);
        registerAnimation(HerobrineAnimationIdentifiers.PHASE_3_IDLE);

        updateAnimation(HerobrineAnimationIdentifiers.IDLE);
        skillManager.runOnSkillEnd(this::updateAnimationOnSkillEnd);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FINAL_MODEL, false)
                .define(DISPLAY_SCARF, false)
                .define(DISPLAY_EYE, true)
                .define(DISPLAY_PICKAXE, false)
                .define(ALPHA, 0xff);
    }

    public void setAlpha(int alpha) {
        entityData.set(ALPHA, alpha);
    }

    public int getAlpha() {
        return entityData.get(ALPHA);
    }

    public void setFinalModel(boolean value) {
        entityData.set(FINAL_MODEL, value);
    }

    public boolean isFinalModel() {
        return entityData.get(FINAL_MODEL);
    }

    public void setDisplayPickaxe(boolean value) {
        entityData.set(DISPLAY_PICKAXE, value);
    }

    public boolean displayPickaxe() {
        return entityData.get(DISPLAY_PICKAXE);
    }

    public boolean displayEye() {
        return entityData.get(DISPLAY_EYE);
    }

    public void setDisplayEye(boolean value) {
        entityData.set(DISPLAY_EYE, value);
    }

    public float getEyeAlpha() {
        return eyeAlpha;
    }

    public ScarfWavingData getScarfWavingData() {
        return scarfWavingData;
    }

    public boolean shouldRenderScarf() {
        return entityData.get(DISPLAY_SCARF);
    }

    protected void updateAnimationOnSkillEnd(Skill<?> skill) {
        if (!skill.hasLinkedSkill()) {
            setAnimation(getIdleAnimation());
        }
    }

    protected Identifier getIdleAnimation() {
        if (isFinalModel())
            return HerobrineAnimationIdentifiers.PHASE_3_IDLE;
        return HerobrineAnimationIdentifiers.IDLE;
    }

    @Override
    public void stopStaticAnimation() {
        if (animationTickLeft >= 0)
            setAnimation(getIdleAnimation());
        super.stopStaticAnimation();
    }

    public boolean shouldPlayIdleAnimation() {
        return true;
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
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false, (target, level) -> isNotHerobrine(target)));

        goalSelector.addGoal(2, new LookAtTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        float alphaAddition = displayEye() ? 0.1f : -0.1f;
        eyeAlpha = Mth.clamp(eyeAlpha + alphaAddition, 0, 1);

        scarfWavingData.update(getDeltaMovement(), yBodyRot - yBodyRotO, onGround());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        boolean finalModel = input.getBooleanOr("FinalModel", false);
        entityData.set(FINAL_MODEL, finalModel);
        if (finalModel)
            entityData.set(DISPLAY_SCARF, true);
        boolean displayPickaxe = input.getBooleanOr("DisplayPickaxe", false);
        setDisplayPickaxe(displayPickaxe);
        setPersistenceRequired();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("FinalModel", isFinalModel());
        output.putBoolean("DisplayPickaxe", displayPickaxe());
    }

    protected abstract Fireball createFireball(ServerLevel level);

    public abstract Optional<ShadowController> getShadowController();

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

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        return false;
    }
}
