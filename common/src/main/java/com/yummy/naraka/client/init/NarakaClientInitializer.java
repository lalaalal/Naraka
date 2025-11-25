package com.yummy.naraka.client.init;

import com.yummy.naraka.core.registries.RegistryLoadedListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface NarakaClientInitializer extends RegistryLoadedListener {

}
