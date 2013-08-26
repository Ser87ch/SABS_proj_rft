package ru.sabstest;

import java.io.File;
import java.sql.Date;

import org.w3c.dom.Element;

public class ED201 extends Packet implements ReadED {

	public String ctrlCode; //Код результата контроля ЭД
	public String ctrlTime; //Время проведения контроля ЭД 
	public String annotation; //Текст пояснения
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((ctrlCode == null) ? 0 : ctrlCode.hashCode());
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
		ED201 other = (ED201) obj;
		if (ctrlCode == null) {
			if (other.ctrlCode != null)
				return false;
		} else if (!ctrlCode.equals(other.ctrlCode))
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
	public int compareTo(ReadED o) {
		return compareTo((Packet) o);
	}

	@Override
	public void readEncodedFile(File src, boolean isUTF) {
		readXML(getEncodedElement(src.getAbsolutePath(), isUTF));
		filename = src.getName();
	}

	@Override
	public void readXML(Element root) {
		super.readXML(root);
		ctrlCode = root.getAttribute("CtrlCode");
		ctrlTime = root.getAttribute("CtrlTime");
		
		Element ied = (Element) root.getElementsByTagNameNS("*", "EDRefID").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");
	}

}
