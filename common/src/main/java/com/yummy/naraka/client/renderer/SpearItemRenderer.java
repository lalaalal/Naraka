package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.entity.SpearRenderer;
import com.yummy.naraka.world.entity.Spear;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class SpearItemRenderer implements CustomRenderManager.CustomItemRenderer, ResourceManagerReloadListener {
    private static final Map<Item, ResourceLocation> TEXTURE_LOCATIONS = Map.of(
            NarakaItems.SPEAR_ITEM.get(), NarakaTextures.SPEAR,
            NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), NarakaTextures.MIGHTY_HOLY_SPEAR,
            NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), NarakaTextures.LONGINUS
    );
    private static final Set<ItemDisplayContext> LONGINUS_EXCLUDING_CONTEXTS = Set.of(
            ItemDisplayContext.GUI, ItemDisplayContext.FIXED
    );
    private static final Set<ItemDisplayContext> SPEAR_EXCLUDING_CONTEXTS = Set.of(
            ItemDisplayContext.GUI, ItemDisplayContext.FIXED, ItemDisplayContext.GROUND
    );
    private static final Vec3 DEFAULT_TRANSLATION = vec3(-0.05, -0.6, 0.1);
    private static final Quaternionf NO_ROTATION = Axis.ZP.rotationDegrees(0);
    private static final Quaternionf DEFAULT_ROTATION = Axis.ZP.rotationDegrees(180);
    private static final Map<ItemDisplayContext, Vec3> SPEAR_TRANSLATION = Map.of(
            ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, vec3(-0.6, -0.1, -0.3),
            ItemDisplayContext.FIRST_PERSON_LEFT_HAND, vec3(0.5, -0.1, -0.3)
    );
    private static final Map<Item, Map<ItemDisplayContext, Vec3>> TRANSLATIONS = Map.of(
            NarakaItems.SPEAR_ITEM.get(), SPEAR_TRANSLATION,
            NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), SPEAR_TRANSLATION,
            NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), Map.of(
                    ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, vec3(-0.6, -1, -0.6)
            )
    );
    private static final Map<ItemDisplayContext, Quaternionf> SPEAR_ROTATION = Map.of(
            ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, Axis.XP.rotationDegrees(25)
    );

    private static Vec3 vec3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    public static final SpearItemRenderer INSTANCE = new SpearItemRenderer();

    private final Minecraft minecraft;
    private Map<Item, EntityModel<? extends Spear>> models = Map.of();

    private SpearItemRenderer() {
        this.minecraft = Minecraft.getInstance();
    }

    @Override
    public boolean applyTransform() {
        return false;
    }

    @Override
    public boolean shouldRenderCustom(ItemStack stack, ItemDisplayContext context) {
        if (stack.is(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get()))
            return !LONGINUS_EXCLUDING_CONTEXTS.contains(context);
        return !SPEAR_EXCLUDING_CONTEXTS.contains(context);
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Item item = stack.getItem();
        if (models.containsKey(item)) {
            ResourceLocation textureLocation = TEXTURE_LOCATIONS.get(item);
            EntityModel<? extends Spear> model = models.get(item);

            poseStack.pushPose();
            applyTransform(item, context, poseStack);

            if (item == NarakaItems.SPEAR_OF_LONGINUS_ITEM.get())
                renderNonShaderLonginus(model, poseStack, bufferSource);
            RenderType renderType = model.renderType(textureLocation);
            VertexConsumer vertexConsumer = getBuffer(bufferSource, renderType, stack);
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 0xffffffff);
            poseStack.popPose();
        }
    }

    private void renderNonShaderLonginus(EntityModel<? extends Spear> model, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (minecraft.player == null)
            return;
        int tickCount = minecraft.player.tickCount;
        float partialTicks = minecraft.getTimer().getGameTimeDeltaPartialTick(true);
        SpearRenderer.renderNonShaderLonginus(model, tickCount + partialTicks, poseStack, bufferSource);
    }

    private void applyTransform(Item item, ItemDisplayContext context, PoseStack poseStack) {
        poseStack.mulPose(DEFAULT_ROTATION);

        Vec3 translation = TRANSLATIONS
                .getOrDefault(item, Map.of())
                .getOrDefault(context, DEFAULT_TRANSLATION);
        Quaternionf rotation = SPEAR_ROTATION
                .getOrDefault(context, NO_ROTATION);
        poseStack.translate(translation.x, translation.y, translation.z);
        poseStack.mulPose(rotation);

        Player player = minecraft.player;
        if (player != null && player.isUsingItem() && !context.firstPerson()) {
            poseStack.mulPose(DEFAULT_ROTATION);
            poseStack.translate(0, -1.5, 0);
        }
    }

    private VertexConsumer getBuffer(MultiBufferSource buffer, RenderType renderType, ItemStack itemStack) {
        if (itemStack.is(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get()))
            return buffer.getBuffer(renderType);
        return ItemRenderer.getFoilBufferDirect(buffer, renderType, false, itemStack.hasFoil());
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        EntityModelSet entityModels = minecraft.getEntityModels();

        SpearModel spearModel = new SpearModel(entityModels.bakeLayer(NarakaModelLayers.SPEAR));
        SpearOfLonginusModel longinusModel = new SpearOfLonginusModel(entityModels.bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS));

        models = Map.of(
                NarakaItems.SPEAR_ITEM.get(), spearModel,
                NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), spearModel,
                NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), longinusModel
        );
    }
}