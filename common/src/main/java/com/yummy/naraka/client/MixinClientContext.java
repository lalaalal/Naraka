package com.yummy.naraka.client;

import net.minecraft.resources.ResourceLocation;

public class MixinClientContext {
    public static final ThreadLocal<ResourceLocation> DYNAMIC_POST_EFFECT = new ThreadLocal<>();
}
