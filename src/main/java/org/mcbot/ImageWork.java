package org.mcbot;

import org.mcbot.datatypes.RGB;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageWork {

    public static final int[] quantizedHuesOld = new int[]{0, 85, 170, 255};
    // increment from 0 to 255 by increments of 255/4, creating 5 sections
    // these \/ are the numbers in the middles of those 5 sections
    // or more simply, the first 4 sections starts + 32
    public static final int[] quantizedHues =    new int[]{32, 96, 159, 223};
    public static final int[] quantizedSection = new int[]{64, 128, 191, 255};
    public static final Rectangle screenDimensions = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    public static final Robot screenGrabber;
    static {
        try {
            screenGrabber = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static RGB getRGB(BufferedImage image, int x, int y) {
        return new RGB(image.getRGB(x, y));
    }
    public static BufferedImage takeScreenshot() {
        return screenGrabber.createScreenCapture(screenDimensions);
    }

    /**
     * Save an image to the given path in PNG format. Do not include ".png" in the path parameter.
     * Deprecated: Did use BMP format.
     * @param image
     * @param path
     */
    public static boolean saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path + ".png"));
            return true;
        } catch (IOException e) {
            Utils.p("Unable to save image: " + e + " " + e.getMessage());
            return false;
        }
    }
    public static BufferedImage retrieveImage(String path) {
        try {
            return ImageIO.read(new File(path + ".png"));
        } catch (IOException e) {
            Utils.p("Unable to read image with path: " + path);
            return null;
        }
    }

    public static BufferedImage convertImageToGrayscale(BufferedImage image) {
        return convertImage(image, BufferedImage.TYPE_BYTE_GRAY);
    }
    /* Reduces the quality of the image by limiting the number of bytes used to store RGB values */
    public static BufferedImage convertImageToReduced(BufferedImage image) {
        return convertImage(image, BufferedImage.TYPE_USHORT_555_RGB);
    }
    /* Reduces the image to only two colors: black and white */
    public static BufferedImage convertImageToBinary(BufferedImage image) {
        return convertImage(image, BufferedImage.TYPE_BYTE_BINARY);
    }
    public static BufferedImage convertImageTo4Level(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Create a new BufferedImage for the 2-bit RGB image
        BufferedImage twoBitRGBImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);

        // Iterate through each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);

                // Extract the individual RGB components
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Quantize the RGB values to 2 bits each (4 levels)
                int quantizedRed = quantizeComponent(red);
                int quantizedGreen = quantizeComponent(green);
                int quantizedBlue = quantizeComponent(blue);

                // Combine the quantized RGB components
                int quantizedRGB = (quantizedRed << 16) | (quantizedGreen << 8) | quantizedBlue;

                // Set the pixel value in the twoBitRGBImage
                twoBitRGBImage.setRGB(x, y, quantizedRGB);
            }
        }
        return twoBitRGBImage;
    }
    private static BufferedImage convertImage(BufferedImage image, int imageType) {
        BufferedImage post = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        Graphics2D artist = post.createGraphics();
        artist.drawImage(image, 0, 0, null);
        artist.dispose();
        return post;
    }

    /* Quantize a component (e.g., red, green, or blue) to 2 bits (4 levels).
    *  Used in convertImageTo4Level() */
    private static int quantizeComponent(int component) {
        // Adjust the quantization as needed to create 4 levels
        if (component < quantizedSection[0]) {
            return quantizedHues[0];
        } else if (component < quantizedSection[1]) {
            return quantizedHues[1];
        } else if (component < quantizedSection[2]) {
            return quantizedHues[2];
        } else {
            return quantizedHues[3];
        }
    }
}