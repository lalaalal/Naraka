package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncProgressOverlayExtensionPacket;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.NarakaPortalBlock;
import com.yummy.naraka.world.entity.ai.skill.origin_herobrine.ChargingSkill;
import com.yummy.naraka.world.entity.ai.skill.origin_herobrine.SwordSwingSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.overlay.NarakaProgressOverlayExtensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.datafix.DataFixTypes;
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
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.*;

public class OriginHerobrine extends SkillUsingMob implements Enemy {
    public static final EntityDataAccessor<List<SoulType>> ABSORBED_SOUL_TYPES = SynchedEntityData.defineId(OriginHerobrine.class, NarakaEntityDataSerializers.SOUL_TYPES);

    public static final float[] HEALTH_BY_PHASE = {528};
    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.WHITE};
    public static final BlockPos SPAWN_POSITION = new BlockPos(0, 68, 0);
    public static final int PROTECTION_PER_LEVEL = 66;

    private final SwordSwingSkill swordSwingSkill = registerSkill(this, SwordSwingSkill::new, HerobrineAnimationIdentifiers.SWORD_ATTACK, HerobrineAnimationIdentifiers.SWORD_ATTACK_SPIN);
    private final ChargingSkill chargingSkill = registerSkill(9, new ChargingSkill(this, swordSwingSkill), HerobrineAnimationIdentifiers.CHARGING);

    private final ServerBossEvent bossEvent = new ServerBossEvent(getName(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
    private final PhaseManager phaseManager = new PhaseManager(HEALTH_BY_PHASE, PROGRESS_COLOR_BY_PHASE, BossEvent.BossBarColor.YELLOW, this, bossEvent);
    private boolean hurtByStar;
    private final List<SoulType> absorbedSoulTypes = new ArrayList<>();
    private final Map<SoulType, Float> soulTypeAlpha = new LinkedHashMap<>();

    private int protectionLevel = 7;
    private int alpha = 0xff;

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

    public OriginHerobrine(EntityType<? extends OriginHerobrine> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 12000;

        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_SPAWN);

        updateAnimation(HerobrineAnimationIdentifiers.PHASE_4_SPAWN);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ABSORBED_SOUL_TYPES, List.of());
    }

    public int getSoulStack() {
        return entityData.get(ABSORBED_SOUL_TYPES).size();
    }

    public void absorbSoul(SoulType soulType) {
        absorbedSoulTypes.add(soulType);
        entityData.set(ABSORBED_SOUL_TYPES, List.copyOf(absorbedSoulTypes));
    }

    public void resetAbsorbedSouls() {
        absorbedSoulTypes.clear();
        entityData.set(ABSORBED_SOUL_TYPES, List.of());
    }

    public List<SoulType> getAbsorbedSoulTypes() {
        return entityData.get(ABSORBED_SOUL_TYPES);
    }

    public boolean isHurtByStar() {
        return hurtByStar;
    }

    public void resetHurtByStar() {
        hurtByStar = false;
    }

    public void removeProtection(int protectionCount) {
        protectionLevel = Math.max(protectionLevel - protectionCount, 0);
        CustomPacketPayload payload = SyncProgressOverlayExtensionPacket.update(
                bossEvent, NarakaProgressOverlayExtensionTypes.ORIGIN_HEROBRINE.get().createData(protectionLevel)
        );
        NetworkManager.clientbound().send(players, payload);
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
            setHealth(Math.max(getHealth(), protectionLevel * PROTECTION_PER_LEVEL));
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        resetAbsorbedSouls();
        CustomPacketPayload payload = SyncProgressOverlayExtensionPacket.remove(bossEvent);
        NetworkManager.clientbound().send(players(), payload);
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
        if (level().isClientSide())
            updateSoulTypeAlpha();
    }

    @Override
    protected void tickDeath() {
        this.deathTime += 1;
        alpha = Math.max(alpha - 2, 0);
        if (this.deathTime >= 200 && level() instanceof ServerLevel level && !this.isRemoved()) {
            level.setBlock(NarakaPortalBlock.IN_NARAKA_DIMENSION_POSITION, NarakaBlocks.NARAKA_PORTAL.get().defaultBlockState(), Block.UPDATE_ALL);

            if (level.dimension().equals(NarakaDimensions.NARAKA)) {
                SpawnData spawnData = level.getDataStorage()
                        .computeIfAbsent(SpawnData.TYPE);
                spawnData.setSpawned(false);
            }
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    public int getAlpha() {
        return alpha;
    }

    private void updateSoulTypeAlpha() {
        List<SoulType> absorbedSoulTypes = getAbsorbedSoulTypes();
        for (SoulType soulType : absorbedSoulTypes) {
            float alpha = soulTypeAlpha.computeIfAbsent(soulType, key -> 0f);
            soulTypeAlpha.put(soulType, Math.min(alpha + 0.01f, 1f));
        }

        for (SoulType soulType : Set.copyOf(soulTypeAlpha.keySet())) {
            if (!absorbedSoulTypes.contains(soulType)) {
                float alpha = soulTypeAlpha.computeIfAbsent(soulType, key -> 0f);
                float newAlpha = Math.max(alpha - 0.04f, 0);
                if (newAlpha > 0)
                    soulTypeAlpha.put(soulType, newAlpha);
                else
                    soulTypeAlpha.remove(soulType);
            }
        }
    }

    public Map<SoulType, Float> getSoulTypeAlpha() {
        return soulTypeAlpha;
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

        CustomPacketPayload payload = SyncProgressOverlayExtensionPacket.register(
                bossEvent, NarakaProgressOverlayExtensionTypes.ORIGIN_HEROBRINE.get().createData(protectionLevel)
        );
        NetworkManager.clientbound().send(serverPlayer, payload);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        bossEvent.removePlayer(serverPlayer);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("ProtectionLevel", protectionLevel);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        protectionLevel = input.getIntOr("ProtectionLevel", 7);
    }

    public static class SpawnData extends SavedData {
        public static final Codec<SpawnData> CODEC = Codec.BOOL
                .xmap(SpawnData::new, SpawnData::isSpawned);

        public static final SavedDataType<SpawnData> TYPE = new SavedDataType<>(
                "origin_herobrine_spawned", SpawnData::new, CODEC, DataFixTypes.LEVEL
        );

        private boolean spawned;

        public SpawnData() {
            this.spawned = false;
        }

        public SpawnData(boolean spawned) {
            this.spawned = spawned;
        }

        public boolean isSpawned() {
            return spawned;
        }

        public void setSpawned(boolean spawned) {
            this.spawned = spawned;
            setDirty();
        }
    }
}
