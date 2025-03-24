package com.yummy.naraka.event;

import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;

public abstract class EventHandler {
    @Nullable
    private static EventHandler INSTANCE;

    public static void initialize(NarakaInitializer initializer) {
        INSTANCE = initializer.getEventHandler();
        INSTANCE.prepare();
    }

    public static EventHandler getInstance() {
        if (INSTANCE == null)
            throw new IllegalStateException("Event handler is not initialized");
        return INSTANCE;
    }

    protected abstract void prepare();

    public abstract Event<CreativeModeTabEvents.ModifyEntries> createModifyTabEntries(ResourceKey<CreativeModeTab> key);
}
