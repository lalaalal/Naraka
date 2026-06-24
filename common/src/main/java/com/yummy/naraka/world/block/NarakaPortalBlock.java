package com.yummy.naraka.world.block;

import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.NarakaPortalBlockEntity;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NarakaPortalBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Shapes.box(-1, 0, 0.25, 2, 3, 0.75);

    public static final BlockPos BASE_POSITION = new BlockPos(0, 64, 0);
    public static final BlockPos IN_NARAKA_DIMENSION_POSITION = new BlockPos(0, 65, 0);

    public static BlockPos createRandomNarakaSpawnPosition(RandomSource random) {
        int z = random.nextInt(3, 7);
        return new BlockPos(0, BASE_POSITION.getY(), z);
    }

    public NarakaPortalBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NarakaPortalBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (player.getMainHandItem().is(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get()))
            return 1;
        return super.getDestroyProgress(state, player, level, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextDouble() < 0.5) {
            level.getBlockEntity(pos, NarakaBlockEntityTypes.NARAKA_PORTAL.get())
                    .ifPresent(NarakaPortalBlockEntity::use);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Vec3 center = Vec3.atCenterOf(pos);
        for (int count = 0; count < 15; count++) {
            double x = center.x;
            double z = random.nextGaussian() - 0.5 + center.z;
            double y = center.y;
            double xSpeed = random.nextGaussian();
            double zSpeed = random.nextGaussian();
            double ySpeed = random.nextGaussian();
            level.addParticle(SoulParticleOption.with(SoulType.GOD_BLOOD), x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canChangeDimensions() && level instanceof ServerLevel serverLevel && !entity.getType().is(NarakaEntityTypeTags.NARAKA_PORTAL_IGNORE)) {
            if (!entity.isOnPortalCooldown()) {
                level.getBlockEntity(pos, NarakaBlockEntityTypes.NARAKA_PORTAL.get())
                        .ifPresent(NarakaPortalBlockEntity::use);

                ServerLevel narakaLevel = serverLevel.getServer().getLevel(NarakaDimensions.NARAKA);
                if (narakaLevel != null)
                    entity.changeDimension(narakaLevel);
            }
        }
    }
}
