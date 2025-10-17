package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;

@Environment(EnvType.CLIENT)
public abstract class SkillUsingModModel<T extends SkillUsingMob> extends HierarchicalModel<T> {
    private final String name;
    private final ModelPart root;

    protected SkillUsingModModel(ModelPart root, String name) {
        this.name = name;
        this.root = root;
    }

    @Override
    public ModelPart root() {
        return root;
    }

    protected void resetPose() {
        root().getAllParts().forEach(ModelPart::resetPose);
    }

    protected void playAnimations(T entity, float ageInTicks) {
        entity.forEachAnimations((location, animationState) -> {
            if (location.getPath().startsWith("animation/" + name))
                animate(animationState, AnimationMapper.get(location), ageInTicks, 1);
        });
    }
}
