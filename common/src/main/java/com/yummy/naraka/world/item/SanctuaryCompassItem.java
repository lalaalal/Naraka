package com.yummy.naraka.world.item;

import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SanctuaryCompassItem extends Item {
    public SanctuaryCompassItem(Properties properties) {
        super(properties.component(NarakaDataComponentTypes.SANCTUARY_TRACKER.get(), SanctuaryTracker.UNTRACKED));
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
        SanctuaryTracker tracker = itemStack.get(NarakaDataComponentTypes.SANCTUARY_TRACKER.get());
        if (tracker != null && (!tracker.tracked() || forceUpdate) && level instanceof ServerLevel serverLevel) {
            tracker = tracker.update(serverLevel, pos, forceUpdate);
            itemStack.set(NarakaDataComponentTypes.SANCTUARY_TRACKER.get(), tracker);
        }
    }
}
