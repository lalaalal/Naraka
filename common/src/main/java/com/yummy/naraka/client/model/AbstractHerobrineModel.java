package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineModel<S extends AbstractHerobrineRenderState> extends EntityModel<S> {
    protected AbstractHerobrineModel(ModelPart root) {
        super(root);
    }

    public abstract ModelPart head();

    public abstract ModelPart main();

    public abstract ModelPart upperBody();

    public abstract ModelPart rightArm();

    public abstract ModelPart leftArm();

    @Override
    public void setupAnim(S renderState) {
        super.setupAnim(renderState);
        this.root.getAllParts().forEach(ModelPart::resetPose);
        if (!renderState.isStaggering)
            applyHeadRotation(renderState);
        if (renderState.isIdle)
            AnimationUtils.bobArms(rightArm(), leftArm(), renderState.ageInTicks);
        this.animateWalk(HerobrineAnimation.WALKING, renderState.walkAnimationPos, renderState.walkAnimationSpeed, 5, 6);
        renderState.animations((animationLocation, animationState) -> {
            this.animate(animationState, AnimationMapper.get(animationLocation), renderState.ageInTicks);
        });
    }

    private void applyHeadRotation(AbstractHerobrineRenderState renderState) {
        float yRot = Mth.clamp(renderState.yRot, -45, 45);
        float xRot = Mth.clamp(renderState.xRot, -80, 45);

        this.head().yRot = yRot * (Mth.PI / 180f);
        this.head().xRot = xRot * (Mth.PI / 180f);
    }
}
