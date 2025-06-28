package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.SwordAura;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SwordAuraSkill extends TargetSkill<AbstractHerobrine> {
    public static final ResourceLocation SINGLE = createLocation("sword_aura.single");
    public static final ResourceLocation TRIPLE = createLocation("sword_aura.triple");
    private final List<Integer> swordAuraSpawnTimes;

    public static SwordAuraSkill single(AbstractHerobrine mob) {
        return new SwordAuraSkill(SINGLE, mob, 65, 180, 40);
    }

    public static SwordAuraSkill triple(AbstractHerobrine mob) {
        return new SwordAuraSkill(TRIPLE, mob, 80, 180, 40, 45, 55);
    }

    protected SwordAuraSkill(ResourceLocation location, AbstractHerobrine mob, int duration, int cooldown, Integer... swordAuraSpawnTimes) {
        super(location, mob, duration, cooldown);
        this.swordAuraSpawnTimes = List.of(swordAuraSpawnTimes);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        run(swordAuraSpawnTimes.contains(tickCount), () -> createSwordAura(level, target));
    }

    private void createSwordAura(ServerLevel level, LivingEntity target) {
        rotateTowardTarget(target);
        SwordAura swordAura = new SwordAura(level, mob, 120);
        swordAura.setPos(mob.getX(), mob.getEyeY(), mob.getZ());
        swordAura.setDeltaMovement(NarakaEntityUtils.getDirectionNormalVector(mob, target));
        level.addFreshEntity(swordAura);
    }
}
