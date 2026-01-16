package com.yummy.naraka.jade;

import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.world.block.NectariumCoreBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class NectariumCoreComponentProvider implements IBlockComponentProvider {
    public static final NectariumCoreComponentProvider INSTANCE = new NectariumCoreComponentProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockState state = blockAccessor.getBlockState();
        int honey = state.getValue(NectariumCoreBlock.HONEY);
        tooltip.add(Component.translatable(activationStateKey(honey)));
        tooltip.append(Component.literal(" "));
        if (honey > 0)
            tooltip.append(Component.translatable(LanguageKey.JADE_NECTARIUM_CORE_HONEY_KEY, honey));
    }

    private String activationStateKey(int honey) {
        if (honey > 0)
            return LanguageKey.JADE_NECTARIUM_CORE_ACTIVATED_KEY;
        return LanguageKey.JADE_NECTARIUM_CORE_INACTIVATED_KEY;
    }

    @Override
    public Identifier getUid() {
        return NarakaJadeProviderComponents.NECTARIUM_CORE.location;
    }
}
