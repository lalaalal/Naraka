package com.yummy.naraka.world.item;

import com.mojang.datafixers.util.Pair;
import com.yummy.naraka.data.worldgen.NarakaStructures;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.Optional;

public class SanctuaryCompassItem extends Item {
    public SanctuaryCompassItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        BlockPos userPos = entity.blockPosition();
        updateTracker(stack, level, userPos, false);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        BlockPos userPos = player.blockPosition();
        ItemStack itemStack = player.getItemInHand(interactionHand);
        updateTracker(itemStack, level, userPos, true);
        return super.use(level, player, interactionHand);
    }

    protected void updateTracker(ItemStack itemStack, Level level, BlockPos pos, boolean forceUpdate) {
        Optional<BlockPos> optionalSanctuaryPos = parseSanctuaryPos(itemStack);
        boolean tracked = optionalSanctuaryPos.isPresent();

        if (level instanceof ServerLevel serverLevel) {
            update(serverLevel, pos, tracked, forceUpdate).ifPresent(
                    globalPos -> saveSanctuaryPosition(itemStack, globalPos)
            );
        }
    }

    private static void saveSanctuaryPosition(ItemStack itemStack, GlobalPos globalPos) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("SanctuaryPosition"))
            tag.remove("SanctuaryPosition");
        tag.put("SanctuaryPosition", NbtUtils.writeBlockPos(globalPos.pos()));
    }

    private static Optional<BlockPos> parseSanctuaryPos(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.contains("SanctuaryPosition")) {
            CompoundTag sanctuaryPosTag = tag.getCompound("SanctuaryPosition");
            return Optional.of(NbtUtils.readBlockPos(sanctuaryPosTag));
        }
        return Optional.empty();
    }

    @Nullable
    public static GlobalPos getGlobalPos(ItemStack itemStack) {
        return parseSanctuaryPos(itemStack)
                .map(pos -> GlobalPos.of(Level.OVERWORLD, pos))
                .orElse(null);
    }

    public static Optional<GlobalPos> update(ServerLevel serverLevel, BlockPos userPos, boolean tracked, boolean forceUpdate) {
        if (tracked && !forceUpdate)
            return Optional.empty();
        HolderLookup.RegistryLookup<Structure> registry = serverLevel.registryAccess().lookupOrThrow(Registries.STRUCTURE);
        Holder<Structure> sanctuary = registry.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY);
        Pair<BlockPos, Holder<Structure>> pair = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, HolderSet.direct(sanctuary), userPos, 100, false);
        if (pair == null)
            return Optional.empty();
        return Optional.of(GlobalPos.of(Level.OVERWORLD, pair.getFirst()));
    }
}
