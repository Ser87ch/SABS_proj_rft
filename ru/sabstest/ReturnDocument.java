package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReturnDocument {
	public PaymentOrder po;
	
	//��������� ��������� ��
	public int iEdNo; //����� �� � ������� �������
	public Date iEdDate; //���� ����������� ��
	public String iEdAuthor; //���������� ������������� ����������� �� (���)
	
	public Element createED(Document doc)
	{
		Element el = doc.createElement("VERReturnPayt");
		el.appendChild(po.createED(doc));
		
		Element ied = doc.createElement("EDRefID");
		ied.setAttribute("EDNo", Integer.toString(iEdNo));
		ied.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(iEdDate));
		ied.setAttribute("EDAuthor", iEdAuthor);
		
		el.appendChild(ied);
		
		return el;
	}
	
	public void readED(Element doc)
	{
		po = new PaymentOrder();
		po.readED((Element) doc.getElementsByTagName("ED101").item(0));
		
		Element el = (Element) doc.getElementsByTagName("EDRefID").item(0);
		iEdNo = Integer.parseInt(el.getAttribute("EDNo"));
		iEdDate = Date.valueOf(el.getAttribute("EDDate"));
		iEdAuthor = el.getAttribute("EDAuthor");
	}
	
	public void generateFromPaymentDocument(PaymentDocument pd, String author)
	{
		iEdAuthor = pd.edAuthor;
		iEdDate = pd.edDate;
		iEdNo = pd.edNo;
		
		po = new PaymentOrder();
		
		po.generateReturnDocument(pd, author);
	}
}
