package com.yummy.naraka.world.entity;

import com.yummy.naraka.network.SyncAfterimagePayload;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.goal.LookAtTargetGoal;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.*;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import dev.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
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
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Herobrine extends SkillUsingMob implements AfterimageEntity, Enemy {
    private static final float[] HEALTH_BY_PHASE = {106, 210, 350};

    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.RED};
    public static final int MAX_HURT_DAMAGE_LIMIT = 50;
    public static final int MAX_WEAKNESS_TICK = 40;

    public final AnimationState punchAnimationState1 = new AnimationState();
    public final AnimationState punchAnimationState2 = new AnimationState();
    public final AnimationState punchAnimationState3 = new AnimationState();
    public final AnimationState rushAnimationState = new AnimationState();
    public final AnimationState throwFireballAnimationState = new AnimationState();
    public final AnimationState blockingSkillAnimationState = new AnimationState();
    public final AnimationState weaknessAnimationState = new AnimationState();

    private final PunchSkill punchSkill = new PunchSkill(this);
    private final DashSkill<Herobrine> dashSkill = new DashSkill<>(this);
    private final DashAroundSkill<Herobrine> dashAroundSkill = new DashAroundSkill<>(this);
    private final RushSkill rushSkill = new RushSkill(this);
    private final ThrowFireballSkill throwFireballSkill = new ThrowFireballSkill(this);
    private final BlockingSkill blockingSkill = new BlockingSkill(this);

    private final List<Skill<?>> GENERAL_SKILLS = List.of();
    private final List<Skill<?>> PHASE_1_SKILLS = List.of(punchSkill, dashSkill, dashAroundSkill, throwFireballSkill, rushSkill);

    private final List<Projectile> ignoredProjectiles = new ArrayList<>();
    protected final List<Afterimage> afterimages = new ArrayList<>();

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, this);
    private int hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
    private boolean hibernateMode = false;
    private int hibernateModeTickCount = 0;
    private int weaknessTickCount = Integer.MAX_VALUE;

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

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level);

        bossEvent.setDarkenScreen(true)
                .setPlayBossMusic(true);
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
        goalSelector.addGoal(3, new LookAtTargetGoal(this));
    }

    protected void registerSkills() {
        registerSkill(blockingSkill, blockingSkillAnimationState);

        registerSkill(punchSkill, punchAnimationState1, punchAnimationState2, punchAnimationState3);
        registerSkill(dashSkill);
        registerSkill(dashAroundSkill);
        registerSkill(rushSkill, rushAnimationState);
        registerSkill(throwFireballSkill, throwFireballAnimationState);
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {
        if (level() instanceof ServerLevel serverLevel) {
            SyncAfterimagePayload payload = new SyncAfterimagePayload(this, afterimage);
            NetworkManager.sendToPlayers(serverLevel.players(), payload);
        }
        this.afterimages.add(afterimage);
    }

    @Override
    public List<Afterimage> getAfterimages() {
        return afterimages;
    }

    @Override
    public void tick() {
        super.tick();
        afterimages.removeIf(Afterimage::tick);
    }

    @Override
    protected void customServerAiStep() {
        if (weaknessTickCount == MAX_WEAKNESS_TICK)
            stopWeakness();
        if (weaknessTickCount <= MAX_WEAKNESS_TICK)
            weaknessTickCount += 1;

        tryAvoidProjectile();
        super.customServerAiStep();
        phaseManager.updatePhase(bossEvent);
    }

    private boolean shouldCheck(Projectile projectile) {
        return !ignoredProjectiles.contains(projectile);
    }

    private void tryAvoidProjectile() {
        List<Projectile> projectiles = level().getEntitiesOfClass(Projectile.class, getBoundingBox().inflate(5), this::shouldCheck);
        for (Projectile projectile : projectiles) {
            if (projectile.getOwner() == this)
                continue;
            ignoredProjectiles.add(projectile);
            if (!projectile.position().equals(new Vec3(projectile.xo, projectile.yo, projectile.zo)) && random.nextBoolean()) {
                lookAt(projectile, 360, 0);
                dashAroundSkill.preventSecondUse();
                skillManager.setCurrentSkillIfAbsence(dashAroundSkill);
                return;
            }
        }
        ignoredProjectiles.removeIf(Projectile::isRemoved);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return getCurrentSkill() != dashAroundSkill && super.canBeHitByProjectile();
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
    public boolean hurt(DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurt(source, damage);
        if (source.getEntity() == this)
            return false;

        float actualDamage = getActualDamage(source, damage);
        updateHurtDamageLimit(actualDamage);

        if (phaseManager.getCurrentPhase() == 1) {
            float healthAfterHurt = phaseManager.getCurrentPhaseHealth() + getAbsorptionAmount() - actualDamage;
            if (healthAfterHurt < 1) {
                setHealth(phaseManager.getActualPhaseMaxHealth(2) + 1);
                startHibernateMode();
                return true;
            }
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            if (hibernateMode && source.getDirectEntity() instanceof NarakaFireball fireball && !fireball.hasTarget()) {
                stopHibernateMode();
                startWeakness();
                if (phaseManager.getCurrentPhaseHealth() == 1)
                    setHealth(getHealth() - 1);
            }
            if (source.getDirectEntity() != null)
                lookAt(source.getDirectEntity(), 360, 0);
            skillManager.setCurrentSkillIfAbsence(blockingSkill);
            return false;
        }
        return super.hurt(source, Math.min(hurtDamageLimit, damage));
    }

    private float getActualDamage(DamageSource source, float damage) {
        damage = getDamageAfterArmorAbsorb(source, damage);
        return getDamageAfterMagicAbsorb(source, damage);
    }

    private void updateHurtDamageLimit(float actualDamage) {
        if (actualDamage >= 50 || level().isClientSide)
            return;

        if (phaseManager.getCurrentPhase() == 1 && hurtDamageLimit > 1) {
            int reduce = Mth.clamp(1, Math.round(actualDamage) - 1, 20);
            hurtDamageLimit = Math.max(1, hurtDamageLimit - reduce);
            if (hurtDamageLimit == 1)
                startHibernateMode();
        }
    }

    private void startHibernateMode() {
        hibernateMode = true;
        skillManager.getSkills().stream()
                .filter(skill -> skill != throwFireballSkill && skill != blockingSkill)
                .forEach(Skill::disable);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
    }

    private void stopHibernateMode() {
        hibernateMode = false;
        hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
        for (Skill<?> skill : PHASE_1_SKILLS)
            skill.enable();
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
    }

    private void startWeakness() {

        weaknessAnimationState.start(tickCount);
        weaknessTickCount = 0;
        skillManager.pause(true);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
    }

    private void stopWeakness() {
        weaknessAnimationState.stop();
        weaknessTickCount = Integer.MAX_VALUE;
        skillManager.resume();
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity livingEntity)
            rewardChallenger(livingEntity);
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HibernateMode", hibernateMode);
        compound.putInt("HibernateModeTickCount", hibernateModeTickCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("HibernatedMode"))
            hibernateMode = compound.getBoolean("HibernatedMode");
        if (compound.contains("HibernateModeTickCount"))
            hibernateModeTickCount = compound.getInt("HibernateModeTickCount");
    }
}
