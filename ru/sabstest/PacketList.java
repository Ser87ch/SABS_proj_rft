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
				Packet epd = new PaymentDocumentList();
				if(epd.generateFromXML((Element) nl.item(i)))
					pList.add(epd);
			}
				
			nl = root.getElementsByTagName("PacketEPDVER");
			for(int i = 0; i < nl.getLength(); i++)
			{
				Packet pack = new PaymentDocumentList();
				if(pack.generateFromXML((Element) nl.item(i)))
					pList.add(pack);
				
				pack = new ConfirmationDocumentList();
				if(pack.generateFromXML((Element) nl.item(i)))
					pList.add(pack);
				
				pack = new ReturnDocumentList();
				if(pack.generateFromXML((Element) nl.item(i)))
					pList.add(pack);
				
			}
		}
			
	}
	
	public void createFile(String folder)
	{
		Iterator<Packet> it = pList.iterator();
		int i = 0;
		while(it.hasNext())
		{
			i++;
			Packet p = it.next();
			p.createFile(folder + Integer.toString(i) + ".xml");
		}
	}
	
	public Packet get(int i)
	{
		return pList.get(i);
	}
}
