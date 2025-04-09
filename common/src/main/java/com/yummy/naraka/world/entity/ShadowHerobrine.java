package com.yummy.naraka.world.entity;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.goal.FollowOwnerGoal;
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
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ShadowHerobrine extends AbstractHerobrine implements TraceableEntity {
    private @Nullable Herobrine herobrine;
    private @Nullable UUID herobrineUUID;

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return AbstractHerobrine.getAttributeSupplier()
                .add(Attributes.MOVEMENT_SPEED, 0.15f)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MAX_HEALTH, 150);
    }

    protected ShadowHerobrine(EntityType<? extends AbstractHerobrine> entityType, Level level) {
        super(entityType, level, true);
        punchSkill.setMaxLinkCount(3);
        punchSkill.changeCooldown(60);
        punchSkill.setStunTarget(false);
        punchSkill.setTraceTarget(false);
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
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if ((herobrine = getHerobrine()) != null && herobrine.isUsingSkill())
            skillManager.pause(false);
        else
            skillManager.resume();
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Nullable
    private Herobrine getHerobrine() {
        if (herobrineUUID == null)
            return null;
        if (herobrine == null && level() instanceof ServerLevel serverLevel)
            return NarakaEntityUtils.findEntityByUUID(serverLevel, herobrineUUID, Herobrine.class);
        if (herobrine != null && herobrine.isRemoved()) {
            herobrineUUID = null;
            return null;
        }
        return herobrine;
    }

    @Override
    protected Fireball createFireball() {
        if ((herobrine = getHerobrine()) != null)
            return herobrine.createFireball();
        return new NarakaFireball(this, Vec3.ZERO, level());
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {

    }

    @Override
    public List<Afterimage> getAfterimages() {
        return List.of();
    }

    @Override
    public void stigmatizeEntity(LivingEntity target) {
        Stigma stigma = StigmaHelper.get(target);
        if (stigma.value() < 1)
            return;
        StigmaHelper.removeStigma(target);
        level().playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE);
        target.hurt(NarakaDamageSources.stigma(this), 6 * stigma.value());
        if ((herobrine = getHerobrine()) != null)
            herobrine.summonShadowHerobrine();
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage();
    }

    @Override
    public void collectStigma(Stigma stigma) {
        if ((herobrine = getHerobrine()) != null)
            herobrine.collectStigma(stigma);
    }

    public float getHurtDamageLimit() {
        if ((herobrine = getHerobrine()) == null)
            return Float.MAX_VALUE;
        float herobrineHurtDamageLimit = herobrine.getHurtDamageLimit();
        if (herobrineHurtDamageLimit <= 1)
            return Float.MAX_VALUE;
        return herobrineHurtDamageLimit;
    }

    @Override
    public boolean canChangeDimensions(Level oldLevel, Level newLevel) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurt(source, amount);
        if ((herobrine = getHerobrine()) != null)
            amount = Math.min(amount, getHurtDamageLimit());
        if (staggeringTickCount < MAX_STAGGERING_TICK)
            return false;
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
        if (herobrine != null)
            herobrine.broadcastShadowHerobrineHurt(this);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity entity) {
        int data = (herobrine = getHerobrine()) == null ? -1 : herobrine.getId();
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
    public @Nullable Entity getOwner() {
        return getHerobrine();
    }
}
