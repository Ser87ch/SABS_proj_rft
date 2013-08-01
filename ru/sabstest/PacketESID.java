package ru.sabstest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PacketESID extends Packet {

	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	public String edReceiver; //УИС составителя
	public String edCreationTime; //Время составления ЭД
	public List<ED206> lt;
	
	public PacketESID()
	{
			
	}
	
	@Override
	public void readXML(Element doc) {
		edNo = Integer.parseInt(doc.getAttribute("EDNo"));
		edDate = Date.valueOf(doc.getAttribute("EDDate"));
		edAuthor = doc.getAttribute("EDAuthor");
		edReceiver = doc.getAttribute("EDReceiver");
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
		result = prime * result
				+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result
				+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
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
		if (lt == null) {
			if (other.lt != null)
				return false;
		} else if (!lt.equals(other.lt))
			return false;
		return true;
	}

	@Override
	@Deprecated
	public void createFile(String folder) {
		

	}

	@Override
	public void insertIntoDB() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFileName() {
		throw new UnsupportedOperationException();
	}

	

}

	