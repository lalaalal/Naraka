package com.yummy.naraka.mixin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(ItemModelGenerators.class)
public abstract class ItemModelGeneratorsMixin {
    @Unique
    private static final List<ItemModelGenerators.TrimModelData> TRIM_MODELS = List.of(
            new ItemModelGenerators.TrimModelData("soul_infused_redstone", 6.1f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_copper", 6.2f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_gold", 6.3f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_emerald", 6.4f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_diamond", 6.5f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_lapis", 6.6f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_amethyst", 6.7f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_nectarium", 6.8f, Map.of())
    );

    @Shadow
    public abstract ResourceLocation getItemModelForTrimMaterial(ResourceLocation modelLocation, String trimId);

    @Shadow
    public abstract void generateLayeredItem(ResourceLocation modelLocation, ResourceLocation layer0, ResourceLocation layer1);

    @Shadow
    public abstract void generateLayeredItem(ResourceLocation modelLocation, ResourceLocation layer0, ResourceLocation layer1, ResourceLocation layer2);

    @Inject(method = "generateArmorTrims", at = @At("RETURN"))
    public void generateArmorTrims(ArmorItem armorItem, CallbackInfo ci) {
        ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(armorItem);
        ResourceLocation texture = TextureMapping.getItemTexture(armorItem);
        ResourceLocation textureOverlay = TextureMapping.getItemTexture(armorItem, "_overlay");

        for (ItemModelGenerators.TrimModelData trimModelData : TRIM_MODELS) {
            String materialName = trimModelData.name(armorItem.getMaterial());
            ResourceLocation trimMaterialModel = getItemModelForTrimMaterial(modelLocation, materialName);
            String armorName = armorItem.getType().getName();
            String trimmedArmorName = armorName + "_trim_" + materialName;
            ResourceLocation trimmedArmorModel = NarakaMod.location(trimmedArmorName).withPrefix("trims/items/");
            if (armorItem.getMaterial().is(ArmorMaterials.LEATHER.unwrapKey().orElseThrow())) {
                generateLayeredItem(trimMaterialModel, texture, textureOverlay, trimmedArmorModel);
            } else {
                generateLayeredItem(trimMaterialModel, texture, trimmedArmorModel);
            }
        }
    }

    @Inject(method = "generateBaseArmorTrimTemplate", at = @At("RETURN"))
    public final void generateBaseArmorTrimTemplate(ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter, Holder<ArmorMaterial> armorMaterial, CallbackInfoReturnable<JsonObject> cir) {
        JsonObject jsonObject = cir.getReturnValue();
        JsonArray overrides = jsonObject.get("overrides").getAsJsonArray();

        for (ItemModelGenerators.TrimModelData trimModelData : TRIM_MODELS) {
            JsonObject predicate = new JsonObject();
            JsonObject model = new JsonObject();
            model.addProperty(ItemModelGenerators.TRIM_TYPE_PREDICATE_ID.getPath(), trimModelData.itemModelIndex());
            predicate.add("predicate", model);
            predicate.addProperty("model", this.getItemModelForTrimMaterial(modelLocation, trimModelData.name(armorMaterial)).toString());
            overrides.add(predicate);
        }
    }
}
