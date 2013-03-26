
package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class PaymentOrder extends PaymentDocument {
	public PaymentOrder()
	{
		super();
	}
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED101");		

		addCommonEDElements(doc, rootElement);		

		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));
		
		return rootElement;
	}

	public void readED(Element doc)
	{
		if(doc.getTagName() == "ED101")
		{
			readCommonEDElements(doc);
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
		}
	}	
}
