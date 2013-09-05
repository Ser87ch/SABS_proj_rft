package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Класс Платежное требование
 * @author Admin
 *
 */
public class ED103 extends PaymentDocument {

	public String paytCondition; //условия оплаты
	public int acptTerm; //срок для акцепта
	public Date docDispatchDate; //дата отсылки (вручения) плательщику предусмотренных договором документов
	public Date receiptDateCollectBank; // дата предоставления документов в банк
	public Date maturityDate; //окончание срока акцепта
	public int acptSum; //сумма исходного расчетного документа, предъявленного к акцепту
	
	public boolean is113Vvod = false, is113 = false;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + acptSum;
		result = prime * result + acptTerm;
		result = prime * result
				+ ((docDispatchDate == null) ? 0 : docDispatchDate.hashCode());
		result = prime * result
				+ ((maturityDate == null) ? 0 : maturityDate.hashCode());
		result = prime * result
				+ ((paytCondition == null) ? 0 : paytCondition.hashCode());
		result = prime
				* result
				+ ((receiptDateCollectBank == null) ? 0
						: receiptDateCollectBank.hashCode());
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
		ED103 other = (ED103) obj;
		if (acptSum != other.acptSum)
			return false;
		if (acptTerm != other.acptTerm)
			return false;
		if (docDispatchDate == null) {
			if (other.docDispatchDate != null)
				return false;
		} else if (!docDispatchDate.equals(other.docDispatchDate))
			return false;
		if (maturityDate == null) {
			if (other.maturityDate != null)
				return false;
		} else if (!maturityDate.equals(other.maturityDate))
			return false;
		if (paytCondition == null) {
			if (other.paytCondition != null)
				return false;
		} else if (!paytCondition.equals(other.paytCondition))
			return false;
		if (receiptDateCollectBank == null) {
			if (other.receiptDateCollectBank != null)
				return false;
		} else if (!receiptDateCollectBank.equals(other.receiptDateCollectBank))
			return false;
		return true;
	}

	public ED103()
	{
		super();
	}

	public ED103(boolean is113, boolean is113Vvod)
	{
		super();
		this.is113Vvod = is113Vvod;
		this.is113 = is113;
	}
	
	@Override
	@Deprecated
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
		if(doc.getLocalName().equals("ED103") || doc.getLocalName().equals("ED113"))
		{
			readCommonEDElements(doc);
			
			if(purpose.startsWith("1"))
				is113Vvod = true;
			
			paytCondition = doc.getAttribute("PaytCondition");
			acptTerm = XML.getOptionalIntAttr("AcptTerm", doc);
			docDispatchDate = XML.getOptionalDateAttr("DocDispatchDate", doc);
			receiptDateCollectBank = XML.getOptionalDateAttr("ReceiptDateCollectBank", doc);
			maturityDate = XML.getOptionalDateAttr("MaturityDate", doc);
			acptSum = XML.getOptionalIntAttr("AcptSum", doc);			
			
		}

	}
	
	@Override
	public void generateFromXMLByType(Element gendoc)
	{
		transKind = "02";
		purpose = "Тестовое платежное требование";
		docDispatchDate = Settings.operDate;
		receiptDateCollectBank = Settings.operDate;
		maturityDate = Settings.operDate;
		if(is113Vvod)
			purpose = "1" + purpose;
		if(gendoc.hasAttribute("PaytCondition"))
			paytCondition = gendoc.getAttribute("PaytCondition");
		else
			paytCondition = "1";
	}

	@Override
	public void insertIntoDbUfebs(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename) 
	{

		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String type = is113 ? "ED113" :"ED103";
			
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
			"0," + DB.toString(type) + ", " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", " + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "." + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + ", " + DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc) + ",\r\n" + 
			DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc) + ", " + DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(transKind) + ", " + DB.toString(priority) + ", null, " + DB.toString(purpose) + ", null, null,\r\n" + 
			"null, null, null, null, null, null, " + DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", null, null, \r\n" + 
			DB.toString(chargeOffDate) + ", " + DB.toString(receiptDateCollectBank) + ", null, " + DB.toString(tax.drawerStatus) + ", " + DB.toString(payer.kpp) + ", " + DB.toString(payee.kpp) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.paytReason) + ",\r\n" + 
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
			"2, null, 0, 0, 'ED103'," + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ",\r\n" + 
			"null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", '" + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "," + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + "',\r\n" +
			DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc)+ ", " + DB.toString(payee.kpp) + ",\r\n" + 
			DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc)+ ", " + DB.toString(payer.kpp) + ",\r\n" +  
			DB.toString(transKind) + ", " + DB.toString(priority) + ", " + DB.toString(purpose) + ", " + DB.toString(paytCondition) + ", " + DB.toString(acptTerm) + ", " + DB.toString(docDispatchDate) + ",\r\n" + 
			"null, null, null, null, null,\r\n" +
			DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", null, " +  DB.toString(chargeOffDate) + ", " + DB.toString(maturityDate) + ", " + DB.toString(receiptDateCollectBank) + ",\r\n" + 
			DB.toString(tax.drawerStatus) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.taxPeriod) + ", " + DB.toString(tax.docNo) + ", " + DB.toString(tax.docDate) + ", " + DB.toString(tax.taxPaytKind) + ", "  + DB.toString(tax.paytReason) + ",\r\n" + 
			DB.toString(acptSum) + ", 'E', null, " + DB.toString(filename) + ", null, null)";			
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
		transKind + razd + Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + razd; 
		
		str = str + ((paytKind.equals("P") || paytKind.equals("T")) ? " "+ razd : "") + razd +
		(is113Vvod ? "{ExtDown}" : "") + ((paytKind.equals("P") || paytKind.equals("T")) ? "+{TAB}{ExtLeft}" : "") + 
		(paytKind.equals("P") ? "{ExtLeft}" : "") + razd + 
		((paytKind.equals("P") || paytKind.equals("T")) ? razd : "");
		
		str = str + payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd +
		payer.inn + razd + 
		(addShift ? "+{ExtEnd}" : "") + payer.name + razd + payee.bic + razd + payee.correspAcc + razd +
		payee.personalAcc + razd + payee.inn + razd + 
		(addShift ? "+{ExtEnd}" : "") + payee.name + razd +
		priority + razd + tax.drawerStatus;
		if(!tax.drawerStatus.equals("") && tax.drawerStatus != null)
			str = str + razd + tax.cbc + razd + tax.okato + razd + tax.paytReason + razd + tax.taxPeriod + razd + tax.docNo + razd + tax.docDate + razd + tax.taxPaytKind;

		str = str + razd + purpose + razd;
		
		if(!is113Vvod)
			str = str + razd + new SimpleDateFormat("ddMMyyyy").format(chargeOffDate) + razd + new SimpleDateFormat("ddMMyyyy").format(receiptDate) + razd + paytCondition + razd + razd;
		else
		{
			str = str + Integer.toString(acptTerm) + razd + new SimpleDateFormat("ddMMyyyy").format(docDispatchDate) + razd +
			new SimpleDateFormat("ddMMyyyy").format(maturityDate) + razd + new SimpleDateFormat("ddMMyyyy").format(chargeOffDate) + razd + new SimpleDateFormat("ddMMyyyy").format(receiptDateCollectBank) + razd +
			((paytKind.equals("P") || paytKind.equals("T")) ? "" : new SimpleDateFormat("ddMMyyyy").format(receiptDate) + razd) +
			paytCondition + razd + razd;
		}
		return str;	
	}
}
