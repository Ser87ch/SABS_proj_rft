package ru.sabstest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

public class GenerateFromPacketEPDList extends GenerateList<PacketEPD> {
	public void generateFromPacketEPD(PacketEPD pd)
	{
		pList = Packet.createGenFromEPD();
		
		for(int i = 0; i < pList.size(); i++)
		{
			Generate<PacketEPD> gen = pList.get(i);
			if(!gen.generateFrom(pd))
				pList.remove(i);
		}
	}
}
