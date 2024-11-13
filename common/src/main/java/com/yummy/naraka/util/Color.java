package com.yummy.naraka.util;

public record Color(int alpha, int red, int green, int blue) {
    public static Color of(int color) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = (color) & 0xFF;

        return new Color(alpha, red, green, blue);
    }

    public float red01() {
        return red / 255f;
    }

    public float green01() {
        return green / 255f;
    }

    public float blue01() {
        return blue / 255f;
    }

    public int pack() {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public int withAlpha(int alpha) {
        return new Color(alpha, red, green, blue).pack();
    }
}
