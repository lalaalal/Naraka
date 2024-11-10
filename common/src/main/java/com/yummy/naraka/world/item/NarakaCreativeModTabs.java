package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.init.RegistryInitializer;
import com.yummy.naraka.util.Platform;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("unused")
public class NarakaCreativeModTabs {
    public static final LazyHolder<CreativeModeTab, CreativeModeTab> NARAKA_TAB = register("naraka", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable(LanguageKey.ITEM_GROUP_NARAKA))
            .icon(() -> NarakaItems.STIGMA_ROD.get().getDefaultInstance())
            .displayItems(NarakaCreativeModTabs::createNarakaTab)
    );
    public static final LazyHolder<CreativeModeTab, CreativeModeTab> SOUL_MATERIALS_TAB = register("soul_materials", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.translatable(LanguageKey.ITEM_GROUP_SOUL_MATERIALS))
            .icon(() -> NarakaItems.EBONY_SWORD.get().getDefaultInstance())
            .displayItems(NarakaCreativeModTabs::createSoulMaterialsTab)
    );
    public static final LazyHolder<CreativeModeTab, CreativeModeTab> NARAKA_TEST_TAB = registerOnlyDev("naraka_test", CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
            .title(Component.translatable(LanguageKey.ITEM_GROUP_TEST))
            .icon(() -> NarakaItems.SPEAR_ITEM.get().getDefaultInstance())
            .displayItems(NarakaCreativeModTabs::createNarakaTestTab)
    );

    private static LazyHolder<CreativeModeTab, CreativeModeTab> register(String name, CreativeModeTab.Builder builder) {
        return RegistryProxy.register(Registries.CREATIVE_MODE_TAB, name, builder::build);
    }

    private static LazyHolder<CreativeModeTab, CreativeModeTab> registerOnlyDev(String name, CreativeModeTab.Builder builder) {
        if (Platform.getInstance().isDevelopmentEnvironment())
            return RegistryProxy.register(Registries.CREATIVE_MODE_TAB, name, builder::build);
        return new LazyHolder<>(BuiltInRegistries.CREATIVE_MODE_TAB, NarakaMod.location(name));
    }

    public static void initialize(NarakaInitializer initializer) {
        RegistryInitializer.get(Registries.CREATIVE_MODE_TAB)
                .onRegistrationFinished();

        initializer.modifyCreativeModeTab(CreativeModeTabs.BUILDING_BLOCKS, NarakaCreativeModTabs::modifyBuildingBlocksTab);
        initializer.modifyCreativeModeTab(CreativeModeTabs.NATURAL_BLOCKS, NarakaCreativeModTabs::modifyNaturalBlocksTab);
        initializer.modifyCreativeModeTab(CreativeModeTabs.FOOD_AND_DRINKS, NarakaCreativeModTabs::modifyFoodAndDrinksTab);
        initializer.modifyCreativeModeTab(CreativeModeTabs.INGREDIENTS, NarakaCreativeModTabs::modifyIngredientsTab);
        initializer.modifyCreativeModeTab(CreativeModeTabs.SPAWN_EGGS, NarakaCreativeModTabs::modifySpawnEggsTab);
    }

    private static void createNarakaTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get());
        output.accept(NarakaItems.GOD_BLOOD.get());

        output.accept(NarakaItems.PURIFIED_SOUL_SHARD.get());
        output.accept(NarakaItems.PURIFIED_SOUL_HELMET.get());
        output.accept(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        output.accept(NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        output.accept(NarakaItems.PURIFIED_SOUL_BOOTS.get());

        output.accept(NarakaItems.HEROBRINE_PHASE_1_DISC.get());
        output.accept(NarakaItems.HEROBRINE_PHASE_2_DISC.get());
        output.accept(NarakaItems.HEROBRINE_PHASE_3_DISC.get());
        output.accept(NarakaItems.HEROBRINE_PHASE_4_DISC.get());
        output.accept(NarakaItems.SANCTUARY_COMPASS.get());

        output.accept(NarakaBlocks.SOUL_STABILIZER.get());
        output.accept(NarakaBlocks.SOUL_SMITHING_BLOCK.get());
        output.accept(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());
        output.accept(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get());
        output.accept(NarakaBlocks.AMETHYST_ORE.get());
        output.accept(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
    }

    private static void createSoulMaterialsTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_COPPER_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_GOLD_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD.get());
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD.get());
        output.accept(NarakaItems.PURIFIED_SOUL_SWORD.get());

        output.accept(NarakaItems.SOUL_INFUSED_REDSTONE.get());
        output.accept(NarakaItems.SOUL_INFUSED_COPPER.get());
        output.accept(NarakaItems.SOUL_INFUSED_GOLD.get());
        output.accept(NarakaItems.SOUL_INFUSED_EMERALD.get());
        output.accept(NarakaItems.SOUL_INFUSED_DIAMOND.get());
        output.accept(NarakaItems.SOUL_INFUSED_LAPIS.get());
        output.accept(NarakaItems.SOUL_INFUSED_AMETHYST.get());
        output.accept(NarakaItems.SOUL_INFUSED_NECTARIUM.get());
        output.accept(NarakaItems.PURIFIED_SOUL_METAL.get());

        output.accept(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.get());
        output.accept(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());
    }

    private static void createNarakaTestTab(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
        output.accept(NarakaItems.EBONY_SWORD.get());
        output.accept(NarakaItems.EBONY_METAL_HELMET.get());
        output.accept(NarakaItems.EBONY_METAL_CHESTPLATE.get());
        output.accept(NarakaItems.EBONY_METAL_LEGGINGS.get());
        output.accept(NarakaItems.EBONY_METAL_BOOTS.get());
        output.accept(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE.get());

        output.accept(NarakaItems.EBONY_METAL_INGOT.get());
        output.accept(NarakaItems.COMPRESSED_IRON_INGOT.get());

        output.accept(NarakaItems.SPEAR_ITEM.get());
        output.accept(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get());
        output.accept(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        output.accept(NarakaBlocks.EBONY_METAL_BLOCK.get());
        output.accept(NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        output.accept(NarakaBlocks.PURIFIED_SOUL_BLOCK.get());

        output.accept(NarakaBlocks.EBONY_LOG.get());
        output.accept(NarakaBlocks.EBONY_WOOD.get());
        output.accept(NarakaBlocks.STRIPPED_EBONY_LOG.get());
        output.accept(NarakaBlocks.STRIPPED_EBONY_WOOD.get());
        output.accept(NarakaBlocks.HARD_EBONY_PLANKS.get());
        output.accept(NarakaBlocks.EBONY_ROOTS.get());

        output.accept(NarakaItems.EBONY_ROOTS_SCRAP.get());
        output.accept(NarakaBlocks.EBONY_LEAVES.get());
        output.accept(NarakaBlocks.EBONY_SAPLING.get());

        output.accept(NarakaItems.STIGMA_ROD.get());
        output.accept(NarakaBlocks.NECTARIUM_ORE.get());
        output.accept(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
        output.accept(NarakaBlocks.FORGING_BLOCK.get());
        output.accept(NarakaBlocks.SOUL_CRAFTING_BLOCK.get());

        output.accept(blessed(NarakaItems.PURIFIED_SOUL_HELMET.get()));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get()));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_LEGGINGS.get()));
        output.accept(blessed(NarakaItems.PURIFIED_SOUL_BOOTS.get()));

        output.accept(reinforced(NarakaItems.PURIFIED_SOUL_HELMET.get()));
        output.accept(reinforced(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get()));
        output.accept(reinforced(NarakaItems.PURIFIED_SOUL_LEGGINGS.get()));
        output.accept(reinforced(NarakaItems.PURIFIED_SOUL_BOOTS.get()));
    }

    private static ItemStack blessed(Item item) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.set(NarakaDataComponentTypes.BLESSED.get(), true);
        return itemStack;
    }

    private static ItemStack reinforced(ArmorItem item) {
        ItemStack itemStack = blessed(item);
        while (Reinforcement.canReinforce(itemStack))
            Reinforcement.increase(itemStack, NarakaReinforcementEffects.byItem(itemStack));
        return itemStack;
    }

    private static void modifyBuildingBlocksTab(TabEntries entries) {
        entries.addAfter(Blocks.AMETHYST_BLOCK,
                NarakaBlocks.AMETHYST_SHARD_BLOCK.get(),
                NarakaBlocks.NECTARIUM_BLOCK.get(),
                NarakaBlocks.IMITATION_GOLD_BLOCK.get()
        );
    }

    private static void modifyNaturalBlocksTab(TabEntries entries) {

    }

    private static void modifyFoodAndDrinksTab(TabEntries entries) {
        entries.addAfter(Items.ENCHANTED_GOLDEN_APPLE, NarakaItems.NECTARIUM.get());
    }

    private static void modifyIngredientsTab(TabEntries entries) {
        entries.addAfter(Items.DIAMOND, NarakaItems.NECTARIUM.get());
    }

    private static void modifySpawnEggsTab(TabEntries entries) {
        entries.addAfter(Blocks.TRIAL_SPAWNER, NarakaBlocks.HEROBRINE_TOTEM.get());
    }

    public interface TabEntries {
        void addBefore(ItemLike pivot, ItemLike... items);

        void addAfter(ItemLike pivot, ItemLike... items);
    }
}
