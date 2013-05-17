package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
		
	public boolean generateFromPaymentDocumentList(PaymentDocumentList pdl)
	{
		rdList = new ArrayList <ReturnDocument>();		

		edNo = pdl.edNo + 1000;
		edDate = pdl.edDate;
		edAuthor = Settings.rkc.substring(2) + "000";//pdl.edReceiver;
		edReceiver = pdl.edAuthor;
		
		for(int i = 0; i < pdl.size(); i++)
		{
			PaymentDocument pd = pdl.get(i);

			if(ErrorCode.contains(pd.resultCode))
			{
				ReturnDocument rd = new ReturnDocument();
				rd.generateFromPaymentDocument(pd, edAuthor);
				rdList.add(rd);
			}

		}
		
		
		if(rdList == null || rdList.size() == 0)
			return false;
		else
		{
			edQuantity = size();
			sum = sum();
			return true;
		}
	}
	

	@Override
	void createFile(String fl) 
	{
		Document doc = XML.createNewDoc();
		Element root = doc.createElement("PacketEPDVER_B");
		doc.appendChild(root);

		root.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");

		root.setAttribute("EDNo", Integer.toString(edNo));
		root.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));
		root.setAttribute("EDAuthor", edAuthor);
		root.setAttribute("EDReceiver", edReceiver);
		root.setAttribute("EDQuantity", Integer.toString(edQuantity));
		root.setAttribute("Sum", Integer.toString(sum));
		

		ListIterator <ReturnDocument> iter = rdList.listIterator();
		while(iter.hasNext())
		{
			ReturnDocument rd = iter.next();
			root.appendChild(rd.createED(doc));
		}

		XML.createXMLFile(doc, fl);
		
	}
	
	void readFile(String src)
	{
		Element root = XML.getXMLRootElement(src);

		if(!root.getNodeName().equals("PacketEPDVER_B"))		
			return;

		rdList = new ArrayList<ReturnDocument>();

		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		

		NodeList nl = root.getElementsByTagName("VERReturnPayt");

		for(int i = 0; i < nl.getLength(); i++)
		{
			ReturnDocument rd = new ReturnDocument();
			rd.readED((Element) nl.item(i));
			rdList.add(rd);
		}
	}
	
	/**
	 * @return количество документов в пакете
	 */
	public int size()
	{
		return rdList.size();
	}

	/**
	 * @return сумма документов в пакете
	 */
	public int sum() 
	{
		int sum = 0;
		ListIterator <ReturnDocument> iter = rdList.listIterator();
		while(iter.hasNext())
		{
			int i = iter.next().po.sum;
			sum = sum + i;
		}

		return sum;
	}
}
