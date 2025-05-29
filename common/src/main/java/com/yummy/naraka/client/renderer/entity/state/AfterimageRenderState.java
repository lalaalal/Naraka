package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.Afterimage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public class AfterimageRenderState extends LivingEntityRenderState {
    public final Afterimage afterimage;
    public final Color color;
    public final boolean canRender;
    public Vec3 translation = Vec3.ZERO;

    public AfterimageRenderState(Afterimage afterimage, float partialTicks, Color color) {
        this.afterimage = afterimage;
        int alpha = afterimage.getAlpha(partialTicks);

        this.color = color.withAlpha(alpha);
        this.canRender = !(alpha == 0 && afterimage.getPartialTicks() < partialTicks);
        this.bodyRot = afterimage.getYRot();
        this.yRot = afterimage.getYRot();
    }

    public interface Provider {
        Collection<AfterimageRenderState> afterimages();
    }
}
