package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.PickaxeSlash;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;

import java.util.List;

public class PickaxeSlashSkill extends TargetSkill<AbstractHerobrine> {
    public static final ResourceLocation SINGLE = createLocation("pickaxe_slash.single");
    public static final ResourceLocation TRIPLE = createLocation("pickaxe_slash.triple");
    private final List<Integer> pickaxeSlashSpawnTimes;
    private int rotateDirection = 1;

    public static PickaxeSlashSkill single(AbstractHerobrine mob) {
        return new PickaxeSlashSkill(SINGLE, mob, 65, 360, 40);
    }

    public static PickaxeSlashSkill triple(AbstractHerobrine mob) {
        return new PickaxeSlashSkill(TRIPLE, mob, 80, 360, 40, 45, 55);
    }

    protected PickaxeSlashSkill(ResourceLocation location, AbstractHerobrine mob, int duration, int cooldown, Integer... pickaxeSlashSpawnTimes) {
        super(location, mob, duration, cooldown);
        this.pickaxeSlashSpawnTimes = List.of(pickaxeSlashSpawnTimes);
    }

    @Override
    public void prepare() {
        super.prepare();
        rotateDirection = 1;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        run(pickaxeSlashSpawnTimes.contains(tickCount), () -> createPickaxeSlash(level, target));
    }

    private void createPickaxeSlash(ServerLevel level, LivingEntity target) {
        rotateTowardTarget(target);
        int zRotDegree = mob.getRandom().nextInt(45, 65) * rotateDirection;
        PickaxeSlash pickaxeSlash = new PickaxeSlash(level, mob, 120);
        pickaxeSlash.setPos(mob.getX(), mob.getEyeY() - 0.75, mob.getZ());
        pickaxeSlash.setDeltaMovement(NarakaEntityUtils.getDirectionNormalVector(mob, target).scale(1.5));
        pickaxeSlash.setZRot(zRotDegree);
        ProjectileUtil.rotateTowardsMovement(pickaxeSlash, 1);
        level.addFreshEntity(pickaxeSlash);
        this.rotateDirection *= -1;
    }
}
