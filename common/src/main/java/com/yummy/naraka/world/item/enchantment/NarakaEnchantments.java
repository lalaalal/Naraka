package com.yummy.naraka.world.item.enchantment;

import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;

public class NarakaEnchantments {
    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantmentHolderGetter = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

        context.register(
                Enchantments.SHARPNESS,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemHolderGetter.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                        itemHolderGetter.getOrThrow(ItemTags.SWORD_ENCHANTABLE),
                                        10,
                                        5,
                                        Enchantment.dynamicCost(1, 11),
                                        Enchantment.dynamicCost(21, 11),
                                        1,
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                        .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                        .withEffect(EnchantmentEffectComponents.DAMAGE, new AddValue(LevelBasedValue.perLevel(1.0F, 0.5F)))
                        .build(Enchantments.SHARPNESS.location())
        );
        context.register(
                Enchantments.LOYALTY,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemHolderGetter.getOrThrow(NarakaItemTags.SPEAR_ENCHANTABLE),
                                        5,
                                        3,
                                        Enchantment.dynamicCost(12, 7),
                                        Enchantment.constantCost(50),
                                        2,
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                        .withEffect(
                                EnchantmentEffectComponents.TRIDENT_RETURN_ACCELERATION,
                                new AddValue(LevelBasedValue.perLevel(1.0F))
                        )
                        .build(Enchantments.LOYALTY.location())
        );
    }
}
