package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class Stigma {
    public static final int MAX_STIGMA = 2;
    public static final int HOLD_ENTITY_DURATION = 20 * 5;
    public static final int STIGMA_DECREASE_COOLDOWN = 20 * 60;

    private int stigma = 0;
    private long lastMarkedTime = 0;
    private boolean dirty = false;

    public Stigma() {

    }

    public Stigma(int stigma, long lastMarkedTime) {
        this.stigma = stigma;
        this.lastMarkedTime = lastMarkedTime;
    }

    public void increaseStigma(LivingEntity livingEntity, Entity cause) {
        if (stigma < MAX_STIGMA) {
            stigma += 1;
        } else {
            consumeStigma(livingEntity, cause);
        }
        holdEntity(livingEntity);
        lastMarkedTime = livingEntity.level().getGameTime();
        dirty = true;
    }

    public void decreaseStigma() {
        if (stigma > 0) {
            stigma -= 1;
            dirty = true;
        }
    }

    public int getStigma() {
        return stigma;
    }

    public long getLastMarkedTime() {
        return lastMarkedTime;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean value) {
        dirty = value;
    }

    public void consumeStigma(LivingEntity livingEntity, Entity cause) {
        stigma = 0;
        DamageSource source = NarakaDamageSources.stigma(cause);
        livingEntity.hurt(source, Float.MAX_VALUE);
    }

    public void holdEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.PREVENT_MOVING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.PREVENT_JUMPING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.ATTACK_DAMAGE,
                NarakaAttributeModifiers.PREVENT_ENTITY_ATTACKING
        );
        NarakaAttributeModifiers.addAttributeModifier(
                livingEntity,
                Attributes.BLOCK_BREAK_SPEED,
                NarakaAttributeModifiers.PREVENT_BLOCK_ATTACKING
        );
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(true);
    }

    public void releaseEntity(LivingEntity livingEntity) {
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.MOVEMENT_SPEED,
                NarakaAttributeModifiers.PREVENT_MOVING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.JUMP_STRENGTH,
                NarakaAttributeModifiers.PREVENT_JUMPING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.ATTACK_DAMAGE,
                NarakaAttributeModifiers.PREVENT_ENTITY_ATTACKING
        );
        NarakaAttributeModifiers.removeAttributeModifier(
                livingEntity,
                Attributes.BLOCK_BREAK_SPEED,
                NarakaAttributeModifiers.PREVENT_BLOCK_ATTACKING
        );
        if (livingEntity instanceof Mob mob)
            mob.setNoAi(false);
    }

    public void tryRelease(LivingEntity livingEntity) {
        if (livingEntity.level().getGameTime() - lastMarkedTime >= HOLD_ENTITY_DURATION)
            releaseEntity(livingEntity);
    }

    public boolean tryDecrease(LivingEntity livingEntity) {
        if (livingEntity.level().getGameTime() - lastMarkedTime >= STIGMA_DECREASE_COOLDOWN) {
            decreaseStigma();
            return stigma == 0;
        }
        return false;
    }
}
