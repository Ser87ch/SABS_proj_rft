package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Платежный ордер
 * @author Admin
 *
 */
public class ED105 extends PaymentDocument {
	public String transContent; //содержание операции
	
	public String ppPaytNo; //номер частичного платежа
	public String ppTransKind; //шифр платежного документа
	public int ppSumResidualPayt; //сумма остатка платежа
	public String ppAccDocNo; //номер исходного расчетного документа
	public Date ppAccDocDate; //дата исходного расчетного дкоумента
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((ppAccDocDate == null) ? 0 : ppAccDocDate.hashCode());
		result = prime * result
				+ ((ppAccDocNo == null) ? 0 : ppAccDocNo.hashCode());
		result = prime * result
				+ ((ppPaytNo == null) ? 0 : ppPaytNo.hashCode());
		result = prime * result + ppSumResidualPayt;
		result = prime * result
				+ ((ppTransKind == null) ? 0 : ppTransKind.hashCode());
		result = prime * result
				+ ((transContent == null) ? 0 : transContent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ED105 other = (ED105) obj;
		if (ppAccDocDate == null) {
			if (other.ppAccDocDate != null)
				return false;
		} else if (!ppAccDocDate.equals(other.ppAccDocDate))
			return false;
		if (ppAccDocNo == null) {
			if (other.ppAccDocNo != null)
				return false;
		} else if (!ppAccDocNo.equals(other.ppAccDocNo))
			return false;
		if (ppPaytNo == null) {
			if (other.ppPaytNo != null)
				return false;
		} else if (!ppPaytNo.equals(other.ppPaytNo))
			return false;
		if (ppSumResidualPayt != other.ppSumResidualPayt)
			return false;
		if (ppTransKind == null) {
			if (other.ppTransKind != null)
				return false;
		} else if (!ppTransKind.equals(other.ppTransKind))
			return false;
		if (transContent == null) {
			if (other.transContent != null)
				return false;
		} else if (!transContent.equals(other.transContent))
			return false;
		return true;
	}

	public ED105()
	{
		super();
	}
	
	@Override
	@Deprecated
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
		if(doc.getLocalName().equals("ED105"))
		{
			readCommonEDElements(doc);
			
			transContent = doc.getAttribute("TransContent");
			
			NodeList nl = doc.getElementsByTagNameNS("*","DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
			Element pp = (Element) doc.getElementsByTagNameNS("*","PartialPayt").item(0);
			
			ppPaytNo = pp.getAttribute("PaytNo");
			ppTransKind = pp.getAttribute("TransKind");
			ppSumResidualPayt = XML.getOptionalIntAttr("SumResidualPayt", pp);
			
			Element accDoc = (Element) pp.getElementsByTagNameNS("*","AccDoc").item(0);			
			ppAccDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));
			ppAccDocNo = accDoc.getAttribute("AccDocNo");
		}

	}

	@Override
	public void generateFromXMLByType(Element gendoc)
	{
		transKind = "16";
		purpose = "Тестовый платежный ордер";
		
		ppPaytNo = "01";
		ppTransKind = "01";
		ppSumResidualPayt = 100;
		ppAccDocNo = "01";
		ppAccDocDate = Settings.operDate;	
		transContent = "Частичная оплата";
		
		chargeOffDate = Settings.operDate;
		receiptDate = Settings.operDate;
		fileDate = Settings.operDate;
		
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
			"0, 'ED105', " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", " + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "." + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + ", " + DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc) + ",\r\n" + 
			DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc) + ", " + DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(transKind) + ", " + DB.toString(priority) + ", null, " + DB.toString(purpose) + ", null, null,\r\n" + 
			"null, " + DB.toString(ppPaytNo) + ", " + DB.toString(ppTransKind) + ", " + DB.toString(ppAccDocNo) + ", " + DB.toString(ppAccDocDate) + ", " + DB.toString(ppSumResidualPayt) + ", " + DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", " + DB.toString(transContent)+ ", null, \r\n" + 
			DB.toString(chargeOffDate) + ", null, null, " + DB.toString(tax.drawerStatus) + ", " + DB.toString(payer.kpp) + ", " + DB.toString(payee.kpp) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.paytReason) + ",\r\n" + 
			DB.toString(tax.taxPeriod) + ", " + DB.toString(tax.docNo) + ", " + DB.toString(tax.docDate) + ", " + DB.toString(tax.taxPaytKind) + ", 'E', null, " + DB.toString(filename) + ", null, null)";			
			db.st.executeUpdate(query);
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}

	@Override
	public void insertIntoDbVer(int idPacet, String filename) 
	{

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
			"2, null, 0, 0, 'ED105'," + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ",\r\n" + 
			"null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", '" + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "," + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + "',\r\n" +
			DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc)+ ", " + DB.toString(payee.kpp) + ",\r\n" + 
			DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc)+ ", " + DB.toString(payer.kpp) + ",\r\n" +  
			DB.toString(transKind) + ", " + DB.toString(priority) + ", " + DB.toString(purpose) + ", null, null, null,\r\n" + 
			DB.toString(ppPaytNo) + ", " + DB.toString(ppTransKind) + ", " + DB.toString(ppAccDocNo) + ", " + DB.toString(ppAccDocDate) + ", " + DB.toString(ppSumResidualPayt) + ",\r\n" +
			DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", " + DB.toString(transContent)+ ", " +  DB.toString(chargeOffDate) + ", null, null,\r\n" + 
			DB.toString(tax.drawerStatus) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.taxPeriod) + ", " + DB.toString(tax.docNo) + ", " + DB.toString(tax.docDate) + ", " + DB.toString(tax.taxPaytKind) + ", "  + DB.toString(tax.paytReason) + ",\r\n" + 
			"null, 'E', null, " + DB.toString(filename) + ", null, null)";			
			db.st.executeUpdate(query);
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}
	
	@Override
	public String toStr(String razd, boolean addShift) {
		
		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd +
		transKind + razd + razd + Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + razd +
		paytKind.toString() + razd + payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd +
		payer.inn + razd + payer.kpp + razd + (addShift ? "+{ExtEnd}" : "") + payer.name + razd + payee.bic + razd + payee.correspAcc + razd +
		payee.personalAcc + razd + payee.inn + razd + payee.kpp + razd + (addShift ? "+{ExtEnd}" : "") + payee.name + razd +
		priority + razd + tax.drawerStatus;
		if(!tax.drawerStatus.equals("") && tax.drawerStatus != null)
			str = str + razd + tax.cbc + razd + tax.okato + razd + tax.paytReason + razd + tax.taxPeriod + razd + tax.docNo + razd + tax.docDate + razd + tax.taxPaytKind;

		str = str + razd + purpose + razd + ppPaytNo + razd + ppTransKind + razd + ppAccDocNo + razd + new SimpleDateFormat("ddMMyyyy").format(ppAccDocDate) + razd +
		Integer.toString(ppSumResidualPayt).substring(0, Integer.toString(ppSumResidualPayt).length() - 2) + "." + 
		Integer.toString(ppSumResidualPayt).substring(Integer.toString(ppSumResidualPayt).length() - 2, Integer.toString(ppSumResidualPayt).length()) + razd + 
		transContent + razd +
		//new SimpleDateFormat("ddMMyyyy").format(chargeOffDate) + razd + new SimpleDateFormat("ddMMyyyy").format(receiptDate) + razd + razd + razd;
		razd + razd + razd + razd;
		return str;	
	}
}
