package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.absolute_herobrine.SwordSwingSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class AbsoluteHerobrine extends SkillUsingMob implements Enemy {
    private final SwordSwingSkill swordSwingSkill = registerSkill(this, SwordSwingSkill::new, HerobrineAnimationIdentifiers.SWORD_ATTACK_SPIN);

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.SAFE_FALL_DISTANCE, 256)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0)
                .add(Attributes.JUMP_STRENGTH, 0)
                .add(Attributes.FLYING_SPEED, 0.5f)
                .add(Attributes.MAX_HEALTH, 8);
    }

    public AbsoluteHerobrine(EntityType<? extends AbsoluteHerobrine> entityType, Level level) {
        super(entityType, level);

        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        registerAnimation(HerobrineAnimationIdentifiers.PHASE_4_SPAWN);

        updateAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);

        skillManager.runOnSkillEnd(this::updateAnimationOnSkillEnd);

        setDeltaMovement(0, -1, 0);
    }

    protected void updateAnimationOnSkillEnd(Skill<?> skill) {
        if (!skill.hasLinkedSkill()) {
            setAnimation(HerobrineAnimationIdentifiers.PHASE_4_IDLE);
        }
    }

    @Override
    public void tick() {
        super.tick();
        setNoGravity(true);
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
        return false;
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
    public boolean canBeHitByProjectile() {
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
}
