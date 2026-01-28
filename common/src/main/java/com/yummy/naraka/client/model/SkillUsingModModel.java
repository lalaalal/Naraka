package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;

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
        entity.getAnimations().forEach(location -> {
            AnimationState state = entity.getAnimationState(location);
            if (location.getPath().startsWith("animation/" + name))
                animate(state, AnimationMapper.get(location), ageInTicks, 1);
        });
    }
}
