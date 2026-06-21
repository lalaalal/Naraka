package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.HiddenOreRenderState;
import com.yummy.naraka.client.renderer.HiddenOreRenderStateProvider;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(LevelRenderState.class)
public abstract class LevelRenderStateMixin implements HiddenOreRenderStateProvider {
    @Unique
    private final List<HiddenOreRenderState> naraka$getHiddenOreRenderStates = new ArrayList<>();

    @Override
    public List<HiddenOreRenderState> naraka$getHiddenOreRenderStates() {
        return naraka$getHiddenOreRenderStates;
    }

    @Override
    public void naraka$clearHiddenOreRenderStates() {
        naraka$getHiddenOreRenderStates.clear();
    }
}
