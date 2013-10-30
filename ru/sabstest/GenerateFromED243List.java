package ru.sabstest;

import java.util.Iterator;

public class GenerateFromED243List extends GenerateList<ED243> {
    public void generateFromReadEDList(ReadEDList el) {
	pList = Packet.createGenFromED243(el.getSize());

	Iterator<Generate<ED243>> itG = pList.iterator();

	Iterator<ReadED> itR = el.pList.iterator();

	while (itR.hasNext()) {
	    ED243 ed243 = (ED243) itR.next();

	    if (!itG.next().generateFrom(ed243))
		itG.remove();
	}
    }
}
