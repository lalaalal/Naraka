package com.yummy.naraka.event;

import com.yummy.naraka.proxy.MethodInvoker;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public abstract class EventHandler {
    public static void prepare() {
        MethodInvoker.invoke(EventHandler.class, "prepare");
    }

    @SuppressWarnings("unchecked")
    public static Event<CreativeModeTabEvents.ModifyEntries> createModifyTabEntries(ResourceKey<CreativeModeTab> key) {
        return MethodInvoker.invoke(EventHandler.class, "createModifyTabEntries", Event.class, key);
    }
}
