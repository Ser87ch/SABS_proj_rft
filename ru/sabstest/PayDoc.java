package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;




abstract public class PayDoc {
	//��������� ��
	public int edNo; //����� �� � ������� �������
	public Date edDate; //���� ����������� ��
	public String edAuthor; //���������� ������������� ����������� �� (���)
		
	//�������� ���������
	public String paytKind; //��� �������
	public int sum; //�����
	public String transKind; //��� ��������
	public String priority; //����������� �������
	public Date receiptDate; //���� ����������� � ���� �����������
	public Date fileDate; //���� ��������� � ���������
	public Date chargeOffDate; //���� �������� �� ����� �����������
	public String systemCode; //������� ������� ��������� (����������� ���� �� �� ������� ������)
	
	//��������� ��������� ���������� ���������
	public int accDocNo; //����� ���������� ���������
	public Date accDocDate; //���� ������� ���������� ���������
	
	public Client payer; //�����������
	public Client payee; //����������
	
	public String purpose; //���������� �������
	
	//������������� ����������
	DepartmentalInfo tax;
	
	
	public PayDoc()
	{	
		this.accDocNo = 0;
		this.accDocDate = new Date(0);
		this.transKind = "";
		this.sum = 0;				
		this.paytKind = "1";
		this.priority = "6";
		tax = new DepartmentalInfo("", "", "", "", "", "", "", "");			
		this.purpose = "����";
		this.chargeOffDate = new Date(0);
		this.receiptDate = new Date(0);	
		this.edNo = 0;
	}

	@Override
	public String toString()
	{
		return toStr(" ", false);
	}

	
	public String toStr(String razd, boolean addShift){
		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd + Float.toString(sum) + razd + paytKind.toString() + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payer.inn + razd + payer.kpp + razd + payer.name + razd + payee.bic + razd + payee.correspAcc + razd + payee.personalAcc + razd + payee.inn + razd + payee.kpp + razd 
		+ (addShift ? "+{ExtEnd}" : "") + payee.name + razd +
		priority + razd + tax.drawerStatus;
		if(tax.drawerStatus != "" && tax.drawerStatus != null)
			str = str + razd + tax.cbc + razd + tax.okato + razd + tax.paytReason + razd + tax.taxPeriod + razd + tax.docNo + razd + tax.docDate + razd + tax.taxPaytKind;

		str = str + razd + purpose + razd + new SimpleDateFormat("ddMMyyyy").format(chargeOffDate) + razd + new SimpleDateFormat("ddMMyyyy").format(receiptDate);
		return str;	
	}

	public String toStrContr(String razd){

		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd + Float.toString(sum) + razd + paytKind.toString() + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payee.bic + razd + payee.correspAcc + razd + payee.personalAcc + razd + priority;

		return str;	
	}

	abstract public Element createED(Document doc);
	
	
	abstract public void readED(Element doc);
	
	
	public void createXML(String fl)
	{
		Document doc = XML.createNewDoc();
		doc.appendChild(createED(doc));
		XML.createXMLFile(doc, fl);
	}
	
	public void readXML(String src)
	{
		readED(XML.getXMLRootElement(src));
	}
}
