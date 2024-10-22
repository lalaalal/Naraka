package com.yummy.naraka.world.inventory;

import com.yummy.naraka.world.block.entity.SoulCraftingBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ResultSlot extends Slot {
    public ResultSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public void onTake(Player player, ItemStack itemStack) {
        if (player instanceof ServerPlayer serverPlayer && this.container instanceof SoulCraftingBlockEntity soulCraftingBlockEntity)
            soulCraftingBlockEntity.award(serverPlayer);
        super.onTake(player, itemStack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack itemStack) {
        super.checkTakeAchievements(itemStack);
    }
}
