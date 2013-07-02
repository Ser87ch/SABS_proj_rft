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
 * Пакет ЭСИС
 * @author Admin
 *
 */
public class ConfirmationDocumentList extends Packet{
	private List<ConfirmationDocument> cdList;

	public int edNo;
	public Date edDate;
	public String edAuthor;
	public String edReceiver;
	public int edQuantity;
	public int sum;
	public String packetCode;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cdList == null) ? 0 : cdList.hashCode());
		result = prime * result
				+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result + edQuantity;
		result = prime * result
				+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
		result = prime * result
				+ ((packetCode == null) ? 0 : packetCode.hashCode());
		result = prime * result + sum;
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
		ConfirmationDocumentList other = (ConfirmationDocumentList) obj;
		if (cdList == null) {
			if (other.cdList != null)
				return false;
		} else if (!cdList.equals(other.cdList))
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
		if (edQuantity != other.edQuantity)
			return false;
		if (edReceiver == null) {
			if (other.edReceiver != null)
				return false;
		} else if (!edReceiver.equals(other.edReceiver))
			return false;
		if (packetCode == null) {
			if (other.packetCode != null)
				return false;
		} else if (!packetCode.equals(other.packetCode))
			return false;
		if (sum != other.sum)
			return false;
		return true;
	}

	@Override
	public void setFileName()
	{
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".PacketESIDVER_RYM";
	}

	/** 
	 * создает XML ЭПД
	 * @param fl полный путь к файлу
	 */
	@Override
	public void createFile(String folder)
	{
		Document doc = XML.createNewDoc();
		Element root = doc.createElement("PacketESIDVER_RYM");
		doc.appendChild(root);

		root.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");

		root.setAttribute("EDNo", Integer.toString(edNo));
		root.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));
		root.setAttribute("EDAuthor", edAuthor);
		root.setAttribute("EDReceiver", edReceiver);
		root.setAttribute("EDQuantity", Integer.toString(edQuantity));
		root.setAttribute("Sum", Integer.toString(sum));
		root.setAttribute("PacketCode", packetCode);

		ListIterator <ConfirmationDocument> iter = cdList.listIterator();
		while(iter.hasNext())
		{
			ConfirmationDocument cd = iter.next();
			root.appendChild(cd.createED(doc));
		}

		XML.createXMLFile(doc, folder + filename);
	}

	void readFile(String src)
	{

		Element root = XML.getXMLRootElement(src);

		if(!root.getLocalName().equals("PacketESIDVER_RYM"))		
			return;

		cdList = new ArrayList<ConfirmationDocument>();

		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		packetCode = root.getAttribute("PacketCode");

		NodeList nl = root.getElementsByTagNameNS("*","ED216");

		for(int i = 0; i < nl.getLength(); i++)
		{
			ConfirmationDocument cd = new ConfirmationDocument();
			cd.readED((Element) nl.item(i));
			cdList.add(cd);
		}
		Collections.sort(cdList);
	}

	public ConfirmationDocumentList() 
	{
		packetType = Packet.Type.PacketESIDVER;
	}

	public boolean generateFromPaymentDocumentList(PaymentDocumentList pdl)
	{

		firstSign = new Sign(Settings.Sign.keyobr,Settings.Sign.signobr);
		secondSign = new Sign(Settings.Sign.keycontr,Settings.Sign.signcontr);
		cdList = new ArrayList <ConfirmationDocument>();		

		edNo = pdl.edNo + 500;
		edDate = pdl.edDate;
		edAuthor = Settings.rkc.substring(2) + "000"; //pdl.edReceiver;
		edReceiver = Settings.bik.substring(2) + "000";

		packetCode = "1";

		setFileName();

		for(int i = 0; i < pdl.size(); i++)
		{
			PaymentDocument pd = pdl.get(i);

			String resultCode = Settings.EsidList.getResultCodeByBIC(pd.payee.bic);
			if(resultCode != null && !resultCode.equals(""))
			{
				ConfirmationDocument cd = new ConfirmationDocument();
				cd.generateFromPaymentDocument(pd, edAuthor, resultCode);
				cdList.add(cd);		
			}
		}
		Collections.sort(cdList);

		if(cdList == null || cdList.size() == 0)
			return false;
		else
		{
			edQuantity = size();
			sum = sum();
			return true;
		}
	}


	/**
	 * @return количество документов в пакете
	 */
	public int size()
	{
		return cdList.size();
	}

	/**
	 * @return сумма документов в пакете
	 */
	public int sum() 
	{
		int sum = 0;
		ListIterator <ConfirmationDocument> iter = cdList.listIterator();
		while(iter.hasNext())
		{
			int i = iter.next().sum;
			sum = sum + i;

		}

		return sum;
	}


	@Override
	/**
	 * вставка пакета в БД ВЭР
	 * @param filename полный путь к файлу
	 */
	public void insertIntoDB()
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String uic = Settings.bik.substring(2) + "000";

			String query =  "INSERT INTO [dbo].[epay_Packet]\r\n" + 
			"([ID_Depart], [ID_ARM], [User_Insert], [InOutMode],\r\n" + 
			" [Date_Oper], [EDNo], [EDDate], [EDAuthor], [EDReceiver], [EDQuantity],\r\n" + 
			" [Summa], [SystemCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n" + 
			" [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n" + 
			" [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n" + 
			" [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate], \r\n" + 
			"[IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n" + 
			"VALUES(null, 0, null, 0,\r\n" + 
			DB.toString(Settings.operDate) + ", " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(edQuantity) + ",\r\n" + 
			DB.toString(sum) + ", " + DB.toString(packetCode) + ", null, 1, " + DB.toString(filename) + ", 0, 0, \r\n" + 
			" 0, null, null, null, " + DB.toString(uic) + ", " + DB.toString(uic) + ", 1, \r\n" +  //Mesto MesFrom?
			" 5, null, '20120202', 3, 1, 0, 0,\r\n" + 
			" 0, 4, 3, 0, 1, null, null,\r\n" + 
			" null, null, null, 20, '', NULL, NULL, NULL, NULL)";			
			db.st.executeUpdate(query);
			db.close();			 

			int idPacet = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_PACKET) from dbo.epay_Packet"));

			ListIterator <ConfirmationDocument> iter = cdList.listIterator();
			while(iter.hasNext())
			{
				iter.next().insertIntoDbVer(idPacet, filename);
			}			

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}

}
