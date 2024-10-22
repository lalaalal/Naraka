package com.yummy.naraka.util;

import net.minecraft.util.Mth;

public class ColorUtil {
    public static Color transform(Color from, Color to, double delta) {
        double red = Math.abs((delta * to.red()) + ((1 - delta) * from.red()));
        double green = Math.abs((delta * to.green()) + ((1 - delta) * from.green()));
        double blue = Math.abs((delta * to.blue()) + ((1 - delta) * from.blue()));

        return new Color(0, Mth.ceil(red), Mth.ceil(green), Mth.ceil(blue));
    }
}
