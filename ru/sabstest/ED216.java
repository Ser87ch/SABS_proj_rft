package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Подтверждение для ВЭР
 * @author Admin
 *
 */
public class ED216 implements Comparable<ED216>{
	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	public int sum; //сумма
	public String transKind; //вид операции
	public String resultCode; //код результата обработки ЭД
	
	//реквизиты исходного расчетного документа
	public int accDocNo; //номер расчетного документа
	public Date accDocDate; //Дата выписки расчетного документа
	
	public Client payer; //плательщика
	public Client payee; //получатель
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	@Override
	public int compareTo(ED216 o) {
		if(sum < o.sum)
			return -1;
		else if(sum > o.sum)
			return 1;
		else
			return 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accDocDate == null) ? 0 : accDocDate.hashCode());
		result = prime * result + accDocNo;
		result = prime * result
				+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result
				+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
		result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
		result = prime * result + ((payee == null) ? 0 : payee.hashCode());
		result = prime * result + ((payer == null) ? 0 : payer.hashCode());
		result = prime * result
				+ ((resultCode == null) ? 0 : resultCode.hashCode());
		result = prime * result + sum;
		result = prime * result
				+ ((transKind == null) ? 0 : transKind.hashCode());
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
		ED216 other = (ED216) obj;
		if (accDocDate == null) {
			if (other.accDocDate != null)
				return false;
		} else if (!accDocDate.equals(other.accDocDate))
			return false;
		if (accDocNo != other.accDocNo)
			return false;
		if (edAuthor == null) {
			if (other.edAuthor != null)
				return false;
		} else if (!edAuthor.equals(other.edAuthor))
			return false;
		if (edDate == null) {
			if (other.edDate != null)
				return false;
		} else if (!edDate.equals(other.edDate))
			return false;
		if (iEdAuthor == null) {
			if (other.iEdAuthor != null)
				return false;
		} else if (!iEdAuthor.equals(other.iEdAuthor))
			return false;
		if (iEdDate == null) {
			if (other.iEdDate != null)
				return false;
		} else if (!iEdDate.equals(other.iEdDate))
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
		if (resultCode == null) {
			if (other.resultCode != null)
				return false;
		} else if (!resultCode.equals(other.resultCode))
			return false;
		if (sum != other.sum)
			return false;
		if (transKind == null) {
			if (other.transKind != null)
				return false;
		} else if (!transKind.equals(other.transKind))
			return false;
		return true;
	}

	/**
	 * создает элемент ED216
	 * @param doc документ
	 * @return элемент
	 */
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED216");		

		rootElement.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");
		rootElement.setAttribute("EDNo", Integer.toString(edNo));
		rootElement.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));
		rootElement.setAttribute("EDAuthor", edAuthor);

		rootElement.setAttribute("Sum", Integer.toString(sum));
		rootElement.setAttribute("TransKind", transKind);
		
		XML.setOptinalAttr(rootElement, "ResultCode", resultCode);		
		
		Element accDoc = doc.createElement("AccDoc");
		rootElement.appendChild(accDoc);

		accDoc.setAttribute("AccDocNo", Integer.toString(accDocNo));
		accDoc.setAttribute("AccDocDate", new SimpleDateFormat("yyyy-MM-dd").format(accDocDate));	
		
		Element ref = doc.createElement("EDRefID");
		ref.setAttribute("EDNo", Integer.toString(iEdNo));
		ref.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(iEdDate));
		ref.setAttribute("EDAuthor", iEdAuthor);
		
		rootElement.appendChild(payer.createXMLShortElement(doc, "ShortPayer"));
		rootElement.appendChild(payee.createXMLShortElement(doc, "ShortPayee"));
		return rootElement;
	}
	
	/**
	 * считываются основные реквизиты платежного документа
	 * @param doc элемент
	 */
	public void readED(Element doc)
	{
		edNo = Integer.parseInt(doc.getAttribute("EDNo"));
		edDate = Date.valueOf(doc.getAttribute("EDDate"));
		edAuthor = doc.getAttribute("EDAuthor");

		
		sum = Integer.parseInt(doc.getAttribute("Sum"));
		transKind = doc.getAttribute("TransKind");		
		resultCode = doc.getAttribute("ResultCode");

		Element accDoc = (Element) doc.getElementsByTagNameNS("*","AccDoc").item(0);			
		accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));
		
		Element ref = (Element) doc.getElementsByTagNameNS("*","EDRefID").item(0);
		iEdNo = Integer.parseInt(ref.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ref.getAttribute("EDDate"));
		iEdAuthor = ref.getAttribute("EDAuthor");
		
		
		payer = new Client();
		payer.readED((Element) doc.getElementsByTagNameNS("*","ShortPayer").item(0));
		payee = new Client();
		payee.readED((Element) doc.getElementsByTagNameNS("*","ShortPayee").item(0));
		
	}
	
	public void generateFromPaymentDocument(PaymentDocument pd, String author, String resultCode)
	{
		this.resultCode = resultCode;
		
		iEdAuthor = pd.edAuthor;
		iEdDate = pd.edDate;
		iEdNo = pd.edNo;
		
		edNo = pd.edNo + 500;
		edDate = pd.edDate;
		edAuthor = author;
		sum = pd.sum;
		transKind = pd.transKind;
		
		accDocNo = pd.accDocNo;
		accDocDate = pd.accDocDate;
		
		payer = pd.payer;
		payee = pd.payee;
	}
	
	public void insertIntoDbVer(int idPacet, String filename) 
	{

		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			
			String type="ED216";
			
//			if(transKind == "01")
//				type = "ED101";
//			else if(transKind == "02")
//				type = "ED103";
//			else if(transKind == "06")
//				type = "ED104";
//			else if(transKind == "16")
//				type = "ED105";
			
			
			
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
			"2, null, 0, "+ DB.toString(resultCode) +", "+ DB.toString(type) +"," + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ",\r\n" + 
			DB.toString(iEdNo) + "," + DB.toString(iEdDate) + "," + DB.toString(iEdAuthor) + ",\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", null, '" + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "," + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + "',\r\n" +
			DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc)+ ", " + DB.toString(payee.kpp) + ",\r\n" + 
			DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc)+ ", " + DB.toString(payer.kpp) + ",\r\n" +  
			DB.toString(transKind) + ", null, null, null, null, null,\r\n" + 
			"null, null, null, null, null,\r\n" +
			"null, null, null, null, null, null,\r\n" + 
			"null, null, null, null, null, null, null, null,\r\n" + 
			"null, 'E', null, " + DB.toString(filename) + ", null, null)";			
			db.st.executeUpdate(query);
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}
	
}
