package com.yummy.naraka.client.init;

import com.yummy.naraka.core.registries.RegistryLoadedListener;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

public interface NarakaClientInitializer extends RegistryLoadedListener {
    void registerClientReloadListener(String name, Supplier<PreparableReloadListener> listener);
}
