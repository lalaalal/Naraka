package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.lang.LanguageKey;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.SmithingTemplateItem;

import java.util.List;

public abstract class NarakaSmithingTemplateItems extends SmithingTemplateItem {
    public static final ResourceLocation EMPTY_SLOT_SWORD = ResourceLocation.withDefaultNamespace("item/empty_slot_sword");
    public static final ResourceLocation EMPTY_SLOT_INGOT = ResourceLocation.withDefaultNamespace("item/empty_slot_ingot");
    public static final ResourceLocation EMPTY_SLOT_REDSTONE_DUST = ResourceLocation.withDefaultNamespace("item/empty_slot_redstone_dust");
    public static final ResourceLocation EMPTY_SLOT_QUARTZ = ResourceLocation.withDefaultNamespace("item/empty_slot_quartz");
    public static final ResourceLocation EMPTY_SLOT_EMERALD = ResourceLocation.withDefaultNamespace("item/empty_slot_emerald");
    public static final ResourceLocation EMPTY_SLOT_DIAMOND = ResourceLocation.withDefaultNamespace("item/empty_slot_diamond");
    public static final ResourceLocation EMPTY_SLOT_LAPIS_LAZULI = ResourceLocation.withDefaultNamespace("item/empty_slot_lapis_lazuli");
    public static final ResourceLocation EMPTY_SLOT_AMETHYST_SHARD = ResourceLocation.withDefaultNamespace("item/empty_slot_amethyst_shard");


    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    public static final Component PURIFIED_SOUL_UPGRADE = Component.translatable(
            LanguageKey.PURIFIED_SOUL_UPGRADE_KEY
    ).withStyle(TITLE_FORMAT);
    public static final Component PURIFIED_SOUL_UPGRADE_APPLIES_TO = Component.translatable(
            LanguageKey.PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY
    ).withStyle(DESCRIPTION_FORMAT);
    public static final Component PURIFIED_SOUL_UPGRADE_INGREDIENTS = Component.translatable(
            LanguageKey.PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY
    ).withStyle(DESCRIPTION_FORMAT);
    public static final Component PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION = Component.translatable(
            LanguageKey.PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY
    );
    public static final Component PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION = Component.translatable(
            LanguageKey.PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY
    );

    public static final ResourceLocation EMPTY_SLOT_SPEAR = NarakaMod.location("item/empty_slot_spear");

    public static final ResourceLocation EMPTY_SLOT_BEAD = NarakaMod.location("item/empty_slot_bead");
    public static final ResourceLocation EMPTY_SLOT_NECTARIUM = NarakaMod.location("item/empty_slot_nectarium");
    public static final ResourceLocation EMPTY_SLOT_SOUL_INFUSED_REDSTONE = NarakaMod.location("item/empty_slot_soul_infused_redstone");

    private static final List<ResourceLocation> PURIFIED_SOUL_UPGRADE_BASE_ICONS = List.of(
            EMPTY_SLOT_SWORD, EMPTY_SLOT_SPEAR
    );
    private static final List<ResourceLocation> PURIFIED_SOUL_UPGRADE_ADDITIONS_ICONS = List.of(
            EMPTY_SLOT_INGOT, EMPTY_SLOT_REDSTONE_DUST, EMPTY_SLOT_EMERALD, EMPTY_SLOT_DIAMOND, EMPTY_SLOT_LAPIS_LAZULI, EMPTY_SLOT_AMETHYST_SHARD, EMPTY_SLOT_BEAD
    );

    private NarakaSmithingTemplateItems(Component component, Component component2, Component component3, Component component4, Component component5, List<ResourceLocation> list, List<ResourceLocation> list2, FeatureFlag... featureFlags) {
        super(component, component2, component3, component4, component5, list, list2, featureFlags);
    }

    public static SmithingTemplateItem createPurifiedSoulUpgradeTemplate(Properties ignored) {
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
}
