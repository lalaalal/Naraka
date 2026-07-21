package com.yummy.naraka.references;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class NarakaBlockItemIds {
    public static final BlockItemId TRANSPARENT_BLOCK = create("transparent_block");

    public static final BlockItemId AMETHYST_ORE = create("amethyst_ore");
    public static final BlockItemId DEEPSLATE_AMETHYST_ORE = create("deepslate_amethyst_ore");
    public static final BlockItemId NECTARIUM_ORE = create("nectarium_ore");
    public static final BlockItemId DEEPSLATE_NECTARIUM_ORE = create("deepslate_nectarium_ore");
    public static final BlockItemId NECTARIUM_BLOCK = create("nectarium_block");
    public static final BlockItemId NECTARIUM_CORE_BLOCK = create("nectarium_core");
    public static final BlockItemId NECTARIUM_CRYSTAL_BLOCK = create("nectarium_crystal");

    public static final BlockItemId PURIFIED_SOUL_LAMP = create("purified_soul_lamp");
    public static final BlockItemId PURIFIED_SOUL_LANTERN = create("purified_soul_lantern");
    public static final BlockItemId PURIFIED_SOUL_METAL_BLOCK = create("purified_soul_metal_block");
    public static final BlockItemId SOUL_SMITHING_BLOCK = create("soul_smithing_block");
    public static final BlockItemId IMITATION_GOLD_BLOCK = create("imitation_gold_block");
    public static final BlockItemId AMETHYST_SHARD_BLOCK = create("amethyst_shard_block");

    public static final BlockItemId SOUL_INFUSED_REDSTONE_BLOCK = create("soul_infused_redstone_block");
    public static final BlockItemId SOUL_INFUSED_COPPER_BLOCK = create("soul_infused_copper_block");
    public static final BlockItemId SOUL_INFUSED_GOLD_BLOCK = create("soul_infused_gold_block");
    public static final BlockItemId SOUL_INFUSED_EMERALD_BLOCK = create("soul_infused_emerald_block");
    public static final BlockItemId SOUL_INFUSED_DIAMOND_BLOCK = create("soul_infused_diamond_block");
    public static final BlockItemId SOUL_INFUSED_LAPIS_BLOCK = create("soul_infused_lapis_block");
    public static final BlockItemId SOUL_INFUSED_AMETHYST_BLOCK = create("soul_infused_amethyst_block");
    public static final BlockItemId SOUL_INFUSED_NECTARIUM_BLOCK = create("soul_infused_nectarium_block");

    public static final BlockItemId HEROBRINE_TOTEM = create("herobrine_totem");
    public static final BlockItemId SOUL_STABILIZER = create("soul_stabilizer");

    private static BlockItemId create(String id) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, NarakaMod.location(id));
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, NarakaMod.location(id));
        return new BlockItemId(blockKey, itemKey);
    }
}
