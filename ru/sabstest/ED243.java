package ru.sabstest;

import java.sql.Date;
import java.util.List;

import org.w3c.dom.Element;

public class ED243 extends Packet {

	//��������� ��
	public int edNo; //����� �� � ������� �������
	public Date edDate; //���� ����������� ��
	public String edAuthor; //���������� ������������� ����������� �� (���)
	public String edReceiver; //��� �����������
	public String edDefineRequestCode; //��� �������
		
	//��������� ��������� ��
	public int iEdNo; //����� �� � ������� �������
	public Date iEdDate; //���� ����������� ��
	public String iEdAuthor; //���������� ������������� ����������� �� (���)
	
	public int accDocNo; //����� ���������� ���������
	public Date accDocDate; //���� ������� ���������� ���������
	
	public String payerAcc;
	public String payerName;
	public String payeeAcc;
	public String payeeName;
	public int sum;
	
	public String edDefineRequestText;
	public List<String> edFieldList;
	public List<Integer> edReestrInfo; 
	
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
		// TODO Auto-generated method stub

	}

	@Override
	public void readXML(Element root) {
		// TODO Auto-generated method stub

	}
	
	public void generateFromXML(Element root)
	{
		
	}

}
