package com.yummy.naraka.world.entity.ai.skill.absolute_herobrine;

import com.mojang.math.Axis;
import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import com.yummy.naraka.world.entity.NarakaSword;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.motion.MotionTypes;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SwordSwingSkill extends AttackSkill<AbsoluteHerobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("absolute_herobrine.sword_swing");

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
        return 0;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        Vec3 position = mob.getLookAngle()
                .horizontal()
                .scale(2)
                .add(mob.position());
        narakaSword = new NarakaSword(level, position, SoulType.NONE);
        level.addFreshEntity(narakaSword);
        narakaSword.setRotation(Axis.YN.rotationDegrees(mob.getYRot() - 90)
                .rotateX(Mth.PI));
        narakaSword.setScale(5);
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        if (narakaSword != null) {
            runAt(5, () -> narakaSword.setMotion(MotionTypes.SWORD_SWING));
            runBetween(5, 26, () -> narakaSword.setAlpha((tickCount - 5) / 20f));
            runBetween(110, 141, () -> narakaSword.setAlpha(1 - (tickCount - 110) / 30f));
        }
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
