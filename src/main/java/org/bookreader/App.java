package org.bookreader;

public class App {
    public static void main(String[] args) throws InterruptedException {
        readOneBook();
//        p(CharRecognition.recognizeRisk(CharArrays.getArray('M'),false) + "");
//        p(CharRecognition.recognizeRisk(CharArrays.getArray('H'),false) + "");
    }
    public static void readOneBook() throws InterruptedException {
        BookReader reader = new BookReader();
        Thread.sleep(4000);
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
