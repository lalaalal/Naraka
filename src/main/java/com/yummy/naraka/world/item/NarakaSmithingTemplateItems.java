package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.lang.NarakaLanguageProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public class NarakaSmithingTemplateItems {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    // Purified Soul Upgrade Components
    private static final Component PURIFIED_SOUL_UPGRADE = Component.translatable(
            NarakaLanguageProvider.PURIFIED_SOUL_UPGRADE_KEY
    ).withStyle(TITLE_FORMAT);
    private static final Component PURIFIED_SOUL_UPGRADE_APPLIES_TO = Component.translatable(
            NarakaLanguageProvider.PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY
    ).withStyle(DESCRIPTION_FORMAT);
    private static final Component PURIFIED_SOUL_UPGRADE_INGREDIENTS = Component.translatable(
            NarakaLanguageProvider.PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY
    ).withStyle(DESCRIPTION_FORMAT);
    private static final Component PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            NarakaLanguageProvider.PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY
    );
    private static final Component PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            NarakaLanguageProvider.PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY
    );

    // Purified Soul Gem Components
    private static final Component PURIFIED_GEM_UPGRADE = Component.translatable(
            NarakaLanguageProvider.PURIFIED_GEM_UPGRADE_KEY
    ).withStyle(TITLE_FORMAT);
    private static final Component PURIFIED_GEM_UPGRADE_APPLIES_TO = Component.translatable(
            NarakaLanguageProvider.PURIFIED_GEM_UPGRADE_APPLIES_TO_KEY
    ).withStyle(DESCRIPTION_FORMAT);
    private static final Component PURIFIED_GEM_UPGRADE_INGREDIENTS = Component.translatable(
            NarakaLanguageProvider.PURIFIED_GEM_UPGRADE_INGREDIENTS_KEY
    ).withStyle(DESCRIPTION_FORMAT);
    private static final Component PURIFIED_GEM_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            NarakaLanguageProvider.PURIFIED_GEM_UPGRADE_BASE_SLOT_DESCRIPTION_KEY
    );
    private static final Component PURIFIED_GEM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            NarakaLanguageProvider.PURIFIED_GEM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY
    );

    private static final ResourceLocation EMPTY_SLOT_HELMET = NarakaMod.mcLocation("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = NarakaMod.mcLocation("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = NarakaMod.mcLocation("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = NarakaMod.mcLocation("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_HOE = NarakaMod.mcLocation("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = NarakaMod.mcLocation("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = NarakaMod.mcLocation("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = NarakaMod.mcLocation("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = NarakaMod.mcLocation("item/empty_slot_pickaxe");
    private static final ResourceLocation EMPTY_SLOT_SPEAR = NarakaMod.location("item/empty_slot_spear");

    private static final ResourceLocation EMPTY_SLOT_INGOT = NarakaMod.mcLocation("item/empty_slot_ingot");
    private static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST = NarakaMod.mcLocation("item/empty_slot_redstone_dust");
    private static final ResourceLocation EMPTY_SLOT_EMERALD = NarakaMod.mcLocation("item/empty_slot_emerald");
    private static final ResourceLocation EMPTY_SLOT_DIAMOND = NarakaMod.mcLocation("item/empty_slot_diamond");
    private static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = NarakaMod.mcLocation("item/empty_slot_lapis_lazuli");
    private static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD = NarakaMod.mcLocation("item/empty_slot_amethyst_shard");
    private static final ResourceLocation EMPTY_SLOT_GOD_BLOOD = NarakaMod.location("item/empty_slot_god_blood");

    private static final List<ResourceLocation> PURIFIED_SOUL_UPGRADE_BASE_ICONS = List.of(
            EMPTY_SLOT_SWORD
    );
    private static final List<ResourceLocation> PURIFIED_SOUL_UPGRADE_ADDITIONS_ICONS = List.of(
            EMPTY_SLOT_INGOT
    );
    private static final List<ResourceLocation> PURIFIED_GEM_UPGRADE_BASE_ICONS = List.of(
            EMPTY_SLOT_SWORD, EMPTY_SLOT_SPEAR
    );
    private static final List<ResourceLocation> PURIFIED_GEM_UPGRADE_ADDITIONS_ICONS = List.of(
            EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_EMERALD, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_AMETHYST_SHARD, EMPTY_SLOT_GOD_BLOOD
    );


    public static SmithingTemplateItem createPurifiedSoulUpgradeTemplate(Item.Properties ignored) {
        return new SmithingTemplateItem(
                PURIFIED_SOUL_UPGRADE_APPLIES_TO,
                PURIFIED_SOUL_UPGRADE_INGREDIENTS,
                PURIFIED_SOUL_UPGRADE,
                PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION,
                PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                PURIFIED_SOUL_UPGRADE_BASE_ICONS,
                PURIFIED_SOUL_UPGRADE_ADDITIONS_ICONS
        );
    }

    public static SmithingTemplateItem createPurifiedGemUpgradeTemplate(Item.Properties ignored) {
        return new SmithingTemplateItem(
                PURIFIED_GEM_UPGRADE_APPLIES_TO,
                PURIFIED_GEM_UPGRADE_INGREDIENTS,
                PURIFIED_GEM_UPGRADE,
                PURIFIED_GEM_UPGRADE_BASE_SLOT_DESCRIPTION,
                PURIFIED_GEM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION,
                PURIFIED_GEM_UPGRADE_BASE_ICONS,
                PURIFIED_GEM_UPGRADE_ADDITIONS_ICONS
        );
    }
}
