package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.DimensionTypeProvider;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SkyRenderState.class)
public abstract class SkyRenderStateMixin implements DimensionTypeProvider {
    @Unique
    private ResourceKey<Level> naraka$dimensionType = Level.OVERWORLD;

    @Override
    public void naraka$setDimensionType(ResourceKey<Level> key) {
        naraka$dimensionType = key;
    }

    @Override
    public ResourceKey<Level> naraka$getDimensionType() {
        return naraka$dimensionType;
    }
}
