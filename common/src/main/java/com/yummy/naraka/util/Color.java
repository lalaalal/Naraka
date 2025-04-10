package com.yummy.naraka.util;

public record Color(int alpha, int red, int green, int blue) {
    public static Color of(int color) {
        int alpha = (color >> 24) & 0xFF;
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = (color) & 0xFF;

        return new Color(alpha, red, green, blue);
    }

    public static Color of(String hex) {
        String pure = hex.replaceAll("^(0x|#)", "");
        String rgb = pure.substring(pure.length() - 6);
        String alpha = pure.substring(0, pure.length() - 6);
        if (alpha.length() < 2)
            alpha = "00";
        return of(Integer.parseInt(rgb, 16))
                .withAlpha(Integer.parseInt(alpha, 16));
    }

    public float alpha01() {
        return alpha / 255f;
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
        return pack(true);
    }

    public int pack(boolean withAlpha) {
        int packed = red << 16 | green << 8 | blue;
        if (withAlpha)
            return packed | alpha << 24;
        return packed;
    }

    public Color withAlpha(int alpha) {
        return new Color(alpha, red, green, blue);
    }

    @Override
    public String toString() {
        if (alpha == 0)
            return "#%06x".formatted(pack(false));
        return "#%08x".formatted(pack());
    }
}
