package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class NarakaBlockEntityTypes {
    public static final HolderProxy<BlockEntityType<?>, BlockEntityType<SoulCraftingBlockEntity>> SOUL_CRAFTING = register(
            "soul_crafting",
            SoulCraftingBlockEntity::new,
            NarakaBlocks.SOUL_CRAFTING_BLOCK
    );

    public static final HolderProxy<BlockEntityType<?>, BlockEntityType<HerobrineTotemBlockEntity>> HEROBRINE_TOTEM = register(
            "herobrine_totem",
            HerobrineTotemBlockEntity::new,
            NarakaBlocks.HEROBRINE_TOTEM
    );

    public static final HolderProxy<BlockEntityType<?>, BlockEntityType<ForgingBlockEntity>> FORGING = register(
            "forging",
            ForgingBlockEntity::new,
            NarakaBlocks.FORGING_BLOCK
    );

    public static final HolderProxy<BlockEntityType<?>, BlockEntityType<SoulSmithingBlockEntity>> SOUL_SMITHING = register(
            "soul_smithing",
            SoulSmithingBlockEntity::new,
            NarakaBlocks.SOUL_SMITHING_BLOCK
    );

    public static final HolderProxy<BlockEntityType<?>, BlockEntityType<SoulStabilizerBlockEntity>> SOUL_STABILIZER = register(
            "soul_stabilizer",
            SoulStabilizerBlockEntity::new,
            NarakaBlocks.SOUL_STABILIZER
    );

    public static final HolderProxy<BlockEntityType<?>, BlockEntityType<UnstableBlockEntity>> UNSTABLE_BLOCK = register(
            "unstable_block",
            UnstableBlockEntity::new,
            NarakaBlocks.UNSTABLE_BLOCK
    );

    private static <T extends BlockEntity> HolderProxy<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> block) {
        return RegistryProxy.register(Registries.BLOCK_ENTITY_TYPE, name, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }

    public static void initialize() {

    }
}
