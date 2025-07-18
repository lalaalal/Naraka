package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.goal.FollowOwnerGoal;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.herobrine.*;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShadowHerobrine extends AbstractHerobrine implements TraceableEntity {
    protected static final EntityDataAccessor<Integer> ALPHA = SynchedEntityData.defineId(ShadowHerobrine.class, EntityDataSerializers.INT);
    protected static final int MAX_ALPHA = 0xaa;

    protected final ShadowPunchSkill punchSkill = registerSkill(1, this, ShadowPunchSkill::new, HerobrineAnimationLocations.COMBO_ATTACK_1);
    protected final DashSkill<ShadowHerobrine> dashSkill = registerSkill(this, DashSkill::new);
    protected final ShadowFlickerSkill flickerSkill = registerSkill(10, new ShadowFlickerSkill(this, dashSkill, punchSkill));

    protected final PickaxeSlashSkill<AbstractHerobrine> pickaxeSlashSkill = registerSkill(PickaxeSlashSkill.single(this), HerobrineAnimationLocations.PICKAXE_SLASH_SINGLE);

    protected final SimpleComboAttackSkill finalComboAttack3 = registerSkill(SimpleComboAttackSkill.combo3(this), HerobrineAnimationLocations.FINAL_COMBO_ATTACK_3);
    protected final SimpleComboAttackSkill finalComboAttack2 = registerSkill(SimpleComboAttackSkill.combo2(this, finalComboAttack3), HerobrineAnimationLocations.FINAL_COMBO_ATTACK_2);
    protected final SimpleComboAttackSkill finalComboAttack1 = registerSkill(SimpleComboAttackSkill.combo1(this, finalComboAttack2), HerobrineAnimationLocations.FINAL_COMBO_ATTACK_1);

    @Nullable
    private Herobrine herobrine;
    @Nullable
    private UUID herobrineUUID;
    private boolean reduceAlpha = false;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return AbstractHerobrine.getAttributeSupplier()
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MAX_HEALTH, 150);
    }

    public static ShadowHerobrine createInstantFinalShadow(Mob spawner, Vec3 position) {
        ShadowHerobrine shadowHerobrine = createInstantFinalShadow(spawner);
        shadowHerobrine.setPos(position);
        shadowHerobrine.forceSetRotation(spawner.getYRot(), spawner.getXRot());

        return shadowHerobrine;
    }

    public static ShadowHerobrine createInstantFinalShadow(Mob spawner) {
        ShadowHerobrine shadowHerobrine = new ShadowHerobrine(spawner.level(), true, true);
        shadowHerobrine.getSkillManager().enableOnly(List.of());
        shadowHerobrine.setTarget(spawner.getTarget());
        shadowHerobrine.goalSelector.removeAllGoals(goal -> true);
        shadowHerobrine.setNoGravity(true);

        return shadowHerobrine;
    }

    protected ShadowHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level) {
        super(entityType, level, true);
        skillManager.enableOnly(List.of(punchSkill));
        entityData.set(DISPLAY_SCARF, true);
        entityData.set(DISPLAY_PICKAXE, false);
        registerAnimation(HerobrineAnimationLocations.SHADOW_SUMMONED);
    }

    public ShadowHerobrine(Level level, Herobrine herobrine) {
        this(NarakaEntityTypes.SHADOW_HEROBRINE.get(), level);
        this.herobrine = herobrine;
        this.herobrineUUID = herobrine.getUUID();
    }

    public ShadowHerobrine(Level level, boolean finalModel, boolean instant) {
        this(NarakaEntityTypes.SHADOW_HEROBRINE.get(), level);
        entityData.set(FINAL_MODEL, finalModel);
        if (instant)
            skillManager.runOnSkillEnd(skill -> reduceAlpha = true);
    }

    public int getAlpha() {
        return entityData.get(ALPHA);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ALPHA, 0x01);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        if (reduceAlpha) {
            entityData.set(ALPHA, Math.max(0, getAlpha() - 15));
        } else if (!isFinalModel() || displayPickaxe()) {
            entityData.set(ALPHA, Math.min(MAX_ALPHA, getAlpha() + 10));
        }
        if (getAlpha() == 0)
            discard();
    }

    public void usePunchOnly() {
        skillManager.enableOnly(List.of(punchSkill));
    }

    public void useFlicker() {
        skillManager.enableOnly(List.of(punchSkill, flickerSkill));
    }

    public boolean otherShadowNotUsingSkill(ServerLevel level) {
        return !this.getHerobrine()
                .map(herobrine -> herobrine.getShadowController().someoneJustUsedSkill(level))
                .orElse(false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new FollowOwnerGoal<>(this));
        goalSelector.addGoal(3, new MoveToTargetGoal(this, 1, 64, 40, 0.6f));
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    public Optional<Herobrine> getHerobrine() {
        if (herobrineUUID == null)
            return Optional.empty();
        if (herobrine == null && level() instanceof ServerLevel serverLevel)
            return Optional.ofNullable(NarakaEntityUtils.findEntityByUUID(serverLevel, herobrineUUID, Herobrine.class));
        if (herobrine != null && herobrine.isRemoved()) {
            herobrineUUID = null;
            return Optional.empty();
        }
        return Optional.ofNullable(herobrine);
    }

    @Override
    protected Fireball createFireball(ServerLevel level) {
        return getHerobrine()
                .map(herobrine -> herobrine.createFireball(level))
                .orElseGet(() -> new NarakaFireball(this, Vec3.ZERO, level()));
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
        Stigma stigma = StigmaHelper.get(target);
        if (stigma.value() < 1)
            return;

        StigmaHelper.removeStigma(target);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE);

        float baseDamage = target.getMaxHealth() * 0.25f;
        target.hurtServer(level, NarakaDamageSources.stigmaConsume(this), baseDamage * stigma.value());
        getHerobrine().ifPresent(herobrine -> herobrine.getShadowController().summonShadowHerobrine(level));
    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {
        getHerobrine().ifPresent(herobrine -> herobrine.collectStigma(level, target, original));
    }

    public float getHurtDamageLimit() {
        return 20;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurtServer(serverLevel, source, amount);
        if (getHerobrine().isPresent())
            amount = Math.min(amount, getHurtDamageLimit());
        if (animationTickLeft > 0 || isFinalModel())
            return false;
        return super.hurtServer(serverLevel, source, amount);
    }

    @Override
    protected void actuallyHurt(ServerLevel level, DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(level, damageSource, damageAmount);
        getHerobrine().ifPresent(herobrine -> {
            herobrine.getShadowController().broadcastShadowHerobrineHurt(level, this);
        });
    }

    @Override
    protected AABB makeBoundingBox(Vec3 position) {
        if (isFinalModel()) {
            AABB boundingBox = super.makeBoundingBox(position);
            return boundingBox.setMaxY(boundingBox.maxY - 1.7);
        }
        return super.makeBoundingBox(position);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        int data = getHerobrine().map(Herobrine::getId).orElse(-1);
        return new ClientboundAddEntityPacket(this, entity, data);
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        int herobrineId = packet.getData();
        if (level().getEntity(herobrineId) instanceof Herobrine entity) {
            this.herobrine = entity;
            this.herobrineUUID = entity.getUUID();
            updateAnimation(HerobrineAnimationLocations.SHADOW_SUMMONED);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (herobrine != null)
            compound.store("Herobrine", UUIDUtil.CODEC, herobrine.getUUID());
        compound.putBoolean("FinalModel", isFinalModel());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        compound.read("Herobrine", UUIDUtil.CODEC).ifPresent(uuid -> this.herobrineUUID = uuid);
        if (isFinalModel())
            reduceAlpha = true;
        compound.getBoolean("FinalModel").ifPresent(this::setFinalModel);
    }

    @Override
    @Nullable
    public Entity getOwner() {
        getHerobrine();
        return herobrine;
    }
}
