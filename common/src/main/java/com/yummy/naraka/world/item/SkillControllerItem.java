package com.yummy.naraka.world.item;

import com.yummy.naraka.client.gui.screen.SkillControlScreen;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SkillControllerItem extends Item {
    public SkillControllerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (player.level().isClientSide && interactionTarget instanceof SkillUsingMob skillUsingMob) {
            Minecraft.getInstance().setScreen(new SkillControlScreen(skillUsingMob));
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
