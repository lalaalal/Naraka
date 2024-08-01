package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataTypes;
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
            EntityDataHelper.setEntityData(player, EntityDataTypes.STIGMA, EntityDataHelper.getEntityData(player, EntityDataTypes.STIGMA) + 1);
        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity user) {
        EntityDataHelper.setEntityData(target, EntityDataTypes.STIGMA, EntityDataHelper.getEntityData(target, EntityDataTypes.STIGMA) + 1);
        return super.hurtEnemy(itemStack, target, user);
    }
}
