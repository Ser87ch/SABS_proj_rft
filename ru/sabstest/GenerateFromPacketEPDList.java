package ru.sabstest;

import java.util.Iterator;

public class GenerateFromPacketEPDList extends GenerateList<PacketEPD> {
	public void generateFromPacketEPD(PacketEPD pd)
	{
		pList = Packet.createGenFromEPD();
		Iterator<Generate<PacketEPD>> it = pList.iterator();
		
		while(it.hasNext())
		{
			Generate<PacketEPD> gen = it.next();
			if(!gen.generateFrom(pd))
				it.remove();			
		}
	}
}
