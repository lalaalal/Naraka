package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.PickaxeSlash;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import com.yummy.naraka.world.entity.ai.skill.InstantShadowSpawner;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PickaxeSlashSkill<T extends AbstractHerobrine> extends TargetSkill<T> {
    public static final Identifier SINGLE = skillIdentifier("final_herobrine.pickaxe_slash.single");
    public static final Identifier TRIPLE = skillIdentifier("final_herobrine.pickaxe_slash.triple");
    private final List<Integer> pickaxeSlashSpawnTimes;
    private int rotateDirection = 1;
    private final boolean spawnShadow;
    private InstantShadowSpawner shadowSpawner = InstantShadowSpawner.EMPTY;
    private int tailColor = 0x0000ff;
    private int slashColor = 0xffffff;

    public static PickaxeSlashSkill<AbstractHerobrine> single(AbstractHerobrine mob) {
        return new PickaxeSlashSkill<>(SINGLE, mob, 65, 360, false, 40);
    }

    public static PickaxeSlashSkill<Herobrine> triple(Herobrine herobrine) {
        return new PickaxeSlashSkill<>(TRIPLE, herobrine, 80, 460, true, 40, 45, 55);
    }

    protected PickaxeSlashSkill(Identifier location, T mob, int duration, int cooldown, boolean spawnShadow, Integer... pickaxeSlashSpawnTimes) {
        super(location, mob, duration, cooldown);
        this.pickaxeSlashSpawnTimes = List.of(pickaxeSlashSpawnTimes);
        this.spawnShadow = spawnShadow;
        if (mob.isShadow) {
            tailColor = 0x505050;
            slashColor = 0x505050;
        }
    }

    @Override
    public void prepare() {
        super.prepare();
        rotateDirection = 1;
        shadowSpawner = createShadowSpawner();
    }

    private InstantShadowSpawner createShadowSpawner() {
        if (spawnShadow)
            return InstantShadowSpawner.simple(mob);
        return InstantShadowSpawner.EMPTY;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runAt(5, () -> shadowSpawner.spawn(level).control(shadowHerobrine -> setupShadowHerobrine(shadowHerobrine, target)));
        if (!mob.isShadow) {
            runAt(0, () -> NarakaSkillUtils.sendParticleFront(level, mob, target, NarakaParticleTypes.TELEPORT.get()));
            runAt(5, () -> teleportToTarget(target, 12));
            runAfter(10, this::stopMoving);
        }
        runBetween(0, 10, () -> rotateTowardTarget(target));
        run(pickaxeSlashSpawnTimes.contains(tickCount), () -> createPickaxeSlash(level, target));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(10, this::stopShadowMoving);
        runAt(30, () -> shadowSpawner.useSkill(SINGLE));
    }

    private void setupShadowHerobrine(ShadowHerobrine shadowHerobrine, LivingEntity target) {
        shadowHerobrine.forceSetRotation(60, true, 0, false);
        shadowHerobrine.setPos(mob.position());
        Vec3 movement = target.position()
                .subtract(mob.position())
                .normalize()
                .yRot(Mth.HALF_PI)
                .scale(1.5f);
        shadowHerobrine.setDeltaMovement(movement);
        shadowHerobrine.setAnimation(HerobrineAnimationIdentifiers.PHASE_3_IDLE);
        shadowHerobrine.setTarget(mob.getTarget());
    }

    private void stopShadowMoving() {
        shadowSpawner.control(shadowHerobrine -> shadowHerobrine.setDeltaMovement(Vec3.ZERO));
    }

    private void createPickaxeSlash(ServerLevel level, LivingEntity target) {
        rotateTowardTarget(target);
        level.playSound(null, mob, SoundEvents.LUNGE_1.value(), SoundSource.HOSTILE, 1.8f, 1.1f);
        level.playSound(null, mob, SoundEvents.LUNGE_1.value(), SoundSource.HOSTILE, 1.7f, 0.95f);
        level.playSound(null, mob, SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 2, 2);
        level.playSound(null, mob, SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 1.7f, 1.85f);
        int zRotDegree = mob.getRandom().nextInt(45, 65) * rotateDirection;
        PickaxeSlash pickaxeSlash = new PickaxeSlash(level, mob, 360);
        pickaxeSlash.setPos(mob.getX(), mob.getEyeY() - 0.75, mob.getZ());
        pickaxeSlash.setDeltaMovement(NarakaEntityUtils.getDirectionNormalVector(mob, target).scale(1.2));
        ProjectileUtil.rotateTowardsMovement(pickaxeSlash, 1);
        pickaxeSlash.setZRot(zRotDegree);
        pickaxeSlash.setColor(slashColor);
        pickaxeSlash.setTailColor(tailColor);
        if (mob.isShadow)
            pickaxeSlash.setStunTarget(true);
        level.addFreshEntity(pickaxeSlash);
        this.rotateDirection *= -1;
    }
}
