package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.server.level.ServerLevel;
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
        if (level instanceof ServerLevel serverLevel)
            StigmaHelper.increaseStigma(serverLevel, player, player);
        return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity user) {
        if (target.level() instanceof ServerLevel serverLevel) {
            StigmaHelper.increaseStigma(serverLevel, target, user);
            Reinforcement.increase(itemStack, NarakaReinforcementEffects.INCREASE_ATTACK_DAMAGE);
        }
        return super.hurtEnemy(itemStack, target, user);
    }
}
