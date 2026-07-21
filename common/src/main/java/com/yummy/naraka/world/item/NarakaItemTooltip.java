package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.references.NarakaBlockItemIds;
import com.yummy.naraka.references.NarakaItemIds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NarakaItemTooltip {
    private static final String NEW_LINE_KEY = "new_line.";
    public static final Style PURPOSE = Style.EMPTY.withColor(ChatFormatting.GRAY);
    public static final Style DETAIL = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true);

    public static final NarakaItemTooltip HEROBRINE_SCARF = simple(NarakaItemIds.HEROBRINE_SCARF, 1, 2);
    public static final NarakaItemTooltip NARAKA_PICKAXE = simple(NarakaItemIds.NARAKA_PICKAXE, 1, 3);
    public static final NarakaItemTooltip GOD_BLOOD = simple(NarakaItemIds.GOD_BLOOD, 2, 3);
    public static final NarakaItemTooltip SPEAR_OF_LONGINUS = simple(NarakaItemIds.SPEAR_OF_LONGINUS_ITEM, 2, 3);
    public static final NarakaItemTooltip SANCTUARY_COMPASS = simple(NarakaItemIds.SANCTUARY_COMPASS, 1, 3);
    public static final NarakaItemTooltip IMITATION_GOLD = simple(NarakaBlockItemIds.IMITATION_GOLD_BLOCK, 2, 3);
    public static final NarakaItemTooltip HEROBRINE_TOTEM = simple(NarakaBlockItemIds.HEROBRINE_TOTEM, 2, 2);
    public static final NarakaItemTooltip SOUL_INFUSED_MATERIALS = simple(NarakaMod.identifier("soul_infused_materials"), 1, 1);
    public static final NarakaItemTooltip SOUL_STABILIZER = simple(NarakaBlockItemIds.SOUL_STABILIZER, 2, 0);
    public static final NarakaItemTooltip SOUL_SMITHING_BLOCK = simple(NarakaBlockItemIds.SOUL_SMITHING_BLOCK, 2, 0);
    public static final NarakaItemTooltip PURIFIED_SOUL_METAL = simple(NarakaItemIds.PURIFIED_SOUL_METAL, 1, 1);
    public static final NarakaItemTooltip PURIFIED_SOUL_SWORD = simple(NarakaItemIds.PURIFIED_SOUL_SWORD, 2, 2);
    public static final NarakaItemTooltip SOUL_INFUSED_SWORDS = simple(NarakaMod.identifier("soul_infused_swords"), 2, 0);
    public static final NarakaItemTooltip SOUL_INFUSED_SWORDS_BLESSED = simple(NarakaMod.identifier("soul_infused_swords.blessed"), 1, 0);
    public static final NarakaItemTooltip PURIFIED_SOUL_ARMORS = simple(NarakaMod.identifier("purified_soul_armors"), 1, 0);
    public static final NarakaItemTooltip NECTARIUM = simple(NarakaItemIds.NECTARIUM, 1, 0);
    public static final NarakaItemTooltip NECTARIUM_CORE = simple(NarakaBlockItemIds.NECTARIUM_ORE, 1, 0);

    private final Identifier id;
    private final Map<String, Component> components = new LinkedHashMap<>();

    private static NarakaItemTooltip simple(BlockItemId id, int purpose, int detail) {
        return simple(id.item(), purpose, detail);
    }

    private static NarakaItemTooltip simple(ResourceKey<Item> key, int purpose, int detail) {
        return simple(key.identifier(), purpose, detail);
    }

    private static NarakaItemTooltip simple(Identifier id, int purpose, int detail) {
        NarakaItemTooltip tooltip = new NarakaItemTooltip(id);
        if (purpose > 0)
            tooltip.append(PURPOSE, purpose);
        if (detail > 0)
            tooltip.newLine().append(DETAIL, detail);
        return tooltip;
    }

    public NarakaItemTooltip(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    private NarakaItemTooltip newLine() {
        long index = components.keySet().stream()
                .filter(key -> key.contains(NEW_LINE_KEY))
                .count();
        components.put(NEW_LINE_KEY + index, Component.empty());
        return this;
    }

    private NarakaItemTooltip append(Style style) {
        int index = translationKeys().size();
        String translationKey = LanguageKey.tooltip(id, String.valueOf(index));
        Component component = Component.translatable(translationKey).withStyle(style);
        components.put(translationKey, component);
        return this;
    }

    private NarakaItemTooltip append(Style style, int repeat) {
        for (int i = 0; i < repeat; i++)
            append(style);
        return this;
    }

    public List<String> translationKeys() {
        return components.keySet().stream()
                .filter(key -> !key.contains(NEW_LINE_KEY))
                .toList();
    }

    public ItemLore itemLore() {
        List<Component> lines = List.copyOf(components.values());
        return new ItemLore(lines, lines);
    }
}
