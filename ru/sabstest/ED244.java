package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ru.sabstest.ED208.CodeList.Code;

public class ED244 extends Packet implements ReadED, Generate<ED243> {

	public String edDefineRequestCode; //код запроса
	public String edDefineAnswerCode; //код ответа
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	//реквизиты исходного ЭД
	public int oEdNo; //Номер ЭД в течение опердня
	public Date oEdDate; //Дата составления ЭД
	public String oEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
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

		Element ied = (Element) root.getElementsByTagNameNS("*", "OriginalEPD").item(0);
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
		
		oEdAuthor = source.iEdAuthor;
		oEdDate = source.iEdDate;
		oEdNo = source.iEdNo;
		
		edDefineRequestCode = source.edDefineRequestCode;
		edDefineAnswerCode = CodeList.getAnswerCodeByNo(source.iEdNo);
		
		if(edDefineAnswerCode.equals(""))
			return false;
		else
			return true;
		
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
			DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(edDefineRequestCode) + ", " + DB.toString(edDefineAnswerCode) + ", null,\r\n" + //" + DB.toString(idED243) +  "
			"null, " + DB.toString(iEdNo) + ", " + DB.toString(iEdDate) + ", " + DB.toString(iEdAuthor) + ", null, '44',\r\n" +
			DB.toString(oEdNo) + ", " + DB.toString(oEdDate) + ", " + DB.toString(oEdAuthor) + ", null, null, null,\r\n" +
			"null, null, null, '0')";			
			db.st.executeUpdate(query);

			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}

		
	}

	
	public static class CodeList
	{
		private static List <Code> eList;

		public static void readXML(String src)
		{

			Element root = XML.getXMLRootElement(src);

			NodeList nl = root.getElementsByTagName("ED744_VER");

			if(nl.getLength() == 0)
				return;
			else
				eList = new ArrayList<Code>();

			for(int i = 0; i < nl.getLength(); i++)
			{
				Element ed = (Element) nl.item(i);

				String edNo = ed.getAttribute("EDNo");
				String edDefineAnswerCode = ed.getAttribute("EDDefineAnswerCode");
			
				eList.add(new Code(edNo, edDefineAnswerCode));
			}			

		}

		public static String getAnswerCodeByNo(int edNo)
		{
			for(Code c:eList)
				if(c.edNo.equals(Integer.toString(edNo)))
					return c.edDefineAnswerCode;
			return "";
		}

		public static class Code
		{
			String edNo;
			String edDefineAnswerCode;

			public Code(String edNo, String edDefineAnswerCode)
			{
				this.edNo = edNo;
				this.edDefineAnswerCode = edDefineAnswerCode;
			}
		}
	}
}
