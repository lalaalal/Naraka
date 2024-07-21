package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Unbreakable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class NarakaItems {
    private static final String SOUL_INFUSED_PREFIX = "soul_infused_";
    private static final Set<Item> SOUL_INFUSED_ITEMS = new HashSet<>();
    private static final Set<SwordItem> SOUL_INFUSED_SWORDS = new HashSet<>();

    // Ingredients
    public static final Item PURIFIED_SOUL_METAL = registerSimpleItem(
            "purified_soul_metal", new Item.Properties().fireResistant()
    );

    public static final Item PURIFIED_SOUL_SHARD = registerSimpleItem(
            "purified_soul_shard", new Item.Properties().fireResistant()
    );

    public static final Item NECTARIUM = registerSimpleItem(
            "nectarium", new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(20)
                            .saturationModifier(1f)
                            .alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 10), 1f)
                            .build()
                    )
    );

    public static final Item GOD_BLOOD = registerSimpleItem(
            "god_blood", new Item.Properties()
                    .stacksTo(1)
                    .fireResistant()
                    .rarity(Rarity.EPIC)
    );

    public static final Item COMPRESSED_IRON_INGOT = registerSimpleItem("compressed_iron_ingot");
    public static final Item FAKE_GOLD_INGOT = registerSimpleItem("fake_gold_ingot");

    public static final Item PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE = registerItem("purified_soul_upgrade_smithing_template", NarakaSmithingTemplateItems::createPurifiedSoulUpgradeTemplate);
    public static final Item PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE = registerItem("purified_gems_upgrade_smithing_template", NarakaSmithingTemplateItems::createPurifiedGemUpgradeTemplate);

    public static final Item SOUL_INFUSED_AMETHYST = registerSoulInfusedItem("amethyst");
    public static final Item SOUL_INFUSED_COPPER = registerSoulInfusedItem("copper");
    public static final Item SOUL_INFUSED_DIAMOND = registerSoulInfusedItem("diamond");
    public static final Item SOUL_INFUSED_EMERALD = registerSoulInfusedItem("emerald");
    public static final Item SOUL_INFUSED_GOLD = registerSoulInfusedItem("gold");
    public static final Item SOUL_INFUSED_LAPIS = registerSoulInfusedItem("lapis");
    public static final Item SOUL_INFUSED_NECTARIUM = registerSoulInfusedItem("nectarium");
    public static final Item SOUL_INFUSED_REDSTONE = registerSoulInfusedItem("redstone");

    // Spears
    public static final SpearItem SPEAR_ITEM = registerItem(
            "spear", properties -> new SpearItem(Tiers.IRON, properties.fireResistant(), NarakaEntityTypes.THROWN_SPEAR)
    );
    public static final SpearItem MIGHTY_HOLY_SPEAR_ITEM = registerItem(
            "mighty_holy_spear", properties -> new SpearItem(Tiers.NETHERITE, properties.fireResistant(), NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR)
    );
    public static final SpearOfLonginusItem SPEAR_OF_LONGINUS_ITEM = registerItem(
            "spear_of_longinus", properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );
    public static final SwordItem EBONY_SWORD = registerItem("ebony_sword", properties -> new SwordItem(
                    Tiers.IRON, properties.attributes(SwordItem.createAttributes(Tiers.IRON, 0, 0))
            )
    );

    public static final SwordItem SOUL_INFUSED_REDSTONE_SWORD = registerSoulInfusedSword("redstone", 2, -2);
    public static final SwordItem SOUL_INFUSED_COPPER_SWORD = registerSoulInfusedSword("copper", 0, 2);
    public static final SwordItem SOUL_INFUSED_GOLD_SWORD = registerSoulInfusedSword("gold", 1, 0);
    public static final SwordItem SOUL_INFUSED_EMERALD_SWORD = registerSoulInfusedSword("emerald", 1, 0);
    public static final SwordItem SOUL_INFUSED_DIAMOND_SWORD = registerSoulInfusedSword("diamond", 3, 1);
    public static final SwordItem SOUL_INFUSED_LAPIS_SWORD = registerSoulInfusedSword("lapis", 0, 0);
    public static final SwordItem SOUL_INFUSED_AMETHYST_SWORD = registerSoulInfusedSword("amethyst", 0, 0);
    public static final SwordItem SOUL_INFUSED_NECTARIUM_SWORD = registerSoulInfusedSword("nectarium", 0, 0);
    public static final SwordItem PURIFIED_SOUL_SWORD = registerItem(
            "purified_soul_sword",
            properties -> new PurifiedSoulSword(Tiers.IRON, properties.fireResistant()
                    .rarity(Rarity.UNCOMMON)
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 1, 0)))
    );


    public static void forEachSoulInfusedItem(Consumer<Item> consumer) {
        for (Item item : SOUL_INFUSED_ITEMS)
            consumer.accept(item);
    }

    public static void forEachSoulInfusedSword(Consumer<SwordItem> consumer) {
        for (SwordItem item : SOUL_INFUSED_SWORDS)
            consumer.accept(item);
    }

    private static Item registerSoulInfusedItem(String name) {
        Item item = registerSimpleItem(SOUL_INFUSED_PREFIX + name, new Item.Properties().fireResistant());
        SOUL_INFUSED_ITEMS.add(item);
        return item;
    }

    private static SwordItem registerSoulInfusedSword(String name, int attackDamage, float attackSpeed) {
        SwordItem item = registerItem(SOUL_INFUSED_PREFIX + name + "_sword",
                properties -> new SwordItem(
                        Tiers.IRON,
                        properties.fireResistant()
                                .rarity(Rarity.RARE)
                                .attributes(SwordItem.createAttributes(Tiers.IRON, attackDamage, attackSpeed))
                )
        );
        SOUL_INFUSED_SWORDS.add(item);
        return item;
    }

    private static <I extends Item> I registerItem(String name, Function<Item.Properties, I> factory, Item.Properties properties) {
        return Registry.register(BuiltInRegistries.ITEM, NarakaMod.location(name), factory.apply(properties));
    }

    private static <I extends Item> I registerItem(String name, Function<Item.Properties, I> factory) {
        return registerItem(name, factory, new Item.Properties());
    }

    private static Item registerSimpleItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new, properties);
    }

    private static Item registerSimpleItem(String name) {
        return registerSimpleItem(name, new Item.Properties());
    }

    public static void initialize() {

    }
}
