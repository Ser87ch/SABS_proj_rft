
package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ListIterator;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Класс Платежное поручение
 * @author Admin
 *
 */
public class PaymentOrder extends PaymentDocument {
	public PaymentOrder()
	{
		super();
	}

	@Override
	public Element createED(Document doc)
	{
		Element rootElement = doc.createElement("ED101");		

		addCommonEDElements(doc, rootElement);		

		if(tax != null && !tax.drawerStatus.equals(""))
			rootElement.appendChild(tax.createXMLElement(doc));

		return rootElement;
	}


	@Override
	public void readED(Element doc)
	{
		
		if(doc.getLocalName().equals("ED101"))
		{
			readCommonEDElements(doc);

			NodeList nl = doc.getElementsByTagNameNS("*","DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
			}

		}
	}

	@Override
	public void generateFromXMLByType(Element gendoc)
	{
		transKind = "01";
		purpose = "Тестовое платежное поручение";		
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
			"0, 'ED101', " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" + 
			"null, " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ", " + DB.toString(paytKind) + ", " + DB.toString(sum).substring(0,DB.toString(sum).length() - 2) +  "." + DB.toString(sum).substring(DB.toString(sum).length() - 2, DB.toString(sum).length()) + ", " + DB.toString(payer.inn) + ", " + DB.toString(payer.name) + ", " + DB.toString(payer.personalAcc) + ", " + DB.toString(payer.bic) + ", " + DB.toString(payer.correspAcc) + ",\r\n" + 
			DB.toString(payee.bic) + ", " + DB.toString(payee.correspAcc) + ", " + DB.toString(payee.inn) + ", " + DB.toString(payee.name) + ", " + DB.toString(payee.personalAcc) + ", " + DB.toString(transKind) + ", " + DB.toString(priority) + ", null, " + DB.toString(purpose) + ", null, null,\r\n" + 
			"null, null, null, null, null, null, " + DB.toString(receiptDate) + ", " + DB.toString(fileDate) + ", null, null, \r\n" + 
			DB.toString(chargeOffDate) + ", null, null, " + DB.toString(tax.drawerStatus) + ", " + DB.toString(payer.kpp) + ", " + DB.toString(payee.kpp) + ", " + DB.toString(tax.cbc) + ", " + DB.toString(tax.okato) + ", " + DB.toString(tax.paytReason) + ",\r\n" + 
			DB.toString(tax.taxPeriod) + ", " + DB.toString(tax.docNo) + ", " + DB.toString(tax.docDate) + ", " + DB.toString(tax.taxPaytKind) + ", 'E', null, " + DB.toString(filename) + ", null, null)";			
			db.st.executeUpdate(query);
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}

	
	public void insertIntoDbVer(int idPacet, String filename, int iEdNo, Date iEdDate, String iEdAuthor) 
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
			"2, null, 0, 0, 'ED101'," + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ",\r\n" + 
			DB.toString(iEdNo) + "," + DB.toString(iEdDate) + "," + DB.toString(iEdAuthor) + ",\r\n" + 
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

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}
	
	@Override
	public void insertIntoDbVer(int idPacet, String filename) 
	{
		insertIntoDbVer(idPacet, filename, 0, null, null);	
	}
	public void generateReturnDocument(PaymentDocument pd, String author, String resultCode)
	{
		edNo = pd.edNo + 1000;
		edDate = pd.edDate;
		edAuthor = author;

		paytKind = pd.paytKind;
		sum = pd.sum;
		transKind = "01";
		priority = pd.priority;
		receiptDate = pd.receiptDate;
		fileDate = pd.fileDate;
		chargeOffDate = pd.chargeOffDate;
		systemCode = pd.systemCode;
					
		accDocNo = pd.accDocNo;
		accDocDate = pd.accDocDate;

		payer = new Client(Settings.bik,"30811810000000000003"); 
		payer.contrrazr();
		payer.name = "Возврат ошибочного электронного платежного документа";
		
		payee = pd.payer;
		

		purpose = "Возврат ошибочного электронного платежного документа " +  String.format("%06d", sum) + " с датой составления "
		+ new SimpleDateFormat("dd/MM/yyyy").format(edDate) +", УИС " 
		+ edAuthor + " на сумму " + Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + " код возврата " + resultCode ;
	}
}
