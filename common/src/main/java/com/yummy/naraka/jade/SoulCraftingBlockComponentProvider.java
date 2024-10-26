package com.yummy.naraka.jade;

import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.block.entity.SoulCraftingBlockEntity;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.crafting.SoulCraftingRecipe;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.Optional;

public class SoulCraftingBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static final SoulCraftingBlockComponentProvider INSTANCE = new SoulCraftingBlockComponentProvider();

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof SoulCraftingBlockEntity soulCraftingBlockEntity) {
            compoundTag.putInt("Fuel", soulCraftingBlockEntity.getFuel());
            float progress = (float) soulCraftingBlockEntity.getCraftingProgress() / SoulCraftingBlockEntity.craftingTime();
            compoundTag.putFloat("Progress", progress);
            if (progress > 0) {
                RegistryAccess registries = blockAccessor.getLevel().registryAccess();
                ItemStack ingredient = soulCraftingBlockEntity.getItem(SoulCraftingBlockEntity.INGREDIENT_SLOT);
                compoundTag.put("Ingredient", NarakaNbtUtils.writeItem(registries, ingredient.copyWithCount(1)));

                Optional<RecipeHolder<SoulCraftingRecipe>> recipeHolder = SoulCraftingBlockEntity.getRecipeFor(blockAccessor.getLevel(), ingredient);
                if (recipeHolder.isPresent()) {
                    SoulCraftingRecipe recipe = recipeHolder.get().value();
                    ItemStack result = recipe.getResultItem(registries);
                    compoundTag.put("Result", NarakaNbtUtils.writeItem(registries, result.copyWithCount(1)));
                }
            }
        }
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        CompoundTag data = blockAccessor.getServerData();
        if (dataExists(data)) {
            RegistryAccess registries = blockAccessor.getLevel().registryAccess();
            IElementHelper elements = IElementHelper.get();
            IElement fuelIcon = elements.smallItem(new ItemStack(NarakaItems.PURIFIED_SOUL_SHARD));

            if (data.getInt("Fuel") > 0) {
                tooltip.add(fuelIcon);
                tooltip.append(Component.translatable(LanguageKey.JADE_SOUL_CRAFTING_FUEL_KEY, data.get("Fuel")));
            }

            if (itemExists(data)) {
                ItemStack ingredient = NarakaNbtUtils.readItem(registries, data.getCompound("Ingredient"));
                ItemStack result = NarakaNbtUtils.readItem(registries, data.getCompound("Result"));

                IElement progressElement = elements.progress(data.getFloat("Progress"));
                IElement ingredientIcon = elements.item(ingredient);
                IElement resultIcon = elements.item(result);
                IElement space = elements.spacer(10, 10);

                tooltip.add(ingredientIcon);
                tooltip.append(space);
                tooltip.append(progressElement);
                tooltip.append(space);
                tooltip.append(resultIcon);
            }
        }
    }

    private boolean dataExists(CompoundTag data) {
        return data.contains("Fuel") && data.contains("Progress");
    }

    private boolean itemExists(CompoundTag data) {
        return data.contains("Ingredient") && data.contains("Result");
    }

    @Override
    public ResourceLocation getUid() {
        return NarakaJadeProviderComponents.SOUL_CRAFTING_BLOCK.location;
    }
}
