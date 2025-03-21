package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.model.*;
import com.yummy.naraka.client.renderer.blockentity.ForgingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulSmithingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulStabilizerBlockEntityRenderer;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public final class NarakaModelLayers {
    public static final ModelLayerLocation HEROBRINE = location("herobrine");
    public static final ModelLayerLocation HEROBRINE_SCARF = location("herobrine_scarf");
    public static final ModelLayerLocation SHADOW_HEROBRINE_ARMOR = location("shadow_herobrine_armor");
    public static final ModelLayerLocation SPEAR = location("spear");
    public static final ModelLayerLocation SPEAR_OF_LONGINUS = location("spear_of_longinus");

    public static final ModelLayerLocation NARAKA_FIREBALL = location("naraka_fireball");

    public static final ModelLayerLocation FORGING_BLOCK = location("forging_block");
    public static final ModelLayerLocation SOUL_SMITHING_BLOCK = location("soul_smithing_block");
    public static final ModelLayerLocation SOUL_STABILIZER = location("soul_stabilizer");
    public static final ModelLayerLocation TRIM_TEMPLATE = location("trim_location");

    public static ModelLayerLocation location(String name) {
        return new ModelLayerLocation(NarakaMod.location(name), "main");
    }

    public static void initialize() {
        EntityModelLayerRegistry.register(NarakaModelLayers.HEROBRINE, HerobrineModel::createForHerobrine);
        EntityModelLayerRegistry.register(NarakaModelLayers.HEROBRINE_SCARF, HerobrineScarfModel::createBodyLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.SHADOW_HEROBRINE_ARMOR, HerobrineModel::createForShadowArmor);

        EntityModelLayerRegistry.register(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);

        EntityModelLayerRegistry.register(NarakaModelLayers.NARAKA_FIREBALL, NarakaFireballModel::createBodyLayer);

        EntityModelLayerRegistry.register(NarakaModelLayers.FORGING_BLOCK, ForgingBlockEntityRenderer::createBodyLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.SOUL_SMITHING_BLOCK, SoulSmithingBlockEntityRenderer::createMainLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.SOUL_STABILIZER, SoulStabilizerBlockEntityRenderer::createBodyLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.TRIM_TEMPLATE, SoulSmithingBlockEntityRenderer::createTrimTemplateLayer);
    }
}
