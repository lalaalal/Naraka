package com.yummy.naraka.references;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class NarakaItemIds {
    public static final ResourceKey<Item> LOCKED_HEALTH = create("locked_health");
    public static final ResourceKey<Item> STIGMA_ROD = create("stigma_rod");
    public static final ResourceKey<Item> STARDUST_STAFF = create("stardust_staff");
    public static final ResourceKey<Item> NARAKA_FIREBALL_STAFF = create("naraka_fireball_staff");
    public static final ResourceKey<Item> NETHERITE_HAMMER = create("netherite_hammer");

    public static final ResourceKey<Item> HEROBRINE_PHASE_1_DISC = create("herobrine_phase_1_disc");
    public static final ResourceKey<Item> HEROBRINE_PHASE_2_DISC = create("herobrine_phase_2_disc");
    public static final ResourceKey<Item> HEROBRINE_PHASE_3_DISC = create("herobrine_phase_3_disc");
    public static final ResourceKey<Item> HEROBRINE_PHASE_4_DISC = create("herobrine_phase_4_disc");

    public static final ResourceKey<Item> NARAKA_PICKAXE = create("naraka_pickaxe");
    public static final ResourceKey<Item> SKILL_CONTROLLER = create("skill_controller");
    public static final ResourceKey<Item> ANIMATION_CONTROLLER = create("animation_controller");

    public static final ResourceKey<Item> PURIFIED_SOUL_METAL = create("purified_soul_metal");
    public static final ResourceKey<Item> PURIFIED_SOUL_SHARD = create("purified_soul_shard");
    public static final ResourceKey<Item> RAINBOW_SWORD = create("rainbow_sword");
    public static final ResourceKey<Item> NECTARIUM = create("nectarium");
    public static final ResourceKey<Item> GOD_BLOOD = create("god_blood");
    public static final ResourceKey<Item> SANCTUARY_COMPASS = create("sanctuary_compass");
    public static final ResourceKey<Item> PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE = create("purified_soul_upgrade_smithing_template");

    public static final ResourceKey<Item> SOUL_INFUSED_REDSTONE = create("soul_infused_redstone");
    public static final ResourceKey<Item> SOUL_INFUSED_COPPER = create("soul_infused_copper");
    public static final ResourceKey<Item> SOUL_INFUSED_GOLD = create("soul_infused_gold");
    public static final ResourceKey<Item> SOUL_INFUSED_EMERALD = create("soul_infused_emerald");
    public static final ResourceKey<Item> SOUL_INFUSED_DIAMOND = create("soul_infused_diamond");
    public static final ResourceKey<Item> SOUL_INFUSED_LAPIS = create("soul_infused_lapis");
    public static final ResourceKey<Item> SOUL_INFUSED_AMETHYST = create("soul_infused_amethyst");
    public static final ResourceKey<Item> SOUL_INFUSED_NECTARIUM = create("soul_infused_nectarium");

    public static final ResourceKey<Item> SPEAR_ITEM = create("spear");
    public static final ResourceKey<Item> MIGHTY_HOLY_SPEAR_ITEM = create("mighty_holy_spear");
    public static final ResourceKey<Item> SPEAR_OF_LONGINUS_ITEM = create("spear_of_longinus");

    public static final ResourceKey<Item> SOUL_INFUSED_REDSTONE_SWORD = create("soul_infused_redstone_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_COPPER_SWORD = create("soul_infused_copper_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_GOLD_SWORD = create("soul_infused_gold_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_EMERALD_SWORD = create("soul_infused_emerald_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_DIAMOND_SWORD = create("soul_infused_diamond_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_LAPIS_SWORD = create("soul_infused_lapis_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_AMETHYST_SWORD = create("soul_infused_amethyst_sword");
    public static final ResourceKey<Item> SOUL_INFUSED_NECTARIUM_SWORD = create("soul_infused_nectarium_sword");
    public static final ResourceKey<Item> PURIFIED_SOUL_SWORD = create("purified_soul_sword");

    public static final ResourceKey<Item> HEROBRINE_SCARF = create("herobrine_scarf");

    public static final ResourceKey<Item> PURIFIED_SOUL_HELMET = create("purified_soul_helmet");
    public static final ResourceKey<Item> PURIFIED_SOUL_CHESTPLATE = create("purified_soul_chestplate");
    public static final ResourceKey<Item> PURIFIED_SOUL_LEGGINGS = create("purified_soul_leggings");
    public static final ResourceKey<Item> PURIFIED_SOUL_BOOTS = create("purified_soul_boots");

    public static final ResourceKey<Item> HEROBRINE_SPAWN_EGG = create("herobrine_spawn_egg");
    public static final ResourceKey<Item> DIAMOND_GOLEM_SPAWN_EGG = create("diamond_golem_spawn_egg");

    public static ResourceKey<Item> create(String id) {
        return ResourceKey.create(Registries.ITEM, NarakaMod.location(id));
    }
}
