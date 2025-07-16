package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.ModelLayerRegistry;
import com.yummy.naraka.client.model.*;
import com.yummy.naraka.client.renderer.blockentity.ForgingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulSmithingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulStabilizerBlockEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public final class NarakaModelLayers {
    public static final ModelLayerLocation HEROBRINE = location("herobrine");
    public static final ModelLayerLocation HEROBRINE_SCARF = location("herobrine_scarf");
    public static final ModelLayerLocation SHADOW_HEROBRINE_ARMOR = location("shadow_herobrine_armor");

    public static final ModelLayerLocation FINAL_HEROBRINE = location("final_herobrine");

    public static final ModelLayerLocation DIAMOND_GOLEM = location("diamond_golem");

    public static final ModelLayerLocation SPEAR = location("spear");
    public static final ModelLayerLocation SPEAR_OF_LONGINUS = location("spear_of_longinus");

    public static final ModelLayerLocation NARAKA_FIREBALL = location("naraka_fireball");
    public static final ModelLayerLocation STARDUST = location("stardust");

    public static final ModelLayerLocation FORGING_BLOCK = location("forging_block");
    public static final ModelLayerLocation SOUL_SMITHING_BLOCK = location("soul_smithing_block");
    public static final ModelLayerLocation SOUL_STABILIZER = location("soul_stabilizer");
    public static final ModelLayerLocation TRIM_TEMPLATE = location("trim_location");

    public static final ModelLayerLocation NARAKA_PICKAXE = location("naraka_pickaxe");

    public static ModelLayerLocation location(String name) {
        return new ModelLayerLocation(NarakaMod.location(name), "main");
    }

    public static void initialize() {
        ModelLayerRegistry.register(NarakaModelLayers.HEROBRINE, HerobrineModel::createForHerobrine);
        ModelLayerRegistry.register(NarakaModelLayers.FINAL_HEROBRINE, FinalHerobrineModel::createBodyLayer);
        ModelLayerRegistry.register(NarakaModelLayers.HEROBRINE_SCARF, HerobrineScarfModel::createBodyLayer);
        ModelLayerRegistry.register(NarakaModelLayers.SHADOW_HEROBRINE_ARMOR, HerobrineModel::createForShadowArmor);

        ModelLayerRegistry.register(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        ModelLayerRegistry.register(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);

        ModelLayerRegistry.register(NarakaModelLayers.NARAKA_FIREBALL, NarakaFireballModel::createBodyLayer);
        ModelLayerRegistry.register(NarakaModelLayers.STARDUST, StardustModel::createBodyLayer);

        ModelLayerRegistry.register(NarakaModelLayers.DIAMOND_GOLEM, DiamondGolemModel::createBodyLayer);

        ModelLayerRegistry.register(NarakaModelLayers.FORGING_BLOCK, ForgingBlockEntityRenderer::createBodyLayer);
        ModelLayerRegistry.register(NarakaModelLayers.SOUL_SMITHING_BLOCK, SoulSmithingBlockEntityRenderer::createMainLayer);
        ModelLayerRegistry.register(NarakaModelLayers.SOUL_STABILIZER, SoulStabilizerBlockEntityRenderer::createBodyLayer);
        ModelLayerRegistry.register(NarakaModelLayers.TRIM_TEMPLATE, SoulSmithingBlockEntityRenderer::createTrimTemplateLayer);

        ModelLayerRegistry.register(NarakaModelLayers.NARAKA_PICKAXE, NarakaPickaxeModel::createBodyLayer);
    }
}
