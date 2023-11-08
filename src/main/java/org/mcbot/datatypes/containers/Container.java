package org.mcbot.datatypes.containers;

import org.mcbot.datatypes.XY;

public interface Container {

    public void readContents();

    public Slot[][] getSlots();

    public XY getStartingCorner();

    public XY getDimensions();
}
