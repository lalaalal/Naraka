package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.Stardust;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class CarpetBombingSkill extends Skill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.carpet_bombing");

    private final Map<Stardust, Integer> stardusts = new HashMap<>();

    public CarpetBombingSkill(Herobrine mob) {
        super(LOCATION, 90, 400, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        stardusts.clear();
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        for (int i = 0; i < 16; i++) {
            int spawnTick = mob.getRandom().nextIntBetweenInclusive(1, 10);
            float yRot = 360f / 16 * i;
            float xRot = mob.getRandom().nextFloat() * 30 + 15;
            Vec3 shootingVector = mob.calculateViewVector(-xRot, yRot);
            int power = mob.getRandom().nextIntBetweenInclusive(3, 5);
            int waitingTick = mob.getRandom().nextIntBetweenInclusive(2, 3) * 10;
            Stardust stardust = new Stardust(level, mob, shootingVector, power, waitingTick, false);
            stardusts.put(stardust, spawnTick);
        }
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void skillTick(ServerLevel level) {
        runAt(10, () -> mob.setDeltaMovement(0, 1.2, 0));
        runBetween(11, 25, () -> reduceSpeed(0.8f));
        runFrom(0, () -> spawnStardust(level, 0));

        runAt(50, () -> mob.setDeltaMovement(0, -8, 0));
        runAt(70, () -> mob.setDeltaMovement(0, 0.4, 0));
        runBetween(71, 90, () -> reduceSpeed(0.4f));
        runAt(90, () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    private void spawnStardust(ServerLevel level, int startTick) {
        int spawnTick = tickCount - startTick;
        stardusts.entrySet().stream()
                .filter(entry -> entry.getValue() == spawnTick)
                .forEach(entry -> {
                    level.addFreshEntity(entry.getKey());
                });
    }
}
