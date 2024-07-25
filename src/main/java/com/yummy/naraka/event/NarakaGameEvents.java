package com.yummy.naraka.event;

import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class NarakaGameEvents {
    public static void initialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(NarakaGameEvents::onServerStarted);
        ServerWorldEvents.LOAD.register(NarakaGameEvents::onWorldLoad);
        UseBlockCallback.EVENT.register(NarakaGameEvents::checkHerobrineTotemTrigger);
    }

    private static void onWorldLoad(MinecraftServer server, ServerLevel level) {
        StructureProtector.initialize(server);
    }

    private static void onServerStarted(MinecraftServer server) {
        RegistryAccess registryAccess = server.registryAccess();
        NarakaDamageSources.initialize(registryAccess);
        NarakaEnchantments.initialize(registryAccess);
        NarakaNbtUtils.initialize(server);
    }

    private static InteractionResult checkHerobrineTotemTrigger(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack item = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (hitResult.getDirection() == Direction.UP
                && item.is(ItemTags.CREEPER_IGNITERS) && state.is(Blocks.NETHERRACK)
                && HerobrineTotemBlockEntity.isTotemStructure(level, pos.below())) {
            BlockState totem = level.getBlockState(pos.below());
            if (HerobrineTotemBlockEntity.isSleeping(totem))
                HerobrineTotem.crack(level, pos.below(), totem);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
