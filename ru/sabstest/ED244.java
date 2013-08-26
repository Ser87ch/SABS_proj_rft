package ru.sabstest;

import java.io.File;
import java.sql.Date;
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
		
		edDefineRequestCode = source.edDefineRequestCode;
		edDefineAnswerCode = CodeList.getAnswerCodeByNo(source.iEdNo);
		
		if(edDefineAnswerCode.equals(""))
			return false;
		else
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
