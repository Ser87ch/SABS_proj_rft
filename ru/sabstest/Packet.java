package ru.sabstest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;


abstract public class Packet{

	public String filename;

	public Sign firstSign;
	public Sign secondSign;

	public boolean isVER = true;
	
	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	public String edReceiver; //УИС составителя
	
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result
				+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
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
		Packet other = (Packet) obj;
		if (edAuthor == null) {
			if (other.edAuthor != null)
				return false;
		} else if (!edAuthor.equals(other.edAuthor))
			return false;
		if (edDate == null) {
			if (other.edDate != null)
				return false;
		} else if (!edDate.equals(other.edDate))
			return false;
		if (edReceiver == null) {
			if (other.edReceiver != null)
				return false;
		} else if (!edReceiver.equals(other.edReceiver))
			return false;
		return true;
	}

	public int compareTo(Packet o) {		
			return filename.compareTo(o.filename);		
	}	
		
	public void insertForRead()
	{
		if(!isVER)
			DB.insertPacetForReadUfebs(filename);
		else
			DB.insertPacetForReadVer(filename);
	}
	
	public Element getEncodedElement(String src, boolean isUTF)
	{
		Element root;
		if(isUTF)
			root = XML.getXMLRootElementFromStringUTF(XML.decodeBase64(src));
		else
			root = XML.getXMLRootElementFromString1251(XML.decodeBase64(src));
		
		return root;
	}
	
	
	
	public static ReadED createReadEDByFile(String type)
	{
		ReadED pc = null;		
				
		if(type.equals("PacketEPDVER") || type.equals("PacketEPD"))
			pc = new PacketEPD();
		else if(type.equals("PacketESIDVER_RYM"))
			pc = new PacketESIDVER();
		else if(type.equals("PacketEPDVER_B"))
			pc = new PacketEPDVER_B();
		else if(type.equals("PacketESID"))
			pc = new PacketESID();
		else if(type.equals("ED243"))
			pc = new ED243();
		
		return pc;
	}
	
	public static Generate<Element> createGenFromXMLByFile(String type)
	{
		Generate<Element> pc = null;		
				
		if(type.equals("PacketEPDVER") || type.equals("PacketEPD"))
			pc = new PacketEPD();		
		else if(type.equals("ED243"))
			pc = new ED243();
		
		return pc;
	}
	
	public static List<Generate<PacketEPD>> createGenFromEPD()
	{
		List<Generate<PacketEPD>> pc = new ArrayList<Generate<PacketEPD>>(); 
		pc.add(new PacketESIDVER());
		pc.add(new PacketEPDVER_B());			
		return pc;
	}
	
	
	public Sign[] getSigns()
	{
		Sign[] sgn = {firstSign, secondSign};
		return sgn;
	}
	
	public boolean isVER()
	{
		if(isVER)
			return true;
		return false;
	}
		
	public void readXML(Element root) {
		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
	}
}
