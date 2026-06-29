package com.yummy.naraka.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public interface NarakaEventBus {
    IEventBus FORGE_BUS = MinecraftForge.EVENT_BUS;
    IEventBus NARAKA_BUS = NarakaModForge.getModEventBus();
}
