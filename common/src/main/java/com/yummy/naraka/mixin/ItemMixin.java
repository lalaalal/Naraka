package com.yummy.naraka.mixin;

import com.mojang.serialization.Codec;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.item.ItemDefaultNbtBuilder;
import com.yummy.naraka.world.item.ItemDefaultNbtProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemDefaultNbtProvider {
    @Unique
    @Nullable
    private CompoundTag naraka$defaultNbt;

    @Override
    public Optional<CompoundTag> naraka$getDefaultNbt() {
        if (naraka$defaultNbt == null)
            return Optional.empty();
        return Optional.of(naraka$defaultNbt.copy());
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadDefaultTag(Item.Properties properties, CallbackInfo ci) {
        if (properties instanceof ItemDefaultNbtProvider provider)
            provider.naraka$getDefaultNbt().ifPresent(itemDetail -> naraka$defaultNbt = itemDetail);
    }

    @Mixin(Item.Properties.class)
    public abstract static class PropertiesMixin implements ItemDefaultNbtBuilder {
        @Unique
        @Nullable
        private CompoundTag naraka$defaultNbt;

        @Override
        public Optional<CompoundTag> naraka$getDefaultNbt() {
            return Optional.ofNullable(naraka$defaultNbt);
        }

        @Override
        public <T> ItemDefaultNbtBuilder naraka$set(String key, Codec<T> codec, T value) {
            if (naraka$defaultNbt == null)
                naraka$defaultNbt = new CompoundTag();
            NarakaNbtUtils.store(naraka$defaultNbt, key, codec, value);
            return this;
        }

        @Override
        public ItemDefaultNbtBuilder naraka$set(String key, Tag tag) {
            if (naraka$defaultNbt == null)
                naraka$defaultNbt = new CompoundTag();
            naraka$defaultNbt.put(key, tag);
            return this;
        }

        @Override
        public Item.Properties naraka$asItemProperties() {
            return (Item.Properties) (Object) this;
        }
    }
}
