package com.yummy.naraka.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SpearSpecialRenderer implements SpecialModelRenderer<Boolean> {
    private final Model<Unit> model;
    private final Identifier texture;

    public SpearSpecialRenderer(Model<Unit> model, Identifier texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public Boolean extractArgument(ItemStack itemStack) {
        return itemStack.hasFoil();
    }

    @Override
    public void submit(@Nullable Boolean hasFoil, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, int overlay, boolean bl, int outlineColor) {
        if (hasFoil == null)
            return;
        poseStack.pushPose();
        poseStack.scale(1, -1, -1);
        RenderType renderType = model.renderType(texture);
        NarakaRenderUtils.submitModelWithFoilRenderTypes(model, Unit.INSTANCE, poseStack, renderType, submitNodeCollector, light, hasFoil);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        poseStack.scale(1, -1, -1);
        model.root().getExtentsForGui(poseStack, output);
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked(ModelLayerLocation modelLayer,
                          Identifier texture) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Identifier.CODEC.fieldOf("model_location").forGetter(unbaked -> unbaked.modelLayer.model()),
                        PrimitiveCodec.STRING.fieldOf("layer").forGetter(unbaked -> unbaked.modelLayer.layer()),
                        Identifier.CODEC.fieldOf("texture").forGetter(unbaked -> unbaked.texture)
                ).apply(instance, Unbaked::new)
        );

        private Unbaked(Identifier model, String layer, Identifier texture) {
            this(new ModelLayerLocation(model, layer), texture);
        }

        @Override
        public SpecialModelRenderer<?> bake(BakingContext context) {
            ModelPart root = context.entityModelSet().bakeLayer(modelLayer);
            Model<Unit> model = new Model.Simple(root, RenderTypes::entityCutout);
            return new SpearSpecialRenderer(model, texture);
        }

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return CODEC;
        }
    }
}
