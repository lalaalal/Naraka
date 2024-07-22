package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class NarakaCustomRenderer implements DynamicItemRenderer, ResourceManagerReloadListener {
    private static final Map<Item, ResourceLocation> TEXTURE_LOCATIONS = Map.of(
            NarakaItems.SPEAR_ITEM, NarakaTextures.SPEAR,
            NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, NarakaTextures.MIGHTY_HOLY_SPEAR
    );

    public static NarakaCustomRenderer INSTANCE = new NarakaCustomRenderer();

    private final Minecraft minecraft;
    private Map<Item, EntityModel<?>> models = Map.of();
    private final EntityModelSet entityModels;


    private NarakaCustomRenderer() {
        this.minecraft = Minecraft.getInstance();
        this.entityModels = minecraft.getEntityModels();
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        SpearModel spearModel = new SpearModel(entityModels.bakeLayer(NarakaModelLayers.SPEAR));
        SpearOfLonginusModel longinusModel = new SpearOfLonginusModel(entityModels.bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS));

        models = Map.of(
                NarakaItems.SPEAR_ITEM, spearModel,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, spearModel,
                NarakaItems.SPEAR_OF_LONGINUS_ITEM, longinusModel
        );
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Item item = stack.getItem();
        if (models.containsKey(item)) {
            ResourceLocation textureLocation = TEXTURE_LOCATIONS.get(item);
            EntityModel<?> model = models.get(item);

            poseStack.pushPose();
            Player player = minecraft.player;
            boolean notFirstPerson = displayContext != ItemDisplayContext.FIRST_PERSON_LEFT_HAND && displayContext != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND;
            if (player != null && player.isUsingItem() && notFirstPerson) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
                poseStack.translate(0, -1.5, 0);
            }
            RenderType renderType = model.renderType(textureLocation);
            VertexConsumer vertexConsumer = getBuffer(buffer, renderType, stack);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 0xffffffff);
            poseStack.popPose();
        }
    }

    private VertexConsumer getBuffer(MultiBufferSource buffer, RenderType renderType, ItemStack itemStack) {
        if (itemStack.is(NarakaItems.SPEAR_OF_LONGINUS_ITEM))
            return buffer.getBuffer(renderType);
        return ItemRenderer.getFoilBufferDirect(buffer, renderType, false, itemStack.hasFoil());
    }
}