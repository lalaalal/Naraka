package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpinUpSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("spin_up");

    @Nullable
    private ShadowHerobrine shadowHerobrine;

    public SpinUpSkill(@Nullable Skill<?> nextSkill, Herobrine mob) {
        super(LOCATION, 40, 0, 1, nextSkill, 40, mob);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runAt(20, this::spinUp);
        runBetween(21, 25, () -> reduceSpeed(0.3));
        runAt(23, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 3));
        runAt(27, this::stopMoving);

        runAt(25, this::spawnShadow);
        runAt(26, this::shadowUseSkill);
    }

    private void spawnShadow() {
        shadowHerobrine = new ShadowHerobrine(mob.level(), true, true);
        shadowHerobrine.setPos(mob.position());
        shadowHerobrine.forceSetRotation(mob.getYRot(), mob.getXRot());
        shadowHerobrine.getSkillManager().enableOnly(List.of());
        mob.level().addFreshEntity(shadowHerobrine);
    }

    private void shadowUseSkill() {
        if (shadowHerobrine != null && shadowHerobrine.isAlive()) {
            shadowHerobrine.setTarget(mob.getTarget());
            shadowHerobrine.useSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_3);
            shadowHerobrine.setDisplayPickaxe(true);
        }
    }

    private void spinUp() {
        Vec3 deltaMovement = mob.getLookAngle()
                .multiply(5, 3, 5)
                .add(0, 2, 0);
        mob.setDeltaMovement(deltaMovement);
    }
}
