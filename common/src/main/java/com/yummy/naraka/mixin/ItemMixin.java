package com.yummy.naraka.mixin;

import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.world.item.ItemDetailBuilder;
import com.yummy.naraka.world.item.ItemDetailProvider;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(Item.class)
public abstract class ItemMixin implements ItemDetailProvider {
    @Unique
    @Nullable
    private ItemEvents.ItemTooltip naraka$itemDetail;

    @Override
    public Optional<ItemEvents.ItemTooltip> naraka$getItemTooltip() {
        return Optional.ofNullable(naraka$itemDetail);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void loadDefaultTag(Item.Properties properties, CallbackInfo ci) {
        if (properties instanceof ItemDetailProvider provider)
            provider.naraka$getItemTooltip().ifPresent(itemDetail -> naraka$itemDetail = itemDetail);
    }

    @Mixin(Item.Properties.class)
    public abstract static class PropertiesMixin implements ItemDetailBuilder {
        @Unique
        @Nullable
        private ItemEvents.ItemTooltip naraka$itemDetail;

        @Override
        public Optional<ItemEvents.ItemTooltip> naraka$getItemTooltip() {
            return Optional.ofNullable(naraka$itemDetail);
        }

        @Override
        public ItemDetailBuilder naraka$setItemTooltip(ItemEvents.ItemTooltip itemDetail) {
            this.naraka$itemDetail = itemDetail;
            return this;
        }

        @Override
        public Item.Properties naraka$asItemProperties() {
            return (Item.Properties) (Object) this;
        }
    }
}
