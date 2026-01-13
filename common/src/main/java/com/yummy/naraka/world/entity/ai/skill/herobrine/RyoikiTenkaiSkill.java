package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AreaEffect;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShinyEffect;
import com.yummy.naraka.world.entity.StunHelper;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RyoikiTenkaiSkill extends TargetSkill<Herobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("final_herobrine.ryoiki_tenkai");
    private final List<LivingEntity> caughtEntities = new ArrayList<>();

    public RyoikiTenkaiSkill(Herobrine mob) {
        super(IDENTIFIER, mob, 70, 500);
    }

    @Override
    public void prepare() {
        super.prepare();
        caughtEntities.clear();
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return true;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.setDeltaMovement(Vec3.ZERO);
        double y = NarakaUtils.findFloor(level, mob.blockPosition()).getY() + 1.0125f;
        Vec3 basePosition = new Vec3(mob.getX(), y, mob.getZ());
        for (int x = -24; x <= 24; x++) {
            for (int z = -24; z <= 24; z++) {
                if ((x + z) % 2 == 0) {
                    level.addFreshEntity(new AreaEffect(level, basePosition.add(x * 2, 0, z * 2), 50, 2, 2, 0x00ff00, 0)
                            .maxAlpha(0xff));
                }
            }
        }

        for (ServerPlayer player : mob.players()) {
            NarakaClientboundEventPacket.Event event = StigmaHelper.hasStigma(player) ? NarakaClientboundEventPacket.Event.FILTER_GREEN_GRAY_EFFECT : NarakaClientboundEventPacket.Event.FILTER_GREEN_COLOR_EFFECT;
            final CustomPacketPayload packet = new NarakaClientboundEventPacket(event);
            NetworkManager.clientbound().send(player, packet);
        }
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(30, () -> checkPlayerPositions(level));
        runBetween(31, 40, () -> spawnShinySparkAndStigmatize(level));
    }

    private void spawnShinySparkAndStigmatize(ServerLevel level) {
        for (LivingEntity target : caughtEntities) {
            if (tickCount % 2 == 0)
                ShinyEffect.spawnShinySpark(level, target.position(), mob.getRandom(), 3, 60, SoulType.EMERALD.color);
            StigmaHelper.increaseStigma(level, target, mob, true);
        }
    }

    private void checkPlayerPositions(ServerLevel level) {
        List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(40, 5, 40));
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
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 1, 1);
    }

    private void handleOnIncorrectPosition(LivingEntity target) {
        StunHelper.stunEntity(target, 20);
        caughtEntities.add(target);
    }
}
