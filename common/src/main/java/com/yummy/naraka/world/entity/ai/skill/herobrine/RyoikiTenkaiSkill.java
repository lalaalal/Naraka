package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.sounds.NarakaSoundEvents;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.*;
import com.yummy.naraka.world.entity.ai.skill.AttackSkill;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RyoikiTenkaiSkill extends AttackSkill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("final_herobrine.ryoiki_tenkai");
    private final List<LivingEntity> caughtEntities = new ArrayList<>();

    public RyoikiTenkaiSkill(Herobrine mob) {
        super(LOCATION, mob, 70, 800);
    }

    @Nullable
    private ServerTickRateManager tickRateManager;
    private boolean isFrozenBefore;

    @Override
    public void prepare() {
        super.prepare();
        caughtEntities.clear();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return mob.getTarget() != null;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.setDeltaMovement(Vec3.ZERO);

        sendEffectToPlayers(level);

        ShinyEffect shinyEffect = new ShinyEffect(level, 25, false, 1, 0, SoulType.EMERALD.color);
        EntityDataHelper.setEntityData(shinyEffect, NarakaEntityDataTypes.KEEP_UNFROZEN.get(), true);
        shinyEffect.setPos(mob.getEyePosition().add(0, 1.5, 0));
        level.addFreshEntity(shinyEffect);

        tickRateManager = level.getServer().tickRateManager();
        isFrozenBefore = tickRateManager.isFrozen();
        tickRateManager.setFrozen(true);
        EntityDataHelper.setEntityData(mob, NarakaEntityDataTypes.KEEP_UNFROZEN.get(), true);
    }

    private void createField(ServerLevel level, @Nullable LivingEntity target) {
        BlockPos yCheckPosition = target == null ? mob.blockPosition() : target.blockPosition();
        double y = NarakaUtils.findFloor(level, yCheckPosition).getY() + 1.0125f;
        Vec3 basePosition = new Vec3(mob.getX(), y, mob.getZ());
        for (int x = -24; x <= 24; x++) {
            for (int z = -24; z <= 24; z++) {
                if ((x + z) % 2 == 0) {
                    int lifetimeOffset = mob.getRandom().nextIntBetweenInclusive(0, 10);
                    AreaEffect areaEffect = new AreaEffect(level, basePosition.add(x * 2, 0, z * 2), 40 + lifetimeOffset, 2, 2, 0x00ff00, 0)
                            .maxAlpha(0xff);
                    level.addFreshEntity(areaEffect);
                    EntityDataHelper.setEntityData(areaEffect, NarakaEntityDataTypes.KEEP_UNFROZEN.get(), true);
                }
            }
        }
    }

    private void sendEffectToPlayers(ServerLevel level) {
        for (ServerPlayer player : mob.players()) {
            boolean hasStigma = StigmaHelper.hasStigma(player);
            NarakaClientboundEventPacket.Event event = hasStigma ? NarakaClientboundEventPacket.Event.RYOIKI_GREEN_EFFECT : NarakaClientboundEventPacket.Event.RYOIKI_GRAY_EFFECT;
            final CustomPacketPayload packet = new NarakaClientboundEventPacket(event);
            NetworkManager.clientbound().send(player, packet);

            float pitch = hasStigma ? 0.001f : 2f;
            level.playSound(null, player, NarakaSoundEvents.RYOIKI_TENKAI.value(), SoundSource.HOSTILE, 2, pitch);
        }
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(0, () -> createField(level, target));
        runAt(30, () -> checkPlayerPositions(level));
        runBetween(31, 35, () -> spawnShinySparkAndStigmatize(level));
        runAt(50, () -> {
            if (tickRateManager != null)
                tickRateManager.setFrozen(isFrozenBefore);
        });
    }

    private void spawnShinySparkAndStigmatize(ServerLevel level) {
        for (LivingEntity target : caughtEntities) {
            ShinyEffect shinyEffect = ShinyEffect.spawnShinySpark(level, target.position(), mob.getRandom(), 3, 60, SoulType.EMERALD.color);
            EntityDataHelper.setEntityData(shinyEffect, NarakaEntityDataTypes.KEEP_UNFROZEN.get(), true);
            mob.stigmatizeEntity(level, target);

            if (NarakaConfig.COMMON.disableStigma.getValue())
                hurtEntity(level, target);
        }
    }

    @Override
    protected float calculateDamage(LivingEntity target) {
        return mob.getAttackDamage() * 0.25f;
    }

    @Override
    protected DamageSource getDamageSource() {
        return NarakaDamageSources.stigma(mob);
    }

    private void checkPlayerPositions(ServerLevel level) {
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, mob.getBoundingBox().inflate(40, 5, 40), AbstractHerobrine::isNotHerobrine);
        for (LivingEntity target : entities) {
            Vec3 check = target.position()
                    .subtract(mob.position())
                    .scale(0.5f);
            boolean onGreen = (Math.round(check.x) + Math.round(check.z)) % 2 == 0;
            if (onGreen ^ StigmaHelper.hasStigma(target)) {
                handleOnIncorrectPosition(target);
            } else {
                handleOnCorrectPosition(level, target);
            }
        }
    }

    private void handleOnCorrectPosition(ServerLevel level, LivingEntity target) {
        StigmaHelper.removeStigma(target);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 10, 1);
    }

    private void handleOnIncorrectPosition(LivingEntity target) {
        StunHelper.stunEntity(target, 20);
        caughtEntities.add(target);
    }

    @Override
    public void interrupt() {
        if (tickRateManager != null)
            tickRateManager.setFrozen(isFrozenBefore);
        EntityDataHelper.removeEntityData(mob, NarakaEntityDataTypes.KEEP_UNFROZEN.get());
    }
}
