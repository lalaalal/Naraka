package com.yummy.naraka.world;

import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class TickFreezeManager {
    private final Set<ResourceKey<Level>> frozenLevels = new HashSet<>();

    public static final TickFreezeManager INSTANCE = new TickFreezeManager();

    private TickFreezeManager() {

    }

    public void freeze(ServerLevel level) {
        NetworkManager.clientbound().send(level.players(), new NarakaClientboundEventPacket(NarakaClientboundEventPacket.Event.FREEZE_TICK));
        frozenLevels.add(level.dimension());
    }

    public void unfreeze(ServerLevel level) {
        NetworkManager.clientbound().send(level.players(), new NarakaClientboundEventPacket(NarakaClientboundEventPacket.Event.UNFREEZE_TICK));
        frozenLevels.remove(level.dimension());
    }

    public boolean shouldFreezeLevel(Level level) {
        return frozenLevels.contains(level.dimension());
    }

    public boolean shouldFreezeEntity(Entity entity) {
        return !EntityDataHelper.getRawEntityData(entity, NarakaEntityDataTypes.KEEP_UNFROZEN.getConcreteValue()) && !(entity instanceof Player);
    }

    public void reset() {
        frozenLevels.clear();
    }
}
