package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SkillUsingMobRenderState extends LivingEntityRenderState implements AnimationRenderState {
    public boolean isIdle = false;
    public boolean doWalkAnimation = true;
    private Consumer<BiConsumer<Identifier, AnimationState>> animationVisitor = consumer -> {

    };

    public void setAnimationVisitor(SkillUsingMob mob) {
        animationVisitor = mob::forEachAnimations;
    }

    @Override
    public void animations(BiConsumer<Identifier, AnimationState> consumer) {
        animationVisitor.accept(consumer);
    }
}
