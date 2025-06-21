package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.goal.FollowOwnerGoal;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.*;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShadowHerobrine extends AbstractHerobrine implements TraceableEntity {
    protected static final EntityDataAccessor<Boolean> FINAL_MODEL = SynchedEntityData.defineId(ShadowHerobrine.class, EntityDataSerializers.BOOLEAN);

    protected final ShadowPunchSkill punchSkill = registerSkill(1, this, ShadowPunchSkill::new, AnimationLocations.COMBO_ATTACK_1);
    protected final DashSkill<ShadowHerobrine> dashSkill = registerSkill(this, DashSkill::new);
    protected final ShadowFlickerSkill flickerSkill = registerSkill(10, new ShadowFlickerSkill(this, dashSkill, punchSkill));

    protected final SimpleComboAttackSkill finalComboAttack3 = registerSkill(SimpleComboAttackSkill.combo3(this), AnimationLocations.FINAL_COMBO_ATTACK_3);
    protected final SimpleComboAttackSkill finalComboAttack2 = registerSkill(SimpleComboAttackSkill.combo2(finalComboAttack3, this), AnimationLocations.FINAL_COMBO_ATTACK_2);
    protected final SimpleComboAttackSkill finalComboAttack1 = registerSkill(SimpleComboAttackSkill.combo1(finalComboAttack2, this), AnimationLocations.FINAL_COMBO_ATTACK_1);

    @Nullable
    private Herobrine herobrine;
    @Nullable
    private UUID herobrineUUID;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return AbstractHerobrine.getAttributeSupplier()
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MAX_HEALTH, 150);
    }

    public static ShadowHerobrine createInstantFinalShadow(Level level, Mob mob) {
        ShadowHerobrine shadowHerobrine = new ShadowHerobrine(mob.level(), true, true);
        shadowHerobrine.setPos(mob.position());
        shadowHerobrine.forceSetRotation(mob.getYRot(), mob.getXRot());
        shadowHerobrine.getSkillManager().enableOnly(List.of());
        shadowHerobrine.setTarget(mob.getTarget());
        level.addFreshEntity(shadowHerobrine);

        return shadowHerobrine;
    }

    protected ShadowHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level) {
        super(entityType, level, true);
        skillManager.enableOnly(List.of(punchSkill));
        entityData.set(DISPLAY_SCARF, true);
        entityData.set(DISPLAY_PICKAXE, false);
        registerAnimation(AnimationLocations.SHADOW_SUMMONED);
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
            skillManager.runOnSkillEnd(skill -> discard());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FINAL_MODEL, false);
    }

    @Override
    protected void updateAnimationOnSkillEnd(Skill<?> skill) {
        if (!skill.hasLinkedSkill()) {
            if (isFinalModel()) {
                setAnimation(AnimationLocations.PHASE_3_IDLE);
            } else {
                setAnimation(AnimationLocations.IDLE);
            }
        }
    }

    public boolean isFinalModel() {
        return entityData.get(FINAL_MODEL);
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
        goalSelector.addGoal(3, new MoveToTargetGoal(this, 1, 64, 0, 40, 0.6f));
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
        target.hurtServer(level, NarakaDamageSources.stigmaConsume(this), 6 * stigma.value());
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
        if (animationTickCount > 0)
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
            updateAnimation(AnimationLocations.SHADOW_SUMMONED);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (herobrine != null)
            compound.putString("Herobrine", herobrine.getStringUUID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        Optional<String> uuid = compound.getString("Herobrine");
        uuid.ifPresent(string -> this.herobrineUUID = UUID.fromString(string));
    }

    @Override
    @Nullable
    public Entity getOwner() {
        getHerobrine();
        return herobrine;
    }
}
