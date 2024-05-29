package com.yummy.naraka.item;

import com.mojang.logging.LogUtils;
import com.yummy.naraka.attachment.StigmaHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

/**
 * Item for testing
 *
 * @author lalaalal
 */
public class TestItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();

    public TestItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        if (player.isLocalPlayer())
            return super.use(pLevel, player, pUsedHand);
        int stigma = StigmaHelper.getStigma(player);
        LOGGER.debug("%s 's value is %s".formatted(player.getDisplayName(), stigma));
        StigmaHelper.increaseStigma(player);
        return super.use(pLevel, player, pUsedHand);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            int stigma = StigmaHelper.getStigma(player);
            LOGGER.debug("%s 's value is %s".formatted(livingEntity.getDisplayName(), stigma));
            StigmaHelper.increaseStigma(livingEntity);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
