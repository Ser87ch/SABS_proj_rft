package ru.sabstest;

import java.sql.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PaymentOrderRegister extends PaymentDocument {

	public List <TransactionInfo> tiList;

	public PaymentOrderRegister()
	{
		super();
	}

	@Override
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED108");		

		addCommonEDElements(doc, rootElement);		

		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));

		ListIterator<TransactionInfo> it = tiList.listIterator();

		while(it.hasNext())
			rootElement.appendChild(it.next().createED(doc));


		return rootElement;
	}


	@Override
	public void readED(Element doc)
	{
		if(doc.getTagName().equals("ED108"))
		{
			readCommonEDElements(doc);

			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}

			nl = doc.getElementsByTagName("CreditTransferTransactionInfo");
			
			for(int i = 0; i < nl.getLength(); i++)
			{
				TransactionInfo ti = new TransactionInfo();
				ti.readED((Element) nl.item(i));
				
				tiList.add(ti);
			}		
			
		}
	}	

	@Override
	public void generateFromXML(Element gendoc, int edNo, String edAuthor)
	{
		this.edNo = edNo;
		edDate = Settings.operDate;
		this.edAuthor = edAuthor;
		paytKind = "1";
		sum = (int) (new Random().nextFloat() * 10000);
		transKind = "01";
		priority = "6";
		accDocNo = edNo;
		accDocDate = Settings.operDate;
		purpose = "Тестовое платежное поручение";
		chargeOffDate = Settings.operDate;
		receiptDate = Settings.operDate;

		Element el = (Element) gendoc.getElementsByTagName("Payer").item(0);		

		payer = Client.createClientFromBICPersonalAcc(el);

		el = (Element) gendoc.getElementsByTagName("Payee").item(0);		

		payee = Client.createClientFromBICPersonalAcc(el);
	}
	
	public static class TransactionInfo
	{
		public int transactionID;

		public Element createED(Document doc)
		{
			Element rootElement = doc.createElement("CreditTransferTransactionInfo");		

			XML.setOptinalAttr(rootElement, "TransactionID", transactionID);


			return rootElement;
		}

		public void readED(Element tr)
		{
			if(tr.getTagName().equals("CreditTransferTransactionInfo"))
			{
				transactionID = XML.getOptionalIntAttr("TransactionID", tr);

			}
		}

	}

	@Override
	public void insertIntoDbUfebs(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename)
	{
		
		
	}

	@Override
	public void insertIntoDbVer(int idPacet, String filename) {
		// TODO Auto-generated method stub
		
	}

	
}
