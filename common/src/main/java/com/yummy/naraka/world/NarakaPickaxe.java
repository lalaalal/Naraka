package com.yummy.naraka.world;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class NarakaPickaxe extends Entity {
    @Nullable
    private Herobrine herobrine;
    private final double speed = 0.6;
    private boolean moveToTarget;
    private int onGroundTick = 0;

    public NarakaPickaxe(EntityType<NarakaPickaxe> entityType, Level level) {
        super(entityType, level);
    }

    public NarakaPickaxe(Level level, Herobrine herobrine) {
        super(NarakaEntityTypes.NARAKA_PICKAXE.get(), level);
        this.herobrine = herobrine;
    }

    public void setHerobrine(@Nullable Herobrine herobrine) {
        this.herobrine = herobrine;
    }

    private void moveRandom() {
        double xRot = random.nextDouble() * Math.TAU;
        double yRot = random.nextDouble() * Math.TAU;
        double x = Math.cos(xRot) * speed;
        double y = Math.sin(yRot) * speed;
        double z = Math.sin(xRot) * speed;
        setDeltaMovement(x, y, z);
    }

    private void moveRandomUpward() {
        double xRot = random.nextDouble() * Math.PI * 0.5 + Math.PI * 0.25;
        double yRot = random.nextDouble() * Math.TAU;
        double x = Math.cos(xRot) * speed;
        double y = Math.sin(yRot) * speed;
        double z = Math.sin(xRot) * speed;
        setDeltaMovement(x, y, z);
    }

    private void moveToTarget() {
        if (herobrine != null) {
            LivingEntity target = herobrine.getTarget();
            if (target != null) {
                Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(this, target);
                setDeltaMovement(delta.scale(speed));
            }
        } else {
            moveRandom();
        }
    }

    private void moveToHerobrine() {
        if (herobrine != null) {
            Vec3 delta = NarakaEntityUtils.getDirectionNormalVector(this, herobrine);
            setDeltaMovement(delta.scale(speed));
        }
    }

    private void rotate() {
        Vec3 movement = getDeltaMovement();
        if (movement.horizontalDistanceSqr() > 0.001) {
            double yRot = Math.atan2(movement.z, movement.x) - Math.PI / 2;
            double xRot = Math.atan2(-movement.y, movement.horizontalDistance());
            setXRot((float) Math.toDegrees(xRot));
            setYRot((float) Math.toDegrees(yRot));
        }
    }

    @Override
    public void tick() {
        flyingTick();
        explodeTick();
        rotate();
        move(MoverType.SELF, getDeltaMovement());
        if (getDeltaMovement().lengthSqr() < 0.001)
            setDeltaMovement(getDeltaMovement().scale(-1));
        if (herobrine != null && (herobrine.isRemoved()))
            discard();
        super.tick();
    }

    private void flyingTick() {
        if (tickCount % 1200 == 0) {
            moveToTarget = true;
        }

        if (level().isClientSide) {
            level().addParticle(NarakaParticleTypes.CORRUPTED_FIRE_FLAME.get(), getX(), getY(), getZ(), 0, 0, 0);
        } else if (level() instanceof ServerLevel level) {
            if (tickCount % 66 == 0 && !moveToTarget) {
                if (herobrine != null && distanceToSqr(herobrine) > 16 * 16) {
                    moveToHerobrine();
                } else if (tickCount % 132 == 0) {
                    moveToTarget();
                } else {
                    moveRandom();
                }
            }

            if (!moveToTarget && onGround())
                moveRandomUpward();
            level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(3, 2, 3), AbstractHerobrine::isNotHerobrine)
                    .forEach(target -> target.hurtServer(level, damageSources().magic(), 6));
        }
    }

    private void hurtAndRemoveTargetStigma(ServerLevel level, LivingEntity target) {
        Stigma original = StigmaHelper.get(target);
        StigmaHelper.removeStigma(target);
        float damage = original.value() * target.getMaxHealth() * 0.1f;
        if (herobrine != null && damage > 0) {
            target.hurtServer(level, NarakaDamageSources.stigmaConsume(herobrine), damage);
        }
    }

    private void explodeTick() {
        if (moveToTarget && onGround()) {
            setDeltaMovement(0, -0.1, 0);
            if (level() instanceof ServerLevel level) {
                level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(10, 2, 10))
                        .forEach(target -> hurtAndRemoveTargetStigma(level, target));
            }
            onGroundTick += 1;

            if (onGroundTick >= 300) {
                onGroundTick = 0;
                moveToTarget = false;
            }
        }

        if (onGroundTick > 10 && level().isClientSide) {
            for (int i = 0; i < 36; i++) {
                double yRot = random.nextDouble() * Math.TAU;
                double distance = random.nextDouble() * 10;
                double x = Math.cos(yRot) * distance + getX();
                double y = getY() + 0.2;
                double z = Math.sin(yRot) * distance + getZ();

                level().addParticle(NarakaParticleTypes.CORRUPTED_FIRE_FLAME.get(), x, y, z, 0, 0, 0);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        tag.read("Owner", UUIDUtil.CODEC).ifPresent(uuid -> {
            if (level() instanceof ServerLevel level) {
                this.herobrine = NarakaEntityUtils.findEntityByUUID(level, uuid, Herobrine.class);
            }
        });
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (herobrine != null)
            tag.storeNullable("Owner", UUIDUtil.CODEC, herobrine.getUUID());
    }
}
