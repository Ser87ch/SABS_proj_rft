package ru.sabstest;

import java.util.Iterator;

public class GenerateFromED744List extends GenerateList<ED744_VER> {
	public void generateFromReadEDList(ReadEDList el)
	{
		pList = Packet.createGenFromED744(el.getSize());		
		
		Iterator<Generate<ED744_VER>> itG = pList.iterator();
		
		Iterator<ReadED> itR = el.pList.iterator();
		
		while(itR.hasNext())
		{
			ED744_VER ed744 = (ED744_VER) itR.next();
		
			if(!itG.next().generateFrom(ed744))
				itG.remove();		

			
		}
	}
}
