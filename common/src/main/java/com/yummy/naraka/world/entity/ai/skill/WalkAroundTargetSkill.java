package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class WalkAroundTargetSkill extends ComboSkill<SkillUsingMob> {
    public static final ResourceLocation LOCATION = createLocation("walk_around");

    private int direction;

    public WalkAroundTargetSkill(SkillUsingMob mob, Skill<?> nextSkill) {
        super(LOCATION, 80, 300, 0.8f, nextSkill, 60, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        direction = mob.getRandom().nextBoolean() ? 1 : -1;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(50);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        Vec3 delta = mob.position().subtract(target.position());
        Vec3 wanted = delta.yRot(Mth.PI / 6 * direction).add(target.position());

        Path path = mob.getNavigation().createPath(wanted.x, wanted.y, wanted.z, 1);
        mob.getNavigation().moveTo(path, 0.8f);
        mob.getLookControl().setLookAt(target);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return 0;
    }
}
