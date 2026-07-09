package com.yummy.naraka.world.item;

import com.yummy.naraka.network.NarakaClientboundEntityEventPacket;
import com.yummy.naraka.network.NetworkManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SkillUsingMobControllerItem extends Item implements PickRangeModifiable {
    private final NarakaClientboundEntityEventPacket.Event event;

    public SkillUsingMobControllerItem(Properties properties, NarakaClientboundEntityEventPacket.Event event) {
        super(properties.stacksTo(1));
        this.event = event;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkManager.clientbound().send(serverPlayer, new NarakaClientboundEntityEventPacket(event, interactionTarget));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public float getPickRange() {
        return 64;
    }
}
