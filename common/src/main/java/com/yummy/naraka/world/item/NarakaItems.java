package com.yummy.naraka.world.item;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.armortrim.NarakaTrimPatterns;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class NarakaItems {
    private static final String SOUL_INFUSED_PREFIX = "soul_infused_";

    private static final Set<LazyHolder<Item, Item>> SOUL_INFUSED_ITEMS = new LinkedHashSet<>();
    private static final Set<LazyHolder<Item, SwordItem>> SOUL_INFUSED_SWORDS = new LinkedHashSet<>();
    private static final Map<SoulType, LazyHolder<Item, SwordItem>> SWORD_BY_SOUL_TYPE = new HashMap<>();

    public static final LazyHolder<Item, Item> STIGMA_ROD = registerItem("stigma_rod", StigmaRodItem::new);

    public static final LazyHolder<Item, Item> HEROBRINE_PHASE_1_DISC = registerDiscItem("herobrine_phase_1_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_1);
    public static final LazyHolder<Item, Item> HEROBRINE_PHASE_2_DISC = registerDiscItem("herobrine_phase_2_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_2);
    public static final LazyHolder<Item, Item> HEROBRINE_PHASE_3_DISC = registerDiscItem("herobrine_phase_3_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_3);
    public static final LazyHolder<Item, Item> HEROBRINE_PHASE_4_DISC = registerDiscItem("herobrine_phase_4_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_4);

    // Ingredients
    public static final LazyHolder<Item, Item> PURIFIED_SOUL_METAL = registerSimpleItem(
            "purified_soul_metal", new Item.Properties().fireResistant()
    );

    public static final LazyHolder<Item, Item> PURIFIED_SOUL_SHARD = registerSimpleItem(
            "purified_soul_shard", new Item.Properties().fireResistant()
    );

    public static final LazyHolder<Item, Item> NECTARIUM = registerSimpleItem(
            "nectarium", new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(20)
                            .saturationModifier(1f)
                            .alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 10), 1f)
                            .build()
                    )
    );

    public static final LazyHolder<Item, Item> EBONY_ROOTS_SCRAP = registerSimpleItem("ebony_roots_scrap");
    public static final LazyHolder<Item, Item> EBONY_METAL_INGOT = registerSimpleItem("ebony_metal_ingot", new Item.Properties().fireResistant());

    public static final LazyHolder<Item, Item> GOD_BLOOD = registerSimpleItem(
            "god_blood", new Item.Properties()
                    .stacksTo(1)
                    .fireResistant()
    );

    public static final LazyHolder<Item, SanctuaryCompassItem> SANCTUARY_COMPASS = registerItem("sanctuary_compass", SanctuaryCompassItem::new);
    public static final LazyHolder<Item, Item> COMPRESSED_IRON_INGOT = registerSimpleItem("compressed_iron_ingot");

    public static final LazyHolder<Item, Item> PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE = registerSimpleItem("purified_soul_upgrade_smithing_template");
    public static final LazyHolder<Item, SmithingTemplateItem> PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE = registerItem(
            "purified_soul_silence_armor_trim_smithing_template",
            properties -> SmithingTemplateItem.createArmorTrimTemplate(NarakaTrimPatterns.PURIFIED_SOUL_SILENCE)
    );

    public static final LazyHolder<Item, Item> SOUL_INFUSED_REDSTONE = registerSoulInfusedItem(SoulType.REDSTONE);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_COPPER = registerSoulInfusedItem(SoulType.COPPER);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_GOLD = registerSoulInfusedItem(SoulType.GOLD);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_EMERALD = registerSoulInfusedItem(SoulType.EMERALD);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_DIAMOND = registerSoulInfusedItem(SoulType.DIAMOND);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_LAPIS = registerSoulInfusedItem(SoulType.LAPIS);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_AMETHYST = registerSoulInfusedItem(SoulType.AMETHYST);
    public static final LazyHolder<Item, Item> SOUL_INFUSED_NECTARIUM = registerSoulInfusedItem(SoulType.NECTARIUM);

    // Spears
    public static final LazyHolder<Item, SpearItem> SPEAR_ITEM = registerItem(
            "spear",
            properties -> new SpearItem(Tiers.IRON,
                    properties
                            .fireResistant()
                            .attributes(SpearItem.createAttributes(8, -3)),
                    NarakaEntityTypes.THROWN_SPEAR
            )
    );
    public static final LazyHolder<Item, SpearItem> MIGHTY_HOLY_SPEAR_ITEM = registerItem(
            "mighty_holy_spear",
            properties -> new SpearItem(Tiers.NETHERITE,
                    properties
                            .fireResistant()
                            .attributes(SpearItem.createAttributes(65, -3)),
                    NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR
            )
    );
    public static final LazyHolder<Item, SpearOfLonginusItem> SPEAR_OF_LONGINUS_ITEM = registerItem(
            "spear_of_longinus",
            properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );
    public static final LazyHolder<Item, SwordItem> EBONY_SWORD = registerItem("ebony_sword", properties -> new SwordItem(
                    Tiers.IRON, properties.attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4f))
            )
    );

    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_REDSTONE_SWORD = registerSoulInfusedSword(SoulType.REDSTONE);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_COPPER_SWORD = registerSoulInfusedSword(SoulType.COPPER);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_GOLD_SWORD = registerSoulInfusedSword(SoulType.GOLD);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_EMERALD_SWORD = registerSoulInfusedSword(SoulType.EMERALD);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_DIAMOND_SWORD = registerSoulInfusedSword(SoulType.DIAMOND);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_LAPIS_SWORD = registerSoulInfusedSword(SoulType.LAPIS);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_AMETHYST_SWORD = registerSoulInfusedSword(SoulType.AMETHYST);
    public static final LazyHolder<Item, SwordItem> SOUL_INFUSED_NECTARIUM_SWORD = registerSoulInfusedSword(SoulType.NECTARIUM);
    public static final LazyHolder<Item, SwordItem> PURIFIED_SOUL_SWORD = registerItem(
            "purified_soul_sword",
            properties -> new PurifiedSoulSword(Tiers.IRON, properties.fireResistant()
                    .attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4f)))
    );

    public static final LazyHolder<Item, ArmorItem> PURIFIED_SOUL_HELMET = registerArmorItem("purified_soul_helmet", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.HELMET, 0);
    public static final LazyHolder<Item, ArmorItem> PURIFIED_SOUL_CHESTPLATE = registerArmorItem("purified_soul_chestplate", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.CHESTPLATE, 0);
    public static final LazyHolder<Item, ArmorItem> PURIFIED_SOUL_LEGGINGS = registerArmorItem("purified_soul_leggings", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.LEGGINGS, 0);
    public static final LazyHolder<Item, ArmorItem> PURIFIED_SOUL_BOOTS = registerArmorItem("purified_soul_boots", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.BOOTS, 0);

    public static final LazyHolder<Item, ArmorItem> EBONY_METAL_HELMET = registerArmorItem("ebony_metal_helmet", NarakaArmorMaterials.EBONY_METAL, ArmorItem.Type.HELMET, 7);
    public static final LazyHolder<Item, ArmorItem> EBONY_METAL_CHESTPLATE = registerArmorItem("ebony_metal_chestplate", NarakaArmorMaterials.EBONY_METAL, ArmorItem.Type.CHESTPLATE, 7);
    public static final LazyHolder<Item, ArmorItem> EBONY_METAL_LEGGINGS = registerArmorItem("ebony_metal_leggings", NarakaArmorMaterials.EBONY_METAL, ArmorItem.Type.LEGGINGS, 7);
    public static final LazyHolder<Item, ArmorItem> EBONY_METAL_BOOTS = registerArmorItem("ebony_metal_boots", NarakaArmorMaterials.EBONY_METAL, ArmorItem.Type.BOOTS, 7);

    public static LazyHolder<Item, ArmorItem> registerArmorItem(String name, Holder<ArmorMaterial> armorMaterial, ArmorItem.Type armorType, int durability) {
        return registerItem(name, properties -> new ArmorItem(armorMaterial, armorType, properties.durability(durability)
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true))));
    }

    public static void forEachSoulInfusedItem(Consumer<Item> consumer) {
        for (LazyHolder<Item, Item> item : SOUL_INFUSED_ITEMS)
            consumer.accept(item.get());
    }

    public static void forEachSoulInfusedItemHolder(Consumer<Holder<Item>> consumer) {
        for (LazyHolder<Item, Item> item : SOUL_INFUSED_ITEMS)
            consumer.accept(item);
    }

    public static void forEachSoulInfusedSword(Consumer<SwordItem> consumer) {
        for (LazyHolder<Item, SwordItem> item : SOUL_INFUSED_SWORDS)
            consumer.accept(item.get());
    }

    public static void forEachSoulInfusedSwordHolder(Consumer<Holder<Item>> consumer) {
        for (LazyHolder<Item, SwordItem> item : SOUL_INFUSED_SWORDS)
            consumer.accept(item);
    }

    @Nullable
    public static Item getSoulSwordOf(SoulType type) {
        if (SWORD_BY_SOUL_TYPE.containsKey(type))
            return SWORD_BY_SOUL_TYPE.get(type).get();
        return null;
    }

    private static LazyHolder<Item, Item> registerSoulInfusedItem(SoulType type) {
        LazyHolder<Item, Item> item = registerItem(
                SOUL_INFUSED_PREFIX + type.getSerializedName(),
                properties -> new Item(properties
                        .component(NarakaDataComponentTypes.SOUL.get(), type)
                        .fireResistant()
                )
        );
        SOUL_INFUSED_ITEMS.add(item);
        return item;
    }

    private static LazyHolder<Item, SwordItem> registerSoulInfusedSword(SoulType type) {
        LazyHolder<Item, SwordItem> item = registerItem(SOUL_INFUSED_PREFIX + type.getSerializedName() + "_sword",
                properties -> new SoulInfusedSwordItem(
                        Tiers.IRON,
                        properties.fireResistant()
                                .rarity(Rarity.RARE)
                                .component(NarakaDataComponentTypes.SOUL.get(), type)
                                .attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4f)),
                        type.color
                )
        );
        SOUL_INFUSED_SWORDS.add(item);
        SWORD_BY_SOUL_TYPE.put(type, item);
        return item;
    }

    private static LazyHolder<Item, Item> registerDiscItem(String name, ResourceKey<JukeboxSong> song) {
        return registerSimpleItem(name, new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE)
                .jukeboxPlayable(song)
        );
    }

    private static <I extends Item> LazyHolder<Item, I> registerItem(String name, Function<Item.Properties, I> factory, Item.Properties properties) {
        return RegistryProxy.register(Registries.ITEM, name, () -> factory.apply(properties));
    }

    private static <I extends Item> LazyHolder<Item, I> registerItemDirect(String name, I item) {
        return RegistryProxy.register(Registries.ITEM, name, () -> item);
    }

    private static <I extends Item> LazyHolder<Item, I> registerItem(String name, Function<Item.Properties, I> factory) {
        return registerItem(name, factory, new Item.Properties());
    }

    private static LazyHolder<Item, Item> registerSimpleItem(String name, Item.Properties properties) {
        return registerItem(name, Item::new, properties);
    }

    private static LazyHolder<Item, Item> registerSimpleItem(String name) {
        return registerSimpleItem(name, new Item.Properties());
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.ITEM)
                .onRegistrationFinished();
    }
}
