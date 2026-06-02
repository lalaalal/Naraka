package com.yummy.naraka.world.entity.ai.skill.absolute_herobrine;

import com.mojang.math.Axis;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import com.yummy.naraka.world.entity.NarakaSword;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import com.yummy.naraka.world.entity.motion.MotionTypes;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class SwordSwingSkill extends AttackSkill<AbsoluteHerobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("absolute_herobrine.sword_swing");

    private static final float HALF_ANGLE = Mth.HALF_PI * 0.125f;

    @Nullable
    private NarakaSword narakaSword;

    public SwordSwingSkill(AbsoluteHerobrine mob) {
        super(IDENTIFIER, mob, 160, 0);
    }

    @Override
    public void prepare() {
        super.prepare();
        narakaSword = null;
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        if (mob.getSoulStack() >= 8)
            return target.getMaxHealth();
        return Math.max(8, (mob.getSoulStack() + 1) * 0.1f * target.getMaxHealth());
    }

    @Override
    protected DamageSource getDamageSource() {
        if (mob.getSoulStack() > 0)
            return NarakaDamageSources.soulAttack(mob);
        return super.getDamageSource();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        Vec3 position = mob.getLookAngle()
                .horizontal()
                .scale(10)
                .add(mob.position().add(0, -10, 0));
        narakaSword = new NarakaSword(level, position, SoulType.NONE);
        narakaSword.setScale(5);
        level.addFreshEntity(narakaSword);
        narakaSword.setRotation(Axis.YN.rotationDegrees(mob.getYRot() - 90)
                .rotateX(Mth.PI));
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        if (narakaSword != null) {
            runAt(5, () -> narakaSword.setMotion(getMotionIdentifier()));
            runBetween(5, 26, () -> narakaSword.setAlpha((tickCount - 5) / 20f));
            runBetween(110, 141, () -> narakaSword.setAlpha(1 - (tickCount - 110) / 30f));

            runAt(110, () -> {
                hurtEntities(level, this::selectTarget, 50);
                mob.removeProtection(mob.getSoulStack());
                mob.resetAbsorbedSouls();
            });

            runAt(110, () -> {
                CustomPacketPayload packet = new NarakaClientboundEventPacket(
                        NarakaClientboundEventPacket.Event.SHAKE_CAMERA,
                        NarakaClientboundEventPacket.Event.MONOCHROME_EFFECT
                );
                NetworkManager.clientbound().send(mob.players(), packet);
                NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), 16,
                        NarakaUtils.CIRCLE,
                        () -> new Vec3(0, mob.getRandom().nextDouble() * 0.3, 0)
                );
                level.playSound(null, mob.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 10000.0F, 0.8F + mob.getRandom().nextFloat() * 0.2F);
                level.playSound(null, mob.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.HOSTILE, 2.0F, 0.5F + mob.getRandom().nextFloat() * 0.2F);
            });
            run(between(110, 125), () -> {
                CustomPacketPayload packet = new NarakaClientboundEventPacket(NarakaClientboundEventPacket.Event.SHAKE_CAMERA);
                NetworkManager.clientbound().send(mob.players(), packet);
                int waveTick = tickCount - 105;
                floatLine(level, 3, waveTick,
                        () -> new Vec3(0, mob.getRandom().nextDouble() * waveTick * 0.05 * 0.5 + 0.5, 0)
                );
            });
        }
    }

    private void floatLine(ServerLevel level, int radius, int waveTick, Supplier<Vec3> movement) {
        BiPredicate<BlockPos, Integer> predicate = (position, r) -> {
            BlockPos actualPosition = mob.blockPosition().offset(position);
            return targetInLookAngle(new Vec3(actualPosition.getX() + 0.5, mob.getY(), actualPosition.getZ() + 0.5), -HALF_ANGLE, HALF_ANGLE)
                    && NarakaUtils.CIRCLE_OUTLINE.test(position, r);
        };
        NarakaSkillUtils.shockwaveBlocks(level, mob.blockPosition(), radius + waveTick, predicate, movement);
    }

    private boolean selectTarget(LivingEntity target) {
        return target != mob;
    }

    private Identifier getMotionIdentifier() {
        if (mob.getCurrentAnimation().equals(HerobrineAnimationIdentifiers.SWORD_ATTACK_SPIN))
            return MotionTypes.SWORD_SWING;
        return MotionTypes.SWORD_STRIKE;
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        if (narakaSword != null)
            narakaSword.discard();
    }

    @Override
    public void interrupt() {
        if (narakaSword != null)
            narakaSword.discard();
    }
}
