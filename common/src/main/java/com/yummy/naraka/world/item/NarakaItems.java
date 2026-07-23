package com.yummy.naraka.world.item;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.network.NarakaClientboundEntityEventPacket;
import com.yummy.naraka.references.NarakaItemIds;
import com.yummy.naraka.sounds.NarakaSoundEvents;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.tooltip.NarakaItemTooltip;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class NarakaItems {
    private static final Set<HolderProxy<Item, Item>> SOUL_INFUSED_ITEMS = new LinkedHashSet<>();
    private static final Set<HolderProxy<Item, Item>> SOUL_INFUSED_SWORDS = new LinkedHashSet<>();
    private static final Map<SoulType, HolderProxy<Item, Item>> SWORD_BY_SOUL_TYPE = new HashMap<>();

    public static final HolderProxy<Item, Item> LOCKED_HEALTH = registerItem(NarakaItemIds.LOCKED_HEALTH, Item::new);

    public static final HolderProxy<Item, Item> STIGMA_ROD = registerItem(NarakaItemIds.STIGMA_ROD, StigmaRodItem::new, properties().rarity(Rarity.EPIC));
    public static final HolderProxy<Item, Item> NARAKA_FIREBALL_STAFF = registerItem(NarakaItemIds.NARAKA_FIREBALL_STAFF, NarakaFireballStaffItem::new, properties().rarity(Rarity.EPIC));

    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_1_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_1_DISC, NarakaSoundEvents.HEROBRINE_PHASE_1, 115);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_2_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_2_DISC, NarakaSoundEvents.HEROBRINE_PHASE_2, 159);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_3_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_3_DISC, NarakaSoundEvents.HEROBRINE_PHASE_3, 200);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_4_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_4_DISC, NarakaSoundEvents.HEROBRINE_PHASE_4, 148);

    public static final HolderProxy<Item, Item> NARAKA_PICKAXE = registerItem(
            NarakaItemIds.NARAKA_PICKAXE,
            properties -> new NarakaPickaxeItem(
                    properties.rarity(Rarity.EPIC)
                            .fireResistant()
                            .durability(-1)
            ),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.NARAKA_PICKAXE)
    );

    public static final HolderProxy<Item, Item> NETHERITE_HAMMER = registerItem(
            NarakaItemIds.NETHERITE_HAMMER,
            properties -> new HammerItem(15, -3.6f, Tiers.NETHERITE, BlockTags.CRYSTAL_SOUND_BLOCKS, properties),
            properties().rarity(Rarity.EPIC)
                    .fireResistant()
    );

    public static final HolderProxy<Item, Item> SKILL_CONTROLLER = registerItem(
            NarakaItemIds.SKILL_CONTROLLER,
            properties -> new SkillUsingMobControllerItem(
                    properties,
                    NarakaClientboundEntityEventPacket.Event.SHOW_SKILL_CONTROL_SCREEN
            )
    );
    public static final HolderProxy<Item, Item> ANIMATION_CONTROLLER = registerItem(
            NarakaItemIds.ANIMATION_CONTROLLER,
            properties -> new SkillUsingMobControllerItem(
                    properties,
                    NarakaClientboundEntityEventPacket.Event.SHOW_ANIMATION_CONTROL_SCREEN
            )
    );

    // Ingredients
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_METAL = registerSimpleItem(
            NarakaItemIds.PURIFIED_SOUL_METAL,
            Item.Properties::fireResistant,
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.PURIFIED_SOUL_METAL)
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_SHARD = registerSimpleItem(
            NarakaItemIds.PURIFIED_SOUL_SHARD, Item.Properties::fireResistant
    );
    public static final HolderProxy<Item, Item> RAINBOW_SWORD = registerSimpleItem(NarakaItemIds.RAINBOW_SWORD);

    public static final HolderProxy<Item, Item> NECTARIUM = registerSimpleItem(
            NarakaItemIds.NECTARIUM,
            properties -> properties.food(
                    new FoodProperties.Builder()
                            .nutrition(20)
                            .saturationMod(1f)
                            .alwaysEat()
                            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 10), 1)
                            .build()
            ),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.NECTARIUM)
    );

    public static final HolderProxy<Item, Item> GOD_BLOOD = registerSimpleItem(
            NarakaItemIds.GOD_BLOOD,
            properties -> properties.stacksTo(1)
                    .rarity(Rarity.EPIC)
                    .fireResistant(),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.GOD_BLOOD)
    );

    public static final HolderProxy<Item, SanctuaryCompassItem> SANCTUARY_COMPASS = registerItem(
            NarakaItemIds.SANCTUARY_COMPASS,
            properties -> new SanctuaryCompassItem(properties.rarity(Rarity.RARE)),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.SANCTUARY_COMPASS)
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE = registerSimpleItem(NarakaItemIds.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE);

    public static final HolderProxy<Item, Item> SOUL_INFUSED_REDSTONE = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_REDSTONE, SoulType.REDSTONE);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_COPPER = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_COPPER, SoulType.COPPER);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_GOLD = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_GOLD, SoulType.GOLD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_EMERALD = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_EMERALD, SoulType.EMERALD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_DIAMOND = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_DIAMOND, SoulType.DIAMOND);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_LAPIS = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_LAPIS, SoulType.LAPIS);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_AMETHYST = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_AMETHYST, SoulType.AMETHYST);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_NECTARIUM = registerSoulInfusedItem(NarakaItemIds.SOUL_INFUSED_NECTARIUM, SoulType.NECTARIUM);

    public static final HolderProxy<Item, Item> HEROBRINE_SPAWN_EGG = registerItem(
            NarakaItemIds.HEROBRINE_SPAWN_EGG,
            properties -> SpawnEggItemProvider.create(NarakaEntityTypes.HEROBRINE, 0, 0xff0000, properties)
    );
    public static final HolderProxy<Item, Item> DIAMOND_GOLEM_SPAWN_EGG = registerItem(
            NarakaItemIds.DIAMOND_GOLEM_SPAWN_EGG,
            properties -> SpawnEggItemProvider.create(NarakaEntityTypes.DIAMOND_GOLEM, 0, SoulType.DIAMOND.getColor(), properties)
    );

    // Spears
    public static final HolderProxy<Item, SpearItem> SPEAR_ITEM = registerItem(
            NarakaItemIds.SPEAR_ITEM,
            properties -> new SpearItem(NarakaTiers.PURIFIED_SOUL,
                    true, 3, -3,
                    properties.fireResistant(),
                    NarakaEntityTypes.THROWN_SPEAR
            )
    );
    public static final HolderProxy<Item, SpearItem> MIGHTY_HOLY_SPEAR_ITEM = registerItem(
            NarakaItemIds.MIGHTY_HOLY_SPEAR_ITEM,
            properties -> new SpearItem(Tiers.NETHERITE,
                    true, 7, -3,
                    properties.fireResistant()
                            .rarity(Rarity.RARE),
                    NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR
            )
    );
    public static final HolderProxy<Item, SpearOfLonginusItem> SPEAR_OF_LONGINUS_ITEM = registerItem(
            NarakaItemIds.SPEAR_OF_LONGINUS_ITEM,
            properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .rarity(Rarity.EPIC)
                    .durability(-1)
            ),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.SPEAR_OF_LONGINUS)
    );

    public static final HolderProxy<Item, Item> SOUL_INFUSED_REDSTONE_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_REDSTONE_SWORD, SoulType.REDSTONE);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_COPPER_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_COPPER_SWORD, SoulType.COPPER);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_GOLD_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_GOLD_SWORD, SoulType.GOLD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_EMERALD_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_EMERALD_SWORD, SoulType.EMERALD);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_DIAMOND_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_DIAMOND_SWORD, SoulType.DIAMOND);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_LAPIS_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_LAPIS_SWORD, SoulType.LAPIS);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_AMETHYST_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_AMETHYST_SWORD, SoulType.AMETHYST);
    public static final HolderProxy<Item, Item> SOUL_INFUSED_NECTARIUM_SWORD = registerSoulInfusedSword(NarakaItemIds.SOUL_INFUSED_NECTARIUM_SWORD, SoulType.NECTARIUM);
    public static final HolderProxy<Item, PurifiedSoulSwordItem> PURIFIED_SOUL_SWORD = registerItem(
            NarakaItemIds.PURIFIED_SOUL_SWORD,
            properties -> new PurifiedSoulSwordItem(
                    NarakaTiers.PURIFIED_SOUL,
                    -2, -2.4f,
                    properties.fireResistant()
            ),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.PURIFIED_SOUL_SWORD)
    );

    public static final HolderProxy<Item, Item> HEROBRINE_SCARF = registerItem(
            NarakaItemIds.HEROBRINE_SCARF,
            properties -> new HerobrineScarfItem(properties.rarity(Rarity.EPIC)),
            builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.HEROBRINE_SCARF)
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_HELMET = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_HELMET, NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.HELMET);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_CHESTPLATE = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_CHESTPLATE, NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.CHESTPLATE);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_LEGGINGS = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_LEGGINGS, NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.LEGGINGS);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_BOOTS = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_BOOTS, NarakaArmorMaterials.PURIFIED_SOUL, ArmorItem.Type.BOOTS);

    public static HolderProxy<Item, Item> registerPurifiedSoulArmorItem(ResourceKey<Item> key, ArmorMaterial armorMaterial, ArmorItem.Type armorType) {
        return registerItem(key, properties ->
                        new PurifiedSoulArmorItem(
                                armorMaterial,
                                armorType,
                                properties.durability(-1)
                        ),
                builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS)
        );
    }

    public static void forEachSoulInfusedItem(Consumer<Item> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_ITEMS)
            consumer.accept(item.getConcreteValue());
    }

    public static void forEachSoulInfusedItemHolder(Consumer<Holder<Item>> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_ITEMS)
            consumer.accept(item);
    }

    public static void forEachSoulInfusedSword(Consumer<Item> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_SWORDS)
            consumer.accept(item.getConcreteValue());
    }

    public static void forEachSoulInfusedSwordHolder(Consumer<Holder<Item>> consumer) {
        for (HolderProxy<Item, Item> item : SOUL_INFUSED_SWORDS)
            consumer.accept(item);
    }

    @Nullable
    public static Item getSoulSwordOf(SoulType type) {
        if (SWORD_BY_SOUL_TYPE.containsKey(type))
            return SWORD_BY_SOUL_TYPE.get(type).getConcreteValue();
        return null;
    }

    @Nullable
    public static Holder<Item> getSoulSwordHolderOf(SoulType type) {
        if (SWORD_BY_SOUL_TYPE.containsKey(type))
            return SWORD_BY_SOUL_TYPE.get(type);
        return null;
    }

    private static HolderProxy<Item, Item> registerSoulInfusedItem(ResourceKey<Item> key, SoulType type) {
        HolderProxy<Item, Item> item = registerItem(
                key,
                properties -> new SoulInfusedItem(
                        properties
                                .rarity(Rarity.UNCOMMON)
                                .fireResistant(),
                        type
                ),
                builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.SOUL_INFUSED_MATERIALS)
        );
        SOUL_INFUSED_ITEMS.add(item);
        return item;
    }

    private static HolderProxy<Item, Item> registerSoulInfusedSword(ResourceKey<Item> key, SoulType type) {
        HolderProxy<Item, Item> item = registerItem(
                key,
                properties -> new SoulInfusedSwordItem(
                        NarakaTiers.PURIFIED_SOUL,
                        properties.fireResistant()
                                .durability(-1)
                                .rarity(Rarity.RARE),
                        type
                ),
                builder -> builder.naraka$setItemTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS)
        );
        SOUL_INFUSED_SWORDS.add(item);
        SWORD_BY_SOUL_TYPE.put(type, item);
        return item;
    }

    private static HolderProxy<Item, Item> registerDiscItem(ResourceKey<Item> key, Holder<SoundEvent> song, int lengthInSeconds) {
        return registerItem(key, properties -> new RecordItem(12, song.value(), properties.stacksTo(1)
                .rarity(Rarity.RARE), lengthInSeconds)
        );
    }

    private static Item.Properties properties() {
        return new Item.Properties();
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(ResourceKey<Item> key, Function<Item.Properties, I> factory, Item.Properties properties) {
        return RegistryWriter.register(key, () -> factory.apply(properties));
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(ResourceKey<Item> key, Function<Item.Properties, I> factory) {
        return registerItem(key, factory, properties());
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(ResourceKey<Item> key, Function<Item.Properties, I> factory, Consumer<ItemDetailBuilder> builderConsumer) {
        Item.Properties properties = properties();
        if (properties instanceof ItemDetailBuilder builder)
            builderConsumer.accept(builder);
        return registerItem(key, factory, properties);
    }

    private static HolderProxy<Item, Item> registerSimpleItem(ResourceKey<Item> key, UnaryOperator<Item.Properties> propertyOperator) {
        return registerItem(key, properties -> new Item(propertyOperator.apply(properties)));
    }

    private static HolderProxy<Item, Item> registerSimpleItem(ResourceKey<Item> key, UnaryOperator<Item.Properties> propertyOperator, Consumer<ItemDetailBuilder> builderConsumer) {
        return registerItem(key, properties -> {
            if (properties instanceof ItemDetailBuilder builder)
                builderConsumer.accept(builder);
            return new Item(propertyOperator.apply(properties));
        });
    }

    private static HolderProxy<Item, Item> registerSimpleItem(ResourceKey<Item> key) {
        return registerItem(key, Item::new);
    }

    public static void initialize() {

    }
}
