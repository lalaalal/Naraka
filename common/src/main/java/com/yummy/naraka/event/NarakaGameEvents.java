package com.yummy.naraka.event;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.TickFreezeManager;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetGroup;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public final class NarakaGameEvents {
    public static void initialize() {
        ServerEvents.SERVER_STARTING.register(NarakaGameEvents::onServerStarting);
        ServerEvents.SERVER_STARTED.register(NarakaGameEvents::onServerStarted);
        ServerEvents.SERVER_LEVEL_LOAD.register(NarakaGameEvents::onWorldLoad);
        ServerEvents.SERVER_STOPPING.register(NarakaGameEvents::onServerStopping);
        ServerEvents.SERVER_TICK_POST.register(NarakaGameEvents::onEndTick);

        EntityEvents.PLAYER_JOIN.register(NarakaGameEvents::syncPlayerEntityData);
        EntityEvents.LIVING_DEATH.register(NarakaGameEvents::useDeathCount);
        EntityEvents.EQUIPMENT_CHANGE.register(NarakaGameEvents::handleReinforcementEffect);
        EntityEvents.EQUIPMENT_CHANGE.register(NarakaGameEvents::handleEquipmentSetEffect);

        LootEvents.MODIFY_LOOT_TABLE.register(NarakaGameEvents::modifyLootTable);
    }

    private static void syncPlayerEntityData(ServerPlayer player) {
        EntityDataHelper.syncEntityData(player);
        NarakaClientboundEventPacket payload = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY,
                NarakaClientboundEventPacket.Event.STOP_WHITE_FOG,
                getEventByTickFreezeState(player.serverLevel())
        );

        NetworkManager.clientbound().send(player, payload);
    }

    private static NarakaClientboundEventPacket.Event getEventByTickFreezeState(ServerLevel level) {
        if (TickFreezeManager.INSTANCE.shouldFreezeLevel(level))
            return NarakaClientboundEventPacket.Event.FREEZE_TICK;
        return NarakaClientboundEventPacket.Event.UNFREEZE_TICK;
    }

    private static boolean useDeathCount(LivingEntity livingEntity, DamageSource source) {
        return source.is(DamageTypes.GENERIC_KILL) || !DeathCountHelper.useDeathCount(livingEntity);
    }

    private static void onWorldLoad(ServerLevel level) {
        StructureProtector.initialize(level);
    }

    private static void onServerStarting(MinecraftServer server) {

    }

    private static void onServerStarted(MinecraftServer server) {
        if (server.isDedicatedServer())
            NarakaMod.isModLoaded = true;
    }

    private static void onServerStopping(MinecraftServer server) {
        if (server.isDedicatedServer())
            NarakaConfig.stopWatching();
    }

    private static void onEndTick(MinecraftServer server) {
        TickSchedule.tick(server.overworld());
    }

    private static void modifyLootTable(ResourceLocation location, LootEvents.Context context) {
        if (location.getPath().contains("chests/village/village_cartographer")) {
            context.addPool(LootPool.lootPool()
                    .when(LootItemRandomChanceCondition.randomChance(0.8f))
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(NarakaItems.SANCTUARY_COMPASS.getConcreteValue()).setWeight(1))
            );
        }
    }

    private static void handleReinforcementEffect(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        RegistryAccess registryAccess = livingEntity.level().registryAccess();
        if (Reinforcement.get(previousStack, registryAccess) == Reinforcement.get(currentStack, registryAccess)) {
            NarakaItemUtils.checkAndUpdateReinforcementEffects(livingEntity, equipmentSlot, currentStack,
                    ReinforcementEffect::onEquippedItemChanged);
            return;
        }

        NarakaItemUtils.updateReinforcementEffects(livingEntity, equipmentSlot, previousStack,
                ReinforcementEffect::onUnequipped);
        NarakaItemUtils.checkAndUpdateReinforcementEffects(livingEntity, equipmentSlot, currentStack,
                ReinforcementEffect::onEquipped);
    }

    private static void handleEquipmentSetEffect(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        RegistryAccess registryAccess = livingEntity.level().registryAccess();
        NarakaItemUtils.readNbtDataOrDefault(previousStack, NarakaItemUtils.TAG_EQUIPMENT_SET_GROUP, EquipmentSetGroup.CODEC, registryAccess, EquipmentSetGroup.EMPTY)
                .update(livingEntity);
        NarakaItemUtils.readNbtDataOrDefault(currentStack, NarakaItemUtils.TAG_EQUIPMENT_SET_GROUP, EquipmentSetGroup.CODEC, registryAccess, EquipmentSetGroup.EMPTY)
                .update(livingEntity);
        EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.ACTIVE_EQUIPMENT_SET_GROUP.getConcreteValue())
                .update(livingEntity);
    }
}
