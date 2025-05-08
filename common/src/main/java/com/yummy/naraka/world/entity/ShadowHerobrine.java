package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.goal.FollowOwnerGoal;
import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.PunchSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.UppercutSkill;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ShadowHerobrine extends AbstractHerobrine implements TraceableEntity {
    protected final UppercutSkill uppercutSkill = registerSkill(new UppercutSkill(null, this), AnimationLocations.COMBO_ATTACK_2);
    protected final PunchSkill punchSkill = registerSkill(new PunchSkill(uppercutSkill, this, 90, false), AnimationLocations.COMBO_ATTACK_1);

    @Nullable
    private Herobrine herobrine;
    @Nullable
    private UUID herobrineUUID;

    private final MoveToTargetGoal moveToTargetGoal = new MoveToTargetGoal(this, 1, 64);
    private final AvoidEntityGoal<LivingEntity> avoidTargetGoal = new AvoidEntityGoal<>(this,
            LivingEntity.class,
            8, 1.4, 2,
            entity -> getHerobrine()
                    .map(herobrine -> herobrine.getTarget() == entity)
                    .orElse(this.getTarget() == entity)
    );

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return AbstractHerobrine.getAttributeSupplier()
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MAX_HEALTH, 150);
    }

    protected ShadowHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level) {
        super(entityType, level, true);
        skillManager.runOnSkillSelect(this::preventUseSkillWithHerobrineInSameTime);
        skillManager.enableOnly(List.of(punchSkill));
    }

    private void preventUseSkillWithHerobrineInSameTime(@Nullable Skill<?> skill) {
        if (skill != null && herobrineJustUsedSkill())
            skillManager.interrupt();
    }

    private boolean herobrineJustUsedSkill() {
        if (herobrine == null)
            return false;
        Skill<?> skill = herobrine.getCurrentSkill();
        if (skill == null)
            return false;
        return skill.getCurrentTickCount() < 20;
    }

    public ShadowHerobrine(Level level, Herobrine herobrine) {
        this(NarakaEntityTypes.SHADOW_HEROBRINE.get(), level);
        this.herobrine = herobrine;
        this.herobrineUUID = herobrine.getUUID();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new FollowOwnerGoal<>(this));
        goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
    }

    public void startAvoidTarget() {
        goalSelector.removeGoal(moveToTargetGoal);
        goalSelector.addGoal(2, avoidTargetGoal);
    }

    public void stopAvoidTarget() {
        goalSelector.removeGoal(avoidTargetGoal);
        goalSelector.addGoal(2, moveToTargetGoal);
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    private Optional<Herobrine> getHerobrine() {
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
        Optional<Herobrine> optional = getHerobrine();
        if (optional.isPresent() && optional.get().isHibernateMode())
            return;

        StigmaHelper.removeStigma(target);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE);
        target.hurtServer(level, NarakaDamageSources.stigma(this), 6 * stigma.value());
        optional.ifPresent(herobrine -> herobrine.getShadowController().summonShadowHerobrine(level));
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {
        getHerobrine().ifPresent(herobrine -> herobrine.collectStigma(level, target, original));
    }

    public float getHurtDamageLimit() {
        Optional<Herobrine> optional = getHerobrine();
        if (optional.isEmpty())
            return Float.MAX_VALUE;
        float herobrineHurtDamageLimit = optional.get().getHurtDamageLimit();
        if (herobrineHurtDamageLimit <= 1)
            return Float.MAX_VALUE;
        return herobrineHurtDamageLimit;
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurtServer(serverLevel, source, amount);
        if (getHerobrine().isPresent())
            amount = Math.min(amount, getHurtDamageLimit());
        if (staggeringTickCount > 0)
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
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (herobrine != null)
            compound.putUUID("Herobrine", herobrine.getUUID());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Herobrine"))
            this.herobrineUUID = compound.getUUID("Herobrine");
    }

    @Override
    @Nullable
    public Entity getOwner() {
        getHerobrine();
        return herobrine;
    }
}
