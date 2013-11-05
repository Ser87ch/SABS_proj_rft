package ru.sabstest;

import java.util.Iterator;

public class GenerateFromED774List extends GenerateList<ED774_VER> {
    public void generateFromReadEDList(ReadEDList el) {
	pList = Packet.createGenFromED774(el.getSize());

	Iterator<Generate<ED774_VER>> itG = pList.iterator();

	Iterator<ReadED> itR = el.pList.iterator();

	while (itR.hasNext()) {
	    ED774_VER ed774 = (ED774_VER) itR.next();

	    Generate<ED774_VER> ed708 = itG.next();
	    if (!ed708.generateFrom(ed774))
		itG.remove();

	}
    }
}
