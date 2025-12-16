package com.yummy.naraka.world.item.equipment.trim;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.Map;

public class NarakaTrimMaterials {
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_REDSTONE = create("soul_infused_redstone");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_COPPER = create("soul_infused_copper");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_GOLD = create("soul_infused_gold");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_EMERALD = create("soul_infused_emerald");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_DIAMOND = create("soul_infused_diamond");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_LAPIS = create("soul_infused_lapis");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_AMETHYST = create("soul_infused_amethyst");
    public static final ResourceKey<TrimMaterial> SOUL_INFUSED_NECTARIUM = create("soul_infused_nectarium");
    public static final ResourceKey<TrimMaterial> GOD_BLOOD = create("god_blood");

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, SOUL_INFUSED_REDSTONE, SoulType.REDSTONE);
        register(context, SOUL_INFUSED_COPPER, SoulType.COPPER);
        register(context, SOUL_INFUSED_GOLD, SoulType.GOLD);
        register(context, SOUL_INFUSED_EMERALD, SoulType.EMERALD);
        register(context, SOUL_INFUSED_DIAMOND, SoulType.DIAMOND);
        register(context, SOUL_INFUSED_LAPIS, SoulType.LAPIS);
        register(context, SOUL_INFUSED_AMETHYST, SoulType.AMETHYST);
        register(context, SOUL_INFUSED_NECTARIUM, SoulType.NECTARIUM);
        register(context, GOD_BLOOD, SoulType.GOD_BLOOD);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, SoulType type) {
        register(context, key, type.getColor());
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, int color) {
        register(context, key, Style.EMPTY.withColor(color), Map.of());
    }

    private static void register(
            BootstrapContext<TrimMaterial> bootstrapContext,
            ResourceKey<TrimMaterial> resourceKey,
            Style style,
            Map<ResourceKey<EquipmentAsset>, String> overrides
    ) {
        MaterialAssetGroup assets = MaterialAssetGroup.create(resourceKey.identifier().getPath(), overrides);
        Component component = Component.translatable(Util.makeDescriptionId("trim_material", resourceKey.identifier()))
                .withStyle(style);
        TrimMaterial trimMaterial = new TrimMaterial(assets, component);
        bootstrapContext.register(resourceKey, trimMaterial);
    }

    public static ResourceKey<TrimMaterial> create(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, NarakaMod.identifier(name));
    }
}
