package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class PickaxeSlash extends LightTailEntity {
    public static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(PickaxeSlash.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> Z_ROT = SynchedEntityData.defineId(PickaxeSlash.class, EntityDataSerializers.FLOAT);

    private int lifetime = Integer.MAX_VALUE;
    @Nullable
    private StigmatizingEntity stigmatizingEntity;
    private float prevAlpha = 1f;
    private float alpha = 1f;
    private boolean stunTarget;

    public PickaxeSlash(EntityType<? extends PickaxeSlash> entityType, Level level) {
        super(entityType, level, 30);
        setNoGravity(true);
        setTailColor(0x0000ff);
    }

    public PickaxeSlash(Level level, AbstractHerobrine owner, int lifetime) {
        this(NarakaEntityTypes.PICKAXE_SLASH.get(), level);
        this.setOwner(owner);
        this.stigmatizingEntity = owner;
        this.lifetime = lifetime;
        this.accelerationPower = 0.05;
    }

    public void setStunTarget(boolean stunTarget) {
        this.stunTarget = stunTarget;
    }

    public float getAlpha(float partialTick) {
        return Mth.lerp(partialTick, prevAlpha, alpha);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(Z_ROT, 60f)
                .define(COLOR, 0xffffff);
    }

    public void setColor(int color) {
        entityData.set(COLOR, color);
    }

    public int getColor(float partialTick) {
        return FastColor.ARGB32.color((int) (getAlpha(partialTick) * 255), entityData.get(COLOR));
    }

    public void setZRot(float zRot) {
        entityData.set(Z_ROT, zRot);
    }

    public float getZRot() {
        return entityData.get(Z_ROT);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    @Nullable
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        ProjectileUtil.rotateTowardsMovement(this, 1);
        if (level() instanceof ServerLevel serverLevel)
            serverTick(serverLevel);
        prevAlpha = alpha;
        if (alpha < 1f)
            alpha = Math.max(0, alpha - 0.1f);
    }

    private void serverTick(ServerLevel level) {
        if (tickCount >= lifetime || alpha <= 0)
            discard();
        level.getEntitiesOfClass(LivingEntity.class, getBoundingBox(), this::checkTarget)
                .forEach(target -> hurtEntity(level, target));
    }

    protected boolean checkTarget(LivingEntity target) {
        return super.canHitEntity(target) && AbstractHerobrine.isNotHerobrine(target);
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Vec3 old = new Vec3(xOld, yOld, zOld);
        setPos(old.add(getDeltaMovement()));
    }

    private void hurtEntity(ServerLevel level, LivingEntity target) {
        DamageSource damageSource = NarakaDamageSources.pickaxeSlash(this);
        float damage = 10 + target.getMaxHealth() * 0.1f;
        if (target.hurt(damageSource, damage) && stigmatizingEntity != null)
            stigmatizingEntity.stigmatizeEntity(level, target);
        if (stunTarget)
            StunHelper.stunEntity(target, 20);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (alpha >= 1)
            alpha = 0.95f;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
    }
}
