package ru.sabstest;

import java.util.Iterator;

public class GenerateFromED743List extends GenerateList<ED743_VER> {
	public void generateFromReadEDList(ReadEDList el)
	{
		pList = Packet.createGenFromED743(el.getSize());		
		
		Iterator<Generate<ED743_VER>> itG = pList.iterator();
		
		Iterator<ReadED> itR = el.pList.iterator();
		
		while(itR.hasNext())
		{
			ED743_VER ed743 = (ED743_VER) itR.next();

			if(!itG.next().generateFrom(ed743))
				itG.remove();			

			if(!itG.next().generateFrom(ed743))
				itG.remove();
		}
	}
}
