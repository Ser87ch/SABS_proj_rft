
package ru.sabstest;

import java.sql.Date;
import java.util.ListIterator;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


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
		if(doc.getTagName() == "ED101")
		{
			readCommonEDElements(doc);
			
			NodeList nl = doc.getElementsByTagName("DepartmentalInfo");
			if(nl.getLength() == 1)
			{
				tax = new DepartmentalInfo();
				tax.readED((Element) nl.item(0));
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

	@Override
	public void insertIntoDB(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename) 
	{
	
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			
			String query = "INSERT INTO [sabs_zapd].[dbo].[UFEBS_Epd]\r\n" + 
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
}
