package ru.sabstest;

import java.io.File;
import java.sql.Date;

import org.w3c.dom.Element;

public class ED244 extends Packet implements ReadED, Generate<ED243> {

	public String edDefineRequestCode; //код запроса
	public String edDefineAnswerCode; //код ответа
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	@Override
	public int compareTo(ReadED o) {
		return compareTo((Packet) o);
	}

	@Override
	public void readEncodedFile(File src, boolean isUTF) {
		readXML(getEncodedElement(src.getAbsolutePath(), isUTF));
		filename = src.getName();
	}

	@Override
	public void readXML(Element root) {
		super.readXML(root);
		edDefineRequestCode = root.getAttribute("EDDefineRequestCode");
		edDefineAnswerCode = root.getAttribute("EDDefineAnswerCode");

		Element ied = (Element) root.getElementsByTagName("OriginalEPD").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");
	}

	@Override
	public boolean generateFrom(ED243 source) {
		edNo = source.edNo + 2500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdAuthor = source.edAuthor;
		iEdDate = source.edDate;
		iEdNo = source.edNo;
		
		edDefineRequestCode = source.edDefineRequestCode;
		edDefineAnswerCode = ""; //TODO разобраться с кодом ответа
		return true;
	}

	@Override
	public void setFileName() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void insertIntoDB() {
		throw new UnsupportedOperationException();
		
	}

}
