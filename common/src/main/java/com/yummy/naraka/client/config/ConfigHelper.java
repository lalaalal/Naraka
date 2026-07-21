package com.yummy.naraka.client.config;

import com.yummy.naraka.config.Configuration;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import net.minecraft.network.chat.Component;

public final class ConfigHelper {
    public static <T, E extends AbstractConfigListEntry<T>, B extends AbstractFieldBuilder<T, E, B>> B entry(Configuration.ConfigValue<T> config, FieldBuilderGetter<T, E, B> getter) {
        return getter.get(config.getComponent(), config.getValue())
                .setTooltip(config.getCommentComponent().toArray(Component[]::new))
                .setDefaultValue(config.getDefaultValue())
                .setSaveConsumer(config::set);
    }

    public interface FieldBuilderGetter<T, E extends AbstractConfigListEntry<T>, B extends AbstractFieldBuilder<T, E, B>> {
        B get(Component component, T value);
    }
}
