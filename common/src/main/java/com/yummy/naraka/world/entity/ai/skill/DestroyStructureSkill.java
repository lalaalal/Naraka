package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.List;

public class DestroyStructureSkill extends Skill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("destroy_structure");
    private static final int SPHERE_RADIUS = 10;

    private final List<BlockPos> positions = new ArrayList<>();
    private int maxRadius = 50;
    private int destroyCount = 5;

    public DestroyStructureSkill(Herobrine mob) {
        super(LOCATION, 400, Integer.MAX_VALUE, mob);
    }

    @Override
    public void prepare() {
        super.prepare();
        maxRadius = 50;
        destroyCount = 5;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.startHerobrineSky();
        if (!mob.hasSpawnPosition()) {
            tickCount = duration;
        }
    }

    @Override
    protected void skillTick(ServerLevel level) {
        if (positions.isEmpty()) {
            maxRadius += 5;
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
        if (maxRadius > 170) {
            tickCount = duration;
        }
    }

    private void determinePositions() {
        int yCount = maxRadius / SPHERE_RADIUS;
        for (int height = 0; height < yCount; height++) {
            double ratio = (1 - height / (double) yCount);
            int radius = (int) (maxRadius * ratio);
            int y = height * SPHERE_RADIUS + mob.getBlockY();

            float circumference = Mth.TWO_PI * radius;
            int n = (int) circumference / SPHERE_RADIUS;
            float degree = Mth.TWO_PI / n;
            for (int t = 0; t < n; t++) {
                int x = (int) (Mth.cos(degree * t) * radius) + mob.getBlockX();
                int z = (int) (Mth.sin(degree * t) * radius) + mob.getBlockZ();
                positions.add(new BlockPos(x, y, z));
            }
        }
    }

    private void destroySphere(ServerLevel level, BlockPos pos) {
        BlockPos start = new BlockPos(pos.getX() - SPHERE_RADIUS, pos.getY() - SPHERE_RADIUS, pos.getZ() - SPHERE_RADIUS);
        BlockPos end = new BlockPos(pos.getX() + SPHERE_RADIUS, pos.getY() + SPHERE_RADIUS, pos.getZ() + SPHERE_RADIUS);
        BoundingBox box = BoundingBox.fromCorners(start, end);
        NarakaUtils.sphere(box, 1, blockPos -> {
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 10);
        });
        level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1, 0, 0, 0, 1);
    }
}
