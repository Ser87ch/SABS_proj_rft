package ru.sabstest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * ЭПД по возврату платежей
 * @author Admin
 *
 */
public class ReturnDocumentList {
	private List<ReturnDocument> rdList;
	
	public int edNo;
	public Date edDate;
	public String edAuthor;
	public String edReceiver;
	public int edQuantity;
	public int sum;
	
	ReturnDocumentList()
	{
		rdList = new ArrayList<ReturnDocument>();
	}
	
	static public class ReturnDocument{
		public PaymentOrder po;
		
		//реквизиты исходного ЭД
		public int iEdNo; //Номер ЭД в течение опердня
		public Date iEdDate; //Дата составления ЭД
		public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	}
}
