package ru.sabstest;

import java.io.File;
import java.sql.Date;

import org.w3c.dom.Element;

public class ED208 extends Packet implements ReadED, Generate<ED243> {

	public String resultCode;
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
		result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
		result = prime * result
				+ ((resultCode == null) ? 0 : resultCode.hashCode());
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
		ED208 other = (ED208) obj;
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
		if (resultCode == null) {
			if (other.resultCode != null)
				return false;
		} else if (!resultCode.equals(other.resultCode))
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
		resultCode = root.getAttribute("ResultCode");
		
		Element ied = (Element) root.getElementsByTagNameNS("*", "EDRefID").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");
	}

	
	@Override
	public boolean generateFrom(ED243 source) {
		edNo = source.edNo + 3500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		resultCode = ""; //TODO разобраться с кодом результата
		
		iEdAuthor = source.edAuthor;
		iEdDate = source.edDate;
		iEdNo = source.edNo;
		return true;
	}

	@Override
	public void setFileName() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void insertIntoDB() {
		throw new UnsupportedOperationException();
		
	}
}
