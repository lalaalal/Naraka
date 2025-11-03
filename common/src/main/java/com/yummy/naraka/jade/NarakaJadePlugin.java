package com.yummy.naraka.jade;

import com.yummy.naraka.world.block.NectariumCoreBlock;
import com.yummy.naraka.world.block.SoulSmithingBlock;
import com.yummy.naraka.world.block.SoulStabilizer;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class NarakaJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {

    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(SoulStabilizerComponentProvider.INSTANCE, SoulStabilizer.class);
        registration.registerBlockComponent(SoulSmithingBlockComponentProvider.INSTANCE, SoulSmithingBlock.class);
        registration.registerBlockComponent(NectariumCoreComponentProvider.INSTANCE, NectariumCoreBlock.class);

        registration.registerEntityComponent(NarakaEntityDataProvider.INSTANCE, LivingEntity.class);
    }
}
