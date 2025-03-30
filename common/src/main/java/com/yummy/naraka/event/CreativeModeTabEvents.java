package com.yummy.naraka.event;

import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import java.util.HashMap;
import java.util.Map;

public final class CreativeModeTabEvents {
    private static final Map<ResourceKey<CreativeModeTab>, Event<ModifyEntries>> EVENT_MAP = new HashMap<>();

    public static Event<ModifyEntries> modifyEntriesEvent(ResourceKey<CreativeModeTab> key) {
        return EVENT_MAP.computeIfAbsent(key, EventHandler::createModifyTabEntries);
    }

    public static void modifyEntries(ResourceKey<CreativeModeTab> key, ModifyEntries modifier) {
        modifyEntriesEvent(key).register(modifier);
    }

    @FunctionalInterface
    public interface ModifyEntries {
        void modify(NarakaCreativeModeTabs.TabEntries entries);
    }
}
