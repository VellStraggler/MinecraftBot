package org.mcbot;

import java.awt.image.BufferedImage;

/**
 * Doesn't bother with alpha values. Unmodifiable.
 */
public class RGB implements Comparable<RGB>{
    public static final int MID = 200;//127;
    public static final RGB WHITE = new RGB(255,255,255);
    public static final RGB F3_WHITE = new RGB(221, 221, 221);
    public static final RGB BLACK = new RGB(0,0,0);
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
    public boolean isWhite() {
        return r + g + b == 255 * 3;
    }
    public boolean equals(RGB o) {
        return (compareTo(o) == 0);
    }
    public int compareTo(RGB o) {
        if (r == o.r && g == o.g && b == o.b) {
            return 0;
        } else if (r+g+b > o.r+o.g+o.b) {
            return 1;
        } else return -1;
    }
    public boolean equals(BufferedImage image, int x, int y) {
        return equals(new RGB(image.getRGB(x,y)));
    }
    public String toString() {
        return "RGB: " + r + ", " + g + ", " + b;
    }
}
