package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class NarakaBlockEntityTypes {
    public static final BlockEntityType<SoulCraftingBlockEntity> SOUL_CRAFTING = register(
            "soul_crafting",
            BlockEntityType.Builder.of(SoulCraftingBlockEntity::new, NarakaBlocks.SOUL_CRAFTING_BLOCK)
    );

    public static final BlockEntityType<HerobrineTotemBlockEntity> HEROBRINE_TOTEM = register(
            "herobrine_totem",
            BlockEntityType.Builder.of(
                    HerobrineTotemBlockEntity::new,
                    NarakaBlocks.HEROBRINE_TOTEM
            )
    );

    public static final BlockEntityType<ForgingBlockEntity> FORGING = register(
            "forging",
            BlockEntityType.Builder.of(
                    ForgingBlockEntity::new,
                    NarakaBlocks.FORGING_BLOCK
            )
    );

    public static final BlockEntityType<SoulSmithingBlockEntity> SOUL_SMITHING = register(
            "soul_smithing",
            BlockEntityType.Builder.of(
                    SoulSmithingBlockEntity::new,
                    NarakaBlocks.SOUL_SMITHING_BLOCK
            )
    );

    public static final BlockEntityType<SoulStabilizerBlockEntity> SOUL_STABILIZER = register(
            "soul_stabilizer",
            BlockEntityType.Builder.of(
                    SoulStabilizerBlockEntity::new,
                    NarakaBlocks.SOUL_STABILIZER
            )
    );

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, NarakaMod.location(name), builder.build(null));
    }

    public static void initialize() {

    }
}
