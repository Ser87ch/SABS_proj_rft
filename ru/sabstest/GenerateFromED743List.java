package ru.sabstest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

public class GenerateFromED743List extends GenerateList<ED743_VER> {
	public void generateFromReadEDList(ReadEDList el)
	{
		pList = Packet.createGenFromED743(el.getSize());		
		
		for(int i = 0; i < el.pList.size(); i++)
		{
			ED743_VER ed743 = (ED743_VER) el.get(i);
			
			if(!pList.get(2 * i).generateFrom(ed743))
				pList.remove(2 * i);
			
			if(!pList.get(2 * i + 1).generateFrom(ed743))
				pList.remove(2 * i + 1);
		}
	}
}
