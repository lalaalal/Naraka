package com.yummy.naraka.world.entity.ai.skill.origin_herobrine;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.entity.CorruptedStar;
import com.yummy.naraka.world.entity.OriginHerobrine;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ChargingSkill extends Skill<OriginHerobrine> {
    public static final ResourceLocation LOCATION = createLocation("origin_herobrine.charging");

    private static final SoulType[] SOUL_TYPES = {SoulType.REDSTONE, SoulType.COPPER, SoulType.GOLD, SoulType.EMERALD, SoulType.DIAMOND, SoulType.LAPIS, SoulType.AMETHYST, SoulType.NECTARIUM};
    private static final float[] ROTATIONS = {-30, -60, -120, -150, 150, 120, 60, 30};

    private int chargingStack;
    private final SwordSwingSkill swordSwingSkill;
    @Nullable
    private CorruptedStar corruptedStar;

    public ChargingSkill(OriginHerobrine mob, SwordSwingSkill swordSwingSkill) {
        super(LOCATION, mob, 100, 0);
        this.swordSwingSkill = swordSwingSkill;
        setLinkedSkill(this);
    }

    @Override
    public void prepare() {
        super.prepare();
        mob.resetHurtByStar();
        corruptedStar = null;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.tickCount > 100;
    }

    @Override
    protected void skillTick(ServerLevel level) {
        runBetween(1, 15, () -> displayChargingParticles(level, tickCount * 10, tickCount));
        runAt(20, this::shootStar);
        runAt(50, () -> updateAndDisplayParticles(level));
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        Vec3 position = new Vec3(0, 0, 16)
                .yRot((float) Math.toRadians(getCurrentRotation()))
                .add(mob.position());

        QuadraticBezier bezier = QuadraticBezier.fromZero(
                new Vec3(0, 0.5, 0),
                new Vec3(0, 1, 0)
        );
        corruptedStar = new CorruptedStar(level, mob, position, bezier, 20);
        corruptedStar.setCanBeDeflectedByPlayer(true);
        corruptedStar.setSoulType(getCurrentSoulType());
        corruptedStar.startShine(20);
        corruptedStar.setStopOnEntityHit(true);
        corruptedStar.setShootingAcceleration(0.075f);

        level.addFreshEntity(corruptedStar);
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        chargingStack += 1;
        if (chargingStack < SOUL_TYPES.length) {
            setLinkedSkill(this);
        } else {
            chargingStack = 0;
            setLinkedSkill(swordSwingSkill);
        }
    }

    private SoulType getCurrentSoulType() {
        return SOUL_TYPES[chargingStack % SOUL_TYPES.length];
    }

    private float getCurrentRotation() {
        return ROTATIONS[chargingStack % ROTATIONS.length];
    }

    private void displayChargingParticles(ServerLevel level, int count, float speed) {
        level.sendParticles(SoulParticleOption.with(getCurrentSoulType()),
                mob.getX(), mob.getEyeY(), mob.getZ(),
                count,
                0.1, 0.1, 0.1,
                speed
        );
    }

    private void updateAndDisplayParticles(ServerLevel level) {
        SoulType soulType = SoulType.GOD_BLOOD;
        if (mob.isHurtByStar()) {
            soulType = getCurrentSoulType();
            mob.absorbSoul(soulType);
        }
        NarakaSkillUtils.sendSphereParticles(level, mob, NarakaFlameParticleOption.fromSoulType(soulType), 1);
    }

    private void shootStar() {
        if (corruptedStar != null) {
            Vec3 shootVector = mob.getEyePosition().subtract(corruptedStar.position());
            corruptedStar.shoot(shootVector.x, shootVector.y, shootVector.z, 0.01f, 0);
        }
    }

    @Override
    public void interrupt() {
        if (corruptedStar != null)
            corruptedStar.discard();
    }
}
