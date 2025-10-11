package com.yummy.naraka.jade;

import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

public class SoulSmithingBlockComponentProvider implements IBlockComponentProvider {
    public static final SoulSmithingBlockComponentProvider INSTANCE = new SoulSmithingBlockComponentProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof SoulSmithingBlockEntity soulSmithingBlockEntity) {
            tooltip.add(JadeUI.spacer(0, 1));
            ItemStack templateItem = soulSmithingBlockEntity.getTemplateItem();
            if (!templateItem.isEmpty())
                tooltip.append(JadeUI.smallItem(templateItem));

            SoulType type = soulSmithingBlockEntity.getSoulType();
            if (!templateItem.isEmpty())
                tooltip.append(Component.literal(" / "));
            int souls = soulSmithingBlockEntity.getSouls();
            tooltip.append(Component.translatable(type.translationKey()));
            tooltip.append(Component.literal(": "));
            tooltip.append(Component.translatable(LanguageKey.JADE_SOUL_STABILIZER_KEY, souls));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return NarakaJadeProviderComponents.SOUL_SMITHING_BLOCK.location;
    }
}
