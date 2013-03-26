package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

		if(paytCondition != null && !paytCondition.equals(""))
			rootElement.setAttribute("PaytCondition", paytCondition);
		if(acptTerm != 0)
			rootElement.setAttribute("AcptTerm", Integer.toString(acptTerm));
		if(docDispatchDate != null)
			rootElement.setAttribute("DocDispatchDate", new SimpleDateFormat("yyyy-MM-dd").format(docDispatchDate));
		if(receiptDateCollectBank != null)
			rootElement.setAttribute("ReceiptDateCollectBank", new SimpleDateFormat("yyyy-MM-dd").format(receiptDateCollectBank));
		if(maturityDate != null)
			rootElement.setAttribute("MaturityDate", new SimpleDateFormat("yyyy-MM-dd").format(maturityDate));
		if(acptSum != 0)
			rootElement.setAttribute("AcptSum", Integer.toString(acptSum));
		
		return rootElement;
	}

	@Override
	public void readED(Element doc) {
		if(doc.getTagName() == "ED103")
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

}
