package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.goal.MoveToTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.diamond_golem.SimpleAttackSkill;
import com.yummy.naraka.world.entity.animation.DiamondGolemAnimationLocations;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DiamondGolem extends SkillUsingMob implements Enemy {
    private final SimpleAttackSkill basicAttackSkill = registerSkill(this, SimpleAttackSkill::basic, DiamondGolemAnimationLocations.BASIC_ATTACK);
    private final SimpleAttackSkill swipeAttackSkill = registerSkill(this, SimpleAttackSkill::swipe, DiamondGolemAnimationLocations.SWIPE_ATTACK);
    private final SimpleAttackSkill strongAttackSkill = registerSkill(this, SimpleAttackSkill::strong, DiamondGolemAnimationLocations.STRONG_ATTACK);

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ARMOR, 20)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.ATTACK_DAMAGE, 20)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.STEP_HEIGHT, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.SAFE_FALL_DISTANCE, 256)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0);
    }

    public DiamondGolem(EntityType<? extends DiamondGolem> entityType, Level level) {
        super(entityType, level);
        setPersistenceRequired();
        registerAnimation(DiamondGolemAnimationLocations.IDLE);
        updateAnimation(DiamondGolemAnimationLocations.IDLE);

        skillManager.runOnSkillEnd(skill -> setAnimation(DiamondGolemAnimationLocations.IDLE));
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, DiamondGolem.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));

        this.goalSelector.addGoal(2, new MoveToTargetGoal(this, 1, 32, 5, 0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    public int getAirSupply() {
        return getMaxAirSupply();
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (damageSource.is(DamageTypes.IN_WALL))
            return true;
        return super.isInvulnerableTo(damageSource);
    }
}
