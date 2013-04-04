package ru.sabstest;

import java.sql.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CollectionOrder extends PaymentDocument {

	public Date receiptDateCollectBank;	
	
	public CollectionOrder()
	{
		super();
	}
	
	@Override
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED104");		

		addCommonEDElements(doc, rootElement);		
		
		XML.setOptinalAttr(rootElement, "ReceiptDateCollectBank", receiptDateCollectBank);
		
		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));
		
		return rootElement;
	}

	@Override
	public void readED(Element doc)
	{
		if(doc.getTagName().equals("ED104"))
		{
			readCommonEDElements(doc);
			
			receiptDateCollectBank = XML.getOptionalDateAttr("ReceiptDateCollectBank", doc);
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
		}
	}	
	
	@Override
	public void generateFromXML(Element gendoc, int edNo, String edAuthor)
	{
		
	}

	@Override
	public void insertIntoDB(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename)
	{
		
		
	}
}
