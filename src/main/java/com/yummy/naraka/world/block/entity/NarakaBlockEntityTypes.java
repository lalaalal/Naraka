package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class NarakaBlockEntityTypes {
    public static final BlockEntityType<SoulCraftingBlockEntity> SOUL_CRAFTING_BLOCK_ENTITY = register(
            "soul_crafting_block_entity",
            BlockEntityType.Builder.of(SoulCraftingBlockEntity::new, NarakaBlocks.SOUL_CRAFTING_BLOCK)
    );

    public static final BlockEntityType<HerobrineTotemBlockEntity> HEROBRINE_TOTEM = register(
            "herobrine_totem",
            BlockEntityType.Builder.of(
                    HerobrineTotemBlockEntity::new,
                    NarakaBlocks.HEROBRINE_TOTEM
            )
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, NarakaMod.location(name), builder.build(null));
    }

    public static void initialize() {

    }
}
