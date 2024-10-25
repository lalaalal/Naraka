package com.yummy.naraka.core.registries;

import net.minecraft.core.Holder;

public class HolderProxy<R, T extends R> {
    private final Holder<R> holder;

    public HolderProxy(Holder<R> holder) {
        this.holder = holder;
    }
}
