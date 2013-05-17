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
	@Override
	void createFile(String fl)
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

		XML.createXMLFile(doc, fl);
	}

	void readFile(String src)
	{

		Element root = XML.getXMLRootElement(src);

		if(!root.getNodeName().equals("PacketESIDVER_RYM"))		
			return;

		cdList = new ArrayList<ConfirmationDocument>();

		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		packetCode = root.getAttribute("PacketCode");

		NodeList nl = root.getElementsByTagName("ED216");

		for(int i = 0; i < nl.getLength(); i++)
		{
			ConfirmationDocument cd = new ConfirmationDocument();
			cd.readED((Element) nl.item(i));
			cdList.add(cd);
		}
	}

	public ConfirmationDocumentList() 
	{
		packetType = Packet.Type.PacketESIDVER;
	}

	public boolean generateFromPaymentDocumentList(PaymentDocumentList pdl)
	{
		cdList = new ArrayList <ConfirmationDocument>();		

		edNo = pdl.edNo + 500;
		edDate = pdl.edDate;
		edAuthor = pdl.edReceiver;
		edReceiver = pdl.edAuthor;

		packetCode = "1";

		for(int i = 0; i < pdl.size(); i++)
		{
			PaymentDocument pd = pdl.get(i);

			if(!pd.resultCode.equals("") && (pd.resultCode != null))
			{
				ConfirmationDocument cd = new ConfirmationDocument();
				cd.generateFromPaymentDocument(pd, edAuthor);
				cdList.add(cd);
			}

		}

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



}
