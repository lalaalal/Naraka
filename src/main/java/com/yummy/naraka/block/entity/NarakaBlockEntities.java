package com.yummy.naraka.block.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
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

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
