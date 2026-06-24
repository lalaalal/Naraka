package com.yummy.naraka.neoforge;

import net.minecraftforge.eventbus.api.IEventBus;

public interface NarakaEventBus {
    IEventBus NEOFORGE_BUS = Forge.EVENT_BUS;
    IEventBus NARAKA_BUS = NarakaModNeoForge.getModEventBus();
}
