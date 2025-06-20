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

public class SplitAttackSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("split_attack");

    private Vec3 shadowSpawnPosition = Vec3.ZERO;
    @Nullable
    private ShadowHerobrine shadowHerobrine;

    public SplitAttackSkill(Skill<?> nextSkill, Herobrine mob) {
        super(LOCATION, 50, 100, 1, nextSkill, 50, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        shadowSpawnPosition = mob.position();
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(36);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        runBetween(15, 20, () -> moveToTarget(target, 1));
        runBetween(15, 20, () -> rotateTowardTarget(target));
        runAt(22, () -> hurtHitEntities(level, AbstractHerobrine::isNotHerobrine, 3));
        runAt(20, this::stopMoving);
        runAt(25, this::spawnShadow);
        runAt(26, () -> shadowUseSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_1));

        runAt(35, this::spawnShadow);
        runAt(36, () -> shadowUseSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_2));
    }

    private void spawnShadow() {
        shadowHerobrine = new ShadowHerobrine(mob.level(), true, true);
        shadowHerobrine.setPos(shadowSpawnPosition);
        shadowHerobrine.forceSetRotation(mob.getYRot(), mob.getXRot());
        shadowHerobrine.getSkillManager().enableOnly(List.of());
        mob.level().addFreshEntity(shadowHerobrine);

        shadowSpawnPosition = mob.position();
    }

    private void shadowUseSkill(ResourceLocation skillLocation) {
        if (shadowHerobrine != null && shadowHerobrine.isAlive()) {
            shadowHerobrine.setTarget(mob.getTarget());
            shadowHerobrine.useSkill(skillLocation);
            shadowHerobrine.setDisplayPickaxe(true);
        }
    }
}
