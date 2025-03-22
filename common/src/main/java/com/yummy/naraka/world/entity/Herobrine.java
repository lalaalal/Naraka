package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.network.NarakaClientboundEventHandler;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.SyncAfterimagePayload;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SummonShadowSkill;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Herobrine extends AbstractHerobrine {
    protected static final EntityDataAccessor<Float> SCARF_ROTATION_DEGREE = SynchedEntityData.defineId(Herobrine.class, EntityDataSerializers.FLOAT);
    private static final float[] HEALTH_BY_PHASE = {106, 210, 350};

    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.BLUE, BossEvent.BossBarColor.YELLOW, BossEvent.BossBarColor.RED};
    public static final int MAX_HURT_DAMAGE_LIMIT = Integer.MAX_VALUE;
    public static final int MAX_ACCUMULATED_DAMAGE_TICK_COUNT = 40;

    public final AnimationState summonShadowAnimationState = new AnimationState();

    protected final SummonShadowSkill summonShadowSkill = registerSkill(this, SummonShadowSkill::new, summonShadowAnimationState);

    private final List<Skill<?>> HIBERNATED_MODE_PHASE_1_SKILLS = List.of(throwFireballSkill, blockingSkill);
    private final List<Skill<?>> HIBERNATED_MODE_PHASE_2_SKILLS = List.of(stigmatizeEntitiesSkill, blockingSkill, summonShadowSkill);
    private final List<Skill<?>> PHASE_1_SKILLS = List.of(punchSkill, dashSkill, dashAroundSkill, throwFireballSkill, rushSkill);
    private final List<Skill<?>> PHASE_2_SKILLS = List.of(punchSkill, dashSkill, dashAroundSkill, throwFireballSkill, rushSkill, summonShadowSkill);

    private final List<List<Skill<?>>> HIBERNATED_MODE_SKILL_BY_PHASE = List.of(
            List.of(), HIBERNATED_MODE_PHASE_1_SKILLS, HIBERNATED_MODE_PHASE_2_SKILLS, List.of()
    );
    private final List<List<Skill<?>>> SKILLS_BY_PHASE = List.of(
            List.of(), PHASE_1_SKILLS, PHASE_2_SKILLS, List.of()
    );

    private final List<Projectile> ignoredProjectiles = new ArrayList<>();
    private final Set<UUID> stigmatizedEntities = new HashSet<>();
    private final Set<UUID> watchingEntities = new HashSet<>();
    private final Set<UUID> shadowHerobrines = new HashSet<>();
    protected final List<Afterimage> afterimages = new ArrayList<>();

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, this, bossEvent);
    private float hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
    private float averageHurtDamage;
    private int hurtCount = 0;

    private boolean hibernateMode = false;

    private int accumulatedDamageTickCount;
    private float accumulatedHurtDamage;

    private @Nullable BlockPos spawnPosition;

    private float prevScarfRotation = 0;
    private final List<Float> scarfWaveSpeedList = new ArrayList<>();

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level, false);

        bossEvent.setDarkenScreen(true)
                .setPlayBossMusic(true);
        phaseManager.addPhaseChangeListener(this::updateMusic);
        phaseManager.addPhaseChangeListener(this::updateUsingSkills);

        skillManager.enableOnly(PHASE_1_SKILLS);
        for (int i = 0; i < NarakaMod.config().herobrineScarfPartitionNumber.getValue(); i++)
            scarfWaveSpeedList.add(1f);
    }

    public Herobrine(Level level, Vec3 pos) {
        this(NarakaEntityTypes.HEROBRINE.get(), level);
        setPos(pos);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SCARF_ROTATION_DEGREE, 0f);
    }

    public void setSpawnPosition(BlockPos pos) {
        this.spawnPosition = pos;
    }

    private void updateMusic(int prevPhase, int currentPhase) {
        NarakaClientboundEventPacket.Event event = NarakaClientboundEventHandler.musicEventByPhase(currentPhase);
        CustomPacketPayload packet = new NarakaClientboundEventPacket(event);
        NetworkManager.sendToPlayers(bossEvent.getPlayers(), packet);
    }

    private void updateUsingSkills(int prevPhase, int currentPhase) {
        skillManager.enableOnly(SKILLS_BY_PHASE.get(currentPhase));
    }

    private <T> Collection<T> getEntities(Collection<UUID> uuids, Class<T> type) {
        if (level() instanceof ServerLevel serverLevel)
            return NarakaEntityUtils.findEntitiesByUUID(serverLevel, uuids, type);
        return List.of();
    }

    public int getPhase() {
        return phaseManager.getCurrentPhase();
    }

    public float getHurtDamageLimit() {
        return hurtDamageLimit;
    }

    @Override
    public void stigmatizeEntity(LivingEntity target) {
        if (!target.getType().is(NarakaEntityTypeTags.HEROBRINE) && getPhase() > 1) {
            StigmaHelper.increaseStigma(target, this, true);
            stigmatizedEntities.add(target.getUUID());
            watchingEntities.add(target.getUUID());
        }
    }

    @Override
    public void collectStigma(Stigma stigma) {
        this.heal(stigma.value() * 22);
    }

    @Override
    public void heal(float healAmount) {
        float maxHealth = phaseManager.getActualCurrentPhaseMaxHealth();
        float healthAfterHeal = Mth.clamp(getHealth() + healAmount, getHealth(), maxHealth);
        setHealth(healthAfterHeal);
    }

    public int getShadowCount() {
        return shadowHerobrines.size();
    }

    public void summonShadowHerobrine() {
        if (shadowHerobrines.size() >= NarakaMod.config().maxShadowHerobrineSpawn.getValue())
            return;
        ShadowHerobrine shadowHerobrine = new ShadowHerobrine(level(), this);
        BlockPos randomPos = NarakaUtils.randomBlockPos(random, blockPosition(), 4);
        BlockPos spawnPos = NarakaUtils.findAir(level(), randomPos, Direction.UP);
        shadowHerobrine.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
        getEntities(shadowHerobrines, ShadowHerobrine.class)
                .stream().findAny()
                .ifPresent(existing -> shadowHerobrine.setHealth(existing.getHealth()));
        level().addFreshEntity(shadowHerobrine);
        shadowHerobrines.add(shadowHerobrine.getUUID());
    }

    public void broadcastShadowHerobrineHurt(ShadowHerobrine shadowHerobrine) {
        for (ShadowHerobrine entity : getEntities(shadowHerobrines, ShadowHerobrine.class)) {
            if (entity != shadowHerobrine)
                entity.setHealth(shadowHerobrine.getHealth());
        }
        if (shadowHerobrine.isDeadOrDying()) {
            shadowHerobrines.clear();
            skillManager.interrupt();
            startWeakness();
            if (hibernateMode)
                stopHibernateMode();
        }
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {
        if (!level().isClientSide) {
            SyncAfterimagePayload payload = new SyncAfterimagePayload(this, afterimage);
            NetworkManager.sendToPlayers(bossEvent.getPlayers(), payload);
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
        int partitionNumber = NarakaMod.config().herobrineScarfPartitionNumber.getValue();
        if (scarfWaveSpeedList.size() < partitionNumber) {
            for (int i = 0; i < partitionNumber - scarfWaveSpeedList.size(); i++)
                scarfWaveSpeedList.add(scarfWaveSpeedList.getLast());
        }
        if (tickCount % 10 == 0) {
            float maxRotation = NarakaMod.config().herobrineScarfDefaultRotation.getValue();
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
        float maxRotation = NarakaMod.config().herobrineScarfDefaultRotation.getValue();
        entityData.set(SCARF_ROTATION_DEGREE, Mth.clamp(scarfRotationDegree, 0, maxRotation));
    }

    @Override
    protected void customServerAiStep() {
        updateAccumulatedDamage();
        updateScarfRotation();

        tryAvoidProjectile();
        collectStigma();
        phaseManager.updatePhase(bossEvent);

        super.customServerAiStep();
    }

    private void updateAccumulatedDamage() {
        if (accumulatedDamageTickCount > MAX_ACCUMULATED_DAMAGE_TICK_COUNT) {
            accumulatedDamageTickCount = 0;
            accumulatedHurtDamage = 0;
        }
        accumulatedDamageTickCount += 1;
    }

    private void collectStigma() {
        final int waitingTick = NarakaMod.config().herobrineTakingStigmaTick.getValue();
        if (level() instanceof ServerLevel serverLevel) {
            watchingEntities.removeIf(uuid -> {
                LivingEntity entity = NarakaEntityUtils.findEntityByUUID(serverLevel, uuid, LivingEntity.class);
                if (entity == null)
                    return false;
                if (entity.isDeadOrDying()) {
                    stigmatizedEntities.remove(uuid);
                    return true;
                }
                return StigmaHelper.collectStigmaAfter(entity, this, waitingTick);
            });
        }
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
        NetworkManager.sendToPlayer(serverPlayer, packet);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        bossEvent.removePlayer(serverPlayer);
        CustomPacketPayload packet = new NarakaClientboundEventPacket(NarakaClientboundEventPacket.Event.STOP_MUSIC);
        NetworkManager.sendToPlayer(serverPlayer, packet);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurt(source, damage);
        if (hibernateMode)
            return true;
        if (source.getEntity() == this)
            return false;

        float actualDamage = getActualDamage(source, damage);
        updateHurtDamageLimit(actualDamage);
        float limitedDamage = Math.min(damage, hurtDamageLimit);
        if (updateHibernateMode(source, getActualDamage(source, limitedDamage)))
            return true;

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            if (source.getDirectEntity() != null)
                lookAt(source.getDirectEntity(), 360, 0);
            skillManager.setCurrentSkillIfAbsence(blockingSkill);
            return false;
        }
        return super.hurt(source, limitedDamage);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
        accumulatedHurtDamage += damageAmount;
        if (accumulatedHurtDamage > 15 || random.nextDouble() < 0.25f)
            switchWithShadowHerobrine();
    }

    private void switchWithShadowHerobrine() {
        if (isDeadOrDying())
            return;
        accumulatedHurtDamage = 0;
        accumulatedDamageTickCount = 0;
        getEntities(shadowHerobrines, ShadowHerobrine.class)
                .stream()
                .findAny()
                .ifPresent(shadowHerobrine -> {
                    Vec3 originalHerobrinePosition = position();
                    this.setPos(shadowHerobrine.position());
                    shadowHerobrine.setPos(originalHerobrinePosition);
                    shadowHerobrine.startWeakness();
                });
    }

    private boolean updateHibernateMode(DamageSource source, float actualDamage) {
        if (source.getDirectEntity() instanceof NarakaFireball fireball && !fireball.hasTarget()) {
            startWeakness();
            if (hibernateMode)
                stopHibernateMode();
            return true;
        }

        return preserveCurrentPhaseMinimumHealth(actualDamage);
    }

    private boolean preserveCurrentPhaseMinimumHealth(float actualDamage) {
        if (getPhase() == phaseManager.getMaxPhase())
            return false;
        float healthAfterHurt = phaseManager.getCurrentPhaseHealth() + getAbsorptionAmount() - actualDamage;
        if (healthAfterHurt < 1) {
            setHealth(phaseManager.getActualPhaseMaxHealth(getPhase() + 1) + 1);
            startHibernateMode();
            return true;
        }

        return false;
    }

    private float getActualDamage(DamageSource source, float damage) {
        damage = getDamageAfterArmorAbsorb(source, damage);
        return getDamageAfterMagicAbsorb(source, damage);
    }

    private double correctRatio(double ratio) {
        final double scale = NarakaMod.config().herobrineHurtLimitCalculationRatioModifier.getValue();
        if (ratio > 5)
            ratio = NarakaUtils.log(4, ratio - 4) + 5;
        return Math.max(ratio * scale, 1.5);
    }

    private int calculateMaxHurtCount(double ratio) {
        final double scale = NarakaMod.config().herobrineMaxHurtCountCalculationModifier.getValue();
        return (int) Math.floor(6 * Math.pow(0.5, (ratio * scale) - 3) + 4);
    }

    private float calculateHurtDamageLimit(double ratio, int maxHurtCount) {
        int power = Math.max(maxHurtCount - hurtCount - 1, 0);
        return (float) Mth.clamp(Math.pow(ratio, power), 1, MAX_HURT_DAMAGE_LIMIT);
    }

    private float calculateAverageHurtDamage(float damage) {
        if (hurtCount == 0)
            return damage;
        return (averageHurtDamage + damage) / 2f;
    }

    private void updateHurtDamageLimit(float damage) {
        if (phaseManager.getCurrentPhase() < 3 && hurtDamageLimit > 1) {
            averageHurtDamage = calculateAverageHurtDamage(damage);

            double ratio = NarakaUtils.log(2, averageHurtDamage);
            int maxHurtCount = calculateMaxHurtCount(ratio);
            ratio = correctRatio(ratio);

            hurtDamageLimit = calculateHurtDamageLimit(ratio, maxHurtCount);

            hurtCount += 1;
            if (hurtDamageLimit <= 1)
                startHibernateMode();
        }
    }

    public boolean isHibernateMode() {
        return hibernateMode;
    }

    private void startHibernateMode() {
        if (spawnPosition != null)
            moveTo(spawnPosition.south(54), 0, 0);
        hibernateMode = true;
        skillManager.interrupt();
        skillManager.enableOnly(HIBERNATED_MODE_SKILL_BY_PHASE.get(getPhase()));
        NarakaAttributeModifiers.addAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.HIBERNATE_PREVENT_MOVING);
    }

    private void stopHibernateMode() {
        hurtCount = 0;
        averageHurtDamage = 0;
        hibernateMode = false;
        hurtDamageLimit = MAX_HURT_DAMAGE_LIMIT;
        skillManager.enableOnly(SKILLS_BY_PHASE.get(getPhase()));
        NarakaAttributeModifiers.removeAttributeModifier(this, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.HIBERNATE_PREVENT_MOVING);

        setHealth(getHealth() - 1);
    }

    @Override
    public Fireball createFireball() {
        NarakaFireball fireball = new NarakaFireball(this, Vec3.ZERO, level(), hibernateMode);
        fireball.setDamageCalculator(this::calculateFireballDamage);
        fireball.addHurtTargetListener((target, damage) -> stigmatizeEntity(target));
        fireball.addHurtTargetListener(this::updateHibernateModeOnTargetSurvivedFromFireball);

        return fireball;
    }

    private float calculateFireballDamage(NarakaFireball fireball) {
        if (!hibernateMode) {
            if (getTarget() != null)
                return getAttackDamage() + getTarget().getMaxHealth() * 0.05f;
            return getAttackDamage();
        }
        float distance = distanceTo(fireball);
        if (distance <= 1)
            return 66;
        return Mth.clamp(66f / (distance - 0.9f) + 9, 10, 66);
    }

    private void updateHibernateModeOnTargetSurvivedFromFireball(LivingEntity target, float damage) {
        if (getPhase() == 1 && hibernateMode && damage >= 66 && target.isAlive()) {
            stopHibernateMode();
            startWeakness();
        }
    }

    @Override
    public @Nullable Entity changeDimension(DimensionTransition transition) {
        for (ShadowHerobrine shadowHerobrine : getEntities(shadowHerobrines, ShadowHerobrine.class))
            shadowHerobrine.changeDimension(transition);
        return super.changeDimension(transition);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (damageSource.getEntity() instanceof LivingEntity livingEntity)
            rewardChallenger(livingEntity);
        for (LivingEntity livingEntity : getEntities(stigmatizedEntities, LivingEntity.class)) {
            LockedHealthHelper.release(livingEntity);
            StigmaHelper.removeStigma(livingEntity);
        }
        for (ShadowHerobrine shadowHerobrine : getEntities(shadowHerobrines, ShadowHerobrine.class))
            shadowHerobrine.kill();
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

    private CompoundTag writeUUID(UUID uuid, CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.putUUID("UUID", uuid);
        return compoundTag;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("HibernateMode", hibernateMode);
        if (spawnPosition != null)
            compound.put("SpawnPosition", NbtUtils.writeBlockPos(spawnPosition));
        NarakaNbtUtils.writeCollection(compound, "StigmatizedEntities", stigmatizedEntities, this::writeUUID, registryAccess());
        NarakaNbtUtils.writeCollection(compound, "WatchingEntities", watchingEntities, this::writeUUID, registryAccess());
        NarakaNbtUtils.writeCollection(compound, "ShadowHerobrines", shadowHerobrines, this::writeUUID, registryAccess());
    }

    private UUID readUUID(CompoundTag tag, HolderLookup.Provider provider) {
        return tag.getUUID("UUID");
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("HibernatedMode"))
            hibernateMode = compound.getBoolean("HibernatedMode");
        NbtUtils.readBlockPos(compound, "SpawnPosition").ifPresent(pos -> spawnPosition = pos);
        NarakaNbtUtils.readCollection(compound, "StigmatizedEntities", () -> stigmatizedEntities, this::readUUID, registryAccess());
        NarakaNbtUtils.readCollection(compound, "WatchingEntities", () -> watchingEntities, this::readUUID, registryAccess());
        NarakaNbtUtils.readCollection(compound, "ShadowHerobrines", () -> shadowHerobrines, this::readUUID, registryAccess());
    }
}
