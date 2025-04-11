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
    private Color defaultColor = DEFAULT_COLOR;

    public OreOutlineColorConfiguration() {
        super("naraka-ore-outline-color", JsonConfigFile::new);
        addDefaultValue("default_color", new ConfigValue<>(DEFAULT_COLOR));
        addDefaultValue("#minecraft:diamond_ores", new ConfigValue<>(Color.of(0xFF49EDD9)));
        addDefaultValue("#minecraft:redstone_ores", new ConfigValue<>(Color.of(0xFFFF0000)));
        addDefaultValue("#minecraft:emerald_ores", new ConfigValue<>(Color.of(0xFF16DD63)));
        addDefaultValue("#minecraft:gold_ores", new ConfigValue<>(Color.of(0xFFFEF55F)));
        addDefaultValue("#minecraft:iron_ores", new ConfigValue<>(Color.of(0xFFFFFFFF)));
        addDefaultValue("minecraft:ancient_debris", new ConfigValue<>(Color.of(0xFF644740)));
        addDefaultValue("#naraka:nectarium_ores", new ConfigValue<>(Color.of(0xFFE494DF)));
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
            if (key.equals("default_color")) {
                defaultColor = configValue.getValue();
            } else if (key.startsWith("#")) {
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
        return defaultColor;
    }
}
