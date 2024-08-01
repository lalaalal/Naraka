package com.yummy.naraka.jade;

import com.yummy.naraka.data.lang.NarakaJadeProviders;
import com.yummy.naraka.data.lang.NarakaLanguageProvider;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class EntityStigmaDataProvider implements IEntityComponentProvider {
    public static final EntityStigmaDataProvider INSTANCE = new EntityStigmaDataProvider();

    @Override
    public ResourceLocation getUid() {
        return NarakaJadeProviders.STIGMA.location;
    }

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
        Entity entity = entityAccessor.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            int stigma = EntityDataHelper.getEntityData(livingEntity, EntityDataTypes.STIGMA);
            if (stigma > 0)
                tooltip.add(Component.translatable(NarakaLanguageProvider.JADE_STIGMA_KEY, stigma));
        }
    }
}
