package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.equipment.NarakaEquipmentAssets;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.function.BiConsumer;

public class NarakaEquipmentAssetProvider extends SimpleDataProvider<EquipmentAsset, EquipmentClientInfo> {
    public NarakaEquipmentAssetProvider(FabricDataOutput output) {
        super(output, PackOutput.Target.RESOURCE_PACK, "equipment", EquipmentClientInfo.CODEC);
    }

    @Override
    protected void register(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(NarakaEquipmentAssets.PURIFIED_SOUL, onlyHumanoid("purified_soul"));
    }

    public static EquipmentClientInfo onlyHumanoid(String name) {
        return EquipmentClientInfo.builder().addHumanoidLayers(NarakaMod.location(name)).build();
    }

    @Override
    public String getName() {
        return "Equipment Assets";
    }
}
