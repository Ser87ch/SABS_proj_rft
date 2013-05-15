package ru.sabstest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

/**
 * ЭПД по возврату платежей
 * @author Admin
 *
 */
public class ReturnDocumentList extends Packet{
	private List<ReturnDocument> rdList;
	
	public int edNo;
	public Date edDate;
	public String edAuthor;
	public String edReceiver;
	public int edQuantity;
	public int sum;
	
	ReturnDocumentList()
	{
		packetType = Packet.Type.PacketEPDVER_B;
		rdList = new ArrayList<ReturnDocument>();
	}
	
	static public class ReturnDocument{
		public PaymentOrder po;
		
		//реквизиты исходного ЭД
		public int iEdNo; //Номер ЭД в течение опердня
		public Date iEdDate; //Дата составления ЭД
		public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	}

	@Override
	boolean generateFromXML(Element packet) {
		return false;
		
	}

	@Override
	void createFile(String fl) {
		// TODO Auto-generated method stub
		
	}
}
