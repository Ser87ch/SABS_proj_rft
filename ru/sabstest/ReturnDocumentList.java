package ru.sabstest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * ��� �� �������� ��������
 * @author Admin
 *
 */
public class ReturnDocumentList {
	private List<ReturnDocument> rdList;
	
	public int edNo;
	public Date edDate;
	public String edAuthor;
	public String edReceiver;
	public int edQuantity;
	public int sum;
	
	ReturnDocumentList()
	{
		rdList = new ArrayList<ReturnDocument>();
	}
	
	static public class ReturnDocument{
		public PaymentOrder po;
		
		//��������� ��������� ��
		public int iEdNo; //����� �� � ������� �������
		public Date iEdDate; //���� ����������� ��
		public String iEdAuthor; //���������� ������������� ����������� �� (���)
	}
}
