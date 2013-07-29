package ru.sabstest;

import java.sql.Date;
import java.util.List;

import org.w3c.dom.Element;

public class ED243 extends Packet {

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
	
	@Override
	@Deprecated
	public void createFile(String folder) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertIntoDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileName() {
		// TODO Auto-generated method stub

	}

	@Override
	public void readXML(Element root) {
		// TODO Auto-generated method stub

	}
	
	public void generateFromXML(Element root)
	{
		
	}

}
