package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaItems {
    private static final String SOUL_INFUSED_PREFIX = "soul_infused_";

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NarakaMod.MOD_ID);

    // Ingredients
    public static final DeferredItem<Item> PURIFIED_SOUL_METAL = ITEMS.registerSimpleItem(
            "purified_soul_metal", new Item.Properties().fireResistant()
    );

    public static final DeferredItem<Item> PURIFIED_SOUL_SHARD = ITEMS.registerSimpleItem(
            "purified_soul_shard", new Item.Properties().fireResistant()
    );

    public static final DeferredItem<Item> NECTARIUM = ITEMS.registerSimpleItem(
            "nectarium", new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(20)
                            .saturationModifier(1f)
                            .alwaysEdible()
                            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1, 10), 1f)
                            .build()
                    )
    );

    public static final DeferredItem<Item> GOD_BLOOD = ITEMS.registerSimpleItem(
            "god_blood", new Item.Properties()
                    .stacksTo(1)
                    .fireResistant()
                    .rarity(Rarity.EPIC)
    );

    public static final DeferredItem<Item> COMPRESSED_IRON_INGOT = ITEMS.registerSimpleItem("compressed_iron_ingot");
    public static final DeferredItem<Item> FAKE_GOLD_INGOT = ITEMS.registerSimpleItem("fake_gold_ingot");

    public static final DeferredItem<Item> PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE = ITEMS.registerItem("purified_soul_upgrade_smithing_template", NarakaSmithingTemplateItems::createPurifiedSoulUpgradeTemplate);
    public static final DeferredItem<Item> PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE = ITEMS.registerItem("purified_gems_upgrade_smithing_template", NarakaSmithingTemplateItems::createPurifiedGemUpgradeTemplate);

    public static final DeferredItem<Item> SOUL_INFUSED_AMETHYST = registerSoulInfusedItem("amethyst");
    public static final DeferredItem<Item> SOUL_INFUSED_COPPER = registerSoulInfusedItem("copper");
    public static final DeferredItem<Item> SOUL_INFUSED_DIAMOND = registerSoulInfusedItem("diamond");
    public static final DeferredItem<Item> SOUL_INFUSED_EMERALD = registerSoulInfusedItem("emerald");
    public static final DeferredItem<Item> SOUL_INFUSED_GOLD = registerSoulInfusedItem("gold");
    public static final DeferredItem<Item> SOUL_INFUSED_LAPIS = registerSoulInfusedItem("lapis");
    public static final DeferredItem<Item> SOUL_INFUSED_NECTARIUM = registerSoulInfusedItem("nectarium");
    public static final DeferredItem<Item> SOUL_INFUSED_REDSTONE = registerSoulInfusedItem("redstone");

    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item", TestItem::new);
    // Spears
    public static final DeferredItem<SpearItem> SPEAR_ITEM = ITEMS.registerItem(
            "spear", properties -> new SpearItem(Tiers.IRON, properties.fireResistant(), NarakaEntityTypes.THROWN_SPEAR)
    );
    public static final DeferredItem<SpearItem> MIGHTY_HOLY_SPEAR_ITEM = ITEMS.registerItem(
            "mighty_holy_spear", properties -> new SpearItem(Tiers.NETHERITE, properties.fireResistant(), NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR)
    );
    public static final DeferredItem<SpearOfLonginusItem> SPEAR_OF_LONGINUS_ITEM = ITEMS.registerItem(
            "spear_of_longinus", properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    public static final DeferredItem<SignItem> EBONY_SIGN = ITEMS.registerItem(
            "ebony_sign", properties -> new SignItem(properties, NarakaBlocks.EBONY_SIGN.get(), NarakaBlocks.EBONY_WALL_SIGN.get())
    );
    public static final DeferredItem<HangingSignItem> EBONY_HANGING_SIGN = ITEMS.registerItem(
            "ebony_hanging_sign", properties -> new HangingSignItem(NarakaBlocks.EBONY_HANGING_SIGN.get(), NarakaBlocks.EBONY_WALL_HANGING_SIGN.get(), properties)
    );
    public static final DeferredItem<SwordItem> EBONY_SWORD = ITEMS.registerItem("ebony_sword", properties -> new SwordItem(
                    Tiers.IRON, properties.attributes(SwordItem.createAttributes(Tiers.IRON, 0, 0))
            )
    );

    public static final DeferredItem<SwordItem> SOUL_INFUSED_REDSTONE_SWORD = registerSoulInfusedSword("redstone", 2, -2);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_COPPER_SWORD = registerSoulInfusedSword("copper", 0, 2);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_GOLD_SWORD = registerSoulInfusedSword("gold", 1, 0);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_EMERALD_SWORD = registerSoulInfusedSword("emerald", 1, 0);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_DIAMOND_SWORD = registerSoulInfusedSword("diamond", 3, 1);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_LAPIS_SWORD = registerSoulInfusedSword("lapis", 0, 0);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_AMETHYST_SWORD = registerSoulInfusedSword("amethyst", 0, 0);
    public static final DeferredItem<SwordItem> SOUL_INFUSED_NECTARIUM_SWORD = registerSoulInfusedSword("nectarium", 0, 0);
    public static final DeferredItem<SwordItem> PURIFIED_SOUL_SWORD = ITEMS.registerItem(
            "purified_soul_sword",
            properties -> new SwordItem(Tiers.IRON, properties.fireResistant()
                    .rarity(Rarity.UNCOMMON)
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 1, 0)))
    );

    private static DeferredItem<Item> registerSoulInfusedItem(String name) {
        return ITEMS.registerSimpleItem(SOUL_INFUSED_PREFIX + name, new Item.Properties().fireResistant());
    }

    private static DeferredItem<SwordItem> registerSoulInfusedSword(String name, float attackDamage, float attackSpeed) {
        return ITEMS.registerItem(SOUL_INFUSED_PREFIX + name + "_sword",
                properties -> new SwordItem(
                        Tiers.IRON,
                        properties.fireResistant()
                                .rarity(Rarity.RARE)
                                .attributes(SwordItem.createAttributes(Tiers.IRON, attackDamage, attackSpeed))
                )
        );
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
