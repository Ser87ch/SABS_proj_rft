package ru.sabstest;

import java.io.File;
import java.sql.Date;

public class ED274 extends Packet implements Generate<ED273>, ReadED {

	public String infoCode;
	public String annotation;
	
	//��������� ��������� ��
	public int iEdNo; //����� �� � ������� �������
	public Date iEdDate; //���� ����������� ��
	public String iEdAuthor; //���������� ������������� ����������� �� (���)
	
	//��������� ��������� ���
	public int refEdNo; //����� �� � ������� �������
	public Date refEdDate; //���� ����������� ��
	public String refEdAuthor; //���������� ������������� ����������� �� (���)
	
	@Override
	public boolean generateFrom(ED273 source) {
		// TODO ��������� 274
		return false;
	}

	@Override
	public void setFileName() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void insertIntoDB() {
		throw new UnsupportedOperationException();

	}

	@Override
	public int compareTo(ReadED arg0) {
		return super.compareTo((Packet) arg0);
	}

	@Override
	public void readEncodedFile(File src, boolean isUTF) {
		throw new UnsupportedOperationException();
		
	}

}
