package com.yummy.naraka.event;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.network.NarakaClientboundEventPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncEntityDataPacket;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.equipmentset.EquipmentSet;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.function.Consumer;

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

        ItemEvents.ITEM_TOOLTIP_TOP.register(NarakaGameEvents::addItemTooltipsTop);
        ItemEvents.ITEM_TOOLTIP_BOTTOM.register(NarakaGameEvents::addItemTooltipsBottom);
    }

    private static void syncPlayerEntityData(ServerPlayer player) {
        EntityDataHelper.syncEntityData(player, SyncEntityDataPacket.Action.LOAD);
        CustomPacketPayload payload = new NarakaClientboundEventPacket(
                NarakaClientboundEventPacket.Event.STOP_HEROBRINE_SKY,
                NarakaClientboundEventPacket.Event.STOP_WHITE_FOG
        );
        NetworkManager.clientbound().send(player, payload);
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

    private static void modifyLootTable(ResourceKey<LootTable> key, LootEvents.Context context) {
        if (key.location().getPath().contains("chests/village/village_cartographer")) {
            context.addPool(LootPool.lootPool()
                    .when(LootItemRandomChanceCondition.randomChance(0.8f))
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(NarakaItems.SANCTUARY_COMPASS.get()).setWeight(1))
            );
        }
    }

    private static void handleReinforcementEffect(LivingEntity livingEntity, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        if (Reinforcement.get(previousStack) == Reinforcement.get(currentStack)) {
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
        List<EquipmentSet> previousItemEquipmentSets = previousStack.getOrDefault(NarakaDataComponentTypes.EQUIPMENT_SET.get(), List.of());
        List<EquipmentSet> currentItemEquipmentSets = currentStack.getOrDefault(NarakaDataComponentTypes.EQUIPMENT_SET.get(), List.of());
        previousItemEquipmentSets.forEach(equipmentSetHolder -> equipmentSetHolder.updateEffect(livingEntity));
        currentItemEquipmentSets.forEach(equipmentSetHolder -> equipmentSetHolder.updateEffect(livingEntity));
        EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.EQUIPMENT_SET.get()).stream()
                .filter(equipmentSet -> !currentItemEquipmentSets.contains(equipmentSet))
                .forEach(equipmentSet -> equipmentSet.updateEffect(livingEntity));
    }

    private static void addItemTooltipsTop(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        List<EquipmentSet> equipmentSets = item.getOrDefault(NarakaDataComponentTypes.EQUIPMENT_SET.get(), List.of());
        for (EquipmentSet equipmentSet : equipmentSets)
            equipmentSet.addToTooltip(item, context, player, tooltipFlag, builder);
        Reinforcement reinforcement = item.getOrDefault(NarakaDataComponentTypes.REINFORCEMENT.get(), Reinforcement.ZERO);
        reinforcement.addToTooltip(context, builder, tooltipFlag);
    }

    private static void addItemTooltipsBottom(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        if (item.getOrDefault(NarakaDataComponentTypes.BLESSED.get(), false))
            builder.accept(Component.translatable(LanguageKey.BLESSED_KEY).withStyle(ComponentStyles.RAINBOW_COLOR));
        if (item.has(NarakaDataComponentTypes.HEROBRINE_SCARF.get()))
            builder.accept(Component.translatable(LanguageKey.HEROBRINE_SCARF_KEY).withStyle(ComponentStyles.RAINBOW_COLOR));
    }
}
