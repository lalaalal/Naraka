package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.entity.SpearRenderer;
import com.yummy.naraka.client.renderer.entity.state.SpearRenderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.Level;
import org.joml.Vector3fc;

import java.util.function.Consumer;

public class SpearOfLonginusSpecialRenderer implements NoDataSpecialModelRenderer {

    private final EntityModel<SpearRenderState> model;
    private final Minecraft minecraft;

    public SpearOfLonginusSpecialRenderer(EntityModel<SpearRenderState> model) {
        this.model = model;
        this.minecraft = Minecraft.getInstance();
    }

    private float getAgeInTicks() {
        if (minecraft.player == null)
            return 0;
        Level level = minecraft.player.level();
        return level.getGameTime() + minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.scale(1, -1, 1);

        if (NarakaClientContext.SHADER_ENABLED.getValue())
            SpearRenderer.renderNonShaderLonginus(model, getAgeInTicks(), poseStack, submitNodeCollector);
        else
            SpearRenderer.renderShaderLonginus(model, poseStack, submitNodeCollector);

        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        this.model.root().getExtentsForGui(poseStack, output);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<Void> bake(BakingContext context) {
            ModelPart root = context.entityModelSet().bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS);
            SpearOfLonginusModel model = new SpearOfLonginusModel(root);
            return new SpearOfLonginusSpecialRenderer(model);
        }

        @Override
        public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
