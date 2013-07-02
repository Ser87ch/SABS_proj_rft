package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

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
		if(doc.getLocalName().equals("ED108"))
		{
			readCommonEDElements(doc);

			NodeList nl = doc.getElementsByTagNameNS("*","DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}

			nl = doc.getElementsByTagNameNS("*","CreditTransferTransactionInfo");

			tiList = new ArrayList <TransactionInfo>();

			for(int i = 0; i < nl.getLength(); i++)
			{
				TransactionInfo ti = new TransactionInfo();
				ti.readED((Element) nl.item(i));

				tiList.add(ti);
			}		
			Collections.sort(tiList);

		}
	}	

	@Override
	public void generateFromXMLByType(Element gendoc)
	{
		transKind = "01";
		purpose = "Тестовый платежный реестр";		

		tiList = new ArrayList <TransactionInfo>();

		TransactionInfo ti = new TransactionInfo();
		ti.generate(sum);
		tiList.add(ti);
	}

	@Override
	public void insertIntoDbUfebs(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String query = "INSERT INTO [dbo].[UFEBS_Epd]\r\n" + 
			"([ID_PACET], [ID_DEPART],\r\n" + 
			" [ID_ARM], [ID_DOC_BON], [InOutMode], [PEpdNo], [PacDate], [PAuthor], \r\n" + 
			"[EKodObr], [Name], [PrNum], [Dvv], [Sub_ID], [IEdNo], [IEdDate], [IEdAutor], \r\n" + 
			"[Receiver], [Nd], [Dd], [VidPlNo], [Su], [InnMfkb], [NamKl], [LsKl], [Mfkb], [Krkb], \r\n" + 
			"[Mf], [Kr], [InnMf], [NamKor], [LsKor], [Vo], [Turn], [Dest], [NKor1], [NKor2], [NKor3], \r\n" + 
			"[NKor4], [N_Ch_PL], [Shifr], [N_pldok], [D_pl], [Su_ost], [Dppl], [Dkart], [Sop], [SopNo], \r\n" + 
			"[Dspl], [D_otmet], [TurKind], [SStatus], [Kppa], [Kppb], [CodBclas], [CodOkato], [NalPlat], \r\n" + 
			"[NalPer], [NdNal], [DdNal], [TypNal], [Typ_Doc], [SS], [NamPost], [Esc_Key], [Esc_Key2])\r\n" + 
			"VALUES(" + DB.toString(idPacet) + ", null,\r\n" + 
			"2, null, 0, " + DB.toString(pEDNo) + ", " + DB.toString(pacDate) + ", " + DB.toString(pAuthor) + ",\r\n" + 
			"0, 'ED108', " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", " + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "." + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + ", " + DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc) + ",\r\n" + 
			DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc) + ", " + DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(transKind) + ", " + DB.toString(priority) + ", null, " + DB.toString(purpose) + ", null, null,\r\n" + 
			"null, null, null, null, null, null, " + DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", null, null, \r\n" + 
			DB.toString(chargeOffDate) + ", null, null, " + DB.toString(tax.drawerStatus) + ", " + DB.toString(payer.kpp) + ", " + DB.toString(payee.kpp) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.paytReason) + ",\r\n" + 
			DB.toString(tax.taxPeriod) + ", " + DB.toString(tax.docNo) + ", " + DB.toString(tax.docDate) + ", " + DB.toString(tax.taxPaytKind) + ", 'E', null, " + DB.toString(filename) + ", null, null)";			
			db.st.executeUpdate(query);
			db.close();		

			int idEPD = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_EPD) from dbo.UFEBS_Epd"));

			for(TransactionInfo ti:tiList)
			{
				ti.insertIntoDb(idPacet, idEPD, edNo, edDate, edAuthor);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}

	}

	@Override
	public void insertIntoDbVer(int idPacet, String filename) {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String query = "INSERT INTO [dbo].[epay_Epd]\r\n" + 
			"([ID_PACKET], [ID_DEPART],\r\n" + 
			" [ID_ARM], [ID_DOC_BON], [InOutMode], [EKodObr], [Name], [EDNo], [EDDate], [EDAuthor],\r\n" + 
			" [ini_EdNo], [ini_EdDate], [ini_EdAutor],\r\n" + 
			" [EDReceiver], [AccDocNo], [AccDocDate], [PaytKind], [Summa],\r\n" + 
			" [payee_INN], [payee_Name], [payee_PersonalAcc], [payee_BIC], [payee_CorrespAcc], [payee_KPP],\r\n" + 
			" [payer_INN], [payer_Name], [payer_PersonalAcc], [payer_BIC], [payer_CorrespAcc], [payer_KPP],\r\n" + 
			" [TransKind], [Priority], [Purpose], [PaytCondition], [AcptTerm], [DocDispatchDate],\r\n" + 
			" [part_PaytNo], [part_TransKind], [part_AccDocNo], [part_AccDocDate], [part_SumResidualPayt], \r\n" + 
			"[ReceiptDate], [FileDate], [TransContent], [ChargeOffDate], [MaturityDate], [ReceiptDateCollectBank], \r\n" + 
			"[dep_DrawerStatus], [dep_CBC], [dep_OKATO], [dep_PaytReason], [dep_TaxPeriod], [dep_DocNo], [dep_DocDate], [dep_TaxPaytKind], \r\n" + 
			"[AcptSum], [Typ_Doc], [SS], [NamPost], [Esc_Key], [Esc_Key2])\r\n" +					 
			"VALUES(" + DB.toString(idPacet) + ", null,\r\n" + 
			"2, null, 0, 0, 'ED108'," + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ",\r\n" + 
			"null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", '" + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "," + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + "',\r\n" +
			DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc)+ ", " + DB.toString(payee.kpp) + ",\r\n" + 
			DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc)+ ", " + DB.toString(payer.kpp) + ",\r\n" +  
			DB.toString(transKind) + ", " + DB.toString(priority) + ", " + DB.toString(purpose) + ", null, null, null,\r\n" + 
			"null, null, null, null, null,\r\n" +
			DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", null, " +  DB.toString(chargeOffDate) + ", null, null,\r\n" + 
			DB.toString(tax.drawerStatus) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.taxPeriod) + ", " + DB.toString(tax.docNo) + ", " + DB.toString(tax.docDate) + ", " + DB.toString(tax.taxPaytKind) + ", "  + DB.toString(tax.paytReason) + ",\r\n" + 
			"null, 'E', null, " + DB.toString(filename) + ", null, null)";			
			db.st.executeUpdate(query);
			db.close();		

			int idEPD = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_EPD) from dbo.UFEBS_Epd"));

			for(TransactionInfo ti:tiList)
			{
				ti.insertIntoDb(idPacet, idEPD, edNo, edDate, edAuthor);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}


	/**
	 * Реестр поручений
	 * @author Admin
	 *
	 */
	public static class TransactionInfo implements Comparable<TransactionInfo>
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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((docIndex == null) ? 0 : docIndex.hashCode());
			result = prime * result
					+ ((operationID == null) ? 0 : operationID.hashCode());
			result = prime * result + ((payee == null) ? 0 : payee.hashCode());
			result = prime * result + ((payer == null) ? 0 : payer.hashCode());
			result = prime * result
					+ ((payerDocDate == null) ? 0 : payerDocDate.hashCode());
			result = prime * result + payerDocNo;
			result = prime
					* result
					+ ((remittanceInfo == null) ? 0 : remittanceInfo.hashCode());
			result = prime
					* result
					+ ((transactionDate == null) ? 0 : transactionDate
							.hashCode());
			result = prime * result + transactionID;
			result = prime
					* result
					+ ((transactionPurpose == null) ? 0 : transactionPurpose
							.hashCode());
			result = prime * result + transactionSum;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TransactionInfo other = (TransactionInfo) obj;
			if (docIndex == null) {
				if (other.docIndex != null)
					return false;
			} else if (!docIndex.equals(other.docIndex))
				return false;
			if (operationID == null) {
				if (other.operationID != null)
					return false;
			} else if (!operationID.equals(other.operationID))
				return false;
			if (payee == null) {
				if (other.payee != null)
					return false;
			} else if (!payee.equals(other.payee))
				return false;
			if (payer == null) {
				if (other.payer != null)
					return false;
			} else if (!payer.equals(other.payer))
				return false;
			if (payerDocDate == null) {
				if (other.payerDocDate != null)
					return false;
			} else if (!payerDocDate.equals(other.payerDocDate))
				return false;
			if (payerDocNo != other.payerDocNo)
				return false;
			if (remittanceInfo == null) {
				if (other.remittanceInfo != null)
					return false;
			} else if (!remittanceInfo.equals(other.remittanceInfo))
				return false;
			if (transactionDate == null) {
				if (other.transactionDate != null)
					return false;
			} else if (!transactionDate.equals(other.transactionDate))
				return false;
			if (transactionID != other.transactionID)
				return false;
			if (transactionPurpose == null) {
				if (other.transactionPurpose != null)
					return false;
			} else if (!transactionPurpose.equals(other.transactionPurpose))
				return false;
			if (transactionSum != other.transactionSum)
				return false;
			return true;
		}

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
			if(tr.getLocalName().equals("CreditTransferTransactionInfo"))
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

				payer.readED((Element) tr.getElementsByTagNameNS("*","TransactionPayerInfo").item(0));
				payee.readED((Element) tr.getElementsByTagNameNS("*","TransactionPayeeInfo").item(0));

				transactionPurpose = tr.getAttribute("TransactionPurpose");
				remittanceInfo = tr.getAttribute("RemittanceInfo");
			}
		}

		public void generate(int sum)
		{
			transactionID = 1;
			transactionDate = Settings.operDate;
			transactionSum = sum;

			payer = new ClientInfo("1", "60302810700000000001", "222222222222", "Ванов В.В.", "г. Москва", "Плательщик");
			payee = new ClientInfo("2", "40703810700000000015", "111111111111", "Петров П.П.", "г. Москва", "Получатель");
			transactionPurpose = "Запись в реестре";
		}

		
		public void insertIntoDb(int idPacket, int idEPD, int edNo, Date edDate, String edAuthor)
		{
			try{ 
				DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
				db.connect();

				String query = "INSERT INTO [dbo].[epay_ED108]([ID_EPD], [ID_PACKET], [ID_DEPART],\r\n" + 
						" [ID_ARM], [InOutMode], [EDNo], [EDDate], [EDAuthor], [TransactionID], [PayerDocNo], \r\n" + 
						"[PayerDocDate], [OperationID], [TransactionDate], [TransactionSum], [DocIndex],\r\n" + 
						" [PayerPersonalID], [PayerAcc], [PayerINN], [PayerPersonName], [PayerPersonAddress],\r\n" + 
						" [PayerTradeName], [PayeePersonalID], [PayeeAcc], [PayeeINN], [PayeePersonName],\r\n" + 
						" [PayeePersonAddress], [PayeeTradeName], [TransactionPurpose], [RemittanceInfo])\r\n" +
						"VALUES(" + DB.toString(idEPD) + ", " + DB.toString(idPacket) + ", null,\r\n" +
						"2, 0, " + DB.toString(edNo) +  ", " + DB.toString(edDate) + ", "  + DB.toString(edAuthor) + ", " + DB.toString(transactionID) + ", "  + DB.toString(payerDocNo) + ",\r\n" +    
						DB.toString(payerDocDate) + ", " + DB.toString(operationID) +  ", " + DB.toString(transactionDate) +  ", " + DB.toString(transactionSum) +  ", " + DB.toString(docIndex) +  ",\r\n" + 
						DB.toString(payer.personID) +  ", " + DB.toString(payer.acc) +  ", " + DB.toString(payer.inn) +  ", " + DB.toString(payer.personName) +  ", " + DB.toString(payer.personAddress) +  ",\r\n" +
						DB.toString(payer.tradeName) +  ", " + DB.toString(payee.personID) +  ", " + DB.toString(payee.acc) +  ", " + DB.toString(payee.inn) +  ", " + DB.toString(payee.personName) +  ", " + 
						DB.toString(payee.personAddress) +  ", " + DB.toString(payee.tradeName) +  ", " + DB.toString(transactionPurpose) +  ", " + DB.toString(remittanceInfo) + ")";
				db.st.executeUpdate(query);
				db.close();
			
			} catch (Exception e) {
				e.printStackTrace();
				Log.msg(e);			
			}
		}
		
		@Override
		public int compareTo(TransactionInfo o) {
			// TODO Auto-generated method stub
			return 0;
		}

		public static class ClientInfo
		{
			public String personID;
			public String acc;
			public String inn;
			public String personName;
			public String personAddress;
			public String tradeName;

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((acc == null) ? 0 : acc.hashCode());
				result = prime * result + ((inn == null) ? 0 : inn.hashCode());
				result = prime
						* result
						+ ((personAddress == null) ? 0 : personAddress
								.hashCode());
				result = prime * result
						+ ((personID == null) ? 0 : personID.hashCode());
				result = prime * result
						+ ((personName == null) ? 0 : personName.hashCode());
				result = prime * result
						+ ((tradeName == null) ? 0 : tradeName.hashCode());
				return result;
			}



			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				ClientInfo other = (ClientInfo) obj;
				if (acc == null) {
					if (other.acc != null)
						return false;
				} else if (!acc.equals(other.acc))
					return false;
				if (inn == null) {
					if (other.inn != null)
						return false;
				} else if (!inn.equals(other.inn))
					return false;
				if (personAddress == null) {
					if (other.personAddress != null)
						return false;
				} else if (!personAddress.equals(other.personAddress))
					return false;
				if (personID == null) {
					if (other.personID != null)
						return false;
				} else if (!personID.equals(other.personID))
					return false;
				if (personName == null) {
					if (other.personName != null)
						return false;
				} else if (!personName.equals(other.personName))
					return false;
				if (tradeName == null) {
					if (other.tradeName != null)
						return false;
				} else if (!tradeName.equals(other.tradeName))
					return false;
				return true;
			}



			ClientInfo()
			{

			}



			public ClientInfo(String personID, String acc, String inn,
					String personName, String personAddress, String tradeName) {
				super();
				this.personID = personID;
				this.acc = acc;
				this.inn = inn;
				this.personName = personName;
				this.personAddress = personAddress;
				this.tradeName = tradeName;
			}

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


	@Override
	public String toStr(String razd, boolean addShift) {

		return null;
	}
}
