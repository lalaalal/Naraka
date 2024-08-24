package com.yummy.naraka.world.item.armortrim;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;

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

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, SOUL_INFUSED_REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE, 0xeb4747, 6.1f);
        register(context, SOUL_INFUSED_COPPER, NarakaItems.SOUL_INFUSED_COPPER, 0xff8000, 6.2f);
        register(context, SOUL_INFUSED_GOLD, NarakaItems.SOUL_INFUSED_GOLD, 0xffd24d, 6.3f);
        register(context, SOUL_INFUSED_EMERALD, NarakaItems.SOUL_INFUSED_EMERALD, 0x0ec70e, 6.4f);
        register(context, SOUL_INFUSED_DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND, 0x33cccc, 6.5f);
        register(context, SOUL_INFUSED_LAPIS, NarakaItems.SOUL_INFUSED_LAPIS, 0x3939c6, 6.6f);
        register(context, SOUL_INFUSED_AMETHYST, NarakaItems.SOUL_INFUSED_AMETHYST, 0x9957db, 6.7f);
        register(context, SOUL_INFUSED_NECTARIUM, NarakaItems.SOUL_INFUSED_NECTARIUM, 0xd65cd6, 6.8f);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, int color, float f) {
        register(context, key, item, color, f, Map.of());
    }

    private static void register(
            BootstrapContext<TrimMaterial> bootstrapContext,
            ResourceKey<TrimMaterial> resourceKey,
            Item item,
            int color,
            float itemModelIndex,
            Map<Holder<ArmorMaterial>, String> map
    ) {
        TrimMaterial trimMaterial = TrimMaterial.create(
                resourceKey.location().getPath(),
                item,
                itemModelIndex,
                Component.translatable(
                        Util.makeDescriptionId("trim_material", resourceKey.location())
                ).withStyle(Style.EMPTY.withColor(color)),
                map
        );
        bootstrapContext.register(resourceKey, trimMaterial);
    }

    private static ResourceKey<TrimMaterial> create(String name) {
        return ResourceKey.create(Registries.TRIM_MATERIAL, NarakaMod.location(name));
    }
}
