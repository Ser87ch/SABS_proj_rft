package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PaymentWarrant extends PaymentDocument {
	public String transContent; //содержание операции
	
	public String ppPaytNo; //номер частичного платежа
	public String ppTransKind; //шифр платежного документа
	public int ppSumResidualPayt; //сумма остатка платежа
	public String ppAccDocNo; //номер исходного расчетного документа
	public Date ppAccDocDate; //дата исходного расчетного дкоумента
	
	public PaymentWarrant()
	{
		super();
	}
	
	@Override
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
		if(doc.getTagName().equals("ED105"))
		{
			readCommonEDElements(doc);
			
			transContent = doc.getAttribute("TransContent");
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}
			
			Element pp = (Element) doc.getElementsByTagName("PartialPayt").item(0);
			
			ppPaytNo = pp.getAttribute("PaytNo");
			ppTransKind = pp.getAttribute("TransKind");
			ppSumResidualPayt = XML.getOptionalIntAttr("SumResidualPayt", pp);
			
			Element accDoc = (Element) pp.getElementsByTagName("AccDoc").item(0);			
			ppAccDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));
			ppAccDocNo = accDoc.getAttribute("AccDocNo");
		}

	}

	@Override
	public void generateFromXML(Element gendoc, int edNo, String edAuthor)
	{
		
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
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", " + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "." + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + ",\r\n" +
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
}
