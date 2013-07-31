package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Element;

public class Holder<T extends Packet> extends Packet {

	//��������� ��
	public int edNo; //����� �� � ������� �������
	public Date edDate; //���� ����������� ��
	public String edAuthor; //���������� ������������� ����������� �� (���)
	public String edReceiver; //��� �����������

	//��������� ��������� ��
	public int iEdNo; //����� �� � ������� �������
	public Date iEdDate; //���� ����������� ��
	public String iEdAuthor; //���������� ������������� ����������� �� (���)

	public T ed;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ed == null) ? 0 : ed.hashCode());
		result = prime * result
		+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result
		+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
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
		Holder other = (Holder) obj;
		if (ed == null) {
			if (other.ed != null)
				return false;
		} else if (!ed.equals(other.ed))
			return false;
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
	@Deprecated
	public void createFile(String folder) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void insertIntoDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".7" + ed.getClass().getName().substring(1);
	}

	@Override
	public void readXML(Element root) {
		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");	

		Element ied = (Element) root.getElementsByTagName("InitialED").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");

		ied = (Element) root.getElementsByTagName(ed.getClass().getName()).item(0);

		ed.readXML(ied);		
	}

}
