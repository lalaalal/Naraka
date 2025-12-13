package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class NarakaToolMaterials {
    public static final ResourceLocation ENTITY_INTERACTION_RANGE_ID = NarakaMod.location("entity_interaction_range");
    public static final ResourceLocation BLOCK_INTERACTION_RANGE_ID = NarakaMod.location("block_interaction_range");

    public static final ToolMaterial LONGINUS = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, Integer.MAX_VALUE, 12, 65, 22, NarakaItemTags.LONGINUS_TOOL_MATERIALS);

    private static final ResourceKey<Block> COBWEB = ResourceKey.create(Registries.BLOCK, NarakaMod.mcLocation("cobweb"));

    public static Item.Properties applyCommonProperties(Item.Properties properties, ToolMaterial material) {
        return properties.durability(material.durability())
                .repairable(material.repairItems())
                .enchantable(material.enchantmentValue());
    }

    public static Item.Properties applyCommonPropertiesWithoutEnchantable(Item.Properties properties, ToolMaterial material) {
        return properties.durability(material.durability())
                .repairable(material.repairItems());
    }

    public static Item.Properties applyWeaponPropertiesWithoutEnchantable(Item.Properties properties, ToolMaterial material, float attackDamage, float attackSpeed) {
        HolderGetter<Block> blocks = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);

        return applyCommonPropertiesWithoutEnchantable(properties, material)
                .component(DataComponents.TOOL, new Tool(List.of(
                                Tool.Rule.minesAndDrops(HolderSet.direct(blocks.getOrThrow(COBWEB)), 15.0f),
                                Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_INSTANTLY_MINES), Float.MAX_VALUE),
                                Tool.Rule.overrideSpeed(blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f)), 1.0f, 2, false
                        )
                )
                .component(DataComponents.WEAPON, new Weapon(1))
                .attributes(createWeaponAttributes(material, attackDamage, attackSpeed).build());
    }

    public static Item.Properties applySpearProperties(Item.Properties properties, ToolMaterial material, boolean enchantable, float attackDamage, float attackSpeed, float interactionRange) {
        properties = applyCommonPropertiesWithoutEnchantable(properties, material);
        if (enchantable)
            properties.enchantable(material.enchantmentValue());
        return properties.attributes(createSpearAttributes(material, attackDamage, attackSpeed, interactionRange).build());
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
