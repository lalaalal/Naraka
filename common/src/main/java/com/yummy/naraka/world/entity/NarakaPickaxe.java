package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.goal.LookAtTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe.StrikeSkill;
import com.yummy.naraka.world.entity.animation.NarakaPickaxeAnimationLocations;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NarakaPickaxe extends SkillUsingMob {
    @Nullable
    private Herobrine herobrine;

    public static boolean isNotNarakaPickaxe(LivingEntity livingEntity) {
        return livingEntity.getType() != NarakaEntityTypes.NARAKA_PICKAXE.get();
    }

    public static AttributeSupplier.Builder getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.SAFE_FALL_DISTANCE, 256)
                .add(Attributes.FLYING_SPEED, 0.5f)
                .add(Attributes.FOLLOW_RANGE, 128)
                .add(Attributes.MAX_HEALTH, 1);
    }

    public NarakaPickaxe(EntityType<NarakaPickaxe> entityType, Level level) {
        super(entityType, level);
        registerAnimation(NarakaPickaxeAnimationLocations.IDLE);
        registerSkill(this, StrikeSkill::new, NarakaPickaxeAnimationLocations.STRIKE);

        skillManager.enableOnly(List.of());
        skillManager.runOnSkillEnd(skill -> discard());
    }

    public NarakaPickaxe(Level level, Herobrine herobrine) {
        this(NarakaEntityTypes.NARAKA_PICKAXE.get(), level);
        this.herobrine = herobrine;
    }

    @Override
    public void tick() {
        setNoGravity(true);
        super.tick();
    }

    private boolean filterTarget(LivingEntity target) {
        return AbstractHerobrine.isNotHerobrine(target)
                && target.getType() != NarakaEntityTypes.NARAKA_PICKAXE.get();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new LookAtTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, this::filterTarget));
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        LivingEntity target = super.getTarget();
        if (target == null && herobrine != null)
            return herobrine.getTarget();
        return target;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }
}
