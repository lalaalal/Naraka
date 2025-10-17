package com.yummy.naraka.world.entity.ai.skill.naraka_pickaxe;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.NarakaPickaxe;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class SwingSkill extends AttackSkill<NarakaPickaxe> {
    public static final ResourceLocation LOCATION = createLocation("naraka_pickaxe.swing");

    public SwingSkill(NarakaPickaxe mob) {
        super(LOCATION, mob, 140, 0);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.1f;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        rotateTowardTarget(target);
        runBetween(55, 60, () -> moveToTarget(target, false, 2));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(0, () -> mob.setDeltaMovement(0, 1, 0));
        runBetween(0, 40, () -> reduceSpeed(0.75));
        runBetween(0, 5, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 1));
        runBetween(60, 75, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 1));
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        Stigma stigma = StigmaHelper.get(target);
        if (stigma.value() > 0) {
            StigmaHelper.decreaseStigma(target);
            target.hurt(NarakaDamageSources.stigmaConsume(mob), stigma.value() * calculateDamage(target));
            level.playSound(null, mob, SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE, 1, 1);
        }
    }
}
