package com.yummy.naraka.mixin;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTrader.class)
public abstract class WanderingTraderMixin extends AbstractVillager {
    @Unique
    private static final VillagerTrades.ItemListing[] naraka$TRADES = new VillagerTrades.ItemListing[]{
            naraka$itemsForEmeralds(NarakaItems.SANCTUARY_COMPASS.get(), 30, 1, 1, 1),
            naraka$itemsForEmeralds(NarakaBlocks.NECTARIUM_CORE_BLOCK.get(), 8, 1, 1, 1)
    };

    @Unique
    private static VillagerTrades.ItemListing naraka$itemsForEmeralds(ItemLike item, int emeraldCost, int numberOfItems, int maxUses, int villagerXp) {
        return (trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, emeraldCost),
                new ItemStack(item, numberOfItems),
                maxUses, villagerXp, 0.05f
        );
    }

    public WanderingTraderMixin(EntityType<? extends AbstractVillager> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updateTrades", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffers;add(Ljava/lang/Object;)Z"), cancellable = true)
    protected void addNarakaItemsToTrades(CallbackInfo ci) {
        if (random.nextFloat() < 0.1) {
            MerchantOffers merchantOffers = getOffers();
            int index = random.nextInt(naraka$TRADES.length);
            MerchantOffer merchantOffer = naraka$TRADES[index].getOffer(this, random);
            merchantOffers.add(merchantOffer);
            ci.cancel();
        }
    }
}
