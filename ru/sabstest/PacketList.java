package ru.sabstest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PacketList {
	public List<Packet> pList;



	public PacketList()
	{
		pList = new ArrayList<Packet>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pList == null) ? 0 : pList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PacketList other = (PacketList) obj;
		if (pList == null) {
			if (other.pList != null)
				return false;
		} else if (!pList.equals(other.pList))
			return false;
		return true;
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
						(nl.item(i).getNodeName().equals("PacketEPDVER") || nl.item(i).getNodeName().equals("PacketEPD")))
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
			Collections.sort(pList);
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
	
	public int getSize()
	{
		return pList.size();
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

	public void generateEsidFromEPD(PacketList pl)
	{
		Iterator<Packet> it = pl.pList.iterator();

		pList = new ArrayList<Packet>();

		while(it.hasNext())
		{
			PaymentDocumentList epd = (PaymentDocumentList) it.next();

			ConfirmationDocumentList rpack = new ConfirmationDocumentList();
			if(rpack.generateFromPaymentDocumentList(epd))
				pList.add(rpack);

			ReturnDocumentList bpack = new ReturnDocumentList();
			if(bpack.generateFromPaymentDocumentList(epd))
				pList.add(bpack);		
			
			Collections.sort(pList);

		}
	}

	
	public void add(Packet p)
	{
		pList.add(p);
	}
	
	public void readFolder(String fld)
	{
		File[] files = new File(fld).listFiles();
		
		for(File fl:files)
		{
			
			Element root = XML.getXMLRootElement(fl.getAbsolutePath());
			String type = root.getElementsByTagName("props:DocType").item(0).getTextContent();
			
			Packet p = Packet.createPacketByFile(type);			
			
			if(p != null)
			{
				p.readEncodedFile(fl.getAbsolutePath(), true);
				pList.add(p);
			}
		}
		Collections.sort(pList);
	}
}
