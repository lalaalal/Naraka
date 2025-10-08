package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.entity.state.PurifiedSoulFlameRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderState.class)
public abstract class EntityRenderStateMixin implements PurifiedSoulFlameRenderState {
    @Unique
    private boolean naraka$displayNarkaFlame;

    @Override
    public boolean naraka$displayPurifiedSoulFlame() {
        return naraka$displayNarkaFlame;
    }

    @Override
    public void naraka$setDisplayPurifiedSoulFlame(boolean flame) {
        this.naraka$displayNarkaFlame = flame;
    }
}
