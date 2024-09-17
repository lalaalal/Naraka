package com.yummy.naraka;

import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import com.yummy.naraka.world.item.equipmentset.NarakaEquipmentSets;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;

public class NarakaGameEvents {
    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(NarakaGameEvents::onServerStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(NarakaGameEvents::onServerStarted);
        ServerWorldEvents.LOAD.register(NarakaGameEvents::onWorldLoad);

        UseBlockCallback.EVENT.register(NarakaGameEvents::checkHerobrineTotemTrigger);
        UseBlockCallback.EVENT.register(NarakaGameEvents::ironNuggetUse);
        UseBlockCallback.EVENT.register(NarakaGameEvents::boneMealUse);
        ServerTickEvents.END_SERVER_TICK.register(NarakaGameEvents::onEndTick);
        ServerLivingEntityEvents.ALLOW_DEATH.register(NarakaGameEvents::useDeathCount);
        ServerEntityEvents.EQUIPMENT_CHANGE.register(NarakaGameEvents::handleReinforcementEffect);
        ServerEntityEvents.EQUIPMENT_CHANGE.register(NarakaGameEvents::handleEquipmentSetEffect);
        ServerEntityEvents.ENTITY_LOAD.register(NarakaGameEvents::syncPlayerEntityData);
        ServerEntityEvents.ENTITY_LOAD.register(NarakaGameEvents::updateReinforcementEffect);

        LootTableEvents.MODIFY.register(NarakaGameEvents::modifyLootTable);
    }

    private static void syncPlayerEntityData(Entity entity, ServerLevel level) {
        if (entity instanceof Player player)
            EntityDataHelper.syncEntityData(player);
    }

    private static void updateReinforcementEffect(Entity entity, ServerLevel level) {
        if (entity instanceof LivingEntity livingEntity)
            NarakaItemUtils.updateAllReinforcementEffects(livingEntity);
    }

    private static boolean useDeathCount(LivingEntity livingEntity, DamageSource source, float damage) {
        return source.is(DamageTypes.GENERIC_KILL) || !DeathCountHelper.useDeathCount(livingEntity);
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
        NarakaEquipmentSets.updateAllSetEffects(livingEntity);
    }

    private static void onWorldLoad(MinecraftServer server, ServerLevel level) {
        StructureProtector.initialize(server);
    }

    private static void onServerStarting(MinecraftServer server) {
        EntityDataHelper.clear();
        StigmaHelper.clear();
    }

    private static void onServerStarted(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        NarakaDamageSources.initialize(registryAccess);
        NarakaEnchantments.initialize(registryAccess);

        NarakaNetworks.initializeServer();
    }

    private static void onEndTick(MinecraftServer server) {
        StigmaHelper.tick();
    }

    private static void modifyLootTable(ResourceKey<LootTable> key, LootTable.Builder tableBuilder, LootTableSource source, HolderLookup.Provider registries) {
        if (key.location().getPath().contains("chests")) {
            tableBuilder.withPool(LootPool.lootPool()
                    .conditionally(LootItemRandomChanceCondition.randomChance(0.2f).build())
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(NarakaItems.SANCTUARY_COMPASS).setWeight(1))
            );
        }
    }

    private static InteractionResult boneMealUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (stack.is(Items.BONE_MEAL) && state.is(NarakaBlocks.EBONY_SAPLING))
            return InteractionResult.FAIL;
        return InteractionResult.PASS;
    }

    private static InteractionResult ironNuggetUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (stack.is(Items.IRON_NUGGET) && state.is(NarakaBlocks.EBONY_SAPLING)
                && growEbony(stack, player, level, pos)) {
            BoneMealItem.addGrowthParticles(level, pos, 10);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static boolean growEbony(ItemStack itemStack, Player player, Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(NarakaBlocks.EBONY_SAPLING)) {
            if (level instanceof ServerLevel serverLevel) {
                if (NarakaBlocks.EBONY_SAPLING.isBonemealSuccess(level, level.random, blockPos, blockState))
                    NarakaBlocks.EBONY_SAPLING.performBonemeal(serverLevel, level.random, blockPos, blockState);
                itemStack.consume(1, player);
            }
            return true;
        }

        return false;
    }

    private static InteractionResult checkHerobrineTotemTrigger(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack item = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (hitResult.getDirection() == Direction.UP
                && item.is(ItemTags.CREEPER_IGNITERS) && state.is(Blocks.NETHERRACK)
                && HerobrineTotemBlockEntity.isTotemStructure(level, pos.below())
                && HerobrineTotemBlockEntity.isSanctuaryExists(level, pos)) {
            BlockState totem = level.getBlockState(pos.below());
            if (HerobrineTotemBlockEntity.isSleeping(totem))
                HerobrineTotem.crack(level, pos.below(), totem);
        }
        return InteractionResult.PASS;
    }
}
