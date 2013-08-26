package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ED243 extends Packet implements Generate<Element>, ReadED{

	public String edDefineRequestCode; //код запроса

	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)

	public int accDocNo; //номер расчетного документа
	public Date accDocDate; //Дата выписки расчетного документа

	public String payerAcc;
	public String payerName;
	public String payeeAcc;
	public String payeeName;
	public int sum;

	public String edDefineRequestText;
	public List<String> edFieldList;
	public List<Integer> edReestrInfo; 

	static int i = 200;
	
	public ED243()
	{
		isVER = false;
		i++;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((accDocDate == null) ? 0 : accDocDate.hashCode());
		result = prime * result + accDocNo;
		result = prime
		* result
		+ ((edDefineRequestCode == null) ? 0 : edDefineRequestCode
				.hashCode());
		result = prime * result
		+ ((edFieldList == null) ? 0 : edFieldList.hashCode());
		result = prime * result
		+ ((edReestrInfo == null) ? 0 : edReestrInfo.hashCode());
		result = prime * result
		+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
		result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
		result = prime * result
		+ ((payeeAcc == null) ? 0 : payeeAcc.hashCode());
		result = prime * result
		+ ((payerAcc == null) ? 0 : payerAcc.hashCode());
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
		ED243 other = (ED243) obj;
		if (accDocDate == null) {
			if (other.accDocDate != null)
				return false;
		} else if (!accDocDate.equals(other.accDocDate))
			return false;
		if (accDocNo != other.accDocNo)
			return false;
		if (edDefineRequestCode == null) {
			if (other.edDefineRequestCode != null)
				return false;
		} else if (!edDefineRequestCode.equals(other.edDefineRequestCode))
			return false;
		if (edFieldList == null) {
			if (other.edFieldList != null)
				return false;
		} else if (!edFieldList.equals(other.edFieldList))
			return false;
		if (edReestrInfo == null) {
			if (other.edReestrInfo != null)
				return false;
		} else if (!edReestrInfo.equals(other.edReestrInfo))
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
		if (payeeAcc == null) {
			if (other.payeeAcc != null)
				return false;
		} else if (!payeeAcc.equals(other.payeeAcc))
			return false;
		if (payerAcc == null) {
			if (other.payerAcc != null)
				return false;
		} else if (!payerAcc.equals(other.payerAcc))
			return false;
		if (sum != other.sum)
			return false;
		return true;
	}

	@Override
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			
			int idPacet = insertIntoDBPacket(db, sum, true);
			
//			String query =  "INSERT INTO [dbo].[epay_ED243DEF]([ID_PACKET], [ID_ARM], [AccDocNo], [AccDocDate],\r\n" + 
//			" [PayeeAcc], [PayerAcc], [Sum], [PayerName], [PayeeName], [EDDefineRequestText],\r\n" + 
//			" [PayeeINN], [PayerINN], [EnterDate], [PayeeCorrAcc], [PayeeBIC], [Purpose], [Address])\r\n" + 
//			"VALUES(null, '0', " + DB.toString(accDocNo) + ", " + DB.toString(accDocDate) + ",\r\n" +  
//			DB.toString(payeeAcc) + ", " + DB.toString(payerAcc) + ", " + DB.toString(sum) + ", " + DB.toString(payerName) + ", " + DB.toString(payeeName) + ", " + DB.toString(edDefineRequestText) + ",\r\n" +   
//			"null, null, null, null, null, null, null)";			
//			db.st.executeUpdate(query);
//
//
//			int idED243 = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_ED243DEF) from dbo.epay_ED243DEF"));

			String query =  "INSERT [dbo].[UFEBS_Es201]([ID_PACET], [ID_DEPART], [EdNo], [EdDate],\r\n" + 
			" [EdAuthor], [EdReceiv], [CtrlCode], [CtrlTime], [Annotat],\r\n" + 
			" [MsgId], [IEdNo], [IEdDate], [IEdAuth], [FTime], [EsidCod],\r\n" + 
			" [PEpdNo], [PacDate], [PAuthor], [BeginDat], [EndDat], [BIC],\r\n" + 
			" [ACC], [Annotat1], [StopReas], [ID_ARM])\r\n" + 
			"VALUES(" + DB.toString(idPacet) + ", null, " + DB.toString(edNo) + ", " + DB.toString(edDate) + ",\r\n" +
			DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(edDefineRequestCode) + ", null, null,\r\n" + //" + DB.toString(idED243) +  "
			"null, " + DB.toString(iEdNo) + ", " + DB.toString(iEdDate) + ", " + DB.toString(iEdAuthor) + ", null, '43',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" +
			"null, null, null, '0')";			
			db.st.executeUpdate(query);

			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}

	}

	@Override
	public void setFileName() {
		if(edAuthor.substring(0,7).equals(Settings.bik.substring(2))) //особый клиент
			filename = "K";
		else
			filename = "B";

		filename = filename + Integer.toString(Integer.parseInt(new SimpleDateFormat("MM").format(edDate)),36)
		+ Integer.toString(Integer.parseInt(new SimpleDateFormat("dd").format(edDate)),36);

		if(edAuthor.substring(0,7).equals(Settings.bik.substring(2))) //особый клиент
			filename = filename + edAuthor.substring(2,4) + edAuthor.substring(7,10) + "." + String.format("%03d", edNo);
		else
			filename = filename + edAuthor.substring(2,7) + "." + String.format("%03d", edNo);

	}

	@Override
	public void readXML(Element root) {
		super.readXML(root);
		edDefineRequestCode = root.getAttribute("EDDefineRequestCode");

		Element ied = (Element) root.getElementsByTagNameNS("*", "OriginalEPD").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");

		Element ri = (Element) root.getElementsByTagNameNS("*", "EDDefineRequestInfo").item(0);
		accDocNo = Integer.parseInt(ri.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(ri.getAttribute("AccDocDate"));
		payerAcc = ri.getAttribute("PayerAcc");
		payeeAcc = ri.getAttribute("PayeeAcc");	
		sum = Integer.parseInt(ri.getAttribute("Sum"));

		payerName = ri.getElementsByTagNameNS("*", "PayerName").item(0).getTextContent();
		payeeName = ri.getElementsByTagNameNS("*", "PayeeName").item(0).getTextContent();
		edDefineRequestText = ri.getElementsByTagNameNS("*", "EDDefineRequestText").item(0).getTextContent();

		NodeList nl = ri.getElementsByTagNameNS("*", "EDFieldList");
		if(nl != null && nl.getLength() != 0)
		{
			edFieldList = new ArrayList<String>();

			for(int i = 0; i < nl.getLength(); i++)
				edFieldList.add(((Element)nl.item(i)).getElementsByTagNameNS("*", "FieldNo").item(0).getTextContent());

			Collections.sort(edFieldList);
		}

		nl = ri.getElementsByTagNameNS("*", "EDReestrInfo");
		if(nl != null && nl.getLength() != 0)
		{
			edReestrInfo = new ArrayList<Integer>();

			for(int i = 0; i < nl.getLength(); i++)
				edReestrInfo.add(Integer.parseInt(((Element)nl.item(i)).getElementsByTagNameNS("*", "TransactionID").item(0).getTextContent()));

			Collections.sort(edReestrInfo);
		}
	}

	public boolean generateFrom(Element root)
	{
		iEdNo = Integer.parseInt(root.getAttribute("EDNo"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		edDefineRequestCode = root.getAttribute("EDDefineRequestCode");

		Client payer = ClientList.getClient(root.getAttribute("IdPayer"));
		Client payee = ClientList.getClient(root.getAttribute("IdPayee"));

		edNo = i;
		edDate = Settings.operDate;
		edAuthor = payer.edAuthor;
		edReceiver = payee.edAuthor;

		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];

		iEdDate = Settings.operDate;
		iEdAuthor = edReceiver;

		accDocNo = edNo;
		accDocDate = Settings.operDate;

		payerAcc = payer.personalAcc;
		payerName = payer.name;
		payeeAcc = payee.personalAcc;
		payeeName = payee.name;

		edDefineRequestText = "Текст";

		setFileName();
		
		return true;

	}


	public void readEncodedFile(File src, boolean isUTF)
	{
		readXML(getEncodedElement(src.getAbsolutePath(), isUTF));
		filename = src.getName();
	}

	@Override
	public int compareTo(ReadED o) {
		return compareTo((Packet) o);
	}
}
