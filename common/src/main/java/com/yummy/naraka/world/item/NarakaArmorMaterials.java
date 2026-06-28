package com.yummy.naraka.world.item;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public class NarakaArmorMaterials implements ArmorMaterial {
    public static final ArmorMaterial PURIFIED_SOUL = new NarakaArmorMaterials(
            "purified_soul", 1,
            Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
                enumMap.put(ArmorItem.Type.BOOTS, 0);
                enumMap.put(ArmorItem.Type.LEGGINGS, 0);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 0);
                enumMap.put(ArmorItem.Type.HELMET, 0);
            }),
            15,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            0, 0.25f,
            () -> Ingredient.EMPTY
    );

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(ArmorItem.Type.class), enumMap -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    private NarakaArmorMaterials(
            String name,
            int durabilityMultiplier,
            EnumMap<ArmorItem.Type, Integer> protectionFunctionForType,
            int enchantmentValue,
            SoundEvent sound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionFunctionForType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return this.protectionFunctionForType.get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
