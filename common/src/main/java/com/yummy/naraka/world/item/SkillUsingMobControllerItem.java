package com.yummy.naraka.world.item;

import com.yummy.naraka.network.NarakaClientboundEntityEventPacket;
import com.yummy.naraka.network.NetworkManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SkillUsingMobControllerItem extends Item {
    private static ItemAttributeModifiers createModifiers() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(NarakaTiers.ENTITY_INTERACTION_RANGE_ID, 32, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                ).build();
    }

    private final NarakaClientboundEntityEventPacket.Event event;

    public SkillUsingMobControllerItem(Properties properties, NarakaClientboundEntityEventPacket.Event event) {
        super(properties.component(DataComponents.ATTRIBUTE_MODIFIERS, createModifiers())
                .stacksTo(1));
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
}
