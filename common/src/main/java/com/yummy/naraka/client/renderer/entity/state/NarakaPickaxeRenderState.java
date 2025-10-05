package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.NarakaPickaxe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeRenderState extends LivingEntityRenderState implements AnimationRenderState {
    public ItemStackRenderState pickaxe = new ItemStackRenderState();
    private Consumer<BiConsumer<ResourceLocation, AnimationState>> animationVisitor = consumer -> {

    };

    public void setAnimationVisitor(NarakaPickaxe narakaPickaxe) {
        animationVisitor = narakaPickaxe::forEachAnimations;
    }

    @Override
    public void animations(BiConsumer<ResourceLocation, AnimationState> consumer) {
        animationVisitor.accept(consumer);
    }
}
