package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PacketESID extends Packet implements ReadED{

	public String edCreationTime; //Время составления ЭД
	public List<ED206> lt;
	
	public PacketESID()
	{
			
	}
	
	@Override
	public void readXML(Element doc) {
		super.readXML(doc);
		edCreationTime = doc.getAttribute("EDCreationTime");
		
		NodeList nl = doc.getElementsByTagNameNS("*", "ED206");
		lt = new ArrayList<ED206>();
		for(int i = 0; i < nl.getLength(); i++)
		{
			ED206 ed = new ED206();
			ed.readED((Element)nl.item(i));
			lt.add(ed);	
		}
		Collections.sort(lt);
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((lt == null) ? 0 : lt.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PacketESID other = (PacketESID) obj;
		if (lt == null) {
			if (other.lt != null)
				return false;
		} else if (!lt.equals(other.lt))
			return false;
		return true;
	}
	
	public void readEncodedFile(File src, boolean isUTF)
	{
		readXML(getEncodedElement(src.getAbsolutePath(), isUTF));
		filename = src.getName();
	}

	@Override
	public int compareTo(ReadED o) {
		return compareTo((Packet) o);
	}
}

	