package org.bookreader;

import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) throws InterruptedException {
        BookReader reader = new BookReader();
        Thread.sleep(4000);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(reader.readThisPage()).append("\n");
        while (reader.hasNext()) {
            stringBuilder.append(reader.readThisPage()).append("\n");
        }
        reader.saveBook(stringBuilder.toString());
    }
}
