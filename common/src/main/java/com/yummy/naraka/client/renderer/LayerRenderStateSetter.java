package com.yummy.naraka.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.item.ItemDisplayContext;

@Environment(EnvType.CLIENT)
public interface LayerRenderStateSetter extends ItemColorSetter {
    void naraka$setItemDisplayContext(ItemDisplayContext itemDisplayContext);
}
