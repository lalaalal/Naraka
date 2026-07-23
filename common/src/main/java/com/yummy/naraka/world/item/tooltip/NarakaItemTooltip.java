package com.yummy.naraka.world.item.tooltip;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.references.BlockItemId;
import com.yummy.naraka.references.NarakaBlockItemIds;
import com.yummy.naraka.references.NarakaItemIds;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.NbtCondition;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class NarakaItemTooltip {
    public static final StyleApplier PURPOSE = new StyleApplier.Static(Style.EMPTY.withColor(ChatFormatting.GRAY));
    public static final StyleApplier DETAIL = new StyleApplier.Static(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withItalic(true));
    public static final StyleApplier RAINBOW = StyleApplier.Rainbow.INSTANCE;

    private static final ResourceLocation PURIFIED_SOUL_ARMORS_ID = NarakaMod.location("purified_soul_armors");
    public static final ConditionalComponents PURIFIED_SOUL_ARMORS_DEFAULT = simpleConditionalComponents(PURIFIED_SOUL_ARMORS_ID, 1, 3).build();
    public static final ConditionalComponents PURIFIED_SOUL_ARMORS_SOUL = simpleConditionalComponents(PURIFIED_SOUL_ARMORS_ID.withSuffix(".soul"), 2, 2)
            .singleTypedConditions(NarakaItemUtils.TAG_SOUL_TYPE, SoulType.CODEC, SoulType.REDSTONE, SoulType.COPPER, SoulType.GOLD, SoulType.EMERALD, SoulType.DIAMOND, SoulType.LAPIS, SoulType.AMETHYST, SoulType.NECTARIUM)
            .newLine()
            .alwaysDisplay(false)
            .build();
    public static final ConditionalComponents PURIFIED_SOUL_ARMOR_BLESSED = simpleConditionalComponents(PURIFIED_SOUL_ARMORS_ID.withSuffix(".blessed"), 0, 2)
            .type(NbtCondition.Type.ALL)
            .singleTypedConditions(NarakaItemUtils.TAG_BLESSED, Codec.BOOL, true)
            .newLine()
            .alwaysDisplay(false)
            .build();

    private static final ResourceLocation SOUL_INFUSED_SWORDS_ID = NarakaMod.location("soul_infused_swords");
    public static final ConditionalComponents SOUL_INFUSED_SWORDS_DEFAULT = simpleConditionalComponents(SOUL_INFUSED_SWORDS_ID, 2, 2).build();
    public static final ConditionalComponents SOUL_INFUSED_SWORDS_BLESSED = ConditionalComponents.all(SOUL_INFUSED_SWORDS_ID.withSuffix(".blessed"))
            .type(NbtCondition.Type.ALL)
            .singleTypedConditions(NarakaItemUtils.TAG_BLESSED, Codec.BOOL, true)
            .appendTranslatableWithSuffix("longinus.prefix", PURPOSE)
            .appendTranslatableWithSuffix("longinus", RAINBOW)
            .appendTranslatableWithSuffix("longinus.suffix", PURPOSE)
            .newLine()
            .newLine()
            .line(DETAIL)
            .line(DETAIL)
            .build();

    public static final DynamicItemLoreHolder.Single HEROBRINE_SCARF = simple(NarakaItemIds.HEROBRINE_SCARF, 1, 2);
    public static final DynamicItemLoreHolder.Single NARAKA_PICKAXE = simple(NarakaItemIds.NARAKA_PICKAXE, 1, 3);
    public static final DynamicItemLoreHolder.Single GOD_BLOOD = simple(NarakaItemIds.GOD_BLOOD, 2, 3);
    public static final DynamicItemLoreHolder.Single SPEAR_OF_LONGINUS = simple(NarakaItemIds.SPEAR_OF_LONGINUS_ITEM, 2, 2);
    public static final DynamicItemLoreHolder.Single SANCTUARY_COMPASS = simple(NarakaItemIds.SANCTUARY_COMPASS, 1, 3);
    public static final DynamicItemLoreHolder.Single IMITATION_GOLD = simple(NarakaBlockItemIds.IMITATION_GOLD_BLOCK, 2, 3);
    public static final DynamicItemLoreHolder.Single HEROBRINE_TOTEM = simple(NarakaBlockItemIds.HEROBRINE_TOTEM, 2, 2);
    public static final DynamicItemLoreHolder.Single SOUL_INFUSED_MATERIALS = simple(NarakaMod.location("soul_infused_materials"), 1, 2);
    public static final DynamicItemLoreHolder.Single SOUL_STABILIZER = simple(NarakaBlockItemIds.SOUL_STABILIZER, 2, 0);
    public static final DynamicItemLoreHolder.Single SOUL_SMITHING_BLOCK = simple(NarakaBlockItemIds.SOUL_SMITHING_BLOCK, 2, 0);
    public static final DynamicItemLoreHolder.Single PURIFIED_SOUL_METAL = simple(NarakaItemIds.PURIFIED_SOUL_METAL, 1, 1);
    public static final DynamicItemLoreHolder.Single PURIFIED_SOUL_SWORD = simple(NarakaItemIds.PURIFIED_SOUL_SWORD, 2, 2);
    public static final DynamicItemLoreHolder SOUL_INFUSED_SWORDS = DynamicItemLoreHolder.of(SOUL_INFUSED_SWORDS_BLESSED, SOUL_INFUSED_SWORDS_DEFAULT);
    public static final DynamicItemLoreHolder PURIFIED_SOUL_ARMORS = DynamicItemLoreHolder.of(PURIFIED_SOUL_ARMOR_BLESSED, PURIFIED_SOUL_ARMORS_SOUL, PURIFIED_SOUL_ARMORS_DEFAULT);
    public static final DynamicItemLoreHolder.Single NECTARIUM = simple(NarakaItemIds.NECTARIUM, 1, 1);
    public static final DynamicItemLoreHolder.Single NECTARIUM_CORE = simple(NarakaBlockItemIds.NECTARIUM_ORE, 1, 2);

    private static DynamicItemLoreHolder.Single simple(ResourceLocation id, int purpose, int detail) {
        return DynamicItemLoreHolder.single(simpleConditionalComponents(id, purpose, detail).build());
    }

    private static DynamicItemLoreHolder.Single simple(ResourceKey<Item> key, int purpose, int detail) {
        return simple(key.location(), purpose, detail);
    }

    private static DynamicItemLoreHolder.Single simple(BlockItemId id, int purpose, int detail) {
        return simple(id.item(), purpose, detail);
    }

    private static ConditionalComponents.Builder simpleConditionalComponents(ResourceLocation id, int purpose, int detail) {
        ConditionalComponents.Builder builder = ConditionalComponents.any(id);
        for (int i = 0; i < purpose; i++)
            builder.line(PURPOSE);
        if (purpose > 0 && detail > 0)
            builder.newLine();
        for (int i = 0; i < detail; i++)
            builder.line(DETAIL);
        return builder;
    }
}
