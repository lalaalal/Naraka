package com.yummy.naraka;

import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;

public final class NarakaGameEvents {
    public static void initialize() {
        LifecycleEvent.SERVER_STARTING.register(NarakaGameEvents::onServerStarting);
        LifecycleEvent.SERVER_STARTED.register(NarakaGameEvents::onServerStarted);
        LifecycleEvent.SERVER_LEVEL_LOAD.register(NarakaGameEvents::onWorldLoad);
        PlayerEvent.PLAYER_JOIN.register(NarakaGameEvents::syncPlayerEntityData);

        InteractionEvent.RIGHT_CLICK_BLOCK.register(NarakaGameEvents::checkHerobrineTotemTrigger);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(NarakaGameEvents::ironNuggetUse);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(NarakaGameEvents::boneMealUse);
        InteractionEvent.RIGHT_CLICK_ITEM.register(NarakaGameEvents::preventItemUseDuringStun);
        PlayerEvent.ATTACK_ENTITY.register(NarakaGameEvents::preventAttackEntityDuringStun);

        TickEvent.SERVER_POST.register(NarakaGameEvents::onEndTick);

        EntityEvent.LIVING_DEATH.register(NarakaGameEvents::useDeathCount);

        EntityEvent.ADD.register(NarakaGameEvents::updateReinforcementEffect);

        LootEvent.MODIFY_LOOT_TABLE.register(NarakaGameEvents::modifyLootTable);
    }

    private static CompoundEventResult<ItemStack> preventItemUseDuringStun(Player player, InteractionHand interactionHand) {
        if (NarakaAttributeModifiers.hasAttributeModifier(player, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING))
            return CompoundEventResult.interruptTrue(player.getItemInHand(interactionHand));
        return CompoundEventResult.pass();
    }

    private static EventResult preventAttackEntityDuringStun(Player player, Level level, Entity target, InteractionHand hand, @Nullable EntityHitResult result) {
        if (NarakaAttributeModifiers.hasAttributeModifier(player, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING))
            return EventResult.interruptTrue();
        return EventResult.pass();
    }

    private static void syncPlayerEntityData(ServerPlayer player) {
        EntityDataHelper.syncEntityData(player);
    }

    private static EventResult updateReinforcementEffect(Entity entity, Level level) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity)
            NarakaItemUtils.updateAllReinforcementEffects(livingEntity);
        return EventResult.pass();
    }

    private static EventResult useDeathCount(LivingEntity livingEntity, DamageSource source) {
        boolean result = source.is(DamageTypes.GENERIC_KILL) || !DeathCountHelper.useDeathCount(livingEntity);
        return EventResult.interrupt(result);
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

        if (server.isDedicatedServer())
            NarakaNetworks.initializeServer();
    }

    private static void onEndTick(MinecraftServer server) {
        TickSchedule.tick(server.overworld());
    }

    private static void modifyLootTable(ResourceKey<LootTable> key, LootEvent.LootTableModificationContext context, boolean builtin) {
        if (key.location().getPath().contains("chests")) {
            context.addPool(LootPool.lootPool()
                    .when(LootItemRandomChanceCondition.randomChance(0.01f))
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(NarakaItems.SANCTUARY_COMPASS.get()).setWeight(1))
            );
        }
    }

    private static EventResult boneMealUse(Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        Level level = player.level();
        ItemStack stack = player.getItemInHand(hand);
        BlockState state = level.getBlockState(pos);

        if (stack.is(Items.BONE_MEAL) && state.is(NarakaBlocks.EBONY_SAPLING.get()))
            return EventResult.interruptTrue();
        return EventResult.pass();
    }

    private static EventResult ironNuggetUse(Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        Level level = player.level();
        ItemStack stack = player.getItemInHand(hand);
        BlockState state = level.getBlockState(pos);

        if (stack.is(Items.IRON_NUGGET) && state.is(NarakaBlocks.EBONY_SAPLING.get())
                && growEbony(stack, player, level, pos)) {
            BoneMealItem.addGrowthParticles(level, pos, 10);
            return EventResult.interruptDefault();
        }
        return EventResult.pass();
    }

    private static boolean growEbony(ItemStack itemStack, Player player, Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(NarakaBlocks.EBONY_SAPLING.get())) {
            if (level instanceof ServerLevel serverLevel) {
                if (NarakaBlocks.EBONY_SAPLING.get().isBonemealSuccess(level, level.random, blockPos, blockState))
                    NarakaBlocks.EBONY_SAPLING.get().performBonemeal(serverLevel, level.random, blockPos, blockState);
                itemStack.consume(1, player);
            }
            return true;
        }

        return false;
    }

    private static EventResult checkHerobrineTotemTrigger(Player player, InteractionHand hand, BlockPos pos, Direction direction) {
        Level level = player.level();
        ItemStack item = player.getItemInHand(hand);
        BlockState state = level.getBlockState(pos);

        if (direction == Direction.UP
                && item.is(ItemTags.CREEPER_IGNITERS) && state.is(Blocks.NETHERRACK)
                && HerobrineTotemBlockEntity.isTotemStructure(level, pos.below())
                && HerobrineTotemBlockEntity.isSanctuaryExists(level, pos)) {
            BlockState totem = level.getBlockState(pos.below());
            if (HerobrineTotemBlockEntity.isSleeping(totem))
                HerobrineTotem.crack(level, pos.below(), totem);
        }
        return EventResult.pass();
    }
}
