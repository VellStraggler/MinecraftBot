package org.mcbot;

import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) {
        //readOneBook();
        BufferedImage image = Utils.retrieveImage("screenshots/F3-DONT-DELETE");
        F3Data data = new F3DataReader(image).readScreen();
        p(data.toString());
    }
    public static void readOneBook() {
        BookReader reader = new BookReader();
        Utils.sleep(4000);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(reader.readThisPage()).append("\n");
        while (reader.hasNext()) {
            stringBuilder.append(reader.readThisPage()).append("\n");
        }
        reader.saveBook(stringBuilder.toString());
    }
    public static void p(String s) {
        System.out.println(s);
    }
}
