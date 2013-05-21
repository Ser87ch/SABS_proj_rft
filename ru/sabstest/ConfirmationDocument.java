package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * ������������� ��� ���
 * @author Admin
 *
 */
public class ConfirmationDocument {
	//��������� ��
	public int edNo; //����� �� � ������� �������
	public Date edDate; //���� ����������� ��
	public String edAuthor; //���������� ������������� ����������� �� (���)
	
	public int sum; //�����
	public String transKind; //��� ��������
	public String resultCode; //��� ���������� ��������� ��
	
	//��������� ��������� ���������� ���������
	public int accDocNo; //����� ���������� ���������
	public Date accDocDate; //���� ������� ���������� ���������
	
	public Client payer; //�����������
	public Client payee; //����������
	
	//��������� ��������� ��
	public int iEdNo; //����� �� � ������� �������
	public Date iEdDate; //���� ����������� ��
	public String iEdAuthor; //���������� ������������� ����������� �� (���)
	
	/**
	 * ������� ������� ED216
	 * @param doc ��������
	 * @return �������
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
		
		rootElement.appendChild(payer.createXMLShortElement(doc, "ShortPayer"));
		rootElement.appendChild(payee.createXMLShortElement(doc, "ShortPayee"));
		return rootElement;
	}
	
	/**
	 * ����������� �������� ��������� ���������� ���������
	 * @param doc �������
	 */
	public void readED(Element doc)
	{
		edNo = Integer.parseInt(doc.getAttribute("EDNo"));
		edDate = Date.valueOf(doc.getAttribute("EDDate"));
		edAuthor = doc.getAttribute("EDAuthor");

		
		sum = Integer.parseInt(doc.getAttribute("Sum"));
		transKind = doc.getAttribute("TransKind");		
		resultCode = doc.getAttribute("ResultCode");

		Element accDoc = (Element) doc.getElementsByTagName("AccDoc").item(0);			
		accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));

		payer = new Client();
		payer.readED((Element) doc.getElementsByTagName("ShortPayer").item(0));
		payee = new Client();
		payee.readED((Element) doc.getElementsByTagName("ShortPayee").item(0));
		
	}
	
	public void generateFromPaymentDocument(PaymentDocument pd, String author)
	{
		resultCode = pd.resultCode;
		
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
			
			String type="";
			
			if(transKind == "01")
				type = "ED101";
			else if(transKind == "02")
				type = "ED103";
			else if(transKind == "06")
				type = "ED104";
			else if(transKind == "16")
				type = "ED105";
			
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
