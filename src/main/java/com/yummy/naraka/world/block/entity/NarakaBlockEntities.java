package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NarakaMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SoulCraftingBlockEntity>> SOUL_CRAFTING_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "soul_crafting_block_entity",
            () -> BlockEntityType.Builder.of(SoulCraftingBlockEntity::new, NarakaBlocks.SOUL_CRAFTING_BLOCK.get()).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NarakaSignBlockEntity>> EBONY_SIGN = BLOCK_ENTITIES.register(
            "ebony_sign",
            () -> BlockEntityType.Builder.of(
                    NarakaSignBlockEntity::new,
                    NarakaBlocks.EBONY_SIGN.get(),
                    NarakaBlocks.EBONY_WALL_SIGN.get()
            ).build(null)
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NarakaHangingSignBlockEntity>> EBONY_HANGING_SIGN = BLOCK_ENTITIES.register(
            "ebony_hanging_sign",
            () -> BlockEntityType.Builder.of(
                    NarakaHangingSignBlockEntity::new,
                    NarakaBlocks.EBONY_HANGING_SIGN.get(),
                    NarakaBlocks.EBONY_WALL_HANGING_SIGN.get()
            ).build(null)
    );

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
