package ru.sabstest;

import java.util.List;

public class GenerateFromPacketEPDList extends GenerateList<PacketEPD> {
	public void generateFromPacketEPD(PacketEPD pd)
	{
		List <Generate<PacketEPD>> genList = Packet.createGenFromEPD();
		
		for(Generate<PacketEPD> gen:genList)
		{
			if(gen.generateFrom(pd))
				pList.add(gen);
		}
	}
}
