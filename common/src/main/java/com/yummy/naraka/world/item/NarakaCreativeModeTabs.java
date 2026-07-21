package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.Platform;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.CreativeModeTabEvents;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.equipmentset.EquipmentSet;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetHelper;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class NarakaCreativeModeTabs {
    public static final HolderProxy<CreativeModeTab, CreativeModeTab> NARAKA_TAB = register("naraka", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable(LanguageKey.ITEM_GROUP_NARAKA))
            .icon(() -> NarakaItems.STIGMA_ROD.getConcreteValue().getDefaultInstance())
            .displayItems(NarakaCreativeModeTabs::createNarakaTab)
    );
    public static final HolderProxy<CreativeModeTab, CreativeModeTab> SOUL_MATERIALS_TAB = register("soul_materials", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable(LanguageKey.ITEM_GROUP_SOUL_MATERIALS))
            .icon(() -> NarakaItems.RAINBOW_SWORD.getConcreteValue().getDefaultInstance())
            .displayItems(NarakaCreativeModeTabs::createSoulMaterialsTab)
    );
    @Nullable
    public static final HolderProxy<CreativeModeTab, CreativeModeTab> NARAKA_TEST_TAB = registerOnlyDev("naraka_test", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
            .title(Component.translatable(LanguageKey.ITEM_GROUP_TEST))
            .icon(() -> NarakaItems.NARAKA_FIREBALL_STAFF.getConcreteValue().getDefaultInstance())
            .displayItems(NarakaCreativeModeTabs::createNarakaTestTab)
    );

    private static HolderProxy<CreativeModeTab, CreativeModeTab> register(String name, CreativeModeTab.Builder builder) {
        return RegistryWriter.register(Registries.CREATIVE_MODE_TAB, name, builder::build);
    }

    @Nullable
    private static HolderProxy<CreativeModeTab, CreativeModeTab> registerOnlyDev(String name, CreativeModeTab.Builder builder) {
        if (Platform.getInstance().isDevelopmentEnvironment() || NarakaConfig.COMMON.showTestCreativeModeTab.getValue())
            return RegistryWriter.register(Registries.CREATIVE_MODE_TAB, name, builder::build);
        return null;
    }

    public static void initialize() {
        CreativeModeTabEvents.modifyEntries(CreativeModeTabs.BUILDING_BLOCKS, NarakaCreativeModeTabs::modifyBuildingBlocksTab);
        CreativeModeTabEvents.modifyEntries(CreativeModeTabs.NATURAL_BLOCKS, NarakaCreativeModeTabs::modifyNaturalBlocksTab);
        CreativeModeTabEvents.modifyEntries(CreativeModeTabs.FOOD_AND_DRINKS, NarakaCreativeModeTabs::modifyFoodAndDrinksTab);
        CreativeModeTabEvents.modifyEntries(CreativeModeTabs.INGREDIENTS, NarakaCreativeModeTabs::modifyIngredientsTab);
        CreativeModeTabEvents.modifyEntries(CreativeModeTabs.SPAWN_EGGS, NarakaCreativeModeTabs::modifySpawnEggsTab);
        CreativeModeTabEvents.modifyEntries(CreativeModeTabs.COMBAT, NarakaCreativeModeTabs::modifyCombatTab);
    }

    private static void createNarakaTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaBlocks.AMETHYST_ORE.getConcreteValue());
        output.accept(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.getConcreteValue());
        output.accept(NarakaBlocks.AMETHYST_SHARD_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.NECTARIUM_ORE.getConcreteValue());
        output.accept(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.getConcreteValue());
        output.accept(NarakaBlocks.NECTARIUM_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.NECTARIUM_CORE_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.getConcreteValue());
        output.accept(NarakaItems.NECTARIUM.getConcreteValue());

        output.accept(NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_SHARD.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue());
        output.accept(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_HELMET.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_CHESTPLATE.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_LEGGINGS.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_BOOTS.getConcreteValue());

        output.accept(NarakaItems.PURIFIED_SOUL_SWORD.getConcreteValue());
        output.accept(NarakaItems.NARAKA_PICKAXE.getConcreteValue());
        output.accept(NarakaItems.SPEAR_ITEM.getConcreteValue());
        output.accept(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue());
        output.accept(NarakaItems.NETHERITE_HAMMER.getConcreteValue());
        output.accept(NarakaItems.HEROBRINE_PHASE_1_DISC.getConcreteValue());
        output.accept(NarakaItems.HEROBRINE_PHASE_2_DISC.getConcreteValue());
        output.accept(NarakaItems.HEROBRINE_PHASE_3_DISC.getConcreteValue());
        output.accept(NarakaItems.HEROBRINE_PHASE_4_DISC.getConcreteValue());

        output.accept(NarakaItems.SPEAR_OF_LONGINUS_ITEM.getConcreteValue());
        output.accept(NarakaItems.DIAMOND_GOLEM_SPAWN_EGG.getConcreteValue());
        output.accept(NarakaItems.HEROBRINE_SPAWN_EGG.getConcreteValue());

        output.accept(NarakaItems.GOD_BLOOD.getConcreteValue());
        output.accept(NarakaItems.HEROBRINE_SCARF.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.getConcreteValue());
        output.accept(NarakaItems.SANCTUARY_COMPASS.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_SMITHING_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_STABILIZER.getConcreteValue());
        output.accept(NarakaBlocks.PURIFIED_SOUL_LAMP.getConcreteValue());
        output.accept(NarakaBlocks.PURIFIED_SOUL_LANTERN.getConcreteValue());
    }

    private static void createSoulMaterialsTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        HolderLookup.Provider registries = parameters.holders();
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_COPPER_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_GOLD_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_EMERALD_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_LAPIS_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, registries));
        output.accept(challengerSword(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, registries));
        output.accept(challengerSword(NarakaItems.PURIFIED_SOUL_SWORD, registries));

        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_COPPER.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_GOLD.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST.getConcreteValue());
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM.getConcreteValue());
        output.accept(NarakaItems.PURIFIED_SOUL_METAL.getConcreteValue());

        output.accept(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.getConcreteValue());
        output.accept(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.getConcreteValue());
    }

    private static void createNarakaTestTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        HolderLookup.Provider registries = parameters.holders();
        addBlessedEquipments(SoulType.REDSTONE, output, registries);
        addBlessedEquipments(SoulType.COPPER, output, registries);
        addBlessedEquipments(SoulType.GOLD, output, registries);
        addBlessedEquipments(SoulType.EMERALD, output, registries);
        addBlessedEquipments(SoulType.DIAMOND, output, registries);
        addBlessedEquipments(SoulType.LAPIS, output, registries);
        addBlessedEquipments(SoulType.AMETHYST, output, registries);
        addBlessedEquipments(SoulType.NECTARIUM, output, registries);
        output.accept(NarakaItems.RAINBOW_SWORD.getConcreteValue());
        addBlessedEquipments(SoulType.GOD_BLOOD, output, registries);

        output.accept(NarakaItems.STIGMA_ROD.getConcreteValue());
        output.accept(NarakaItems.SKILL_CONTROLLER.getConcreteValue());
        output.accept(NarakaItems.ANIMATION_CONTROLLER.getConcreteValue());
        output.accept(NarakaItems.NARAKA_FIREBALL_STAFF.getConcreteValue());
    }

    private static void addBlessedEquipments(SoulType type, CreativeModeTab.Output output, HolderLookup.Provider registries) {
        Holder<Item> sword = NarakaItems.getSoulSwordHolderOf(type);
        if (sword != null)
            output.accept(blessedChallengerSword(sword, registries));
        output.accept(challenger(NarakaItems.PURIFIED_SOUL_HELMET, type, registries));
        output.accept(challenger(NarakaItems.PURIFIED_SOUL_CHESTPLATE, type, registries));
        output.accept(challenger(NarakaItems.PURIFIED_SOUL_LEGGINGS, type, registries));
        output.accept(challenger(NarakaItems.PURIFIED_SOUL_BOOTS, type, registries));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_HELMET, type, registries));
        output.accept(scarfAttached(blessed(NarakaItems.PURIFIED_SOUL_CHESTPLATE, type, registries)));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_LEGGINGS, type, registries));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_BOOTS, type, registries));
    }

    private static ItemStack trimmed(ItemStack itemStack, SoulType soulType, HolderLookup.Provider registries) {
        HolderGetter<TrimMaterial> trimMaterials = registries.lookupOrThrow(Registries.TRIM_MATERIAL);
        HolderGetter<TrimPattern> trimPatterns = registries.lookupOrThrow(Registries.TRIM_PATTERN);

        Holder<TrimMaterial> material = trimMaterials.getOrThrow(soulType.material);
        Holder<TrimPattern> pattern = trimPatterns.getOrThrow(TrimPatterns.SILENCE);

        ArmorTrim armorTrim = new ArmorTrim(material, pattern);
        NarakaItemUtils.storeNbtData(itemStack, "Trim", ArmorTrim.CODEC, registries, armorTrim);
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_SOUL_TYPE, SoulType.CODEC, soulType);

        return itemStack;
    }

    private static ItemStack challenger(Holder<Item> item, SoulType soulType, HolderLookup.Provider registries) {
        ItemStack itemStack = trimmed(item.value().getDefaultInstance().copy(), soulType, registries);
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_EQUIPMENT_SET, EquipmentSet.CODEC.listOf(), registries, EquipmentSetHelper.createChallengerSet(soulType));

        while (Reinforcement.canReinforce(itemStack, registries))
            Reinforcement.increase(itemStack, NarakaReinforcementEffects.byItem(itemStack), registries);

        return itemStack;
    }

    private static ItemStack blessed(ItemStack itemStack) {
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_BLESSED, Codec.BOOL, true);

        return itemStack;
    }

    private static ItemStack challengerSword(Holder<Item> item, HolderLookup.Provider registries) {
        ItemStack itemStack = item.value().getDefaultInstance().copy();
        SoulType soulType = NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_SOUL_TYPE, SoulType.CODEC, SoulType.NONE);
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_EQUIPMENT_SET, EquipmentSet.CODEC.listOf(), registries, EquipmentSetHelper.createChallengerSet(soulType));
        return itemStack;
    }

    private static ItemStack blessedChallengerSword(Holder<Item> item, HolderLookup.Provider registries) {
        ItemStack itemStack = blessed(item.value().getDefaultInstance().copy());
        SoulType soulType = NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_SOUL_TYPE, SoulType.CODEC, SoulType.NONE);
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_ITEM_DETAIL, ItemDetail.CODEC, NarakaItemTooltip.SOUL_INFUSED_SWORDS_BLESSED.itemDetail());
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_EQUIPMENT_SET, EquipmentSet.CODEC.listOf(), registries, EquipmentSetHelper.createChallengerSet(soulType));
        return itemStack;
    }

    private static ItemStack blessed(Holder<Item> item, SoulType soulType, HolderLookup.Provider registries) {
        ItemStack itemStack = trimmed(item.value().getDefaultInstance().copy(), soulType, registries);
        blessed(itemStack);
        NarakaItemUtils.storeNbtData(itemStack, NarakaItemUtils.TAG_EQUIPMENT_SET, EquipmentSet.CODEC.listOf(), registries, EquipmentSetHelper.createBlessedSet());

        while (Reinforcement.canReinforce(itemStack, registries))
            Reinforcement.increase(itemStack, NarakaReinforcementEffects.byItem(itemStack), registries);

        return itemStack;
    }

    private static ItemStack scarfAttached(ItemStack itemStack) {
        NarakaItemUtils.storeNbtData(itemStack, "HerobrineScarf", Codec.BOOL, true);
        return itemStack;
    }

    private static void modifyBuildingBlocksTab(TabEntries entries) {
        entries.addAfter(Blocks.AMETHYST_BLOCK,
                NarakaBlocks.AMETHYST_SHARD_BLOCK.getConcreteValue(),
                NarakaBlocks.NECTARIUM_BLOCK.getConcreteValue(),
                NarakaBlocks.IMITATION_GOLD_BLOCK.getConcreteValue()
        );
    }

    private static void modifyNaturalBlocksTab(TabEntries entries) {

    }

    private static void modifyFoodAndDrinksTab(TabEntries entries) {
        entries.addAfter(Items.ENCHANTED_GOLDEN_APPLE, NarakaItems.NECTARIUM.getConcreteValue());
    }

    private static void modifyIngredientsTab(TabEntries entries) {
        entries.addAfter(Items.DIAMOND, NarakaItems.NECTARIUM.getConcreteValue());
    }

    private static void modifySpawnEggsTab(TabEntries entries) {
        entries.addAfter(Blocks.SPAWNER, NarakaBlocks.HEROBRINE_TOTEM.getConcreteValue());
        entries.addAfter(Items.SHULKER_SPAWN_EGG, NarakaItems.DIAMOND_GOLEM_SPAWN_EGG.getConcreteValue(), NarakaItems.HEROBRINE_SPAWN_EGG.getConcreteValue());
    }

    private static void modifyCombatTab(TabEntries entries) {
        entries.addAfter(Items.NETHERITE_AXE, NarakaItems.NETHERITE_HAMMER.getConcreteValue());
    }

    public interface TabEntries {
        void addBefore(ItemLike pivot, ItemLike... items);

        void addAfter(ItemLike pivot, ItemLike... items);
    }
}
