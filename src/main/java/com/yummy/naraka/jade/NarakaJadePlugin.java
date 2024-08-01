package com.yummy.naraka.jade;

import com.yummy.naraka.world.block.SoulCraftingBlock;
import com.yummy.naraka.world.block.entity.SoulCraftingBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;

public class NarakaJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(SoulCraftingBlockComponentProvider.INSTANCE, SoulCraftingBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SoulCraftingBlockComponentProvider.INSTANCE, SoulCraftingBlock.class);
    }
}
