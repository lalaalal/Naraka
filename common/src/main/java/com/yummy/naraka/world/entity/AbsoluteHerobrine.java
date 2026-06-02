package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.NarakaPortalBlock;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.absolute_herobrine.ChargingSkill;
import com.yummy.naraka.world.entity.ai.skill.absolute_herobrine.SwordSwingSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
import java.util.List;

public class AbsoluteHerobrine extends SkillUsingMob implements Enemy {
    public static final EntityDataAccessor<List<SoulType>> ABSORBED_SOUL_TYPES = SynchedEntityData.defineId(AbsoluteHerobrine.class, NarakaEntityDataSerializers.SOUL_TYPES);

    public static final float[] HEALTH_BY_PHASE = {528};
    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.WHITE};
    public static final BlockPos SPAWN_POSITION = new BlockPos(0, 68, 0);

    private final SwordSwingSkill swordSwingSkill = registerSkill(this, SwordSwingSkill::new, HerobrineAnimationIdentifiers.SWORD_ATTACK, HerobrineAnimationIdentifiers.SWORD_ATTACK_SPIN);
    private final ChargingSkill chargingSkill = registerSkill(9, new ChargingSkill(this, swordSwingSkill), HerobrineAnimationIdentifiers.CHARGING);

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, BossEvent.BossBarColor.YELLOW, this, bossEvent);
    private boolean hurtByStar;
    private final List<SoulType> absorbedSoulTypes = new ArrayList<>();

    private int protectedHealth = 66 * 7;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_ABSORPTION, 1024)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.SAFE_FALL_DISTANCE, 256)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0)
                .add(Attributes.JUMP_STRENGTH, 0)
                .add(Attributes.FLYING_SPEED, 0.5f)
                .add(Attributes.MAX_HEALTH, 528);
    }

    public AbsoluteHerobrine(EntityType<? extends AbsoluteHerobrine> entityType, Level level) {
        super(entityType, level);

        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_SPAWN);

        updateAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);

        skillManager.runOnSkillEnd(this::updateAnimationOnSkillEnd);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ABSORBED_SOUL_TYPES, List.of());
    }

    protected void updateAnimationOnSkillEnd(Skill<?> skill) {
        if (!skill.hasLinkedSkill()) {
            setAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        }
    }

    public int getSoulStack() {
        return entityData.get(ABSORBED_SOUL_TYPES).size();
    }

    public void absorbSoul(SoulType soulType) {
        absorbedSoulTypes.add(soulType);
        entityData.set(ABSORBED_SOUL_TYPES, List.copyOf(absorbedSoulTypes));
    }

    public List<SoulType> getAbsorbedSoulTypes() {
        return absorbedSoulTypes;
    }

    public boolean isHurtByStar() {
        return hurtByStar;
    }

    public void resetHurtByStar() {
        hurtByStar = false;
    }

    public void removeProtection(int protectionCount) {
        protectedHealth = Math.max(protectedHealth - protectionCount * 66, 0);
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource damageSource) {
        return damageSource.is(DamageTypeTags.IS_PROJECTILE) || damageSource.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(level, damageSource);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        if (damageSource.getDirectEntity() instanceof CorruptedStar)
            hurtByStar = true;
        return super.hurtServer(level, damageSource, amount);
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource damageSource, float amount) {
        super.actuallyHurt(level, damageSource, amount);
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            setHealth(Math.max(getHealth(), protectedHealth));
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (!level().isClientSide()) {
            level().setBlock(NarakaPortalBlock.IN_NARAKA_DIMENSION_POSITION, NarakaBlocks.NARAKA_PORTAL.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        phaseManager.updatePhase(bossEvent);
        super.customServerAiStep(level);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public int getAirSupply() {
        return getMaxAirSupply();
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return effectInstance.is(MobEffects.ABSORPTION);
    }

    @Override
    public boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return false;
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
    public boolean canFreeze() {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        bossEvent.removePlayer(serverPlayer);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("ProtectedHealth", protectedHealth);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        protectedHealth = input.getIntOr("ProtectedHealth", 66 * 7);
    }
}
