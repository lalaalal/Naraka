package com.yummy.naraka.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Style;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Component styles
 *
 * @author lalaalal
 */
@Environment(EnvType.CLIENT)
public class ComponentStyles {
    public static final ColorTransforming LONGINUS_COLOR = new ColorTransforming(
            Stream.of(0xB02E26, 0xF9801D, 0xFED83D, 0x80C71F, 0x169C9C, 0x3C44AA, 0x8932B8, 0xC74EBD)
                    .map(Color::of).toList(),
            10
    );
    private static long tickCount = 0;

    public static void tick() {
        LONGINUS_COLOR.updateColor();

        tickCount++;
    }

    public static class ColorTransforming implements UnaryOperator<Style> {
        private final List<Color> colors;
        private final long transformTime;
        private long lastChangedTime;
        private Color previousColor;
        private Color currentColor;

        public ColorTransforming(List<Color> colors, long transformTime) {
            this.colors = colors;
            this.transformTime = transformTime;
            previousColor = colors.getFirst();
            currentColor = previousColor;
            lastChangedTime = tickCount;
        }

        public Color next() {
            int index = colors.indexOf(previousColor);
            return colors.get((index + 1) % colors.size());
        }

        public void updateColor() {
            if (tickCount - lastChangedTime >= transformTime) {
                previousColor = next();
                lastChangedTime = tickCount;
            }
            Color nextColor = next();
            double delta = (tickCount - lastChangedTime) / (double) transformTime;
            currentColor = ColorUtil.transform(previousColor, nextColor, delta);
        }

        public Color getCurrentColor() {
            return currentColor;
        }

        @Override
        public Style apply(Style style) {
            return style.withColor(currentColor.pack());
        }
    }
}
