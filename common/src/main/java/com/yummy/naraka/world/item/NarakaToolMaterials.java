package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class NarakaToolMaterials {
    private static final ResourceLocation ENTITY_INTERACTION_RANGE_ID = NarakaMod.location("entity_interaction_range");
    private static final ResourceLocation BLOCK_INTERACTION_RANGE_ID = NarakaMod.location("block_interaction_range");

    public static final ToolMaterial LONGINUS = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 0, 6, 66, 6, NarakaItemTags.LONGINUS_TOOL_MATERIALS);

    public static Item.Properties applyCommonPropertiesWithoutEnchantable(Item.Properties properties, ToolMaterial material) {
        return properties.durability(material.durability())
                .repairable(material.repairItems());
    }

    public static Item.Properties applyWeaponPropertiesWithoutEnchantable(Item.Properties properties, ToolMaterial material, float attackDamage, float attackSpeed) {
        return applyCommonPropertiesWithoutEnchantable(properties, material)
                .attributes(createWeaponAttributes(material, attackDamage, attackSpeed).build());
    }

    public static Item.Properties applySpearProperties(Item.Properties properties, ToolMaterial material, float attackDamage, float attackSpeed, float interactionRange) {
        return applyCommonPropertiesWithoutEnchantable(properties, material)
                .attributes(createSpearAttributes(material, attackDamage, attackSpeed, interactionRange).build());
    }

    public static ItemAttributeModifiers.Builder createWeaponAttributes(ToolMaterial material, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamage + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                );
    }

    public static ItemAttributeModifiers.Builder createSpearAttributes(ToolMaterial material, float attackDamage, float attackSpeed, float interactionRange) {
        return createWeaponAttributes(material, attackDamage, attackSpeed)
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(ENTITY_INTERACTION_RANGE_ID, interactionRange, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.BLOCK_INTERACTION_RANGE,
                        new AttributeModifier(BLOCK_INTERACTION_RANGE_ID, interactionRange, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                );
    }
}
