package ru.sabstest;

import java.io.File;
import java.sql.Date;

import org.w3c.dom.Element;

public class ED208 extends Packet implements ReadED {

	public String resultCode;
	
	//��������� ��������� ��
	public int iEdNo; //����� �� � ������� �������
	public Date iEdDate; //���� ����������� ��
	public String iEdAuthor; //���������� ������������� ����������� �� (���)
	
	
	
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
		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		resultCode = root.getAttribute("ResultCode");
		
		Element ied = (Element) root.getElementsByTagName("EDRefID").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");

	}

}
