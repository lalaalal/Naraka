package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncProgressOverlayExtensionPacket;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.NarakaPortalBlock;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.origin_herobrine.ChargingSkill;
import com.yummy.naraka.world.entity.ai.skill.origin_herobrine.SwordSwingSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.overlay.NarakaProgressOverlayExtensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class OriginHerobrine extends AbstractHerobrine {
    public static final EntityDataAccessor<List<SoulType>> ABSORBED_SOUL_TYPES = SynchedEntityData.defineId(OriginHerobrine.class, NarakaEntityDataSerializers.SOUL_TYPES);

    public static final float[] HEALTH_BY_PHASE = {528};
    public static final BossEvent.BossBarColor[] PROGRESS_COLOR_BY_PHASE = {BossEvent.BossBarColor.WHITE};
    public static final BlockPos SPAWN_POSITION = new BlockPos(0, 68, 0);
    public static final int PROTECTION_PER_LEVEL = 66;

    private final SwordSwingSkill swordSwingSkill = registerSkill(this, SwordSwingSkill::new, HerobrineAnimationLocations.SWORD_ATTACK, HerobrineAnimationLocations.SWORD_ATTACK_SPIN);
    private final ChargingSkill chargingSkill = registerSkill(9, new ChargingSkill(this, swordSwingSkill), HerobrineAnimationLocations.CHARGING);

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
        super(entityType, level, false);
        this.xpReward = 12000;

        registerAnimation(HerobrineAnimationLocations.PHASE_4_IDLE);
        registerAnimation(HerobrineAnimationLocations.PHASE_4_SPAWN);

        updateAnimation(HerobrineAnimationLocations.PHASE_4_SPAWN);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ABSORBED_SOUL_TYPES, List.of());
    }

    @Override
    protected void registerGoals() {

    }

    protected void updateAnimationOnSkillEnd(Skill<?> skill) {

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
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypeTags.IS_PROJECTILE) || source.is(DamageTypeTags.IS_EXPLOSION) || super.isInvulnerableTo(source);

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof CorruptedStar)
            hurtByStar = true;
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
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
                        .computeIfAbsent(SpawnData.TYPE, "origin_herobrine_spawn_data");
                spawnData.setSpawned(false);
            }
            this.level().broadcastEntityEvent(this, (byte) 60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public boolean isCurrentlyGlowing() {
        return true;
    }

    @Override
    public int getTeamColor() {
        return FastColor.ARGB32.color(alpha, 0xffffff);
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
    protected void customServerAiStep() {
        phaseManager.updatePhase(bossEvent);
        super.customServerAiStep();
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
    protected boolean canAddPassenger(Entity passenger) {
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
    public void addAdditionalSaveData(CompoundTag output) {
        super.addAdditionalSaveData(output);
        output.putInt("ProtectionLevel", protectionLevel);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag input) {
        super.readAdditionalSaveData(input);
        if (input.contains("ProtectionLevel"))
            protectionLevel = input.getInt("ProtectionLevel");
        else
            protectionLevel = 7;
    }

    @Override
    protected Fireball createFireball(ServerLevel level) {
        return new NarakaFireball(this, Vec3.ZERO, level);
    }

    @Override
    public Optional<ShadowController> getShadowController() {
        return Optional.empty();
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {

    }

    @Override
    public List<Afterimage> getAfterimages() {
        return List.of();
    }

    @Override
    public void stigmatizeEntity(ServerLevel level, LivingEntity target) {

    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {

    }

    public static class SpawnData extends SavedData {
        public static final Codec<SpawnData> CODEC = Codec.BOOL
                .xmap(SpawnData::new, SpawnData::isSpawned);

        public static final Factory<SpawnData> TYPE = new Factory<>(
                SpawnData::new, SpawnData::create, DataFixTypes.LEVEL
        );

        private boolean spawned;

        private static SpawnData create(CompoundTag tag, HolderLookup.Provider registries) {
            boolean spawned = tag.getBoolean("Spawned");
            return new SpawnData(spawned);
        }

        public SpawnData() {
            this.spawned = false;
        }

        @Override
        public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
            tag.putBoolean("Spawned", spawned);
            return tag;
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
