package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.model.AnimatedEntityModel;
import com.yummy.naraka.entity.Animatable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @param <T>
 * @param <M>
 * @author lalaalal
 */
@OnlyIn(Dist.CLIENT)
public abstract class AnimatedLivingEntityRenderer<T extends LivingEntity & Animatable, M extends AnimatedEntityModel<T>> extends LivingEntityRenderer<T, M> {
    protected final Supplier<M> modelCreator;
    private final Map<T, M> modelsByEntity = new HashMap<>();

    protected AnimatedLivingEntityRenderer(EntityRendererProvider.Context context, Supplier<M> modelCreator, float shadowRadius) {
        super(context, modelCreator.get(), shadowRadius);
        this.modelCreator = modelCreator;
    }

    protected M getModel(T livingEntity) {
        if (!modelsByEntity.containsKey(livingEntity))
            replaceDeadEntity(livingEntity);
        return modelsByEntity.get(livingEntity);
    }

    private @Nullable T getDeadEntity() {
        for (T livingEntity : modelsByEntity.keySet()) {
            if (livingEntity.isRemoved())
                return livingEntity;
        }
        return null;
    }

    private void replaceDeadEntity(T livingEntity) {
        T deadEntity = getDeadEntity();
        M model = modelsByEntity.getOrDefault(deadEntity, modelCreator.get());
        if (deadEntity != null)
            modelsByEntity.remove(deadEntity);
        modelsByEntity.put(livingEntity, model);
    }

    @Override
    public void render(T livingEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = getModel(livingEntity);
        super.render(livingEntity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }
}
