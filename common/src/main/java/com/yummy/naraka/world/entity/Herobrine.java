package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.*;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Herobrine extends SkillUsingMob implements DeathCountingEntity {
    private static final float[] HEALTH_BY_PHASE = {106, 210, 350};
    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.RED, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.BLUE};
    public static final int MAX_HURT_DAMAGE_LIMIT = 50;

    public final AnimationState punchAnimationState = new AnimationState();
    public final AnimationState dashAnimationState = new AnimationState();
    public final AnimationState godBlowAnimationState = new AnimationState();

    private final Skill punchSkill = new PunchSkill(this);
    private final Skill dashSkill = new DashSkill(this);
    private final Skill godBlowSkill = new GodBlowSkill(this);

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, this);
    private final DeathCountingInstance countingInstance;
    private int hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
    private boolean hibernateMode = false;
    private final boolean madMode = false;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ARMOR, 66)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
                .add(Attributes.STEP_HEIGHT, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MAX_HEALTH, 666);
    }

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level);
        this.countingInstance = new DeathCountingInstance(level.registryAccess());

        bossEvent.setDarkenScreen(true)
                .setPlayBossMusic(true);
        DeathCountHelper.attachCountingEntity(this);
        setPersistenceRequired();
        registerSkills();
    }

    public Herobrine(Level level, Vec3 pos) {
        this(NarakaEntityTypes.HEROBRINE.get(), level);
        setPos(pos);
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

        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new MoveToTargetGoal(this, 1, 64));
//        goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8));
    }

    protected void registerSkills() {
        registerSkill(new PunchSkill(this), punchAnimationState);
        registerSkill(new DashSkill(this), dashAnimationState);
        registerSkill(new GodBlowSkill(this), godBlowAnimationState);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        phaseManager.updatePhase(bossEvent);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        bossEvent.addPlayer(serverPlayer);
        phaseManager.updatePhase(bossEvent);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        bossEvent.removePlayer(serverPlayer);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        updateHurtDamageLimit();
        Entity cause = damageSource.getEntity();
        if (cause instanceof LivingEntity livingEntity)
            countDeath(livingEntity);
        if (damageSource.is(DamageTypeTags.IS_PROJECTILE)) {
            this.skillManager.setCurrentSkill(BlockingSkill.NAME);
            return false;
        }
        return super.hurt(damageSource, damage);
    }

    private void updateHurtDamageLimit() {
        if (phaseManager.getCurrentPhase() == 1) {
            hurtDamageLimit /= 2;
            if (hurtDamageLimit == 1)
                startHibernateMode();
        }
    }

    private void startHibernateMode() {
        hibernateMode = true;
    }

    @Override
    public void die(DamageSource damageSource) {
        if (level() instanceof ServerLevel serverLevel)
            getCountingInstance().countedEntities(serverLevel)
                    .forEach(this::rewardChallenger);
        super.die(damageSource);
    }

    @Override
    public boolean canDisableShield() {
        return true;
    }

    private void rewardChallenger(LivingEntity livingEntity) {
        if (livingEntity.hasEffect(NarakaMobEffects.CHALLENGERS_BLESSING)) {
            livingEntity.removeEffect(NarakaMobEffects.CHALLENGERS_BLESSING);
            for (ItemStack stack : livingEntity.getArmorSlots()) {
                stack.consume(1, livingEntity);
                level().playSound(null, livingEntity.getOnPos(), stack.getBreakingSound(), SoundSource.PLAYERS);
            }
            ItemStack weaponStack = livingEntity.getMainHandItem();
            weaponStack.set(NarakaDataComponentTypes.BLESSED.get(), true);
        }
    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        DeathCountHelper.detachCountingEntity(this);
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
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypes.IN_WALL))
            return true;
        return super.isInvulnerableTo(source);
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
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        countingInstance.save(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        countingInstance.load(compoundTag);
    }

    @Override
    public LivingEntity living() {
        return this;
    }

    @Override
    public DeathCountingInstance getCountingInstance() {
        return countingInstance;
    }

    @Override
    public void onEntityUseDeathCount(LivingEntity livingEntity) {
        DamageSource source = livingEntity.getLastDamageSource();
        if (source != null && source.is(NarakaDamageTypes.STIGMA))
            heal(66);
    }
}
