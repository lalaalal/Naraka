package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Afterimage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineRenderState extends LivingEntityRenderState implements AnimationRenderState, AfterimageRenderState.Provider {
    public boolean isShadow = false;
    public boolean isStaggering = false;
    public boolean isIdle = false;
    private Collection<AfterimageRenderState> afterimages = List.of();
    private Consumer<BiConsumer<ResourceLocation, AnimationState>> animationVisitor = consumer -> {

    };

    protected AbstractHerobrineRenderState() {

    }

    public void setAnimationVisitor(AbstractHerobrine herobrine) {
        animationVisitor = herobrine::forEachAnimations;
    }

    public void setAfterimages(AbstractHerobrine herobrine, float partialTicks) {
        Color color = NarakaConfig.CLIENT.afterimageColor.getValue();
        this.afterimages = herobrine.getAfterimages().stream()
                .map(afterimage -> createAfterimageRenderState(herobrine, afterimage, color, partialTicks))
                .toList();
    }

    private static AfterimageRenderState createAfterimageRenderState(AbstractHerobrine herobrine, Afterimage afterimage, Color color, float partialTicks) {
        AfterimageRenderState renderState = new AfterimageRenderState(afterimage, partialTicks, color);
        renderState.translation = afterimage.translation(herobrine, partialTicks);
        return renderState;
    }

    @Override
    public void animations(BiConsumer<ResourceLocation, AnimationState> consumer) {
        animationVisitor.accept(consumer);
    }

    @Override
    public Collection<AfterimageRenderState> afterimages() {
        return afterimages;
    }
}
