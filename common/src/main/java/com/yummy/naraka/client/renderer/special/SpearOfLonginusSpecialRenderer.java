package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.entity.SpearRenderer;
import com.yummy.naraka.client.renderer.entity.state.SpearRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.Level;
import org.joml.Vector3fc;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
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
    public void submit(ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean bl, int color) {
        poseStack.pushPose();
        poseStack.scale(1, -1, 1);
        if (displayContext == ItemDisplayContext.GROUND)
            poseStack.scale(4, 4, 4);

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

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = MapCodec.unit(new Unbaked());

        @Override
        public SpecialModelRenderer<?> bake(BakingContext context) {
            ModelPart root = context.entityModelSet().bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS);
            SpearOfLonginusModel model = new SpearOfLonginusModel(root);
            return new SpearOfLonginusSpecialRenderer(model);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
