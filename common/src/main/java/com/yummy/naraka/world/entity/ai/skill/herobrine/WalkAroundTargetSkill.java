package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class WalkAroundTargetSkill extends TargetSkill<SkillUsingMob> {
    public static final ResourceLocation LOCATION = createLocation("herobrine.walk_around");

    private static final int DEFAULT_DURATION = 80;
    private int direction;
    private final PunchSkill<?> punchSkill;
    private final FlickerSkill<?> flickerSkill;

    public WalkAroundTargetSkill(SkillUsingMob mob, PunchSkill<?> punchSkill, FlickerSkill<?> flickerSkill) {
        super(LOCATION, mob, DEFAULT_DURATION, 0);
        this.punchSkill = punchSkill;
        this.flickerSkill = flickerSkill;
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
            setLinkedSkill(punchSkill);
            tickCount = duration;
        }

        runAt(duration - 1, () -> setLinkedSkill(flickerSkill));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    private void moveAndLook(LivingEntity target) {
        Vec3 delta = mob.position().subtract(target.position());
        Vec3 wanted = delta.yRot(Mth.PI / 6 * direction).add(target.position());

        Path path = mob.getNavigation().createPath(wanted.x, wanted.y, wanted.z, 1);
        mob.getNavigation().moveTo(path, 1);
    }
}
