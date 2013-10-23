package ru.sabstest;

import java.util.Iterator;

import org.w3c.dom.Element;

public class GenerateFromED273List extends GenerateList<ED273> {
	public void generateFromGenerateFromXML(GenerateFromXMLList el)
	{
		pList = Packet.createGenFromED273(el);		
		
		Iterator<Generate<ED273>> itG = pList.iterator();
		
		Iterator<Generate<Element>> itR = el.pList.iterator();
		
		while(itR.hasNext())
		{
			ED273 ed273 = (ED273) itR.next();


			for(int i = 0; i < ed273.pdList.size(); i++)
			{
				Generate<ED273> g = itG.next();
				if(!g.generateFrom(ed273))
					itG.remove();			

				
			}
		}
	}
}
