package io.github.alexeychurchill.rgbcolorpicker.utils;

import java.util.Locale;

/**
 * Color picker utils
 */

public class ColorPickerUtils {
    /**
     * Represents color as a web color
     * @param color Color in integer type (bytes: AA RR GG BB)
     * @return String with a web-color
     */
    public static String toWebColor (int color) {
        return toWebColor(color, false);
    }

    /**
     * Represents color as a web color (in a hexadecimal format)
     * @param color Color in integer type (bytes: AA RR GG BB)
     * @param hasAlpha Is needed to show alpha channel (byte AA)
     * @return String with a web-color
     */
    public static String toWebColor(int color, boolean hasAlpha) {
        color &= (hasAlpha) ? 0xFFFFFFFF : 0xFFFFFF;
        String formatter = (hasAlpha) ? "#%08X" : "#%06X";
        return String.format(Locale.getDefault(), formatter, color);
    }
}
