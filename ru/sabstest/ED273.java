package ru.sabstest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ED273 extends Packet implements ReadED, Generate<Element> {

	public List<PaymentDocument> pdList;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pdList == null) ? 0 : pdList.hashCode());
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
		ED273 other = (ED273) obj;
		if (pdList == null) {
			if (other.pdList != null)
				return false;
		} else if (!pdList.equals(other.pdList))
			return false;
		return true;
	}

	public ED273()
	{
		isVER = false;
	}

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

		pdList = new ArrayList<PaymentDocument>();
		NodeList nl = root.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++)
			if(nl.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				PaymentDocument pd = PaymentDocument.createDocFromXML((Element) nl.item(i));
				if(pd != null)
					pdList.add(pd);
			}

		Collections.sort(pdList);
	}

	@Override
	public boolean generateFrom(Element root) {


		pdList = new ArrayList<PaymentDocument>();

		edNo = Integer.parseInt(root.getAttribute("EPDNo"));
		edDate = Settings.operDate;

		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");


		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];

		setFileName();

		int edNo = Integer.parseInt(root.getAttribute("EDFirstNo"));
		int sum = XML.getOptionalIntAttr("Sum", root);

		NodeList nl = root.getElementsByTagName("ED");

		for(int i = 0; i < nl.getLength(); i++)
		{
			Element ed = (Element) nl.item(i);

			String[] typeList = ed.getAttribute("Type").split(",");
			int quantity = Integer.parseInt(ed.getAttribute("Quantity"));

			for(String type:typeList)
			{
				for(int j = 0; j < quantity; j++)
				{
					PaymentDocument pd = PaymentDocument.createByTypeED273(type);					

					pd.generateFromXML(ed, edNo, edAuthor, sum);
					edNo++;
					if(sum != 0)
						sum++;
					pdList.add(pd);
				}
			}
		}		

		if(pdList == null || pdList.size() == 0)
			return false;
		else
		{

			Collections.sort(pdList);
			return true;
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
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			int idPacet = insertIntoDBPacket(db, 0, "1");


			String query =  "INSERT [dbo].[UFEBS_Es201]([ID_PACET], [ID_DEPART], [EdNo], [EdDate],\r\n" + 
			" [EdAuthor], [EdReceiv], [CtrlCode], [CtrlTime], [Annotat],\r\n" + 
			" [MsgId], [IEdNo], [IEdDate], [IEdAuth], [FTime], [EsidCod],\r\n" + 
			" [PEpdNo], [PacDate], [PAuthor], [BeginDat], [EndDat], [BIC],\r\n" + 
			" [ACC], [Annotat1], [StopReas], [ID_ARM])\r\n" + 
			"VALUES(" + DB.toString(idPacet) + ", null, " + DB.toString(edNo) + ", " + DB.toString(edDate) + ",\r\n" +
			DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(idPacet) + ", '1', null,\r\n" +
			"null, '', '', '', null, '273',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" +
			"null, '0', null, '0')";			
			db.st.executeUpdate(query);		

			db.close();



			ListIterator <PaymentDocument> iter = pdList.listIterator();
			while(iter.hasNext())
			{
				iter.next().insertIntoDbUfebs(idPacet, edNo, edDate, edAuthor, filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}

	}

}
