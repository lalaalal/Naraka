package com.yummy.naraka.event;

import com.yummy.naraka.invoker.MethodInvoker;
import com.yummy.naraka.world.item.NarakaCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @see #modifyEntries(ResourceKey, EntryModifier)
 */
public final class CreativeModeTabEvents {
    private static final Map<ResourceKey<CreativeModeTab>, Event<EntryModifier>> EVENT_MAP = new HashMap<>();
    @Nullable
    private static ModifyEntriesEventFactory modifyEntriesEventFactory;

    private static Event<EntryModifier> create(ResourceKey<CreativeModeTab> key) {
        if (modifyEntriesEventFactory == null)
            modifyEntriesEventFactory = MethodInvoker.of(CreativeModeTabEvents.class, "getModifyEntriesEventFactory")
                    .invoke().result(ModifyEntriesEventFactory.class);
        return modifyEntriesEventFactory.create(key);
    }

    public static Event<EntryModifier> modifyEntriesEvent(ResourceKey<CreativeModeTab> key) {
        return EVENT_MAP.computeIfAbsent(key, CreativeModeTabEvents::create);
    }

    public static void modifyEntries(ResourceKey<CreativeModeTab> key, EntryModifier modifier) {
        modifyEntriesEvent(key).register(modifier);
    }

    @FunctionalInterface
    public interface ModifyEntriesEventFactory {
        Event<EntryModifier> create(ResourceKey<CreativeModeTab> key);
    }

    @FunctionalInterface
    public interface EntryModifier {
        void modify(NarakaCreativeModeTabs.TabEntries entries);
    }

    public static class ModifyTabEntriesEvent extends Event<EntryModifier> {
        public final ResourceKey<CreativeModeTab> key;

        public ModifyTabEntriesEvent(ResourceKey<CreativeModeTab> key) {
            this.key = key;
        }

        @Override
        public EntryModifier invoker() {
            if (listeners.size() == 1)
                return listeners.getFirst();
            return entries -> {
                for (EntryModifier listener : listeners)
                    listener.modify(entries);
            };
        }
    }
}
