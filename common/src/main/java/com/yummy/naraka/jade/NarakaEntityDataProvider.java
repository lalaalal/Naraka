package com.yummy.naraka.jade;

import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.world.entity.data.DeathCountHelper;
import com.yummy.naraka.world.entity.data.Stigma;
import com.yummy.naraka.world.entity.data.StigmaHelper;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

public class NarakaEntityDataProvider implements IEntityComponentProvider {
    public static final NarakaEntityDataProvider INSTANCE = new NarakaEntityDataProvider();

    @Override
    public Identifier getUid() {
        return NarakaJadeProviderComponents.ENTITY_DATA.location;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
        Entity entity = entityAccessor.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            Stigma stigma = StigmaHelper.get(livingEntity);
            if (stigma.value() > 0) {
                tooltip.add(JadeUI.smallItem(NarakaItems.STIGMA_ROD.get().getDefaultInstance()));
                tooltip.append(Component.translatable(LanguageKey.JADE_STIGMA_KEY, stigma.value()));
            }
            if (DeathCountHelper.isDeathCounted(livingEntity))
                tooltip.add(Component.translatable(LanguageKey.JADE_DEATH_COUNT_KEY, DeathCountHelper.get(livingEntity)));
        }
    }
}
