package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class StrikeDownSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final.strike_down");

    private final Supplier<Vec3> movementSupplier = () -> new Vec3(0, mob.getRandom().nextFloat() * 0.4 + 0.3, 0);
    private int onGroundTick;
    private Vec3 shadowPosition = Vec3.ZERO;
    private InstantShadowSpawner shadowSpawner = InstantShadowSpawner.EMPTY;

    public StrikeDownSkill(Herobrine mob, @Nullable Skill<?> nextSkill) {
        super(LOCATION, mob, 60, 0, 0.5f, 60, nextSkill);
        this.shieldCooldown = 100;
        this.shieldDamage = 15;
    }

    @Override
    public void prepare() {
        super.prepare();
        onGroundTick = 0;
        shadowPosition = mob.position();
        shadowSpawner = InstantShadowSpawner.simple(mob);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.1f;
    }

    private Vec3 modifyMovement(Vec3 original) {
        return original.scale(0.5).add(0, -2, 0);
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        runBefore(15, () -> rotateTowardTarget(target));
        runBefore(15, () -> lookTarget(target));
        runBetween(19, 27, () -> moveToTarget(target, true, this::modifyMovement));
        runAt(21, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 5));

        runAt(20, () -> shadowSpawner.control(this::displayShadowPickaxe).spawn(level, shadowPosition, mob.getYRot()));
        runAt(21, () -> shadowSpawner.useSkill(SimpleComboAttackSkill.FINAL_COMBO_ATTACK_3));

        runAt(38, () -> mob.setDeltaMovement(0, 0.4, 0));
        runAfter(40, () -> lookTarget(target));
        runBetween(40, 48, () -> reduceSpeed(0.5));
        runAt(50, this::stopMoving);
        onGround(level);
    }

    private void displayShadowPickaxe(ShadowHerobrine shadowHerobrine) {
        shadowHerobrine.setDisplayPickaxe(true);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }

    private void onGround(ServerLevel level) {
        if (onGroundTick == 1) {
            level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
            mob.shakeCamera();
        }
        if ((mob.onGround() || onGroundTick > 0) && onGroundTick < 5) {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 15, 0.5, 1, 0.5, 0.3);
            NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 3 + onGroundTick, movementSupplier);
            onGroundTick += 1;
        }
    }
}
