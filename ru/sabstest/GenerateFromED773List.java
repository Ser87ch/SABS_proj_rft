package ru.sabstest;

import java.util.Iterator;

public class GenerateFromED773List extends GenerateList<ED773_VER> {
	public void generateFromReadEDList(ReadEDList el)
	{
		
		pList = Packet.createGenFromED773(el);		
		
		Iterator<Generate<ED773_VER>> itG = pList.iterator();
		
		Iterator<ReadED> itR = el.pList.iterator();
		
		while(itR.hasNext())
		{
			ED773_VER ed773 = (ED773_VER) itR.next();

			Generate<ED773_VER> g = itG.next();
			if(!g.generateFrom(ed773))
				itG.remove();			

			g = itG.next();
			if(!g.generateFrom(ed773))
				itG.remove();
		}
	}
}
