package com.yummy.naraka.world.item;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.equipment.NarakaEquipmentAssets;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;

import java.util.Map;

public class NarakaArmorMaterials {
    public static final ArmorMaterial PURIFIED_SOUL = register(
            0,
            Map.of(ArmorType.BOOTS, 0,
                    ArmorType.LEGGINGS, 0,
                    ArmorType.CHESTPLATE, 0,
                    ArmorType.HELMET, 0,
                    ArmorType.BODY, 0),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            0, 0.25f,
            NarakaItemTags.SOUL_MATERIALS,
            NarakaEquipmentAssets.PURIFIED_SOUL
    );

    private static ArmorMaterial register(
            int durability,
            Map<ArmorType, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            TagKey<Item> repairIngredient,
            ResourceKey<EquipmentAsset> assetId
    ) {
        return new ArmorMaterial(durability, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, assetId);
    }

    public static Item.Properties humanoidPropertiesWithoutEnchantable(Item.Properties properties, ArmorMaterial material, ArmorType armorType) {
        return properties.durability(armorType.getDurability(material.durability()))
                .attributes(createAttributes(material, armorType))
                .component(DataComponents.EQUIPPABLE, Equippable.builder(armorType.getSlot())
                        .setEquipSound(material.equipSound())
                        .setAsset(material.assetId()).build())
                .repairable(material.repairIngredient());
    }

    private static ItemAttributeModifiers createAttributes(ArmorMaterial material, ArmorType armorType) {
        int defense = material.defense().getOrDefault(armorType, 0);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup equipmentSlotGroup = EquipmentSlotGroup.bySlot(armorType.getSlot());
        ResourceLocation resourceLocation = ResourceLocation.withDefaultNamespace("armor." + armorType.getName());
        builder.add(Attributes.ARMOR, new AttributeModifier(resourceLocation, defense, AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
        builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourceLocation, material.toughness(), AttributeModifier.Operation.ADD_VALUE), equipmentSlotGroup);
        if (material.knockbackResistance() > 0) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(resourceLocation, material.knockbackResistance(), AttributeModifier.Operation.ADD_VALUE),
                    equipmentSlotGroup
            );
        }

        return builder.build();
    }
}
