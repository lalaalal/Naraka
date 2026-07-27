package com.yummy.naraka.references;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record BlockItemId(ResourceKey<Block> block, ResourceKey<Item> item) {
}
