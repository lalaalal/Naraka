package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FlickerSkill<T extends AbstractHerobrine> extends TargetSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("flicker");

    private final DashSkill<?> dashSkill;
    private final PunchSkill<?> punchSkill;

    public FlickerSkill(T mob, DashSkill<?> dashSkill, PunchSkill<?> punchSkill) {
        super(LOCATION, mob, 1, 50);
        this.dashSkill = dashSkill;
        this.punchSkill = punchSkill;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        Vec3 deltaNormal = NarakaEntityUtils.getDirectionNormalVector(mob, target);
        Vec3 position = mob.position().add(deltaNormal);
        level.sendParticles(NarakaParticleTypes.FLICKER.get(), position.x, position.y + mob.getEyeHeight(), position.z, 1, 0, 0, 0, 1);
        dashSkill.setLinkedSkill(punchSkill);
        punchSkill.setLinkedFromPrevious(true);
        this.setLinkedSkill(dashSkill);
    }
}
