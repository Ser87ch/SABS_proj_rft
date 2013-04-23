package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PaymentWarrant extends PaymentDocument {
	public String transContent; //содержание операции
	
	public String ppPaytNo; //номер частичного платежа
	public String ppTransKind; //шифр платежного документа
	public int ppSumResidualPayt; //сумма остатка платежа
	public String ppAccDocNo; //номер исходного расчетного документа
	public Date ppAccDocDate; //дата исходного расчетного дкоумента
	
	public PaymentWarrant()
	{
		super();
	}
	
	@Override
	public Element createED(Document doc) {
		Element rootElement = doc.createElement("ED105");		

		addCommonEDElements(doc, rootElement);		
		
		XML.setOptinalAttr(rootElement, "TransContent", transContent);
		
		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));
		
		Element pp = doc.createElement("PartialPayt");
		rootElement.appendChild(pp);		
		XML.setOptinalAttr(pp, "PaytNo", ppPaytNo);
		pp.setAttribute("TransKind", ppTransKind);
		XML.setOptinalAttr(pp, "SumResidualPayt", ppSumResidualPayt);
		
		Element accDoc = doc.createElement("AccDoc");
		pp.appendChild(accDoc);
		accDoc.setAttribute("AccDocNo", ppAccDocNo);
		accDoc.setAttribute("AccDocDate", new SimpleDateFormat("yyyy-MM-dd").format(ppAccDocDate));
		
		return rootElement;
	}

	@Override
	public void readED(Element doc) {
		if(doc.getTagName() == "ED105")
		{
			readCommonEDElements(doc);
			
			transContent = doc.getAttribute("TransContent");
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
		}

	}

}
