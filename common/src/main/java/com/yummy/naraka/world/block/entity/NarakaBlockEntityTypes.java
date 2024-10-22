package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class NarakaBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<SoulCraftingBlockEntity>> SOUL_CRAFTING = register(
            "soul_crafting",
            SoulCraftingBlockEntity::new,
            NarakaBlocks.SOUL_CRAFTING_BLOCK
    );

    public static final RegistrySupplier<BlockEntityType<HerobrineTotemBlockEntity>> HEROBRINE_TOTEM = register(
            "herobrine_totem",
            HerobrineTotemBlockEntity::new,
            NarakaBlocks.HEROBRINE_TOTEM
    );

    public static final RegistrySupplier<BlockEntityType<ForgingBlockEntity>> FORGING = register(
            "forging",
            ForgingBlockEntity::new,
            NarakaBlocks.FORGING_BLOCK
    );

    public static final RegistrySupplier<BlockEntityType<SoulSmithingBlockEntity>> SOUL_SMITHING = register(
            "soul_smithing",
            SoulSmithingBlockEntity::new,
            NarakaBlocks.SOUL_SMITHING_BLOCK
    );

    public static final RegistrySupplier<BlockEntityType<SoulStabilizerBlockEntity>> SOUL_STABILIZER = register(
            "soul_stabilizer",
            SoulStabilizerBlockEntity::new,
            NarakaBlocks.SOUL_STABILIZER
    );

    private static <T extends BlockEntity> RegistrySupplier<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> supplier, Supplier<? extends Block> block) {
        return BLOCK_ENTITY_TYPES.register(name, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
    }

    public static void initialize() {
        BLOCK_ENTITY_TYPES.register();
    }
}
