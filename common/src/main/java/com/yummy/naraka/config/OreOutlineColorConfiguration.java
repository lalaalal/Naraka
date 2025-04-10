package com.yummy.naraka.config;

import com.yummy.naraka.util.Color;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class OreOutlineColorConfiguration extends DynamicConfiguration<Color> {
    public static final Color DEFAULT_COLOR = Color.of(0xaaaaaaaa);

    private final Map<TagKey<Block>, Color> tagMappings = new HashMap<>();
    private final Map<ResourceKey<Block>, Color> blockMappings = new HashMap<>();

    public OreOutlineColorConfiguration() {
        super("naraka-ore-outline-color", JsonConfigFile::new);
        addDefaultValue("#minecraft:diamond_ores", new ConfigValue<>(Color.of(0xff0000ff)));
        addDefaultValue("#minecraft:redstone_ores", new ConfigValue<>(Color.of(0xfff0000)));
        addDefaultValue("#minecraft:emerald_ores", new ConfigValue<>(Color.of(0xff00ff00)));
        addDefaultValue("#naraka:nectarium_ores", new ConfigValue<>(Color.of(0xffff00ff)));
    }

    @Override
    protected ConfigValue<Color> createDefaultValue() {
        return new ConfigValue<>(DEFAULT_COLOR);
    }

    @Override
    public void loadValues() {
        super.loadValues();
        tagMappings.clear();
        blockMappings.clear();
        configurations.forEach((key, configValue) -> {
            if (key.startsWith("#")) {
                TagKey<Block> tag = TagKey.create(Registries.BLOCK, ResourceLocation.parse(key.substring(1)));
                tagMappings.put(tag, configValue.getValue());
            } else {
                ResourceKey<Block> block = ResourceKey.create(Registries.BLOCK, ResourceLocation.parse(key));
                blockMappings.put(block, configValue.getValue());
            }
        });
    }

    public Color getColor(BlockState blockState) {
        for (TagKey<Block> tagKey : tagMappings.keySet()) {
            if (blockState.is(tagKey))
                return tagMappings.get(tagKey);
        }
        for (ResourceKey<Block> blockKey : blockMappings.keySet()) {
            if (blockState.is(blockKey))
                return blockMappings.get(blockKey);
        }
        return DEFAULT_COLOR;
    }
}
