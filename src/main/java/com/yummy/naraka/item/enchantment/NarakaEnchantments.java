package com.yummy.naraka.item.enchantment;

import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class NarakaEnchantments {
    private static Registry<Enchantment> registry;

    public static void initialize(RegistryAccess registryAccess) {
        registry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Enchantment> enchantmentHolderGetter = context.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemHolderGetter = context.lookup(Registries.ITEM);

        context.register(
                Enchantments.IMPALING,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemHolderGetter.getOrThrow(NarakaItemTags.SPEAR_ENCHANTABLE),
                                        2,
                                        5,
                                        Enchantment.dynamicCost(1, 8),
                                        Enchantment.dynamicCost(21, 8),
                                        4,
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                        .exclusiveWith(enchantmentHolderGetter.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                        .withEffect(
                                EnchantmentEffectComponents.DAMAGE,
                                new AddValue(LevelBasedValue.perLevel(2.5F)),
                                LootItemEntityPropertyCondition.hasProperties(
                                        LootContext.EntityTarget.THIS,
                                        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityTypeTags.SENSITIVE_TO_IMPALING)).build()
                                )
                        )
                        .build(Enchantments.IMPALING.location())
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

    public static Holder<Enchantment> get(ResourceKey<Enchantment> key) {
        return registry.getHolderOrThrow(key);
    }
}
