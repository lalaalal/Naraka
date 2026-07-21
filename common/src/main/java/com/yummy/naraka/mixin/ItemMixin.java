package com.yummy.naraka.mixin;

import com.yummy.naraka.world.item.DefaultItemTagBuilder;
import com.yummy.naraka.world.item.DefaultItemTagProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin implements DefaultItemTagProvider {
    @Unique
    @Nullable
    private CompoundTag naraka$defaultTag;

    @Override
    public Optional<CompoundTag> naraka$getDefaultTag() {
        return Optional.ofNullable(naraka$defaultTag);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadDefaultTag(Item.Properties properties, CallbackInfo ci) {
        if (properties instanceof DefaultItemTagProvider tagProvider)
            tagProvider.naraka$getDefaultTag().ifPresent(compoundTag -> naraka$defaultTag = compoundTag);
    }

    @Mixin(Item.Properties.class)
    public abstract static class PropertiesMixin implements DefaultItemTagBuilder {
        @Unique
        @Nullable
        private CompoundTag naraka$defaultTag;

        @Override
        public void naraka$setDefaultTag(CompoundTag tag) {
            this.naraka$defaultTag = tag;
        }

        @Override
        public Item.Properties naraka$asItemProperties() {
            return (Item.Properties) (Object) this;
        }

        @Override
        public Optional<CompoundTag> naraka$getDefaultTag() {
            return Optional.ofNullable(naraka$defaultTag);
        }
    }
}
