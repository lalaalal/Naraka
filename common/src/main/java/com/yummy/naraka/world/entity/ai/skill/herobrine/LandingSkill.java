package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.ComboSkill;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LandingSkill extends ComboSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("herobrine.landing");

    private final Supplier<Vec3> floatingBlockMovement = () -> new Vec3(0, mob.getRandom().nextDouble() * 0.3 + 0.3, 0);

    public LandingSkill(Herobrine mob) {
        super(LOCATION, mob, 50, 0, 0, 50, null);
        this.shieldCooldown = 60;
        this.shieldDamage = 15;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(15, () -> this.land(level));
        run(between(15, 20) && tickCount % 2 == 0, () -> {
            level.sendParticles(ParticleTypes.FIREWORK, mob.getX(), mob.getY(), mob.getZ(), 10, 0.5, 1, 0.5, 0.3);
            NarakaSkillUtils.shockwaveBlocks(level, mob.players(), mob.blockPosition(), (tickCount - 9) / 2, floatingBlockMovement);
        });
    }

    private void land(ServerLevel level) {
        mob.shakeCamera();
        hurtEntities(level, AbstractHerobrine::isNotHerobrine, 4);
        level.playSound(mob, mob.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.HOSTILE, 1, 1);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() + target.getMaxHealth() * 0.03f;
    }
}
