package org.mcbot;

import org.mcbot.datatypes.Blocks;
import org.mcbot.datatypes.Items;
import org.mcbot.skills.BookReader;
import org.mcbot.skills.Farming;

public class App {
    public static void main(String[] args) {
        //setup
        Items items = new Items();
        Blocks blocks = new Blocks(items);
        Utils.sleep(2000);
        F3DataReader dataReader = new F3DataReader();
        Movement movement = new Movement(dataReader, blocks);

//        Utils.rightClickHere();
//        Utils.clickHere();
        new Farming(movement, dataReader).farmAndPlantCrop("carrots",1000);
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
