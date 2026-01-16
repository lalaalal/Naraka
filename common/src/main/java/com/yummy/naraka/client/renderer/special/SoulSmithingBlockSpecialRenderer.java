package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SoulSmithingBlockSpecialRenderer implements NoDataSpecialModelRenderer {
    private final Model<?> model;

    public SoulSmithingBlockSpecialRenderer(Model<?> model) {
        this.model = model;
    }

    @Override
    public void submit(ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, int packedOverlay, boolean bl, int color) {
        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotation(Mth.PI), 0.5f, 0.5f, 0.5f);
        RenderType renderType = model.renderType(NarakaTextures.SOUL_SMITHING_BLOCK);
        submitNodeCollector.submitModelPart(model.root(), poseStack, renderType, packedLight, packedOverlay, null, -1, null);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack poseStack = new PoseStack();
        model.root().getExtentsForGui(poseStack, consumer);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(BakingContext context) {
            ModelPart root = context.entityModelSet().bakeLayer(NarakaModelLayers.SOUL_SMITHING_BLOCK);
            Model<?> model = new Model.Simple(root, RenderTypes::entityTranslucent);
            return new SoulSmithingBlockSpecialRenderer(model);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
