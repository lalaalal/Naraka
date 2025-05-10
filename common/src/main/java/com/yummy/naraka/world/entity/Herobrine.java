package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.network.NarakaClientboundEventHandler;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncAfterimagePayload;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.*;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Herobrine extends AbstractHerobrine {
    protected static final EntityDataAccessor<Float> SCARF_ROTATION_DEGREE = SynchedEntityData.defineId(Herobrine.class, EntityDataSerializers.FLOAT);
    private static final float[] HEALTH_BY_PHASE = {106, 210, 350};

    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.RED};
    public static final int MAX_HURT_DAMAGE_LIMIT = 20;
    public static final int MAX_ACCUMULATED_DAMAGE_TICK_COUNT = 40;

    protected final DashSkill<AbstractHerobrine> dashSkill = registerSkill(this, DashSkill::new);
    protected final DashAroundSkill<AbstractHerobrine> dashAroundSkill = registerSkill(this, DashAroundSkill::new);
    protected final StigmatizeEntitiesSkill<AbstractHerobrine> stigmatizeEntitiesSkill = registerSkill(this, StigmatizeEntitiesSkill::new);
    protected final ThrowFireballSkill throwFireballSkill = registerSkill(new ThrowFireballSkill(this, this::createFireball), AnimationLocations.THROW_NARAKA_FIREBALL);
    protected final BlockingSkill blockingSkill = registerSkill(this, BlockingSkill::new, AnimationLocations.BLOCKING);
    protected final SummonShadowSkill summonShadowSkill = registerSkill(0, this, SummonShadowSkill::new);
    protected final RolePlayShadowSkill rolePlayShadowSkill = registerSkill(1, this, RolePlayShadowSkill::new);
    protected final RushSkill<AbstractHerobrine> rushSkill = registerSkill(new RushSkill<>(this, AbstractHerobrine::isNotHerobrine), AnimationLocations.RUSH);
    protected final WalkAroundTargetSkill walkAroundTargetSkill = registerSkill(new WalkAroundTargetSkill(this, dashSkill));

    protected final LandingSkill landingSkill = registerSkill(this, LandingSkill::new, AnimationLocations.COMBO_ATTACK_5);
    protected final SuperHitSkill superHitSkill = registerSkill(new SuperHitSkill(landingSkill, this), AnimationLocations.COMBO_ATTACK_4);
    protected final SpinningSkill spinningSkill = registerSkill(new SpinningSkill(superHitSkill, this), AnimationLocations.COMBO_ATTACK_3);
    protected final UppercutSkill uppercutSkill = registerSkill(new UppercutSkill(spinningSkill, this), AnimationLocations.COMBO_ATTACK_2);
    protected final PunchSkill<AbstractHerobrine> punchSkill = registerSkill(2, new PunchSkill<>(uppercutSkill, this, 140, true), AnimationLocations.COMBO_ATTACK_1);

    private final List<Skill<?>> HIBERNATED_MODE_PHASE_1_SKILLS = List.of(throwFireballSkill, blockingSkill);
    private final List<Skill<?>> HIBERNATED_MODE_PHASE_2_SKILLS = List.of(stigmatizeEntitiesSkill, blockingSkill, summonShadowSkill, rolePlayShadowSkill);
    private final List<Skill<?>> PHASE_1_SKILLS = List.of(punchSkill, dashSkill, dashAroundSkill, rushSkill, throwFireballSkill, walkAroundTargetSkill);
    private final List<Skill<?>> PHASE_2_SKILLS = List.of(punchSkill, dashSkill, dashAroundSkill, rushSkill, throwFireballSkill, summonShadowSkill, walkAroundTargetSkill);

    private final List<List<Skill<?>>> HIBERNATED_MODE_SKILL_BY_PHASE = List.of(
            List.of(), HIBERNATED_MODE_PHASE_1_SKILLS, HIBERNATED_MODE_PHASE_2_SKILLS, List.of()
    );
    private final List<List<Skill<?>>> SKILLS_BY_PHASE = List.of(
            List.of(), PHASE_1_SKILLS, PHASE_2_SKILLS, List.of()
    );

    private final List<Projectile> ignoredProjectiles = new ArrayList<>();
    private final Set<UUID> stigmatizedEntities = new HashSet<>();
    private final Set<UUID> watchingEntities = new HashSet<>();
    protected final List<Afterimage> afterimages = new ArrayList<>();

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, this, bossEvent);
    private final ShadowController shadowController = new ShadowController(this);

    private float hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;

    private boolean hibernateMode = false;

    protected int idleTickCount = 0;

    protected int accumulatedDamageTickCount;
    protected float accumulatedHurtDamage;

    private @Nullable BlockPos spawnPosition;

    private float prevScarfRotation = 0;
    private final List<Float> scarfWaveSpeedList = new ArrayList<>();

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level, false);

        bossEvent.setDarkenScreen(true)
                .setPlayBossMusic(true);
        phaseManager.addPhaseChangeListener(this::updateMusic);
        phaseManager.addPhaseChangeListener(this::updateUsingSkills);
        phaseManager.addPhaseChangeListener((prev, current) -> resetDamageLimit());

        skillManager.enableOnly(PHASE_1_SKILLS);
        skillManager.runOnSkillStart(this::replaceRush);
        skillManager.runOnSkillSelect(this::useComboAttackOnIdle);

        for (int i = 0; i < NarakaConfig.CLIENT.herobrineScarfPartitionNumber.getValue(); i++)
            scarfWaveSpeedList.add(1f);
    }

    public Herobrine(Level level, Vec3 pos) {
        this(NarakaEntityTypes.HEROBRINE.get(), level);
        setPos(pos);
    }

    public ShadowController getShadowController() {
        return shadowController;
    }

    private void replaceRush(Skill<?> skill) {
        if (!isHibernateMode() && skill.location.equals(RushSkill.LOCATION)
                && random.nextFloat() < 0.4f && punchSkill.isEnabled() && punchSkill.readyToUse()) {
            dashAndPunch();
        }
    }

    private void useComboAttackOnIdle(@Nullable Skill<?> skill) {
        if (getTarget() != null
                && !isStaggering() && !isHibernateMode()
                && skill == null && punchSkill.isEnabled() && idleTickCount > 20) {
            dashAndPunch();
        }
    }

    private void dashAndPunch() {
        dashSkill.setLinkedSkill(punchSkill);
        skillManager.setCurrentSkill(dashSkill);
        punchSkill.setLinkedFromPrevious(true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new MoveToTargetGoal(this, 1, 64, 1, 1, 0));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SCARF_ROTATION_DEGREE, 0f);
    }

    public void setSpawnPosition(BlockPos pos) {
        this.spawnPosition = pos;
    }

    private void resetDamageLimit() {
        this.hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
    }

    private void updateMusic(int prevPhase, int currentPhase) {
        NarakaClientboundEventPacket.Event event = NarakaClientboundEventHandler.musicEventByPhase(currentPhase);
        CustomPacketPayload packet = new NarakaClientboundEventPacket(event);
        NetworkManager.sendToClient(bossEvent.getPlayers(), packet);
    }

    private void updateUsingSkills(int prevPhase, int currentPhase) {
        skillManager.enableOnly(SKILLS_BY_PHASE.get(currentPhase));
    }

    public int getPhase() {
        return phaseManager.getCurrentPhase();
    }

    public float getHurtDamageLimit() {
        return hurtDamageLimit;
    }

    @Override
    public void stigmatizeEntity(ServerLevel level, LivingEntity target) {
        if (!target.getType().is(NarakaEntityTypeTags.HEROBRINE) && getPhase() > 1) {
            StigmaHelper.increaseStigma(level, target, this, true);
            stigmatizedEntities.add(target.getUUID());
            watchingEntities.add(target.getUUID());
        }
    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {
        this.heal(original.value() * 22);
        if (getPhase() == 2 && target.isDeadOrDying())
            stopHibernateMode(level);
    }

    @Override
    public void heal(float healAmount) {
        float maxHealth = phaseManager.getActualCurrentPhaseMaxHealth();
        float healthAfterHeal = Mth.clamp(getHealth() + healAmount, getHealth(), maxHealth);
        setHealth(healthAfterHeal);
    }

    public int getShadowCount() {
        return shadowController.getShadowCount();
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {
        if (!level().isClientSide) {
            SyncAfterimagePayload payload = new SyncAfterimagePayload(this, afterimage);
            NetworkManager.sendToClient(bossEvent.getPlayers(), payload);
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
        prevScarfRotation = entityData.get(SCARF_ROTATION_DEGREE);

        updateScarfWaveSpeeds();
    }

    public float getScarfRotationDegree(float partialTick) {
        return Mth.lerp(partialTick, prevScarfRotation, entityData.get(SCARF_ROTATION_DEGREE));
    }

    public List<Float> getScarfWaveSpeedList() {
        return scarfWaveSpeedList;
    }

    private void updateScarfWaveSpeeds() {
        int partitionNumber = NarakaConfig.CLIENT.herobrineScarfPartitionNumber.getValue();
        if (scarfWaveSpeedList.size() < partitionNumber) {
            for (int i = 0; i < partitionNumber - scarfWaveSpeedList.size(); i++)
                scarfWaveSpeedList.add(scarfWaveSpeedList.getLast());
        }
        if (tickCount % 10 == 0) {
            float maxRotation = NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
            float speed = Mth.lerp(prevScarfRotation / maxRotation, 1, 3);
            scarfWaveSpeedList.addFirst(speed);
            scarfWaveSpeedList.removeLast();
        } else {
            scarfWaveSpeedList.addFirst(scarfWaveSpeedList.getFirst());
            scarfWaveSpeedList.removeLast();
        }
    }

    private void updateScarfRotation() {
        float scarfRotationDegree = entityData.get(SCARF_ROTATION_DEGREE);
        Vec3 delta = getDeltaMovement();
        float z = Mth.sin(getYRot());
        float x = Mth.cos(getYRot());
        Vec3 projection = NarakaUtils.projection(delta, new Vec3(x, 0, z));
        float targetRotation = (float) projection.length() * 100 * 10;
        if (scarfRotationDegree < targetRotation)
            scarfRotationDegree += 5;
        if (scarfRotationDegree > targetRotation)
            scarfRotationDegree -= 3;
        float maxRotation = NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
        entityData.set(SCARF_ROTATION_DEGREE, Mth.clamp(scarfRotationDegree, 0, maxRotation));
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        updateIdleTick();
        updateAccumulatedDamage();
        updateScarfRotation();

        tryAvoidProjectile();
        collectStigma(serverLevel);
        phaseManager.updatePhase(bossEvent);

        super.customServerAiStep(serverLevel);
    }

    private void updateIdleTick() {
        if (getCurrentAnimation().equals(AnimationLocations.IDLE)) {
            idleTickCount += 1;
        } else {
            idleTickCount = 0;
        }
    }

    private void updateAccumulatedDamage() {
        if (accumulatedDamageTickCount > MAX_ACCUMULATED_DAMAGE_TICK_COUNT) {
            accumulatedDamageTickCount = 0;
            accumulatedHurtDamage = 0;
        }
        accumulatedDamageTickCount += 1;
    }

    private void collectStigma(ServerLevel serverLevel) {
        final int waitingTick = NarakaConfig.COMMON.herobrineTakingStigmaTick.getValue();
        watchingEntities.removeIf(uuid -> {
            LivingEntity entity = NarakaEntityUtils.findEntityByUUID(serverLevel, uuid, LivingEntity.class);
            if (entity == null)
                return false;
            if (entity.isDeadOrDying()) {
                stigmatizedEntities.remove(uuid);
                return true;
            }
            return StigmaHelper.collectStigmaAfter(serverLevel, entity, this, waitingTick);
        });
    }

    private boolean shouldCheckProjectile(Projectile projectile) {
        return !ignoredProjectiles.contains(projectile);
    }

    private void tryAvoidProjectile() {
        List<Projectile> projectiles = level().getEntitiesOfClass(Projectile.class, getBoundingBox().inflate(5), this::shouldCheckProjectile);
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
        NarakaClientboundEventPacket.Event event = NarakaClientboundEventHandler.musicEventByPhase(getPhase());
        CustomPacketPayload packet = new NarakaClientboundEventPacket(event);
        NetworkManager.sendToClient(serverPlayer, packet);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        bossEvent.removePlayer(serverPlayer);
        CustomPacketPayload packet = new NarakaClientboundEventPacket(NarakaClientboundEventPacket.Event.STOP_MUSIC);
        NetworkManager.sendToClient(serverPlayer, packet);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurtServer(level, source, damage);
        if (source.getEntity() == this)
            return false;

        float limitedDamage = Math.min(damage, hurtDamageLimit);
        if (updateHibernateMode(level, source, getActualDamage(source, limitedDamage)))
            return true;
        if (hibernateMode)
            return true;

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            if (source.getDirectEntity() != null)
                lookAt(source.getDirectEntity(), 360, 0);
            skillManager.setCurrentSkillIfAbsence(blockingSkill);
            return false;
        }
        return super.hurtServer(level, source, limitedDamage);
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(level, damageSource, damageAmount);
        updateHurtDamageLimit(level);
        accumulatedHurtDamage += damageAmount;
        if (accumulatedHurtDamage > 15 || random.nextDouble() < 0.25f)
            shadowController.switchWithShadowHerobrine(level);
    }

    private float getPhaseMinimumHealth() {
        return phaseManager.getActualPhaseMaxHealth(getPhase() + 1) + 1;
    }

    private boolean updateHibernateMode(ServerLevel level, DamageSource source, float actualDamage) {
        if (source.getDirectEntity() instanceof NarakaFireball fireball && !fireball.hasTarget()) {
            if (getHealth() > getPhaseMinimumHealth())
                startStaggering();
            resetDamageLimit();
            if (hibernateMode)
                stopHibernateMode(level);
            return true;
        }

        return preserveCurrentPhaseMinimumHealth(level, actualDamage);
    }

    private boolean preserveCurrentPhaseMinimumHealth(ServerLevel level, float actualDamage) {
        if (getPhase() == phaseManager.getMaxPhase())
            return false;
        float healthAfterHurt = phaseManager.getCurrentPhaseHealth() + getAbsorptionAmount() - actualDamage;
        if (healthAfterHurt < 1) {
            setHealth(getPhaseMinimumHealth());
            startHibernateMode(level);
            return true;
        }

        return false;
    }

    private float getActualDamage(DamageSource source, float damage) {
        damage = getDamageAfterArmorAbsorb(source, damage);
        return getDamageAfterMagicAbsorb(source, damage);
    }

    private void updateHurtDamageLimit(ServerLevel level) {
        if (phaseManager.getCurrentPhase() < 3 && hurtDamageLimit > 1) {
            float damageLimitReduce = NarakaConfig.COMMON.herobrineHurtLimitReduce.getValue();
            hurtDamageLimit = hurtDamageLimit - damageLimitReduce;
            if (hurtDamageLimit <= 1) {
                hurtDamageLimit = 0;
                startHibernateMode(level);
            }
        }
    }

    public boolean isHibernateMode() {
        return hibernateMode;
    }

    protected void startHibernateMode(ServerLevel level) {
        if (hibernateMode)
            return;
        if (spawnPosition != null)
            moveTo(spawnPosition.south(54), 0, 0);
        hibernateMode = true;
        skillManager.enableOnly(HIBERNATED_MODE_SKILL_BY_PHASE.get(getPhase()));
        shadowController.updateRolePlaying(level);
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.HIBERNATE_PREVENT_MOVING);
    }

    protected void stopHibernateMode(ServerLevel level) {
        hibernateMode = false;
        resetDamageLimit();
        skillManager.enableOnly(SKILLS_BY_PHASE.get(getPhase()));
        shadowController.stopRolePlaying(level);
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.HIBERNATE_PREVENT_MOVING);

        setHealth(getHealth() - 1);
    }

    @Override
    protected void stopStaggering() {
        super.stopStaggering();
        resetDamageLimit();
    }

    @Override
    public Fireball createFireball(ServerLevel level) {
        NarakaFireball fireball = new NarakaFireball(this, Vec3.ZERO, level(), hibernateMode);
        fireball.setDamageCalculator(this::calculateFireballDamage);
        fireball.addHurtTargetListener((target, damage) -> stigmatizeEntity(level, target));
        fireball.addHurtTargetListener((target, damage) -> updateHibernateModeOnTargetSurvivedFromFireball(level, target, damage));

        return fireball;
    }

    private float calculateFireballDamage(NarakaFireball fireball) {
        if (!hibernateMode) {
            if (getTarget() != null)
                return getAttackDamage() + getTarget().getMaxHealth() * 0.05f;
            return getAttackDamage();
        }
        float distance = distanceTo(fireball);
        if (distance <= 2)
            return 66;
        return Mth.clamp(66f / (distance - 2), 10, 66);
    }

    private void updateHibernateModeOnTargetSurvivedFromFireball(ServerLevel level, LivingEntity target, float damage) {
        if (target != this && getPhase() == 1 && hibernateMode && damage >= 66 && target.isAlive()) {
            stopHibernateMode(level);
        }
    }

    @Override
    @Nullable
    public Entity teleport(TeleportTransition teleportTransition) {
        if (!level().dimension().equals(teleportTransition.newLevel().dimension()) && level() instanceof ServerLevel level) {
            for (ShadowHerobrine shadowHerobrine : shadowController.getShadows(level))
                shadowHerobrine.teleport(teleportTransition);
        }
        return super.teleport(teleportTransition);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity livingEntity)
            rewardChallenger(livingEntity);
        if (level() instanceof ServerLevel serverLevel) {
            for (LivingEntity livingEntity : NarakaEntityUtils.findEntitiesByUUID(serverLevel, stigmatizedEntities, LivingEntity.class)) {
                LockedHealthHelper.release(livingEntity);
                StigmaHelper.removeStigma(livingEntity);
            }
            shadowController.killShadows(serverLevel);
        }
        super.die(damageSource);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
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
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("HurtDamageLimit", hurtDamageLimit);
        compound.putBoolean("HibernateMode", hibernateMode);
        if (spawnPosition != null)
            compound.put("SpawnPosition", NbtUtils.writeBlockPos(spawnPosition));
        NarakaNbtUtils.writeCollection(compound, "StigmatizedEntities", stigmatizedEntities, NarakaEntityUtils::writeUUID, registryAccess());
        NarakaNbtUtils.writeCollection(compound, "WatchingEntities", watchingEntities, NarakaEntityUtils::writeUUID, registryAccess());
        shadowController.save(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("HurtDamageLimit"))
            hurtDamageLimit = compound.getFloat("HurtDamageLimit");
        if (compound.contains("HibernatedMode")) {
            hibernateMode = compound.getBoolean("HibernatedMode");
            if (hibernateMode && level() instanceof ServerLevel level)
                startHibernateMode(level);
        }
        NbtUtils.readBlockPos(compound, "SpawnPosition").ifPresent(pos -> spawnPosition = pos);
        NarakaNbtUtils.readCollection(compound, "StigmatizedEntities", () -> stigmatizedEntities, NarakaEntityUtils::readUUID, registryAccess());
        NarakaNbtUtils.readCollection(compound, "WatchingEntities", () -> watchingEntities, NarakaEntityUtils::readUUID, registryAccess());
        shadowController.load(compound);
    }
}
