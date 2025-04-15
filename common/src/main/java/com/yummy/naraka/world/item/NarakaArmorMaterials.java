package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class NarakaArmorMaterials {
    public static final Holder<ArmorMaterial> PURIFIED_SOUL = register(
            "purified_soul",
            Map.of(ArmorItem.Type.BOOTS, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.BODY, 0),
            15, SoundEvents.ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.of(NarakaItems.PURIFIED_SOUL_METAL.get()),
            0, 0.25f
    );
    public static final Holder<ArmorMaterial> EBONY_METAL = register(
            "ebony_metal",
            Map.of(ArmorItem.Type.BOOTS, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.HELMET, 0,
                    ArmorItem.Type.BODY, 0),
            12, SoundEvents.ARMOR_EQUIP_GENERIC,
            () -> Ingredient.of(NarakaItems.EBONY_METAL_INGOT.get()),
            1, 0.1f
    );

    private static Holder<ArmorMaterial> register(String name,
                                                  Map<ArmorItem.Type, Integer> defense,
                                                  int enchantmentValue,
                                                  Holder<SoundEvent> equipSound,
                                                  Supplier<Ingredient> repairIngredient,
                                                  float toughness,
                                                  float knockbackResistance) {
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(NarakaMod.location(name)));
        return RegistryProxy.register(Registries.ARMOR_MATERIAL, name, () -> new ArmorMaterial(
                defense, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance
        ));
    }

    public static void initialize() {

    }
}
