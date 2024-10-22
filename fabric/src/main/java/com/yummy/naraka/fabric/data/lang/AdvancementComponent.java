package com.yummy.naraka.fabric.data.lang;

import net.minecraft.network.chat.Component;

public interface AdvancementComponent {
    String rootName();

    String advancementName();

    default String titleKey() {
        return title(rootName(), advancementName());
    }

    default String descriptionKey() {
        return description(rootName(), advancementName());
    }

    default Component title() {
        return Component.translatable(titleKey());
    }

    default Component description() {
        return Component.translatable(descriptionKey());
    }

    static String title(String parent, String title) {
        return "advancements." + parent + "." + title + ".title";
    }

    static String description(String parent, String description) {
        return "advancements." + parent + "." + description + ".description";
    }
}
