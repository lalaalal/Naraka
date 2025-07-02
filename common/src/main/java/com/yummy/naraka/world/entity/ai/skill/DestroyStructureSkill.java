package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.NarakaSkillUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DestroyStructureSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("destroy_structure");
    private static final int SPHERE_RADIUS = 10;

    private final List<BlockPos> positions = new ArrayList<>();
    private int radius = 40;
    private int destroyCount = 5;

    public DestroyStructureSkill(Herobrine mob) {
        super(LOCATION, mob, 200, Integer.MAX_VALUE);
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
    protected void onFirstTick(ServerLevel level) {
        if (mob.hasSpawnPosition()) {
            mob.startWhiteScreen();
        } else {
            tickCount = duration - 1;
            mob.sendMusic(3);
            mob.startHerobrineSky(level);
        }
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        if (tickCount == 20)
            mob.startHerobrineSky(level);
        if (tickCount < 15 || NarakaConfig.COMMON.disableHerobrineDestroyingStructure.getValue() || !mob.hasSpawnPosition())
            return;
        runAt(80, this::startPhase3);
        runAt(160, () -> hurtEntities(level, AbstractHerobrine::isNotHerobrine, 5));
        if (radius < 95) {
            if (positions.isEmpty()) {
                radius += 5;
                determinePositions();
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
    }

    @Override
    protected void hurtEntities(ServerLevel level, Predicate<LivingEntity> predicate, double size) {
        super.hurtEntities(level, predicate, size);
        NarakaSkillUtils.sendCircleParticle(level, mob.position(), NarakaParticleTypes.CORRUPTED_FIRE_FLAME.get(), size);
    }

    protected void startPhase3() {
        mob.stopWhiteScreen();
        mob.sendMusic(3);
        mob.stopStaticAnimation();
        mob.setFinalModel(true);
        mob.playStaticAnimation(AnimationLocations.ENTER_PHASE_3, 120, false);
    }

    private void determinePositions() {
        int yCount = radius / SPHERE_RADIUS;
        for (int height = 0; height < yCount; height++) {
            double ratio = (1 - height / (double) yCount);
            int radius = (int) (this.radius * ratio);
            int y = 15 + height * SPHERE_RADIUS + mob.getBlockY();

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
