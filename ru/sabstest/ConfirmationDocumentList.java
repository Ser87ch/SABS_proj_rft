package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	
	/** 
	 * создает XML ЭПД
	 * @param fl полный путь к файлу
	 */
	public void createEPD(String fl)
	{
		Document doc = XML.createNewDoc();
		Element root = doc.createElement("PacketEPD");
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

		XML.createXMLFile(doc, fl);
	}

	public ConfirmationDocumentList() {
		packetType = Packet.Type.PacketESIDVER;
	}
	
	public void generate(PaymentDocumentList pdl)
	{
		cdList = new ArrayList <ConfirmationDocument>();
		
		
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
