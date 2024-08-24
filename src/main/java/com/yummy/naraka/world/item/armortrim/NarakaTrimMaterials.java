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
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;

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
        register(context, TrimMaterials.QUARTZ, Items.QUARTZ, 14931140, 0.055f);
        register(context, TrimMaterials.IRON, Items.IRON_INGOT, 15527148, 0.110f, Map.of(ArmorMaterials.IRON, "iron_darker"));
        register(context, TrimMaterials.NETHERITE, Items.NETHERITE_INGOT, 6445145, 0.165f, Map.of(ArmorMaterials.NETHERITE, "netherite_darker"));
        register(context, TrimMaterials.REDSTONE, Items.REDSTONE, 9901575, 0.220f);
        register(context, TrimMaterials.COPPER, Items.COPPER_INGOT, 11823181, 0.275f);
        register(context, TrimMaterials.GOLD, Items.GOLD_INGOT, 14594349, 0.330f, Map.of(ArmorMaterials.GOLD, "gold_darker"));
        register(context, TrimMaterials.EMERALD, Items.EMERALD, 1155126, 0.385f);
        register(context, TrimMaterials.DIAMOND, Items.DIAMOND, 7269586, 0.440f, Map.of(ArmorMaterials.DIAMOND, "diamond_darker"));
        register(context, TrimMaterials.LAPIS, Items.LAPIS_LAZULI, 4288151, 0.495f);
        register(context, TrimMaterials.AMETHYST, Items.AMETHYST_SHARD, 10116294, 0.550f);
        register(context, SOUL_INFUSED_REDSTONE, NarakaItems.SOUL_INFUSED_REDSTONE, 0xeb4747, 0.605f);
        register(context, SOUL_INFUSED_COPPER, NarakaItems.SOUL_INFUSED_COPPER, 0xff8000, 0.660f);
        register(context, SOUL_INFUSED_GOLD, NarakaItems.SOUL_INFUSED_GOLD, 0xffd24d, 0.715f);
        register(context, SOUL_INFUSED_EMERALD, NarakaItems.SOUL_INFUSED_EMERALD, 0x0ec70e, 0.770f);
        register(context, SOUL_INFUSED_DIAMOND, NarakaItems.SOUL_INFUSED_DIAMOND, 0x33cccc, 0.825f);
        register(context, SOUL_INFUSED_LAPIS, NarakaItems.SOUL_INFUSED_LAPIS, 0x3939c6, 0.880f);
        register(context, SOUL_INFUSED_AMETHYST, NarakaItems.SOUL_INFUSED_AMETHYST, 0x9957db, 0.935f);
        register(context, SOUL_INFUSED_NECTARIUM, NarakaItems.SOUL_INFUSED_NECTARIUM, 0xd65cd6, 1.0f);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, Item item, int color, float itemModelIndex) {
        register(context, key, item, color, itemModelIndex, Map.of());
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
