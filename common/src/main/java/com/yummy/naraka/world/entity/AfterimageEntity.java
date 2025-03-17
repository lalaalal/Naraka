package com.yummy.naraka.world.entity;

import java.util.List;

public interface AfterimageEntity {
    void addAfterimage(Afterimage afterimage);

    default void addAfterimage(Afterimage afterimage, int count, boolean include) {
        if (getAfterimages().isEmpty()) {
            addAfterimage(afterimage);
            return;
        }
        Afterimage previous = getAfterimages().getLast();
        for (int i = 1; i < count; i++) {
            float partialTick = 1f / count * i;
            addAfterimage(Afterimage.lerp(partialTick, previous, afterimage));
        }

        if (include)
            addAfterimage(afterimage);
    }

    List<Afterimage> getAfterimages();
}
