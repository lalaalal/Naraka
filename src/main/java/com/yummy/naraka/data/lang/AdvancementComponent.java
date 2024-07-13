package com.yummy.naraka.data.lang;

import net.minecraft.network.chat.Component;

public interface AdvancementComponent {
    String titleKey();

    String descriptionKey();

    Component title();

    Component description();

    static String title(String parent, String title) {
        return "advancements." + parent + "." + title + ".title";
    }

    static String description(String parent, String description) {
        return "advancements." + parent + "." + description + ".description";
    }
}
