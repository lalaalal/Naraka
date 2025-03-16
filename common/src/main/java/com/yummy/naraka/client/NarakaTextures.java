package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class NarakaTextures {
    public static final ResourceLocation HEROBRINE = entity("herobrine.png");
    public static final ResourceLocation SHADOW_HEROBRINE = entity("shadow_herobrine.png");
    public static final ResourceLocation HEROBRINE_EYE = entity("herobrine_eye.png");
    public static final ResourceLocation HEROBRINE_AFTERIMAGE = entity("herobrine_afterimage.png");

    public static final ResourceLocation LONGINUS = entity("longinus.png");

    public static final ResourceLocation SPEAR = entity("spear.png");
    public static final ResourceLocation MIGHTY_HOLY_SPEAR = entity("mighty_holy_spear.png");

    public static final ResourceLocation NARAKA_FIREBALL = entity("naraka_fireball.png");

    public static final ResourceLocation STARDUST = entity("stardust_core.png");

    public static final ResourceLocation FORGING_BLOCK = entity("forging_block.png");
    public static final ResourceLocation SOUL_STABILIZER = entity("soul_stabilizer.png");
    public static final ResourceLocation SOUL_SMITHING_BLOCK = entity("soul_smithing_block.png");

    public static final ResourceLocation SOUL_CRAFTING = gui("container/soul_crafting.png");

    public static final ResourceLocation NARAKA_ADVANCEMENT_ROOT_BACKGROUND = gui("advancements/backgrounds/naraka.png");

    private static ResourceLocation texture(String parent, String path) {
        return NarakaMod.location("textures/%s".formatted(parent), path);
    }

    private static ResourceLocation entity(String path) {
        return texture("entity", path);
    }

    private static ResourceLocation gui(String path) {
        return texture("gui", path);
    }

    private static final Map<Item, ResourceLocation> TRIM_TEMPLATE_TEXTURES = new HashMap<>();

    public static ResourceLocation getTemplateTexture(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (TRIM_TEMPLATE_TEXTURES.containsKey(item))
            return TRIM_TEMPLATE_TEXTURES.get(item);

        String trimName = BuiltInRegistries.ITEM.getKey(item)
                .getPath()
                .replace("_smithing_template", "")
                .replace("_armor_trim", "");
        ResourceLocation trimTemplateLocation = entity("trims/" + trimName + ".png");
        TRIM_TEMPLATE_TEXTURES.put(item, trimTemplateLocation);
        return trimTemplateLocation;
    }
}
