package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.NarakaPortalBlockEntity;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NarakaPortalBlock extends BaseEntityBlock implements Portal {
    public static final MapCodec<NarakaPortalBlock> CODEC = simpleCodec(NarakaPortalBlock::new);
    private static final VoxelShape SHAPE = Shapes.box(0.25, 0, -1, 0.75, 3, 2);

    public static BlockPos createRandomNarakaSpawnPosition(RandomSource random) {
        int x = random.nextInt(3, 7);
        return new BlockPos(x, 10, 0);
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
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NarakaPortalBlockEntity(pos, state);
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
    protected void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {
        if (entity.canUsePortal(false) && !level.isClientSide()) {
            if (!entity.isOnPortalCooldown()) {
                level.getBlockEntity(blockPos, NarakaBlockEntityTypes.NARAKA_PORTAL.get())
                        .ifPresent(NarakaPortalBlockEntity::use);
            }
            entity.setAsInsidePortal(this, blockPos);
        }
    }

    @Override
    @Nullable
    public TeleportTransition getPortalDestination(ServerLevel level, Entity entity, BlockPos pos) {
        LevelData.RespawnData respawnData = level.getRespawnData();
        ResourceKey<Level> currentDimension = level.dimension();
        boolean toRespawn = currentDimension == NarakaDimensions.NARAKA;
        ResourceKey<Level> destinationDimension = toRespawn ? respawnData.dimension() : NarakaDimensions.NARAKA;
        ServerLevel destinationLevel = level.getServer().getLevel(destinationDimension);
        float yRot = toRespawn ? respawnData.yaw() : 0;
        float xRot = toRespawn ? respawnData.pitch() : 0;
        if (destinationLevel == null)
            return null;
        BlockPos destinationPosition = toRespawn ? respawnData.pos() : createRandomNarakaSpawnPosition(level.getRandom());
        Vec3 destinationPositionVec = Vec3.atBottomCenterOf(destinationPosition);
        return new TeleportTransition(destinationLevel, destinationPositionVec, Vec3.ZERO, yRot, xRot, TeleportTransition.PLAY_PORTAL_SOUND);
    }
}
