package com.yummy.naraka;

import com.yummy.naraka.event.EntityEvents;
import com.yummy.naraka.event.LootEvents;
import com.yummy.naraka.event.ServerEvents;
import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class NarakaGameEvents {
    public static void initialize() {
        ServerEvents.SERVER_STARTING.register(NarakaGameEvents::onServerStarting);
        ServerEvents.SERVER_STARTED.register(NarakaGameEvents::onServerStarted);
        ServerEvents.SERVER_LEVEL_LOAD.register(NarakaGameEvents::onWorldLoad);
        ServerEvents.SERVER_STOPPING.register(NarakaGameEvents::onServerStopping);

        EntityEvents.PLAYER_JOIN.register(NarakaGameEvents::syncPlayerEntityData);

        ServerEvents.SERVER_TICK_POST.register(NarakaGameEvents::onEndTick);

        EntityEvents.LIVING_DEATH.register(NarakaGameEvents::useDeathCount);

        LootEvents.MODIFY_LOOT_TABLE.register(NarakaGameEvents::modifyLootTable);
    }

    private static void syncPlayerEntityData(ServerPlayer player) {
        EntityDataHelper.syncEntityData(player);
    }

    private static boolean useDeathCount(LivingEntity livingEntity, DamageSource source) {
        return source.is(DamageTypes.GENERIC_KILL) || !DeathCountHelper.useDeathCount(livingEntity);
    }

    private static void onWorldLoad(ServerLevel level) {
        StructureProtector.initialize(level);
    }

    private static void onServerStarting(MinecraftServer server) {
        EntityDataHelper.clear();
    }

    private static void onServerStarted(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        NarakaDamageSources.initialize(registryAccess);
        NarakaEnchantments.initialize(registryAccess);
    }

    private static void onServerStopping(MinecraftServer server) {
        if (server.isDedicatedServer())
            NarakaMod.config().stop();
    }

    private static void onEndTick(MinecraftServer server) {
        TickSchedule.tick(server.overworld());
    }

    private static void modifyLootTable(ResourceKey<LootTable> key, LootEvents.Context context) {
        if (key.location().getPath().contains("chests")) {
            context.addPool(LootPool.lootPool()
                    .when(LootItemRandomChanceCondition.randomChance(0.01f))
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(NarakaItems.SANCTUARY_COMPASS.get()).setWeight(1))
            );
        }
    }
}
