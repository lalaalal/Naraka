package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public interface AnimationRenderState {
    void animations(BiConsumer<ResourceLocation, AnimationState> consumer);
}
