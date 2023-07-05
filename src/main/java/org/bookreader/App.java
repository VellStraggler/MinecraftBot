package org.bookreader;

import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) throws InterruptedException {
//        System.out.println(CharRecognition.recognizeRisk(CharArrays.getArray('j'),false));
        BookReader reader = new BookReader();
        Thread.sleep(4000);
        Book book = new Book();
        book.setTitle("");
        reader.takeScreenshot();
        reader.saveImage();
        String page = reader.processPage();
        reader.p(page);
        reader.nextPage();

        // await command to begin
        // /readbook bookName
        // Wait for a slash
//        while(!key.isPressed("/")) {
//            Thread.sleep(10);
//        }
//        while(true) {
//            if(command.equals("readbook\n")) {
//
//            } else break;


    }
}
