package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ED243 extends Packet implements Generate<Element>, ReadED{

	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	public String edReceiver; //УИС составителя
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

	public ED243()
	{
		isVER = false;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((accDocDate == null) ? 0 : accDocDate.hashCode());
		result = prime * result + accDocNo;
		result = prime * result
		+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime
		* result
		+ ((edDefineRequestCode == null) ? 0 : edDefineRequestCode
				.hashCode());
		result = prime * result
		+ ((edFieldList == null) ? 0 : edFieldList.hashCode());
		result = prime * result
		+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
		result = prime * result
		+ ((edReestrInfo == null) ? 0 : edReestrInfo.hashCode());
		result = prime * result
		+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
		result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
		result = prime * result
		+ ((payeeAcc == null) ? 0 : payeeAcc.hashCode());
		result = prime * result
		+ ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result
		+ ((payerAcc == null) ? 0 : payerAcc.hashCode());
		result = prime * result
		+ ((payerName == null) ? 0 : payerName.hashCode());
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
		if (edReceiver == null) {
			if (other.edReceiver != null)
				return false;
		} else if (!edReceiver.equals(other.edReceiver))
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
		if (payeeName == null) {
			if (other.payeeName != null)
				return false;
		} else if (!payeeName.equals(other.payeeName))
			return false;
		if (payerAcc == null) {
			if (other.payerAcc != null)
				return false;
		} else if (!payerAcc.equals(other.payerAcc))
			return false;
		if (payerName == null) {
			if (other.payerName != null)
				return false;
		} else if (!payerName.equals(other.payerName))
			return false;
		if (sum != other.sum)
			return false;
		return true;
	}

	@Override
	public void insertIntoDB() {
		// TODO Auto-generated method stub

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
		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");		
		edDefineRequestCode = root.getAttribute("EDDefineRequestCode");

		Element ied = (Element) root.getElementsByTagName("OriginalEPD").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");

		Element ri = (Element) root.getElementsByTagName("EDDefineRequestInfo").item(0);
		accDocNo = Integer.parseInt(ri.getAttribute("AccDocNo"));
		accDocDate = Date.valueOf(ri.getAttribute("AccDocDate"));
		payerAcc = ri.getAttribute("PayerAcc");
		payeeAcc = ri.getAttribute("PayeeAcc");	
		sum = Integer.parseInt(ri.getAttribute("Sum"));

		payerName = ri.getElementsByTagName("PayerName").item(0).getTextContent();
		payeeName = ri.getElementsByTagName("PayeeName").item(0).getTextContent();
		edDefineRequestText = ri.getElementsByTagName("EDDefineRequestText").item(0).getTextContent();

		NodeList nl = ri.getElementsByTagName("EDFieldList");
		if(nl != null && nl.getLength() != 0)
		{
			edFieldList = new ArrayList<String>();

			for(int i = 0; i < nl.getLength(); i++)
				edFieldList.add(((Element)nl.item(i)).getElementsByTagName("FieldNo").item(0).getTextContent());

			Collections.sort(edFieldList);
		}

		nl = ri.getElementsByTagName("EDReestrInfo");
		if(nl != null && nl.getLength() != 0)
		{
			edReestrInfo = new ArrayList<Integer>();

			for(int i = 0; i < nl.getLength(); i++)
				edReestrInfo.add(Integer.parseInt(((Element)nl.item(i)).getElementsByTagName("TransactionID").item(0).getTextContent()));

			Collections.sort(edReestrInfo);
		}
	}

	public boolean generateFrom(Element root)
	{
		firstSign = new Sign(root.getAttribute("key1"),root.getAttribute("profile1"));
		secondSign = new Sign(root.getAttribute("key2"),root.getAttribute("profile2"));

		iEdNo = Integer.parseInt(root.getAttribute("EDNo"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		edDefineRequestCode = root.getAttribute("EDDefineRequestCode");

		Client payer = ClientList.getClient(root.getAttribute("IdPayer"));
		Client payee = ClientList.getClient(root.getAttribute("IdPayee"));

		edNo = iEdNo + 1500;
		edDate = Settings.operDate;
		edAuthor = payer.edAuthor;
		edReceiver = payee.edAuthor;

		iEdDate = Settings.operDate;
		iEdAuthor = edReceiver;

		accDocNo = edNo;
		accDocDate = Settings.operDate;

		payerAcc = payer.personalAcc;
		payerName = payer.name;
		payeeAcc = payee.personalAcc;
		payeeName = payee.name;

		edDefineRequestText = "Текст";
		
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
