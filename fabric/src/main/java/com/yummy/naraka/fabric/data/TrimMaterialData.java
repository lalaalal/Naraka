package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

public record TrimMaterialData(ResourceKey<TrimMaterial> trimMaterial) {
    public String name() {
        return trimMaterial.identifier().getPath();
    }

    public Identifier modelLocation(Identifier modelLocation) {
        return modelLocation.withSuffix("_" + name() + "_trim");
    }

    public Material material(String slotName) {
        return new Material(NarakaMod.mcLocation("trims/items/" + slotName + "_trim_" + name()));
    }
}
