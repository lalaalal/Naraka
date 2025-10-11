package com.yummy.naraka.client.model;

import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineModel<S extends AbstractHerobrineRenderState> extends SkillUsingModModel<S> {
    protected AbstractHerobrineModel(ModelPart root, AnimationDefinition walkAnimationDefinition, String entityName) {
        super(root, walkAnimationDefinition, entityName);
    }

    public abstract ModelPart head();

    public abstract ModelPart main();

    public abstract ModelPart upperBody();

    public abstract ModelPart rightArm();

    public abstract ModelPart rightHand();

    public abstract ModelPart leftArm();

    public abstract ModelPart leftHand();

    @Override
    public void setupAnim(S renderState) {
        super.setupAnim(renderState);

        applyHeadRotation(renderState);
        if (renderState.isIdle)
            AnimationUtils.bobArms(rightArm(), leftArm(), renderState.ageInTicks);
        if (renderState.doWalkAnimation)
            animateWalk(renderState, 5, 6);
        animate(renderState);
    }

    public void applyHeadRotation(AbstractHerobrineRenderState renderState) {
        float yRot = Mth.clamp(renderState.yRot, -45, 45);
        float xRot = Mth.clamp(renderState.xRot, -80, 45);

        this.head().yRot = yRot * (Mth.PI / 180f);
        this.head().xRot = xRot * (Mth.PI / 180f);
    }
}
