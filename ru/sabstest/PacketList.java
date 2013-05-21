package ru.sabstest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PacketList {
	List<Packet> pList;

	public PacketList()
	{

	}

	public void generateFromXML(String fl)
	{
		Element root = XML.getXMLRootElement(fl);

		if(root.getNodeName().equals("Generation"))
		{
			pList = new ArrayList<Packet> ();

			NodeList nl = root.getElementsByTagName("PacketEPD");
			for(int i = 0; i < nl.getLength(); i++)
			{
				PaymentDocumentList epd = new PaymentDocumentList();
				epd.filename = Integer.toString(i + 1) + "su.xml";
				epd.generateFromXML((Element) nl.item(i));
				pList.add(epd);
			}

			nl = root.getElementsByTagName("PacketEPDVER");
			for(int i = 0; i < nl.getLength(); i++)
			{
				PaymentDocumentList epd = new PaymentDocumentList();
				epd.filename = Integer.toString(i + 1) + "s.xml";
				epd.generateFromXML((Element) nl.item(i));
				pList.add(epd);

				ConfirmationDocumentList rpack = new ConfirmationDocumentList();
				if(rpack.generateFromPaymentDocumentList(epd))
				{
					rpack.filename = Integer.toString(i + 1) + "r.xml";
					pList.add(rpack);
				}

				ReturnDocumentList bpack = new ReturnDocumentList();
				if(bpack.generateFromPaymentDocumentList(epd))
				{
					bpack.filename = Integer.toString(i + 1) + "b.xml";
					pList.add(bpack);
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
			p.insertIntoDb();
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
