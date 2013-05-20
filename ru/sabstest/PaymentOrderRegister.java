package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Класс Реестр платежный документов
 * @author Admin
 *
 */
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
	public void generateFromXMLByType(Element gendoc)
	{
		transKind = "01";
		purpose = "Тестовый платежный реестр";		
	}
	
	@Override
	public void insertIntoDbUfebs(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename)
	{
		
		
	}

	@Override
	public void insertIntoDbVer(int idPacet, String filename) {

		
	}

	
	/**
	 * Реестр поручений
	 * @author Admin
	 *
	 */
	public static class TransactionInfo
	{
		public int transactionID;
		public int payerDocNo;
		public Date payerDocDate;
		public String operationID;
		public Date transactionDate;
		public int transactionSum;
		public String docIndex;
		public ClientInfo payer;
		public ClientInfo payee;
		public String transactionPurpose;
		public String remittanceInfo;

		public Element createED(Document doc)
		{
			Element rootElement = doc.createElement("CreditTransferTransactionInfo");		

			rootElement.setAttribute("TransactionID", Integer.toString(transactionID));			
			XML.setOptinalAttr(rootElement, "PayerDocNo", payerDocNo);
			XML.setOptinalAttr(rootElement, "PayerDocDate", payerDocDate);
			XML.setOptinalAttr(rootElement, "OperationID", operationID);
			rootElement.setAttribute("TransactionDate", new SimpleDateFormat("yyyy-MM-dd").format(transactionDate));
			rootElement.setAttribute("TransactionSum", Integer.toString(transactionSum));
			XML.setOptinalAttr(rootElement, "DocIndex", docIndex);
			
			rootElement.appendChild(payer.createED(doc, "TransactionPayerInfo"));
			rootElement.appendChild(payee.createED(doc, "TransactionPayeeInfo"));
			
			XML.setOptinalAttr(rootElement, "TransactionPurpose", transactionPurpose);
			XML.setOptinalAttr(rootElement, "RemittanceInfo", remittanceInfo);
			
			return rootElement;
		}

		public void readED(Element tr)
		{
			if(tr.getTagName().equals("CreditTransferTransactionInfo"))
			{
				transactionID = Integer.parseInt(tr.getAttribute("TransactionID"));
				payerDocNo = XML.getOptionalIntAttr("PayerDocNo", tr);
				payerDocDate = XML.getOptionalDateAttr("PayerDocDate", tr);
				operationID = tr.getAttribute("OperationID");
				transactionDate = Date.valueOf(tr.getAttribute("TransactionDate"));
				transactionSum = Integer.parseInt(tr.getAttribute("TransactionSum"));
				docIndex = tr.getAttribute("DocIndex");
				
				payer = new ClientInfo();
				payee = new ClientInfo();
				
				payer.readED((Element) tr.getElementsByTagName("TransactionPayerInfo").item(0));
				payee.readED((Element) tr.getElementsByTagName("TransactionPayeeInfo").item(0));
				
				transactionPurpose = tr.getAttribute("TransactionPurpose");
				remittanceInfo = tr.getAttribute("RemittanceInfo");
			}
		}
		
		public static class ClientInfo
		{
			public String personID;
			public String acc;
			public String inn;
			public String personName;
			public String personAddress;
			public String tradeName;
			
			public Element createED(Document doc, String name)
			{
				Element rootElement = doc.createElement(name);
				
				XML.setOptinalAttr(rootElement, "PersonID", personID);
				XML.setOptinalAttr(rootElement, "Acc", acc);
				XML.setOptinalAttr(rootElement, "INN", inn);
				
				XML.createNode(doc, rootElement, "PersonName", personName);
				XML.createNode(doc, rootElement, "PersonAddress", personAddress);
				XML.createNode(doc, rootElement, "TradeName", tradeName);
				
				return rootElement;
			}
			
			public void readED(Element cl)
			{
				personID = cl.getAttribute("PersonID");
				acc = cl.getAttribute("Acc");
				inn = cl.getAttribute("INN");
				
				personName = XML.getChildValueString("PersonName", cl);
				personAddress = XML.getChildValueString("PersonAddress", cl);
				tradeName = XML.getChildValueString("TradeName", cl);
			}
		}

	}

	
	
}
