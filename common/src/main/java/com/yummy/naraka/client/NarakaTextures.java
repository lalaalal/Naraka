package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class NarakaTextures {
    public static final ResourceLocation HEROBRINE = entity("herobrine/herobrine.png");
    public static final ResourceLocation ABSOLUTE_HEROBRINE = entity("herobrine/absolute_herobrine.png");
    public static final ResourceLocation FINAL_HEROBRINE = entity("herobrine/final_herobrine.png");
    public static final ResourceLocation HEROBRINE_EYE = entity("herobrine/herobrine_eye.png");
    public static final ResourceLocation HEROBRINE_AFTERIMAGE = entity("herobrine/herobrine_afterimage.png");
    public static final ResourceLocation HEROBRINE_SCARF = entity("herobrine/herobrine_scarf.png");

    public static final ResourceLocation FINAL_HEROBRINE_SCARF = entity("herobrine/final_herobrine.png");
    public static final ResourceLocation FINAL_HEROBRINE_EYE = entity("herobrine/final_herobrine_eye.png");

    public static final ResourceLocation DIAMOND_GOLEM = entity("diamond_golem.png");

    public static final ResourceLocation SHADOW_HEROBRINE = entity("shadow_herobrine/shadow_herobrine.png");
    public static final ResourceLocation SHADOW_HEROBRINE_HEAD = entity("shadow_herobrine/shadow_herobrine_head.png");
    public static final ResourceLocation SHADOW_HEROBRINE_SCARF = entity("shadow_herobrine/shadow_herobrine_scarf.png");
    public static final ResourceLocation SHADOW_HEROBRINE_75 = entity("shadow_herobrine/shadow_herobrine_crack_75.png");
    public static final ResourceLocation SHADOW_HEROBRINE_50 = entity("shadow_herobrine/shadow_herobrine_crack_50.png");
    public static final ResourceLocation SHADOW_HEROBRINE_25 = entity("shadow_herobrine/shadow_herobrine_crack_25.png");

    public static final ResourceLocation ECLIPSE = texture("environment", "eclipse.png");

    public static final ResourceLocation SHADOW_ARMOR = entity("shadow_armor.png");

    public static final ResourceLocation LONGINUS = entity("longinus.png");
    public static final ResourceLocation SPEAR = entity("spear.png");
    public static final ResourceLocation MIGHTY_HOLY_SPEAR = entity("mighty_holy_spear.png");

    public static final ResourceLocation NARAKA_FIREBALL = entity("naraka_fireball.png");

    public static final ResourceLocation STARDUST = entity("stardust_core.png");
    public static final ResourceLocation MAGIC_CIRCLE = entity("magic_circle.png");
    public static final ResourceLocation PICKAXE_SLASH = entity("pickaxe_slash.png");

    public static final ResourceLocation SOUL_STABILIZER = entity("soul_stabilizer.png");
    public static final ResourceLocation SOUL_SMITHING_BLOCK = entity("soul_smithing_block.png");
    public static final ResourceLocation LIGHTNING_CIRCLE = entity("lightning_circle.png");

    public static final ResourceLocation NARAKA_ADVANCEMENT_ROOT_BACKGROUND = advancements("backgrounds/naraka");

    private static ResourceLocation texture(String parent, String path) {
        return NarakaMod.location("textures/%s".formatted(parent), path);
    }

    private static ResourceLocation entity(String path) {
        return texture("entity", path);
    }

    private static ResourceLocation advancements(String path) {
        return NarakaMod.location("gui/advancements", path);
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
