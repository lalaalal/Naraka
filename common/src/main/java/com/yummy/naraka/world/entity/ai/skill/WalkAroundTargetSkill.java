package com.yummy.naraka.world.entity.ai.skill;

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
    private final Skill<?> rushSKill;

    public WalkAroundTargetSkill(SkillUsingMob mob, PunchSkill<AbstractHerobrine> punchSKill, DashSkill<?> dashSkill, Skill<?> rushSkill) {
        super(LOCATION, 40, 300, mob);
        this.punchSKill = punchSKill;
        this.dashSkill = dashSkill;
        this.rushSKill = rushSkill;
    }

    @Override
    public void prepare() {
        super.prepare();
        duration = DEFAULT_DURATION;
        direction = mob.getRandom().nextBoolean() ? 1 : -1;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return targetInRange(50);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        moveAndLook(target);
        if (targetInRange(target, 4)) {
            setLinkedSkill(punchSKill);
            duration = 0;
        }

        runAt(duration - 1, this::determineNextSkill);
    }

    private void determineNextSkill() {
        if (mob.getRandom().nextBoolean()) {
            dashSkill.setLinkedSkill(rushSKill);
            dashSkill.setScale(-0.5f);
            this.setLinkedSkill(dashSkill);
        } else {
            if (mob.getRandom().nextBoolean()) {
                dashSkill.setLinkedSkill(punchSKill);
                punchSKill.setLinkedFromPrevious(true);
            }
            this.setLinkedSkill(dashSkill);
        }
    }

    private void moveAndLook(LivingEntity target) {
        Vec3 delta = mob.position().subtract(target.position());
        Vec3 wanted = delta.yRot(Mth.PI / 6 * direction).add(target.position());

        Path path = mob.getNavigation().createPath(wanted.x, wanted.y, wanted.z, 1);
        mob.getNavigation().moveTo(path, 1);
        lookTarget(target);
    }
}
