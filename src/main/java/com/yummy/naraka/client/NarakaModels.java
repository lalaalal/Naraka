package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class NarakaModels {
    public static final ModelResourceLocation SPEAR = location("spear");

    private static ModelResourceLocation invenyory(String path) {
        return ModelResourceLocation.inventory(NarakaMod.location(path));
    }

    private static ModelResourceLocation location(String path) {
        return new ModelResourceLocation(NarakaMod.location(path), "");
    }
}
