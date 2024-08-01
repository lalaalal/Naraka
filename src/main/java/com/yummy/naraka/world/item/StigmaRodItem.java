package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.data.StigmaHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StigmaRodItem extends Item {
    public StigmaRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide())
            StigmaHelper.increaseStigma(player, null);
        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity user) {
        if (!user.level().isClientSide)
            StigmaHelper.increaseStigma(target, user);
        return super.hurtEnemy(itemStack, target, user);
    }
}
