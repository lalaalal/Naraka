package com.yummy.naraka.client.init;

import com.yummy.naraka.core.registries.RegistryLoadedListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public interface NarakaClientInitializer extends RegistryLoadedListener {
    void registerClientReloadListener(String name, Supplier<PreparableReloadListener> listener);
}
