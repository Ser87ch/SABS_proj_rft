package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ED274 extends Packet implements Generate<ED273>, ReadED {

	public String infoCode;
	public String annotation;
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	//реквизиты исходного ЭПД
	public int refEdNo; //Номер ЭД в течение опердня
	public Date refEdDate; //Дата составления ЭД
	public String refEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	public int ed273No;
	
	public ED274(int i)
	{
		ed273No = i;
		isVER = false;
	}
	
	public ED274()
	{
		isVER = false;
	}
	
	@Override
	public boolean generateFrom(ED273 source) {
		edNo = source.pdList.get(ed273No).edNo + 111;
		edDate = source.edDate;
		edAuthor = source.pdList.get(ed273No).payee.edAuthor;
		edReceiver = source.edReceiver;

		infoCode = "8";
		annotation = "Положительные результаты всех видов контролей, предшествующих исполнению";
		refEdAuthor = source.edAuthor;
		refEdDate = source.edDate;
		refEdNo = source.edNo;
		
		iEdAuthor = source.pdList.get(ed273No).edAuthor;
		iEdDate = source.pdList.get(ed273No).edDate;
		iEdNo = source.pdList.get(ed273No).edNo;
		
		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];
		
		setFileName();
		
		if((EPDNoList.eList == null) || (EPDNoList.eList.size() == 0))
			return true;
		else if(EPDNoList.eList.contains(Integer.toString(source.edNo)))
			return true;
		else 
			return false;
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
			DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(infoCode) + ", null, " + DB.toString(annotation) + ",\r\n" +
			"null, '', '', '', null, '274',\r\n" +
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
	public int compareTo(ReadED arg0) {
		return super.compareTo((Packet) arg0);
	}

	@Override
	public void readEncodedFile(File src, boolean isUTF) {
		throw new UnsupportedOperationException();
		
	}

	public static class EPDNoList
	{
		public static List<String> eList;
		
		public static void readXML(String src)
		{

			Element root = XML.getXMLRootElement(src);

			NodeList nl = root.getElementsByTagName("ED274");

			if(nl.getLength() == 0)
				return;
			
			eList = new ArrayList<String>();
			
			eList.addAll(Arrays.asList(((Element)nl.item(0)).getAttribute("EPDNo").split(",")));
		}
	}
}
