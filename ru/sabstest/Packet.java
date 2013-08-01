package ru.sabstest;

import org.w3c.dom.Element;


abstract public class Packet implements Comparable<Packet>{

	public String filename;

	public Sign firstSign;
	public Sign secondSign;

	public boolean isVER = true;
	
	@Override
	public int compareTo(Packet o) {		
			return filename.compareTo(o.filename);		
	}	

	@Deprecated
	public abstract void createFile(String folder);

	public abstract void insertIntoDB();

	public abstract void setFileName();
	
	public abstract void readXML(Element root);
	
	public void insertForRead()
	{
		if(!isVER)
			DB.insertPacetForReadUfebs(filename);
		else
			DB.insertPacetForReadVer(filename);
	}
	
	public void readEncodedFile(String src, boolean isUTF)
	{
		Element root;
		if(isUTF)
			root = XML.getXMLRootElementFromStringUTF(XML.decodeBase64(src));
		else
			root = XML.getXMLRootElementFromString1251(XML.decodeBase64(src));
		
		readXML(root);
	}
	
	void readFile(String src)
	{
		Element root = XML.getXMLRootElement(src);

		readXML(root);
	}
	
	public static Packet createPacketByFile(String type)
	{
		Packet pc = null;		
				
		if(type.equals("PacketEPDVER") || type.equals("PacketEPD"))
			pc = new PacketEPD();
		else if(type.equals("PacketESIDVER_RYM"))
			pc = new PacketESIDVER();
		else if(type.equals("PacketEPDVER_B"))
			pc = new PacketEPDVER_B();
		else if(type.equals("PacketESID"))
			pc = new PacketESID();
		
		return pc;
	}
}
