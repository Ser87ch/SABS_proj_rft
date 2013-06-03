package ru.sabstest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PacketList {
	public List<Packet> pList;



	public PacketList()
	{

	}

	public void generateFromXML(String fl)
	{
		Element root = XML.getXMLRootElement(fl);

		if(root.getNodeName().equals("Generation"))
		{
			pList = new ArrayList<Packet> ();

			NodeList nl = root.getChildNodes();
			for(int i = 0; i < nl.getLength(); i++)
			{
				if(nl.item(i).getNodeType() == Node.ELEMENT_NODE && 
						(nl.item(i).getNodeName() == "PacketEPDVER" || nl.item(i).getNodeName() == "PacketEPD"))
				{
					PaymentDocumentList epd = new PaymentDocumentList();				
					epd.generateFromXML((Element) nl.item(i));
					pList.add(epd);

//					ConfirmationDocumentList rpack = new ConfirmationDocumentList();
//					if(rpack.generateFromPaymentDocumentList(epd))
//						pList.add(rpack);
//
//					ReturnDocumentList bpack = new ReturnDocumentList();
//					if(bpack.generateFromPaymentDocumentList(epd))
//						pList.add(bpack);
				}
			}			
		}
	}

	public void createFile(String folder)
	{
		Iterator<Packet> it = pList.iterator();

		while(it.hasNext())
		{
			Packet p = it.next();
			p.createFile(folder);
		}
	}

	public Packet get(int i)
	{
		return pList.get(i);
	}

	public void insertIntoDB()
	{
		Iterator<Packet> it = pList.iterator();

		while(it.hasNext())
		{
			Packet p = it.next();
			p.insertIntoDB();
		}
	}

	public void insertForRead()
	{
		Iterator<Packet> it = pList.iterator();

		while(it.hasNext())
		{
			Packet p = it.next();
			if(p.packetType == Packet.Type.PacketEPD)
				DB.insertPacetForReadUfebs(p.filename);
			else
				DB.insertPacetForReadVer(p.filename);
		}
	}
}
