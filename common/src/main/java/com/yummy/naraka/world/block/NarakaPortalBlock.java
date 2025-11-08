package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.NarakaPortalBlockEntity;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NarakaPortalBlock extends BaseEntityBlock implements Portal {
    public static final MapCodec<NarakaPortalBlock> CODEC = simpleCodec(NarakaPortalBlock::new);
    private static final VoxelShape SHAPE = Shapes.box(0.25, 0, -1, 0.75, 3, 2);

    public static final BlockPos BASE_POSITION = new BlockPos(0, 63, 0);

    public static BlockPos createRandomNarakaSpawnPosition(RandomSource random) {
        int x = random.nextInt(3, 7);
        return new BlockPos(x, BASE_POSITION.getY(), 0);
    }

    public NarakaPortalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NarakaPortalBlockEntity(pos, state);
    }

    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (player.getMainHandItem().is(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get()))
            return 1;
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
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

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity.canUsePortal(false) && !level.isClientSide()) {
            if (!entity.isOnPortalCooldown()) {
                level.getBlockEntity(pos, NarakaBlockEntityTypes.NARAKA_PORTAL.get())
                        .ifPresent(NarakaPortalBlockEntity::use);
            }
            entity.setAsInsidePortal(this, pos);
        }
    }


    @Override
    @Nullable
    public DimensionTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        ResourceKey<Level> currentDimension = level.dimension();
        boolean toRespawn = currentDimension == NarakaDimensions.NARAKA;
        DimensionTransition.PostDimensionTransition dimensionTransition = DimensionTransition.PLAY_PORTAL_SOUND
                .then(DimensionTransition.PLACE_PORTAL_TICKET);
        if (toRespawn && entity instanceof ServerPlayer serverPlayer) {
            return serverPlayer.findRespawnPositionAndUseSpawnBlock(false, dimensionTransition);
        }

        ResourceKey<Level> destinationDimension = NarakaDimensions.NARAKA;
        ServerLevel destinationLevel = level.getServer().getLevel(destinationDimension);
        if (destinationLevel == null)
            return null;
        BlockPos destinationPosition = createRandomNarakaSpawnPosition(level.getRandom());
        Vec3 destinationPositionVec = Vec3.atBottomCenterOf(destinationPosition);
        return new DimensionTransition(destinationLevel, destinationPositionVec, Vec3.ZERO, 0, 0, dimensionTransition);
    }
}
