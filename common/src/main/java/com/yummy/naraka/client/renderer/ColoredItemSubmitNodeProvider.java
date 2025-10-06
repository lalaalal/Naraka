package com.yummy.naraka.client.renderer;

import net.minecraft.client.renderer.SubmitNodeStorage;

import java.util.Map;

public interface ColoredItemSubmitNodeProvider {
    Map<SubmitNodeStorage.ItemSubmit, Integer> naraka$getColoredItemSubmits();
}
