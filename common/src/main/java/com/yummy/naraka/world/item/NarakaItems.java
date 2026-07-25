package com.yummy.naraka.world.item;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.network.NarakaClientboundEntityEventPacket;
import com.yummy.naraka.references.NarakaItemIds;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetHelper;
import com.yummy.naraka.world.item.tooltip.NarakaItemTooltip;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class NarakaItems {
    public static final Set<HolderProxy<Item, Item>> SOUL_INFUSED_ITEMS = new LinkedHashSet<>();
    public static final Set<HolderProxy<Item, Item>> SOUL_INFUSED_SWORDS = new LinkedHashSet<>();
    private static final Map<SoulType, HolderProxy<Item, Item>> SWORD_BY_SOUL_TYPE = new HashMap<>();

    public static final HolderProxy<Item, Item> LOCKED_HEALTH = registerItem(NarakaItemIds.LOCKED_HEALTH, Item::new);

    public static final HolderProxy<Item, Item> STIGMA_ROD = registerItem(NarakaItemIds.STIGMA_ROD, StigmaRodItem::new, properties().rarity(Rarity.EPIC));
    public static final HolderProxy<Item, Item> STARDUST_STAFF = registerItem(NarakaItemIds.STARDUST_STAFF, StardustStaffItem::new, properties().rarity(Rarity.EPIC));
    public static final HolderProxy<Item, Item> CORRUPTED_STAR_STAFF = registerItem(NarakaItemIds.CORRUPTED_STAR_STAFF, CorruptedStarStaffItem::new, properties().rarity(Rarity.EPIC));
    public static final HolderProxy<Item, Item> NARAKA_FIREBALL_STAFF = registerItem(NarakaItemIds.NARAKA_FIREBALL_STAFF, NarakaFireballStaffItem::new, properties().rarity(Rarity.EPIC));

    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_1_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_1_DISC, NarakaJukeboxSongs.HEROBRINE_PHASE_1);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_2_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_2_DISC, NarakaJukeboxSongs.HEROBRINE_PHASE_2);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_3_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_3_DISC, NarakaJukeboxSongs.HEROBRINE_PHASE_3);
    public static final HolderProxy<Item, Item> HEROBRINE_PHASE_4_DISC = registerDiscItem(NarakaItemIds.HEROBRINE_PHASE_4_DISC, NarakaJukeboxSongs.HEROBRINE_PHASE_4);

    public static final HolderProxy<Item, Item> NARAKA_PICKAXE = registerSimpleItem(
            NarakaItemIds.NARAKA_PICKAXE,
            properties -> properties.fireResistant()
                    .tool(NarakaToolMaterials.LONGINUS, NarakaBlockTags.MINABLE_WITH_NARAKA_PICKAXE, calculateAttackDamageModifier(), -2.8f, 5)
                    .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.NARAKA_PICKAXE.tooltip())
                    .rarity(Rarity.EPIC)
    );

    private static float calculateAttackDamageModifier() {
        return NarakaConfig.COMMON.narakaPickaxeDamage.getValue() - (NarakaToolMaterials.LONGINUS.attackDamageBonus() + 1);
    }

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
            NarakaItemIds.PURIFIED_SOUL_METAL, properties -> properties.fireResistant()
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.PURIFIED_SOUL_METAL.tooltip())
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_SHARD = registerSimpleItem(
            NarakaItemIds.PURIFIED_SOUL_SHARD, Item.Properties::fireResistant
    );
    public static final HolderProxy<Item, Item> RAINBOW_SWORD = registerSimpleItem(NarakaItemIds.RAINBOW_SWORD);

    public static final HolderProxy<Item, Item> NECTARIUM = registerSimpleItem(
            NarakaItemIds.NECTARIUM, properties -> properties.food(
                            new FoodProperties.Builder()
                                    .nutrition(20)
                                    .saturationModifier(1f)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.defaultFood().onConsume(
                                    new ApplyStatusEffectsConsumeEffect(
                                            new MobEffectInstance(MobEffects.INSTANT_HEALTH, 1, 10)
                                    )
                            ).build()
                    )
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.NECTARIUM.tooltip())
    );

    public static final HolderProxy<Item, Item> GOD_BLOOD = registerSimpleItem(
            NarakaItemIds.GOD_BLOOD,
            properties -> properties.stacksTo(1)
                    .rarity(Rarity.EPIC)
                    .trimMaterial(SoulType.GOD_BLOOD.material)
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.GOD_BLOOD.tooltip())
                    .fireResistant()
    );

    public static final HolderProxy<Item, SanctuaryCompassItem> SANCTUARY_COMPASS = registerItem(
            NarakaItemIds.SANCTUARY_COMPASS,
            properties -> new SanctuaryCompassItem(properties
                    .rarity(Rarity.RARE)
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.SANCTUARY_COMPASS.tooltip())
            )
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

    // Spears
    public static final HolderProxy<Item, SpearItem> SPEAR_ITEM = registerItem(
            NarakaItemIds.SPEAR_ITEM,
            properties -> new SpearItem(NarakaToolMaterials.PURIFIED_SOUL,
                    true, 3, -3, 3,
                    properties.fireResistant()
                            .delayedHolderComponent(DataComponents.DAMAGE_TYPE, NarakaDamageTypes.SPEAR)
                            .component(DataComponents.TOOL, TridentItem.createToolProperties())
                            .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.SPEAR.tooltip()),
                    NarakaEntityTypes.THROWN_SPEAR
            )
    );
    public static final HolderProxy<Item, SpearItem> MIGHTY_HOLY_SPEAR_ITEM = registerItem(
            NarakaItemIds.MIGHTY_HOLY_SPEAR_ITEM,
            properties -> new SpearItem(ToolMaterial.NETHERITE,
                    true, 7, -3, 3,
                    properties.fireResistant()
                            .rarity(Rarity.RARE)
                            .delayedHolderComponent(DataComponents.DAMAGE_TYPE, NarakaDamageTypes.SPEAR)
                            .component(DataComponents.TOOL, TridentItem.createToolProperties())
                            .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.MIGHTY_HOLY_SPEAR.tooltip()),
                    NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR
            )
    );
    public static final HolderProxy<Item, SpearOfLonginusItem> SPEAR_OF_LONGINUS_ITEM = registerItem(
            NarakaItemIds.SPEAR_OF_LONGINUS_ITEM,
            properties -> new SpearOfLonginusItem(properties
                    .fireResistant()
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.SPEAR_OF_LONGINUS.tooltip())
                    .delayedHolderComponent(DataComponents.DAMAGE_TYPE, NarakaDamageTypes.SPEAR_OF_LONGINUS)
                    .component(DataComponents.TOOL, TridentItem.createToolProperties())
            )
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
            properties -> new PurifiedSoulSwordItem(NarakaToolMaterials.PURIFIED_SOUL,
                    -2, -2.4f,
                    properties.fireResistant()
                            .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                            .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.PURIFIED_SOUL_SWORD.tooltip())
            )
    );

    public static final HolderProxy<Item, Item> HEROBRINE_SCARF = registerSimpleItem(
            NarakaItemIds.HEROBRINE_SCARF,
            properties -> properties.equippable(EquipmentSlot.CHEST)
                    .rarity(Rarity.EPIC)
                    .component(NarakaDataComponentTypes.HEROBRINE_SCARF.get(), Unit.INSTANCE)
                    .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.HEROBRINE_SCARF.tooltip())
    );

    public static final HolderProxy<Item, Item> PURIFIED_SOUL_HELMET = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_HELMET, NarakaArmorMaterials.PURIFIED_SOUL, ArmorType.HELMET);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_CHESTPLATE = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_CHESTPLATE, NarakaArmorMaterials.PURIFIED_SOUL, ArmorType.CHESTPLATE);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_LEGGINGS = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_LEGGINGS, NarakaArmorMaterials.PURIFIED_SOUL, ArmorType.LEGGINGS);
    public static final HolderProxy<Item, Item> PURIFIED_SOUL_BOOTS = registerPurifiedSoulArmorItem(NarakaItemIds.PURIFIED_SOUL_BOOTS, NarakaArmorMaterials.PURIFIED_SOUL, ArmorType.BOOTS);

    public static final HolderProxy<Item, Item> HEROBRINE_SPAWN_EGG = registerItem(
            NarakaItemIds.HEROBRINE_SPAWN_EGG,
            properties -> new SpawnEggItem(
                    properties.component(
                            DataComponents.ENTITY_DATA,
                            TypedEntityData.of(NarakaEntityTypes.HEROBRINE.get(), new CompoundTag())
                    )
            )
    );

    public static final HolderProxy<Item, Item> DIAMOND_GOLEM_SPAWN_EGG = registerItem(
            NarakaItemIds.DIAMOND_GOLEM_SPAWN_EGG,
            properties -> new SpawnEggItem(
                    properties.component(
                            DataComponents.ENTITY_DATA,
                            TypedEntityData.of(NarakaEntityTypes.DIAMOND_GOLEM.get(), new CompoundTag())
                    )
            )
    );

    public static HolderProxy<Item, Item> registerPurifiedSoulArmorItem(ResourceKey<Item> key, ArmorMaterial armorMaterial, ArmorType armorType) {
        return registerSimpleItem(key, properties -> NarakaArmorMaterials.humanoidPropertiesWithoutEnchantable(properties, armorMaterial, armorType)
                .component(DataComponents.UNBREAKABLE, Unit.INSTANCE)
                .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.PURIFIED_SOUL_ARMORS.tooltip())
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

    @Nullable
    public static Holder<Item> getSoulSwordHolderOf(SoulType type) {
        if (SWORD_BY_SOUL_TYPE.containsKey(type))
            return SWORD_BY_SOUL_TYPE.get(type);
        return null;
    }

    private static HolderProxy<Item, Item> registerSoulInfusedItem(ResourceKey<Item> key, SoulType type) {
        HolderProxy<Item, Item> item = registerItem(
                key,
                properties -> new Item(properties
                        .rarity(Rarity.UNCOMMON)
                        .component(NarakaDataComponentTypes.SOUL.get(), type)
                        .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.SOUL_INFUSED_MATERIALS.tooltip())
                        .trimMaterial(type.material)
                        .fireResistant()
                )
        );
        SOUL_INFUSED_ITEMS.add(item);
        return item;
    }

    private static HolderProxy<Item, Item> registerSoulInfusedSword(ResourceKey<Item> key, SoulType type) {
        HolderProxy<Item, Item> item = registerItem(key,
                properties -> new SoulInfusedSwordItem(
                        properties.fireResistant()
                                .sword(NarakaToolMaterials.PURIFIED_SOUL, 5, -2.4f)
                                .rarity(Rarity.RARE)
                                .component(NarakaDataComponentTypes.SOUL.get(), type)
                                .component(NarakaDataComponentTypes.DYNAMIC_ITEM_LORE.get(), NarakaItemTooltip.SOUL_INFUSED_SWORDS.tooltip())
                                .delayedComponent(NarakaDataComponentTypes.EQUIPMENT_SET.get(), _ -> EquipmentSetHelper.createChallengerSet(type))
                                .component(DataComponents.UNBREAKABLE, Unit.INSTANCE),
                        type.color
                )
        );
        SOUL_INFUSED_SWORDS.add(item);
        SWORD_BY_SOUL_TYPE.put(type, item);
        return item;
    }

    private static HolderProxy<Item, Item> registerDiscItem(ResourceKey<Item> key, ResourceKey<JukeboxSong> song) {
        return registerSimpleItem(key, properties -> properties.stacksTo(1)
                .rarity(Rarity.RARE)
                .jukeboxPlayable(song)
        );
    }

    private static Item.Properties properties() {
        return new Item.Properties();
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(ResourceKey<Item> key, Function<Item.Properties, I> factory, Item.Properties properties) {
        properties.setId(key);
        return RegistryProxy.register(key, () -> factory.apply(properties));
    }

    private static <I extends Item> HolderProxy<Item, I> registerItem(ResourceKey<Item> key, Function<Item.Properties, I> factory) {
        return registerItem(key, factory, properties());
    }

    private static HolderProxy<Item, Item> registerSimpleItem(ResourceKey<Item> key, UnaryOperator<Item.Properties> operator) {
        return registerItem(key, properties -> new Item(operator.apply(properties)));
    }

    private static HolderProxy<Item, Item> registerSimpleItem(ResourceKey<Item> key) {
        return registerItem(key, Item::new);
    }

    public static void initialize() {

    }
}
