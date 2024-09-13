package com.yummy.naraka.jade;

import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.data.lang.NarakaLanguageProviders;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class SoulStabilizerComponentProvider implements IBlockComponentProvider {
    public static final SoulStabilizerComponentProvider INSTANCE = new SoulStabilizerComponentProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig pluginConfig) {
        if (blockAccessor.getBlockEntity() instanceof SoulStabilizerBlockEntity soulStabilizerBlockEntity) {
            SoulType type = soulStabilizerBlockEntity.getSoulType();
            int souls = soulStabilizerBlockEntity.getSouls();
            if (type != null) {
                tooltip.add(Component.translatable(type.translationKey()));
                tooltip.append(Component.literal(": "));
                tooltip.append(Component.translatable(NarakaLanguageProviders.JADE_SOUL_STABILIZER_KEY, souls));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return NarakaJadeProviderComponents.SOUL_STABILIZER.location;
    }
}
