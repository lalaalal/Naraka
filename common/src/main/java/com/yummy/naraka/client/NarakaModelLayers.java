package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.ForgingBlockEntityRenderer;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;

@Environment(EnvType.CLIENT)
public class NarakaModelLayers {
    public static final ModelLayerLocation HEROBRINE = location("herobrine");
    public static final ModelLayerLocation SPEAR = location("spear");
    public static final ModelLayerLocation SPEAR_OF_LONGINUS = location("spear_of_longinus");

    public static final ModelLayerLocation FORGING_BLOCK = location("forging_block");

    public static ModelLayerLocation location(String name) {
        return new ModelLayerLocation(NarakaMod.location(name), "main");
    }

    public static void initialize() {
        EntityModelLayerRegistry.register(NarakaModelLayers.HEROBRINE, HerobrineModel::createBodyLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        EntityModelLayerRegistry.register(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);

        EntityModelLayerRegistry.register(NarakaModelLayers.FORGING_BLOCK, ForgingBlockEntityRenderer::createBodyLayer);
    }
}
