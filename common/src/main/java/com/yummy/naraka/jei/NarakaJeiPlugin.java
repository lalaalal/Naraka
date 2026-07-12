package com.yummy.naraka.jei;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.crafting.ComponentPredicateRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class NarakaJeiPlugin implements IModPlugin {
    @Override
    public Identifier getPluginUid() {
        return NarakaMod.identifier("jei_plugin");
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory()
                .addExtension(ComponentPredicateRecipe.class, new ComponentPredicateCraftingExtension());
    }
}
