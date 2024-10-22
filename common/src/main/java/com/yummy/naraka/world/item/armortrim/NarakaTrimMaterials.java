package com.yummy.naraka.world.item.armortrim;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.SoulType;
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
    public static final ResourceKey<TrimMaterial> GOD_BLOOD = create("god_blood");

    public static void bootstrap(BootstrapContext<TrimMaterial> context) {
        register(context, TrimMaterials.QUARTZ, Items.QUARTZ, 14931140, 0.052f);
        register(context, TrimMaterials.IRON, Items.IRON_INGOT, 15527148, 0.104f, Map.of(ArmorMaterials.IRON, "iron_darker"));
        register(context, TrimMaterials.NETHERITE, Items.NETHERITE_INGOT, 6445145, 0.156f, Map.of(ArmorMaterials.NETHERITE, "netherite_darker"));
        register(context, TrimMaterials.REDSTONE, Items.REDSTONE, 9901575, 0.208f);
        register(context, TrimMaterials.COPPER, Items.COPPER_INGOT, 11823181, 0.26f);
        register(context, TrimMaterials.GOLD, Items.GOLD_INGOT, 14594349, 0.312f, Map.of(ArmorMaterials.GOLD, "gold_darker"));
        register(context, TrimMaterials.EMERALD, Items.EMERALD, 1155126, 0.364f);
        register(context, TrimMaterials.DIAMOND, Items.DIAMOND, 7269586, 0.416f, Map.of(ArmorMaterials.DIAMOND, "diamond_darker"));
        register(context, TrimMaterials.LAPIS, Items.LAPIS_LAZULI, 4288151, 0.468f);
        register(context, TrimMaterials.AMETHYST, Items.AMETHYST_SHARD, 10116294, 0.52f);
        register(context, SOUL_INFUSED_REDSTONE, SoulType.REDSTONE, 0.572f);
        register(context, SOUL_INFUSED_COPPER, SoulType.COPPER, 0.624f);
        register(context, SOUL_INFUSED_GOLD, SoulType.GOLD, 0.676f);
        register(context, SOUL_INFUSED_EMERALD, SoulType.EMERALD, 0.728f);
        register(context, SOUL_INFUSED_DIAMOND, SoulType.DIAMOND, 0.780f);
        register(context, SOUL_INFUSED_LAPIS, SoulType.LAPIS, 0.832f);
        register(context, SOUL_INFUSED_AMETHYST, SoulType.AMETHYST, 0.884f);
        register(context, SOUL_INFUSED_NECTARIUM, SoulType.NECTARIUM, 0.935f);
        register(context, GOD_BLOOD, SoulType.GOD_BLOOD, 1.0f);
    }

    private static void register(BootstrapContext<TrimMaterial> context, ResourceKey<TrimMaterial> key, SoulType type, float itemModelIndex) {
        register(context, key, type.getItem(), type.getColor(), itemModelIndex);
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
