package ru.sabstest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;


abstract public class Packet{

	public String filename;

	public Sign firstSign;
	public Sign secondSign;

	public boolean isVER = true;
		
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
}
