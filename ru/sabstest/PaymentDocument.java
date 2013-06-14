package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;




/**
 * ����������� ����� ��������� ��������
 * @author Admin
 *
 */
abstract public class PaymentDocument {
	//��������� ��
	public int edNo; //����� �� � ������� �������
	public Date edDate; //���� ����������� ��
	public String edAuthor; //���������� ������������� ����������� �� (���)

	//�������� ���������
	public String paytKind; //��� �������
	public int sum; //�����
	public String transKind; //��� ��������
	public String priority; //����������� �������
	public Date receiptDate; //���� ����������� � ���� �����������
	public Date fileDate; //���� ��������� � ���������
	public Date chargeOffDate; //���� �������� �� ����� �����������
	public String systemCode; //������� ������� ��������� (����������� ���� �� �� ������� ������)

	//��������� ��������� ���������� ���������
	public int accDocNo; //����� ���������� ���������
	public Date accDocDate; //���� ������� ���������� ���������

	public Client payer; //�����������
	public Client payee; //����������

	public String purpose; //���������� �������

	//������������� ����������
	DepartmentalInfo tax;


	//	public String resultCode;

	public PaymentDocument()
	{	
		this.accDocNo = 0;
		this.accDocDate = new Date(0);
		this.transKind = "";
		this.sum = 0;				
		//this.paytKind = "1";
		this.priority = "6";
		tax = new DepartmentalInfo("", "", "", "", "", "", "", "");			
		this.purpose = "����";
		//this.chargeOffDate = new Date(0);
		//this.receiptDate = new Date(0);	
		this.edNo = 0;

	}

	@Override
	public String toString()
	{
		return toStr(" ", false);
	}


	/**
	 * @param razd ����������� ����������
	 * @param addShift ����������� �� Shift
	 * @return ������ � ����������� ���������
	 */
	abstract public String toStr(String razd, boolean addShift);


	/**
	 * @param razd ����������� ����������
	 * @return ������ ��� ������������ �����
	 */
	public String toStrContr(String razd){

		String str = "";

		str = Integer.toString(accDocNo) + razd + new SimpleDateFormat("ddMMyyyy").format(accDocDate) + razd + transKind + razd +
		Integer.toString(sum).substring(0, Integer.toString(sum).length() - 2) + "." + 
		Integer.toString(sum).substring(Integer.toString(sum).length() - 2, Integer.toString(sum).length()) + razd + paytKind.toString() + razd + 
		payer.bic + razd + payer.correspAcc + razd + payer.personalAcc + razd + payee.bic + razd + payee.correspAcc + razd +
		payee.personalAcc + razd + priority;

		return str;	
	}

	/**
	 * ������� ������� � ����������� ���������
	 * @param doc ��������
	 * @return �������
	 */
	abstract public Element createED(Document doc);


	/**
	 * ��������� ��������� �� ��������
	 * @param doc �������
	 */
	abstract public void readED(Element doc);

	/**
	 * ���������� �������� �� ������ �������� �� xml ���������
	 * @param gendoc �������
	 * @param edNo ����� ���������
	 * @param edAuthor ���
	 */
	abstract public void generateFromXMLByType(Element gendoc);

	public void generateFromXML(Element gendoc, int edNo, String edAuthor, int sum)
	{
		this.edNo = edNo;
		edDate = Settings.operDate;
		//this.edAuthor = edAuthor;
		//paytKind = "1";
		this.sum = sum;
		if(this.sum == 0)
			this.sum = (int) (new Random().nextFloat() * 10000);		

		priority = "6";
		accDocNo = edNo;
		accDocDate = Settings.operDate;

		chargeOffDate = Settings.operDate;
		receiptDate = Settings.operDate;

		payer = ClientList.getClient(gendoc.getAttribute("IdPayer"));
		payee = ClientList.getClient(gendoc.getAttribute("IdPayee"));	

		if(edAuthor.substring(0,7).equals(Settings.bik.substring(2)))
			this.edAuthor = edAuthor;
		else
			this.edAuthor = payer.bic.substring(2) + "000";

		generateFromXMLByType(gendoc);
		
		String errorCode = gendoc.getAttribute("ErrorCode");
		
		if(errorCode != null && !errorCode.equals(""))
			ErrorCode.addError(this, Integer.parseInt(errorCode));
	}

	/**
	 * ������� ������ � �� ��� �������� xml ���
	 * @param idPacet �� ������
	 * @param pEDNo ����� ������
	 * @param pacDate ���� ������
	 * @param pAuthor ���
	 * @param filename ����
	 */
	abstract public void insertIntoDbUfebs(int idPacet, int pEDNo, Date pacDate, String pAuthor, String filename);

	/**
	 * ������� ������ � �� ��� �������� xml ���
	 * @param idPacet �� ������
	 * @param filename ����
	 */
	abstract public void insertIntoDbVer(int idPacet, String filename);

	/**
	 * ������� xml ���� � ����������� ���������
	 * @param ������ ���� � �����
	 */
	public void createXML(String fl)
	{
		Document doc = XML.createNewDoc();
		doc.appendChild(createED(doc));
		XML.createXMLFile(doc, fl);
	}


	/**
	 * ������� ��������� �� XML
	 * @param src ���� � �����
	 */
	public void readXML(String src)
	{
		readED(XML.getXMLRootElement(src));
	}

	/**
	 * ����������� �������� ��������� ���������� ��������� � ��������
	 * @param doc ��������
	 * @param rootElement �������
	 */
	public void addCommonEDElements(Document doc, Element rootElement)
	{
		rootElement.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");
		rootElement.setAttribute("EDNo", Integer.toString(edNo));	
		rootElement.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));

		rootElement.setAttribute("EDAuthor", edAuthor);
		rootElement.setAttribute("PaytKind", paytKind);
		rootElement.setAttribute("Sum", Integer.toString(sum));
		rootElement.setAttribute("TransKind", transKind);
		rootElement.setAttribute("Priority", priority);

		XML.setOptinalAttr(rootElement, "ReceiptDate", receiptDate);
		XML.setOptinalAttr(rootElement, "FileDate", fileDate);
		XML.setOptinalAttr(rootElement, "ChargeOffDate", chargeOffDate);
		XML.setOptinalAttr(rootElement, "SystemCode", systemCode);		

		Element accDoc = doc.createElement("AccDoc");
		rootElement.appendChild(accDoc);

		accDoc.setAttribute("AccDocNo", Integer.toString(accDocNo));
		accDoc.setAttribute("AccDocDate", new SimpleDateFormat("yyyy-MM-dd").format(accDocDate));		

		rootElement.appendChild(payer.createXMLElement(doc, "Payer"));
		rootElement.appendChild(payee.createXMLElement(doc, "Payee"));

		XML.createNode(doc, rootElement, "Purpose", purpose);
	}

	/**
	 * ����������� �������� ��������� ���������� ���������
	 * @param doc �������
	 */
	public void readCommonEDElements(Element doc)
	{
		edNo = Integer.parseInt(doc.getAttribute("EDNo"));
		edDate = Date.valueOf(doc.getAttribute("EDDate"));
		edAuthor = doc.getAttribute("EDAuthor");

		paytKind = doc.getAttribute("PaytKind");
		sum = Integer.parseInt(doc.getAttribute("Sum"));
		transKind = doc.getAttribute("TransKind");
		priority = doc.getAttribute("Priority");
		receiptDate = XML.getOptionalDateAttr("ReceiptDate", doc);
		fileDate = XML.getOptionalDateAttr("FileDate", doc);
		chargeOffDate = XML.getOptionalDateAttr("ChargeOffDate", doc);
		systemCode = doc.getAttribute("SystemCode");

		Element accDoc = (Element) doc.getElementsByTagNameNS("*","AccDoc").item(0);			
		accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));

		payer = new Client();
		payer.readED((Element) doc.getElementsByTagNameNS("*","Payer").item(0));
		payee = new Client();
		payee.readED((Element) doc.getElementsByTagNameNS("*","Payee").item(0));

		purpose = XML.getChildValueString("Purpose", doc);
	}

	/**
	 * @param src ������ ���� � �����
	 * @return ��������� �������� ��������� �� xml
	 */
	public static PaymentDocument createDocFromXML(String src)
	{
		Element root = XML.getXMLRootElement(src);
		PaymentDocument pd = null;
		if(root.getLocalName().equals("ED101"))
		{
			pd = new PaymentOrder();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED103"))
		{
			pd = new PaymentRequest();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED104"))
		{
			pd = new CollectionOrder();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED105"))
		{
			pd = new PaymentWarrant();
			pd.readED(root);
		}
		else if(root.getLocalName().equals("ED108"))
		{
			pd = new PaymentOrderRegister();
			pd.readED(root);
		}
		return pd;
	}


	public static PaymentDocument createByType(String type)
	{
		PaymentDocument pd = null;

		if(type.equals("101"))
			pd = new PaymentOrder();
		else if(type.equals("103"))
			pd = new PaymentRequest();
		else if(type.equals("104"))
			pd = new CollectionOrder();
		else if(type.equals("105"))
			pd = new PaymentWarrant();
		else if(type.equals("108"))
			pd = new PaymentOrderRegister();

		return pd;
	}
}
