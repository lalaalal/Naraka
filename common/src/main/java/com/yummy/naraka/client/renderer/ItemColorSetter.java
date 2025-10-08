package com.yummy.naraka.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface ItemColorSetter {
    void naraka$setColor(int color);
}
