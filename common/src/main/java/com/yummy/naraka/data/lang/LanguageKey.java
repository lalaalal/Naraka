package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public final class LanguageKey {
    public static final String ITEM_GROUP_NARAKA = itemGroup("naraka");
    public static final String ITEM_GROUP_TEST = itemGroup("naraka.test");
    public static final String ITEM_GROUP_SOUL_MATERIALS = itemGroup("naraka.soul_materials");

    public static final String KEY_CATEGORIES_NARAKA = keyMapping("naraka");

    public static final String KEY_TOGGLE_ORE_SEE_THROUGH = keyMapping("naraka.toggle_ore_see_through");

    public static final String PURIFIED_SOUL_UPGRADE_KEY = Util.makeDescriptionId("upgrade", NarakaMod.location("purified_soul_upgrade"));
    public static final String PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.applies_to"));
    public static final String PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.ingredients"));
    public static final String PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.base_slot_description"));
    public static final String PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.additions_slot_description"));
    public static final String JADE_SOUL_CRAFTING_FUEL_KEY = "jade.naraka.soul_crafting.fuel";
    public static final String JADE_STIGMA_KEY = "jade.naraka.stigma";
    public static final String JADE_DEATH_COUNT_KEY = "jade.naraka.death_count";
    public static final String JADE_SOUL_STABILIZER_KEY = "jade.naraka.soul_stabilizer";
    public static final String REINFORCEMENT_KEY = "item.reinforcement";
    public static final String BLESSED_KEY = "item.blessed";

    public static final String CHALLENGERS_BLESSING = Util.makeDescriptionId("effect", NarakaMod.location("challengers_blessing"));

    public static String reinforcementEffect(Holder<ReinforcementEffect> reinforcementEffect) {
        Optional<ResourceKey<ReinforcementEffect>> key = reinforcementEffect.unwrapKey();
        if (key.isEmpty())
            throw new IllegalStateException("Resource key doesn't exists : " + reinforcementEffect);
        ResourceLocation id = key.get().location();
        return Util.makeDescriptionId("reinforcement_effect", id);
    }

    public static String tooltip(Block block) {
        return block.getDescriptionId() + ".tooltip";
    }

    public static String itemGroup(String path) {
        return "itemGroup." + path;
    }

    public static String keyMapping(String path) {
        return "key." + path;
    }

    public static String keyMappingCategory(String path) {
        return "key.categories." + path;
    }
}
