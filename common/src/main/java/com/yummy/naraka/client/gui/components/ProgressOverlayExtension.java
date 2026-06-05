package com.yummy.naraka.client.gui.components;

import com.yummy.naraka.world.overlay.ProgressOverlayData;
import com.yummy.naraka.world.overlay.ProgressOverlayExtensionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

@Environment(EnvType.CLIENT)
public interface ProgressOverlayExtension<T> {
    ProgressOverlayExtensionType<T> getType();

    void render(int x, int y, GuiGraphics graphics);

    void update(T data);

    default void update(ProgressOverlayData<?> data) {
        getType().cast(data.value())
                .ifPresent(this::update);
    }
}
