package ru.sabstest;

import java.sql.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PaymentRequest extends PaymentDocument {

	public String paytCondition; //услови€ оплаты
	public int acptTerm; //срок дл€ акцепта
	public Date docDispatchDate; //дата отсылки (вручени€) плательщику предусмотренных договором документов
	public Date receiptDateCollectBank; // дата предоставлени€ документов в банк
	public Date maturityDate; //окончание срока акцепта
	public int acptSum; //сумма исходного расчетного документа, предъ€вленного к акцепту
	
	
	public PaymentRequest()
	{
		super();
	}
	
	@Override
	public Element createED(Document doc) {
		Element rootElement = doc.createElement("ED103");		

		addCommonEDElements(doc, rootElement);		
		
		XML.setOptinalAttr(rootElement, "PaytCondition", paytCondition);
		XML.setOptinalAttr(rootElement, "AcptTerm", acptTerm);
		XML.setOptinalAttr(rootElement, "DocDispatchDate", docDispatchDate);
		XML.setOptinalAttr(rootElement, "ReceiptDateCollectBank", receiptDateCollectBank);
		XML.setOptinalAttr(rootElement, "MaturityDate", maturityDate);
		XML.setOptinalAttr(rootElement, "AcptSum", acptSum);
		
		return rootElement;
	}

	@Override
	public void readED(Element doc) {
		if(doc.getTagName().equals("ED103"))
		{
			readCommonEDElements(doc);
			
			paytCondition = doc.getAttribute("PaytCondition");
			acptTerm = XML.getOptionalIntAttr("AcptTerm", doc);
			docDispatchDate = XML.getOptionalDateAttr("DocDispatchDate", doc);
			receiptDateCollectBank = XML.getOptionalDateAttr("ReceiptDateCollectBank", doc);
			maturityDate = XML.getOptionalDateAttr("MaturityDate", doc);
			acptSum = XML.getOptionalIntAttr("AcptSum", doc);			
			
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
