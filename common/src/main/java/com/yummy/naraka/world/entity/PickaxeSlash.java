package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public class PickaxeSlash extends AbstractHurtingProjectile {
    private int lifetime = Integer.MAX_VALUE;

    public PickaxeSlash(EntityType<? extends PickaxeSlash> entityType, Level level) {
        super(entityType, level);
        setNoGravity(true);
    }

    public PickaxeSlash(Level level, LivingEntity owner, int lifetime) {
        this(NarakaEntityTypes.PICKAXE_SLASH.get(), level);
        this.setOwner(owner);
        this.lifetime = lifetime;
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
        if (!level().isClientSide && tickCount >= lifetime)
            discard();
    }

    @Override
    public boolean deflect(ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity livingEntity && level() instanceof ServerLevel serverLevel) {
            DamageSource damageSource = NarakaDamageSources.pickaxeSlash(this, getOwner());
            livingEntity.hurtServer(serverLevel, damageSource, 6);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        discard();
    }
}
