package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class NarakaModelLayers {
    public static final ModelLayerLocation HEROBRINE = location("herobrine");
    public static final ModelLayerLocation SPEAR = location("spear");
    public static final ModelLayerLocation SPEAR_OF_LONGINUS = location("spear_of_longinus");

    public static ModelLayerLocation location(String name) {
        return new ModelLayerLocation(NarakaMod.location(name), "main");
    }

}
