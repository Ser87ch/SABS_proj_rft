package ru.sabstest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

public class GenerateFromPacketEPDList extends GenerateList<PacketEPD> {
	public void generateFromPacketEPD(PacketEPD pd)
	{
		List <Generate<PacketEPD>> genList = Packet.createGenFromEPD();
		
		pList = new ArrayList<Generate<PacketEPD>> ();
		
		for(Generate<PacketEPD> gen:genList)
		{
			if(gen.generateFrom(pd))
				pList.add(gen);
		}
	}
}
