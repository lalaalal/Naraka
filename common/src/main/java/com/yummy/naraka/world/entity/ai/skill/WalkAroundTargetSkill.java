package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class WalkAroundTargetSkill extends TargetSkill<SkillUsingMob> {
    public static final ResourceLocation LOCATION = createLocation("walk_around");

    private static final int DEFAULT_DURATION = 80;
    private int direction;
    private final PunchSkill<AbstractHerobrine> punchSKill;
    private final DashSkill<?> dashSkill;

    public WalkAroundTargetSkill(SkillUsingMob mob, PunchSkill<AbstractHerobrine> punchSKill, DashSkill<?> dashSkill) {
        super(LOCATION, DEFAULT_DURATION, 0, mob);
        this.punchSKill = punchSKill;
        this.dashSkill = dashSkill;
    }

    @Override
    public void prepare() {
        super.prepare();
        duration = DEFAULT_DURATION;
        direction = mob.getRandom().nextBoolean() ? 1 : -1;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(81);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        if (tickCount % 5 == 0)
            moveAndLook(target);
        if (targetInRange(target, 25)) {
            setLinkedSkill(punchSKill);
            tickCount = duration;
        }

        runAt(duration - 1, () -> setupDashAndPunch(level, target));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    private void setupDashAndPunch(ServerLevel level, LivingEntity target) {
        Vec3 deltaNormal = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        Vec3 position = mob.position().add(deltaNormal);
        level.sendParticles(NarakaParticleTypes.FLICKER.get(), position.x, position.y + mob.getEyeHeight(), position.z, 1, 0, 0, 0, 1);
        dashSkill.setLinkedSkill(punchSKill);
        punchSKill.setLinkedFromPrevious(true);
        this.setLinkedSkill(dashSkill);
    }

    private void moveAndLook(LivingEntity target) {
        Vec3 delta = mob.position().subtract(target.position());
        Vec3 wanted = delta.yRot(Mth.PI / 6 * direction).add(target.position());

        Path path = mob.getNavigation().createPath(wanted.x, wanted.y, wanted.z, 1);
        mob.getNavigation().moveTo(path, 1);
    }
}
