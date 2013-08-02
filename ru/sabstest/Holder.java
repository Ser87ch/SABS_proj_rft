package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Element;

public class Holder<T extends ReadED> extends Packet implements ReadED {

	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)

	public T ed;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ed == null) ? 0 : ed.hashCode());
		result = prime * result
				+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
		result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
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
		Holder<?> other = (Holder<?>) obj;
		if (ed == null) {
			if (other.ed != null)
				return false;
		} else if (!ed.equals(other.ed))
			return false;
		if (iEdAuthor == null) {
			if (other.iEdAuthor != null)
				return false;
		} else if (!iEdAuthor.equals(other.iEdAuthor))
			return false;
		if (iEdDate == null) {
			if (other.iEdDate != null)
				return false;
		} else if (!iEdDate.equals(other.iEdDate))
			return false;
		return true;
	}


	@Override
	public void readXML(Element root) {
		super.readXML(root);

		Element ied = (Element) root.getElementsByTagName("InitialED").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");

		ied = (Element) root.getElementsByTagName(ed.getClass().getName()).item(0);

		ed.readXML(ied);		
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
