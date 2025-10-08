package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DestroyStructureSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("herobrine.destroy_structure");
    private static final int SPHERE_RADIUS = 10;
    private static final int FLOOR_DESTROY_START_TICK = 100;
    private static final int FLOOR_DESTROY_END_TICK = 117;
    private static final int FLOOR_DESTROY_BASE_RADIUS = 25;

    private final List<BlockPos> positions = new ArrayList<>();
    private int radius = 40;
    private int destroyCount = 5;

    public DestroyStructureSkill(Herobrine mob) {
        super(LOCATION, mob, 280, Integer.MAX_VALUE);
    }

    @Override
    public void prepare() {
        super.prepare();
        radius = 40;
        destroyCount = 5;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(25, () -> mob.playStaticAnimation(HerobrineAnimationLocations.PREPARE_PHASE_3, 95, false, true));

        runAt(85, mob::startWhiteScreen);
        runAt(87, () -> mob.setDeltaMovement(0, 0.6, 0));
        runBetween(85, 100, () -> reduceSpeed(0.75));

        runAt(140, () -> mob.fixTimeAndWeather(level));
        runAt(160, () -> startPhase3(level));
        runAt(240, () -> hurtEntities(level, this::checkTarget, 5));
        runAt(240, () -> mob.setDeltaMovement(0, -0.5, 0));
        runAfter(240, () -> reduceSpeed(0.75));
        run(canDestroyStructure(), () -> destroyStructure(level));
        run(between(FLOOR_DESTROY_START_TICK, FLOOR_DESTROY_END_TICK) && canDestroyStructure(), () -> destroyFloor(level));
    }

    private boolean checkTarget(LivingEntity target) {
        return targetInLookAngle(target, -Mth.HALF_PI, Mth.HALF_PI) && AbstractHerobrine.isNotHerobrine(target);
    }

    private boolean canDestroyStructure() {
        return after(95) && radius < 95 && !NarakaConfig.COMMON.disableHerobrineDestroyingStructure.getValue() && mob.hasSpawnPosition();
    }

    private void destroyStructure(ServerLevel level) {
        if (positions.isEmpty()) {
            radius += 5;
            determinePositions(level);
            destroyCount *= 2;
        } else {
            for (int i = 0; i < destroyCount; i++) {
                int randomIndex = mob.getRandom().nextInt(positions.size());
                BlockPos pos = positions.get(randomIndex);
                positions.remove(pos);

                destroySphere(level, pos);

                if (positions.isEmpty())
                    break;
            }
        }
    }

    private void destroyFloor(ServerLevel level) {
        if (tickCount % 2 == 1)
            return;
        int currentRadius = (tickCount - FLOOR_DESTROY_START_TICK) / 2 + FLOOR_DESTROY_BASE_RADIUS;

        BlockPos base = NarakaUtils.findFloor(level, mob.blockPosition());
        NarakaUtils.square(base, currentRadius, NarakaUtils.CIRCLE_OUTLINE, blockPos -> {
            if ((base.getX() - 2 < blockPos.getX() && blockPos.getX() < base.getX() + 2) || (base.getZ() - 2 < blockPos.getZ() && blockPos.getZ() < base.getZ() + 2))
                return;
            BlockPos current = blockPos;
            BlockState state = level.getBlockState(blockPos);
            while (state.is(Blocks.CALCITE)) {
                current = current.below();
                state = level.getBlockState(current);
            }
            BlockPos floor = NarakaUtils.findFloor(level, current);
            state = level.getBlockState(floor);
            if (state.is(Blocks.BLACKSTONE)) {
                for (int i = 0; i < 17; i++) {
                    level.setBlock(floor.below(i), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                }
            }
        });
    }

    @Override
    protected void hurtEntities(ServerLevel level, Predicate<LivingEntity> predicate, double size) {
        super.hurtEntities(level, predicate, size);
        NarakaSkillUtils.sendCircleParticle(level, mob.position(), NarakaFlameParticleOption.GOD_BLOOD, size);
    }

    @Override
    protected void onHurtEntity(ServerLevel level, LivingEntity target) {
        mob.stigmatizeEntity(level, target);
        level.playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 1, 1);
    }

    protected void startPhase3(ServerLevel level) {
        mob.stopWhiteScreen();
        mob.startHerobrineSky();
        mob.sendMusic(3);
        mob.setFinalModel(true);
        mob.playStaticAnimation(HerobrineAnimationLocations.ENTER_PHASE_3, 120, false, true);
        Skill<?> stormSkill = mob.getSkillManager().getSkill(StormSkill.LOCATION);
        if (stormSkill != null)
            stormSkill.setCooldown();
    }

    private void determinePositions(ServerLevel level) {
        int yCount = radius / SPHERE_RADIUS;
        BlockPos floor = NarakaUtils.findFloor(level, mob.blockPosition());
        for (int height = 0; height < yCount; height++) {
            double ratio = (1 - height / (double) yCount);
            int radius = (int) (this.radius * ratio);
            int y = 15 + height * SPHERE_RADIUS + floor.getY() + 1;

            float circumference = Mth.TWO_PI * radius;
            int n = (int) circumference / SPHERE_RADIUS;
            float angle = Mth.TWO_PI / n;
            for (int t = 0; t < n; t++) {
                int x = (int) (Mth.cos(angle * t) * radius) + mob.getBlockX();
                int z = (int) (Mth.sin(angle * t) * radius) + mob.getBlockZ();
                positions.add(new BlockPos(x, y, z));
            }
        }
    }

    private void destroySphere(ServerLevel level, BlockPos pos) {
        BlockPos start = new BlockPos(pos.getX() - SPHERE_RADIUS, pos.getY() - SPHERE_RADIUS, pos.getZ() - SPHERE_RADIUS);
        BlockPos end = new BlockPos(pos.getX() + SPHERE_RADIUS, pos.getY() + SPHERE_RADIUS, pos.getZ() + SPHERE_RADIUS);
        BoundingBox box = BoundingBox.fromCorners(start, end);
        NarakaUtils.sphere(box, 1, blockPos -> {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        });
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage();
    }
}
