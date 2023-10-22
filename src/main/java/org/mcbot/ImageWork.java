package org.mcbot;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageWork {

    public static BufferedImage convertImageToGrayscale(BufferedImage image) {
        return convertImage(image, BufferedImage.TYPE_BYTE_GRAY);
    }
    public static BufferedImage convertImageToReduced(BufferedImage image) {
        return convertImage(image, BufferedImage.TYPE_USHORT_555_RGB);
    }
    public static BufferedImage convertImageToBinary(BufferedImage image) {
        return convertImage(image, BufferedImage.TYPE_BYTE_BINARY);
    }
    private static BufferedImage convertImage(BufferedImage image, int imageType) {
        BufferedImage post = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        Graphics2D artist = post.createGraphics();
        artist.drawImage(image, 0, 0, null);
        artist.dispose();
        return post;
    }
    public static BufferedImage takeScreenshot() {
        try {

            Rectangle screenDimensions = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            return new Robot().createScreenCapture(screenDimensions);

        } catch (AWTException e) {
            System.out.println("Image grab failed: " + e + " " + e.getMessage());
            return null;
        }
    }

    /**
     * Save an image to the given path. PNG only. Do not include ".png" in the path parameter.
     * @param image
     * @param path
     */
    public static void saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path + ".png"));
        } catch (IOException e) {
            System.out.println("Unable to save image: " + e + " " + e.getMessage());
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

        // Save the 2-bit RGB image
        //ImageIO.write(twoBitRGBImage, "bmp", new File("screenshots/altered.bmp"));
        return twoBitRGBImage;

    }

    /* Quantize a component (e.g., red, green, or blue) to 2 bits (4 levels) */
    private static int quantizeComponent(int component) {
        // Adjust the quantization as needed to create 4 levels: 0, 85, 170, 255
        if (component < 85) {
            return 0;
        } else if (component < 170) {
            return 85;
        } else if (component < 255) {
            return 170;
        } else {
            return 255;
        }
    }
}