package org.bookreader;

/**
 * Does not bother with alpha values.
 */
public class RGB {
    public static final int MID = 200;//127;
    public final int r;
    public final int g;
    public final int b;

    public RGB(int bufferedImageRgbInt) {
        r = (bufferedImageRgbInt & 0xff0000) >> 16;
        g = (bufferedImageRgbInt & 0xff00) >> 8;
        b = bufferedImageRgbInt & 0xff;
    }
    public RGB(int r, int b, int g) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public boolean isDark() {
        return r + g + b < MID * 3;
    }
}
