package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class NarakaTextures {
    public static final Identifier LOCATION_BLOCKS = NarakaMod.mcLocation("textures/atlas/blocks.png");

    public static final Identifier HEROBRINE = entity("herobrine/herobrine.png");
    public static final Identifier ABSOLUTE_HEROBRINE = entity("herobrine/absolute_herobrine.png");
    public static final Identifier FINAL_HEROBRINE = entity("herobrine/final_herobrine.png");
    public static final Identifier HEROBRINE_EYE = entity("herobrine/herobrine_eye.png");
    public static final Identifier HEROBRINE_AFTERIMAGE = entity("herobrine/herobrine_afterimage.png");
    public static final Identifier HEROBRINE_SCARF = entity("herobrine/herobrine_scarf.png");

    public static final Identifier FINAL_HEROBRINE_SCARF = entity("herobrine/final_herobrine.png");
    public static final Identifier FINAL_HEROBRINE_EYE = entity("herobrine/final_herobrine_eye.png");

    public static final Identifier DIAMOND_GOLEM = entity("diamond_golem.png");

    public static final Identifier SHADOW_HEROBRINE = entity("shadow_herobrine/shadow_herobrine.png");
    public static final Identifier SHADOW_HEROBRINE_HEAD = entity("shadow_herobrine/shadow_herobrine_head.png");
    public static final Identifier SHADOW_HEROBRINE_SCARF = entity("shadow_herobrine/shadow_herobrine_scarf.png");
    public static final Identifier SHADOW_HEROBRINE_75 = entity("shadow_herobrine/shadow_herobrine_crack_75.png");
    public static final Identifier SHADOW_HEROBRINE_50 = entity("shadow_herobrine/shadow_herobrine_crack_50.png");
    public static final Identifier SHADOW_HEROBRINE_25 = entity("shadow_herobrine/shadow_herobrine_crack_25.png");

    public static final Identifier ECLIPSE = texture("environment", "eclipse.png");
    public static final Identifier INVERTED_ECLIPSE = texture("environment", "inverted_eclipse.png");

    public static final Identifier SPACE = entity("space.png");

    public static final Identifier LONGINUS = entity("longinus.png");
    public static final Identifier SPEAR = entity("spear.png");
    public static final Identifier MIGHTY_HOLY_SPEAR = entity("mighty_holy_spear.png");

    public static final Identifier NARAKA_FIREBALL = entity("naraka_fireball.png");

    public static final Identifier STARDUST = entity("stardust_core.png");
    public static final Identifier MAGIC_CIRCLE = entity("magic_circle.png");
    public static final Identifier PICKAXE_SLASH = entity("pickaxe_slash.png");
    public static final Identifier NARAKA_SWORD = entity("naraka_sword.png");

    public static final Identifier SOUL_STABILIZER = entity("soul_stabilizer.png");
    public static final Identifier SOUL_SMITHING_BLOCK = entity("soul_smithing_block.png");
    public static final Identifier LIGHTNING_CIRCLE = entity("lightning_circle.png");

    public static final Identifier NARAKA_ADVANCEMENT_ROOT_BACKGROUND = advancements("backgrounds/naraka");

    public static final Identifier NARAKA_PORTAL_1 = entity("naraka_portal/naraka_portal_waxing_crescent.png");
    public static final Identifier NARAKA_PORTAL_2 = entity("naraka_portal/naraka_portal_first_quarter.png");
    public static final Identifier NARAKA_PORTAL_3 = entity("naraka_portal/naraka_portal_waxing_gibbous.png");

    private static Identifier texture(String parent, String path) {
        return NarakaMod.location("textures/%s".formatted(parent), path);
    }

    private static Identifier entity(String path) {
        return texture("entity", path);
    }

    private static Identifier advancements(String path) {
        return NarakaMod.location("gui/advancements", path);
    }

    private static final Map<Item, Identifier> TRIM_TEMPLATE_TEXTURES = new HashMap<>();

    public static Identifier getTemplateTexture(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (TRIM_TEMPLATE_TEXTURES.containsKey(item))
            return TRIM_TEMPLATE_TEXTURES.get(item);

        String trimName = BuiltInRegistries.ITEM.getKey(item)
                .getPath()
                .replace("_smithing_template", "")
                .replace("_armor_trim", "");
        Identifier trimTemplateLocation = entity("trims/" + trimName + ".png");
        TRIM_TEMPLATE_TEXTURES.put(item, trimTemplateLocation);
        return trimTemplateLocation;
    }
}
