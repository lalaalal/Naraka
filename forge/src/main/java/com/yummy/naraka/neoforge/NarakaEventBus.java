package com.yummy.naraka.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;

public interface NarakaEventBus {
    IEventBus NEOFORGE_BUS = NeoForge.EVENT_BUS;
    IEventBus NARAKA_BUS = NarakaModNeoForge.getModEventBus();
}
