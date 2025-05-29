package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (level instanceof ServerLevel serverLevel)
            StigmaHelper.increaseStigma(serverLevel, player, player);
        return InteractionResult.CONSUME;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.level() instanceof ServerLevel level) {
            StigmaHelper.increaseStigma(level, target, attacker);
            Reinforcement.increase(stack, NarakaReinforcementEffects.INCREASE_ATTACK_DAMAGE);
        }
    }
}
