package com.yummy.naraka.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.entity.NarakaEntities;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaItems {
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

    public static final DeferredItem<Item> PURIFIED_SOUL_UPGRADING_SMITHING_TEMPLATE = ITEMS.registerSimpleItem("purified_soul_upgrading_smithing_template");

    // Spears
    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item", TestItem::new);
    public static final DeferredItem<SpearItem> SPEAR_ITEM = ITEMS.registerItem(
            "spear", properties -> new SpearItem(Tiers.IRON, properties, NarakaEntities.THROWN_SPEAR)
    );
    public static final DeferredItem<SpearItem> MIGHTY_HOLY_SPEAR_ITEM = ITEMS.registerItem(
            "mighty_holy_spear", properties -> new SpearItem(Tiers.NETHERITE, properties, NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR)
    );
    public static final DeferredItem<SpearOfLonginusItem> SPEAR_OF_LONGINUS_ITEM = ITEMS.registerItem(
            "spear_of_longinus", properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(false))
            )
    );


    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
