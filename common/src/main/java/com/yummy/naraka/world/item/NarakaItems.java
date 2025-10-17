package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.network.NarakaClientboundEntityEventPacket;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
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
import java.util.function.UnaryOperator;

public class NarakaItems {
    private static final String SOUL_INFUSED_PREFIX = "soul_infused_";

    private static final Set<HolderProxy<Item, Item>> SOUL_INFUSED_ITEMS = new LinkedHashSet<>();
    private static final Set<HolderProxy<Item, Item>> SOUL_INFUSED_SWORDS = new LinkedHashSet<>();
    private static final Map<SoulType, HolderProxy<Item, Item>> SWORD_BY_SOUL_TYPE = new HashMap<>();

    public static final HolderProxy<Item, Item> STIGMA_ROD = registerItem("stigma_rod", StigmaRodItem::new);
    public static final HolderProxy<Item, Item> STARDUST_STAFF = registerItem("stardust_staff", StardustStaffItem::new);
    public static final HolderProxy<Item, Item> NARAKA_FIREBALL_STAFF = registerItem("naraka_fireball_staff", NarakaFireballStaffItem::new);

    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_1_DISC = registerDiscItem("herobrine_phase_1_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_1);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_2_DISC = registerDiscItem("herobrine_phase_2_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_2);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_3_DISC = registerDiscItem("herobrine_phase_3_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_3);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_4_DISC = registerDiscItem("herobrine_phase_4_disc", NarakaJukeboxSongs.HEROBRINE_PHASE_4);

    public static final HolderProxy<Item, Item> HEROBRINE_PICKAXE = registerSimpleItem("herobrine_pickaxe");

    public static final HolderProxy<Item, Item> SKILL_CONTROLLER = registerItem(
            "skill_controller",
            properties -> new SkillUsingMobControllerItem(
                    properties,
                    NarakaClientboundEntityEventPacket.Event.SHOW_SKILL_CONTROL_SCREEN
            )
    );
    public static final HolderProxy<Item, Item> ANIMATION_CONTROLLER = registerItem(
            "animation_controller",
            properties -> new SkillUsingMobControllerItem(
                    properties,
                    NarakaClientboundEntityEventPacket.Event.SHOW_ANIMATION_CONTROL_SCREEN
            )
    );

    // Ingredients
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_METAL = registerSimpleItem(
            "purified_soul_metal", Item.Properties::fireResistant
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_SHARD = registerSimpleItem(
            "purified_soul_shard", Item.Properties::fireResistant
    );
    public static final HolderProxy<Item, Item> RAINBOW_SWORD = registerSimpleItem("rainbow_sword");

    public static final HolderProxy<Item, Item> NECTARIUM = registerSimpleItem(
            "nectarium", properties -> properties.food(
                    new FoodProperties.Builder()
                            .nutrition(20)
                            .saturationModifier(1f)
                            .alwaysEdible()
                            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 10), 1)
                            .build()
            )

    );

    public static final HolderProxy<Item, Item> GOD_BLOOD = registerSimpleItem(
            "god_blood",
            properties -> properties.stacksTo(1)
                    .fireResistant()
    );

    public static final HolderProxy<Item, SanctuaryCompassItem> SANCTUARY_COMPASS = registerItem("sanctuary_compass", SanctuaryCompassItem::new);
    public static final HolderProxy<Item, Item> COMPRESSED_IRON_INGOT = registerSimpleItem("compressed_iron_ingot");

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE = registerSimpleItem("purified_soul_upgrade_smithing_template");

    public static final HolderProxy<Item, Item> SOUL_INFUSED_REDSTONE = registerSoulInfusedItem(SoulType.REDSTONE);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_COPPER = registerSoulInfusedItem(SoulType.COPPER);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_GOLD = registerSoulInfusedItem(SoulType.GOLD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_EMERALD = registerSoulInfusedItem(SoulType.EMERALD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_DIAMOND = registerSoulInfusedItem(SoulType.DIAMOND);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_LAPIS = registerSoulInfusedItem(SoulType.LAPIS);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_AMETHYST = registerSoulInfusedItem(SoulType.AMETHYST);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_NECTARIUM = registerSoulInfusedItem(SoulType.NECTARIUM);

    // Spears
    public static final HolderProxy<Item, SpearItem> SPEAR_ITEM = registerItem(
            "spear",
            properties -> new SpearItem(Tiers.IRON,
                    true, 3, -3, 3,
                    properties.fireResistant()
                            .component(DataComponents.TOOL, TridentItem.createToolProperties()),
                    NarakaEntityTypes.THROWN_SPEAR
            )
    );
    public static final HolderProxy<Item, SpearItem> MIGHTY_HOLY_SPEAR_ITEM = registerItem(
            "mighty_holy_spear",
            properties -> new SpearItem(Tiers.NETHERITE,
                    true, 61, -3, 3,
                    properties.fireResistant()
                            .component(DataComponents.TOOL, TridentItem.createToolProperties()),
                    NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR
            )
    );
    public static final HolderProxy<Item, SpearOfLonginusItem> SPEAR_OF_LONGINUS_ITEM = registerItem(
            "spear_of_longinus",
            properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                    .component(DataComponents.TOOL, TridentItem.createToolProperties())
            )
    );

    public static final HolderProxy<Item, Item> SOUL_INFUSED_REDSTONE_SWORD = registerSoulInfusedSword(SoulType.REDSTONE);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_COPPER_SWORD = registerSoulInfusedSword(SoulType.COPPER);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_GOLD_SWORD = registerSoulInfusedSword(SoulType.GOLD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_EMERALD_SWORD = registerSoulInfusedSword(SoulType.EMERALD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_DIAMOND_SWORD = registerSoulInfusedSword(SoulType.DIAMOND);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_LAPIS_SWORD = registerSoulInfusedSword(SoulType.LAPIS);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_AMETHYST_SWORD = registerSoulInfusedSword(SoulType.AMETHYST);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_NECTARIUM_SWORD = registerSoulInfusedSword(SoulType.NECTARIUM);
    public static final HolderProxy<Item, PurifiedSoulSwordItem> PURIFIED_SOUL_SWORD = registerItem(
            "purified_soul_sword",
            properties -> new PurifiedSoulSwordItem(
                    Tiers.IRON,
                    -2, -2.4f,
                    properties.fireResistant()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );
    public static final HolderProxy<Item, Item> ULTIMATE_SWORD = registerItem(
            "ultimate_sword",
            properties -> new SwordItem(
                    NarakaTiers.LONGINUS,
                    properties.attributes(NarakaTiers.createWeaponAttributes(NarakaTiers.LONGINUS, 3, -2.4f).build())
            )
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_HELMET = registerPurifiedSoulArmorItem("purified_soul_helmet", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.HELMET);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_CHESTPLATE = registerPurifiedSoulArmorItem("purified_soul_chestplate", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.CHESTPLATE);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_LEGGINGS = registerPurifiedSoulArmorItem("purified_soul_leggings", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.LEGGINGS);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_BOOTS = registerPurifiedSoulArmorItem("purified_soul_boots", NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.BOOTS);

    public static final HolderProxy<Item, Item> HEROBRINE_SPAWN_EGG = registerItem("herobrine_spawn_egg", properties -> new SpawnEggItem(NarakaEntityTypes.HEROBRINE.get(), 0, 0xff0000, properties));

    public static HolderProxy<Item, Item> registerPurifiedSoulArmorItem(String name, Holder<ArmorMaterial> armorMaterial, ArmorItem.Type armorType) {
        return registerItem(name, properties ->
                new ArmorItem(
                        armorMaterial,
                        armorType,
                        properties.component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                )
        );
    }

    public static void forEachSoulInfusedItem(Consumer<Item> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_ITEMS)
            consumer.accept(item.get());
    }

    public static void forEachSoulInfusedItemHolder(Consumer<Holder<Item>> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_ITEMS)
            consumer.accept(item);
    }

    public static void forEachSoulInfusedSword(Consumer<Item> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_SWORDS)
            consumer.accept(item.get());
    }

    public static void forEachSoulInfusedSwordHolder(Consumer<Holder<Item>> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_SWORDS)
            consumer.accept(item);
    }

    @Nullable
    public static Item getSoulSwordOf(SoulType type) {
        if (SWORD_BY_SOUL_TYPE.containsKey(type))
            return SWORD_BY_SOUL_TYPE.get(type).get();
        return null;
    }

    private static HolderProxy<Item, Item> registerSoulInfusedItem(SoulType type) {
        HolderProxy<Item, Item> item = registerItem(
                SOUL_INFUSED_PREFIX + type.getSerializedName(),
                properties -> new Item(properties
                        .component(NarakaDataComponentTypes.SOUL.get(), type)
                        .fireResistant()
                )
        );
        SOUL_INFUSED_ITEMS.add(item);
        return item;
    }

    private static HolderProxy<Item, Item> registerSoulInfusedSword(SoulType type) {
        HolderProxy<Item, Item> item = registerItem(SOUL_INFUSED_PREFIX + type.getSerializedName() + "_sword",
                properties -> new SoulInfusedSwordItem(
                        Tiers.IRON,
                        properties.fireResistant()
                                .rarity(Rarity.RARE)
                                .component(NarakaDataComponentTypes.SOUL.get(), type)
                                .component(DataComponents.UNBREAKABLE, new Unbreakable(true)),
                        type.color
                )
        );
        SOUL_INFUSED_SWORDS.add(item);
        SWORD_BY_SOUL_TYPE.put(type, item);
        return item;
    }

    private static HolderProxy<Item, Item> registerDiscItem(String name, ResourceKey<JukeboxSong> song) {
        return registerSimpleItem(name, properties -> properties.stacksTo(1)
                .rarity(Rarity.RARE)
                .jukeboxPlayable(song)
        );
    }

    public static ResourceKey<Item> key(String name) {
        return ResourceKey.create(Registries.ITEM, NarakaMod.location(name));
    }

    private static Item.Properties properties() {
        return new Item.Properties();
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(String name, Function<Item.Properties, I> factory, Item.Properties properties) {
        return RegistryProxy.register(Registries.ITEM, name, () -> factory.apply(properties));
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(String name, Function<Item.Properties, I> factory) {
        return registerItem(name, factory, properties());
    }

    private static HolderProxy<Item, Item> registerSimpleItem(String name, UnaryOperator<Item.Properties> operator) {
        return registerItem(name, properties -> new Item(operator.apply(properties)));
    }

    private static HolderProxy<Item, Item> registerSimpleItem(String name) {
        return registerItem(name, Item::new);
    }

    public static void initialize() {

    }
}
