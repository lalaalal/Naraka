package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineModel<T extends AbstractHerobrine> extends HierarchicalModel<T> {
    private final ModelPart root;

    protected AbstractHerobrineModel(ModelPart root) {
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return root;
    }

    public abstract ModelPart head();

    public abstract ModelPart main();

    public abstract ModelPart upperBody();

    public abstract ModelPart rightArm();

    public abstract ModelPart leftArm();

    @Override
    public void setupAnim(T herobrine, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root.getAllParts().forEach(ModelPart::resetPose);
        if (!herobrine.getCurrentAnimation().equals(AnimationLocations.STAGGERING))
            applyHeadRotation(netHeadYaw, headPitch);
        if (herobrine.getCurrentAnimation().equals(AnimationLocations.IDLE))
            AnimationUtils.bobArms(rightArm(), leftArm(), ageInTicks);
        this.animateWalk(HerobrineAnimation.WALKING, limbSwing, limbSwingAmount, 5, 6);
        herobrine.forEachAnimations((animationLocation, animationState) -> {
            this.animate(animationState, AnimationMapper.get(animationLocation), ageInTicks);
        });
    }

    private void applyHeadRotation(float netHeadYaw, float headPitch) {
        netHeadYaw = Mth.clamp(netHeadYaw, -45, 45);
        headPitch = Mth.clamp(headPitch, -80, 45);

        head().yRot = netHeadYaw * (Mth.PI / 180f);
        head().xRot = headPitch * (Mth.PI / 180f);
    }
}
