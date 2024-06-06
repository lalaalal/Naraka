package com.yummy.naraka.client;

import com.yummy.naraka.event.NarakaClientEventBus;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

/**
 * @see NarakaClientEventBus#registerShaders(RegisterShadersEvent)
 */
public class NarakaShaders {
    public static ShaderInstance longinus;

    /**
     * Used end portal shader, just grayscale...
     *
     * @return Longinus shader
     */
    public static ShaderInstance longinus() {
        return longinus;
    }
}
