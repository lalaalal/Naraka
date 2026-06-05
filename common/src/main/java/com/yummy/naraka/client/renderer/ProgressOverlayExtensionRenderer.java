package com.yummy.naraka.client.renderer;

import com.yummy.naraka.client.gui.components.ProgressOverlayExtension;
import com.yummy.naraka.init.ProgressOverlayExtensionFactoryRegistry;
import com.yummy.naraka.world.overlay.ProgressOverlayData;
import com.yummy.naraka.world.overlay.ProgressOverlayExtensionType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ProgressOverlayExtensionRenderer {
    public static final ProgressOverlayExtensionRenderer INSTANCE = new ProgressOverlayExtensionRenderer();

    private final Map<UUID, ProgressOverlayExtension<?>> extensions = new HashMap<>();

    private ProgressOverlayExtensionRenderer() {

    }

    public void render(UUID uuid, int x, int y, GuiGraphics graphics) {
        if (extensions.containsKey(uuid))
            extensions.get(uuid).render(x, y, graphics);
    }

    public <T> void register(UUID uuid, ProgressOverlayExtensionType<T> type) {
        ProgressOverlayExtension<T> extension = ProgressOverlayExtensionFactoryRegistry.create(type);
        extensions.put(uuid, extension);
    }

    public void remove(UUID uuid) {
        extensions.remove(uuid);
    }

    public void update(UUID uuid, ProgressOverlayData<?> data) {
        if (extensions.containsKey(uuid)) {
            ProgressOverlayExtension<?> extension = extensions.get(uuid);
            extension.update(data);
        }
    }
}
