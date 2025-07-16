package com.yummy.naraka.world;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.goal.LookAtTargetGoal;
import com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe.ExplodeSkill;
import com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe.SwingSkill;
import com.yummy.naraka.world.entity.animation.NarakaPickaxeAnimationLocations;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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

public class NarakaPickaxe extends SkillUsingMob {
    private final SwingSkill swingSkill = registerSkill(this, SwingSkill::new, NarakaPickaxeAnimationLocations.SWING);
    private final ExplodeSkill explodeSkill = registerSkill(this, ExplodeSkill::new, NarakaPickaxeAnimationLocations.EXPLODE);

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
        explodeSkill.setCooldown();
    }

    public NarakaPickaxe(Level level, Herobrine herobrine) {
        this(NarakaEntityTypes.NARAKA_PICKAXE.get(), level);
        this.herobrine = herobrine;
    }

    @Override
    public void tick() {
        setNoGravity(true);
        super.tick();
        if (herobrine != null && (herobrine.isDeadOrDying() || herobrine.isRemoved()))
            discard();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(1, new LookAtTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, false, (entity, level) -> AbstractHerobrine.isNotHerobrine(entity)));
    }

    @Override
    public @Nullable LivingEntity getTarget() {
        LivingEntity target = super.getTarget();
        if (target == null && herobrine != null)
            return herobrine.getTarget();
        return target;
    }

    @Override
    public boolean isInvulnerableTo(ServerLevel level, DamageSource damageSource) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        tag.read("Owner", UUIDUtil.CODEC).ifPresent(uuid -> {
            if (level() instanceof ServerLevel level) {
                this.herobrine = NarakaEntityUtils.findEntityByUUID(level, uuid, Herobrine.class);
            }
        });
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        if (herobrine != null)
            tag.store("Owner", UUIDUtil.CODEC, herobrine.getUUID());
    }
}
