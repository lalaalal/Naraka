package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
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
}
