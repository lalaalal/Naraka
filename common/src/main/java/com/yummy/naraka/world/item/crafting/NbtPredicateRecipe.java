package com.yummy.naraka.world.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class NbtPredicateRecipe implements CraftingRecipe {
    private static final Logger LOG = LogUtils.getLogger();

    public static final MapCodec<NbtPredicateRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(NbtPredicateRecipe::getId),
                    Codec.STRING.fieldOf("group").forGetter(NbtPredicateRecipe::getGroup),
                    CraftingBookCategory.CODEC.fieldOf("group").forGetter(NbtPredicateRecipe::category),
                    ResourceLocation.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Codec.INT.fieldOf("count").forGetter(recipe -> recipe.count),
                    RecipeSlot.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients)
            ).apply(instance, NbtPredicateRecipe::new)
    );

    private final ResourceLocation id;
    private final String group;
    private final CraftingBookCategory category;
    private final ResourceLocation result;
    private final int count;
    private final List<RecipeSlot> ingredients;

    public NbtPredicateRecipe(ResourceLocation id, String group, CraftingBookCategory category, ResourceLocation result, int count, List<RecipeSlot> ingredients) {
        this.id = id;
        this.group = group;
        this.category = category;
        this.result = result;
        this.count = count;
        this.ingredients = ingredients;
    }

    @Override
    public CraftingBookCategory category() {
        return category;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        for (RecipeSlot slot : ingredients) {
            if (!slot.matches(container))
                return false;
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return getResultItem(registryAccess).copyWithCount(count);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width <= 3 && height <= 3;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        HolderGetter<Item> items = registryAccess.lookupOrThrow(Registries.ITEM);
        return items.get(ResourceKey.create(Registries.ITEM, result))
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return NarakaRecipeSerializers.NBT_PREDICATE_RECIPE.value();
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients.stream().map(slot -> {
                    ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(slot.itemId));
                    itemStack.setTag(slot.tag.copy());
                    return Ingredient.of(itemStack);
                })
                .collect(Collectors.toCollection(NonNullList::create));
    }

    public List<RecipeSlot> getRecipeSlots() {
        return ingredients;
    }

    public record RecipeSlot(int slot, ResourceLocation itemId, CompoundTag tag) {
        public static final Codec<RecipeSlot> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.INT.fieldOf("slot").forGetter(RecipeSlot::slot),
                        ResourceLocation.CODEC.fieldOf("item_id").forGetter(RecipeSlot::itemId),
                        CompoundTag.CODEC.fieldOf("tag").forGetter(RecipeSlot::tag)
                ).apply(instance, RecipeSlot::new)
        );

        public boolean matches(CraftingContainer container) {
            ItemStack itemStack = container.getItem(slot);
            CompoundTag compoundTag = itemStack.getTag();
            if (compoundTag == null)
                return false;
            for (String key : tag.getAllKeys()) {
                if (!compoundTag.contains(key))
                    return false;
                Tag element = tag.get(key);
                if (element == null || !element.equals(compoundTag.get(key)))
                    return false;
            }
            return itemStack.getItemHolder().is(itemId);
        }
    }

    public static class Serializer implements RecipeSerializer<NbtPredicateRecipe> {
        @Override
        public NbtPredicateRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            DataResult<MapLike<JsonElement>> mapResult = JsonOps.INSTANCE.getMap(serializedRecipe);
            DataResult<NbtPredicateRecipe> result = NbtPredicateRecipe.MAP_CODEC.decode(
                    JsonOps.INSTANCE,
                    mapResult.getOrThrow(false, LOG::warn)
            );
            return result.getOrThrow(false, LOG::warn);
        }

        @Override
        public NbtPredicateRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return buffer.readJsonWithCodec(NbtPredicateRecipe.MAP_CODEC.codec());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, NbtPredicateRecipe recipe) {
            buffer.writeJsonWithCodec(NbtPredicateRecipe.MAP_CODEC.codec(), recipe);
        }
    }
}
