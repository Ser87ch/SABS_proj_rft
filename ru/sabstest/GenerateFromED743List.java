package ru.sabstest;

import java.util.Iterator;

public class GenerateFromED743List extends GenerateList<ED743_VER> {
    public void generateFromReadEDList(ReadEDList el) {
	pList = Packet.createGenFromED743(el.getSize());

	Iterator<Generate<ED743_VER>> itG = pList.iterator();

	Iterator<ReadED> itR = el.pList.iterator();

	while (itR.hasNext()) {
	    ED743_VER ed743 = (ED743_VER) itR.next();

	    ED708_VER ed708 = (ED708_VER) itG.next();
	    if (!ed708.generateFrom(ed743))
		itG.remove();

	    ED744_VER ed744 = (ED744_VER) itG.next();
	    if (!ed744.generateFrom(ed743))
		itG.remove();
	}
    }
}
