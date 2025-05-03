package com.yummy.naraka.event;

import com.yummy.naraka.invoker.MethodInvoker;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class CreativeModeTabEvents {
    private static final Map<ResourceKey<CreativeModeTab>, Event<ModifyEntries>> EVENT_MAP = new HashMap<>();
    @Nullable
    private static ModifyEntriesEventFactory modifyEntriesEventFactory;

    private static Event<ModifyEntries> create(ResourceKey<CreativeModeTab> key) {
        if (modifyEntriesEventFactory == null)
            modifyEntriesEventFactory = MethodInvoker.of(CreativeModeTabEvents.class, "getModifyEntriesEventFactory")
                    .invoke().result(ModifyEntriesEventFactory.class);
        return modifyEntriesEventFactory.create(key);
    }

    public static Event<ModifyEntries> modifyEntriesEvent(ResourceKey<CreativeModeTab> key) {
        return EVENT_MAP.computeIfAbsent(key, CreativeModeTabEvents::create);
    }

    public static void modifyEntries(ResourceKey<CreativeModeTab> key, ModifyEntries modifier) {
        modifyEntriesEvent(key).register(modifier);
    }

    @FunctionalInterface
    public interface ModifyEntriesEventFactory {
        Event<ModifyEntries> create(ResourceKey<CreativeModeTab> key);
    }

    @FunctionalInterface
    public interface ModifyEntries {
        void modify(NarakaCreativeModeTabs.TabEntries entries);
    }

    public static class ModifyTabEntriesEvent extends Event<ModifyEntries> {
        public final ResourceKey<CreativeModeTab> key;

        public ModifyTabEntriesEvent(ResourceKey<CreativeModeTab> key) {
            this.key = key;
        }

        @Override
        public ModifyEntries invoker() {
            if (listeners.size() == 1)
                return listeners.getFirst();
            return entries -> {
                for (ModifyEntries listener : listeners)
                    listener.modify(entries);
            };
        }
    }
}
