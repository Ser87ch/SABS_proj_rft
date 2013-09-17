package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ru.sabstest.PacketESIDVER.CodeList.Code;

public class ED208 extends Packet implements ReadED, Generate<ED243> {

	public String resultCode;

	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	public String ctrlCode;
	public String annotation;

	public int ed273No;

	public ED208(int i)
	{
		ed273No = i;
	}

	public ED208()
	{

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
		+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
		result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
		result = prime * result
		+ ((resultCode == null) ? 0 : resultCode.hashCode());
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
		ED208 other = (ED208) obj;
		if (iEdAuthor == null) {
			if (other.iEdAuthor != null)
				return false;
		} else if (!iEdAuthor.equals(other.iEdAuthor))
			return false;
		if (iEdDate == null) {
			if (other.iEdDate != null)
				return false;
		} else if (!iEdDate.equals(other.iEdDate))
			return false;
		if (resultCode == null) {
			if (other.resultCode != null)
				return false;
		} else if (!resultCode.equals(other.resultCode))
			return false;
		return true;
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
		resultCode = root.getAttribute("ResultCode");

		Element ied = (Element) root.getElementsByTagNameNS("*", "EDRefID").item(0);
		iEdNo = Integer.parseInt(ied.getAttribute("EDNo"));
		iEdDate = Date.valueOf(ied.getAttribute("EDDate"));
		iEdAuthor = ied.getAttribute("EDAuthor");
	}


	@Override
	public boolean generateFrom(ED243 source) {
		edNo = source.edNo + 3500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		resultCode = CodeList.getResultCodeByNo(source.iEdNo);
		ctrlCode = CodeList.getCtrlCodeByNo(source.iEdNo);
		annotation = CodeList.getAnnotationByNo(source.iEdNo);		

		iEdAuthor = source.edAuthor;
		iEdDate = source.edDate;
		iEdNo = source.edNo;
		if(resultCode.equals(""))
			return false;
		else
			return true;
	}


	public boolean generateFrom(ED244 source) {
		edNo = source.edNo + 3500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		resultCode = "2";

		iEdAuthor = source.edAuthor;
		iEdDate = source.edDate;
		iEdNo = source.edNo;

		return true;
	}


	public boolean generateFrom(ED273 source) {
		edNo = source.edNo + 4500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		resultCode = "2";

		iEdAuthor = source.pdList.get(ed273No).edAuthor;
		iEdDate = source.pdList.get(ed273No).edDate;
		iEdNo = source.pdList.get(ed273No).edNo;

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

			NodeList nl = root.getElementsByTagName("ED208");

			if(nl.getLength() == 0)
				return;
			else
				eList = new ArrayList<Code>();

			for(int i = 0; i < nl.getLength(); i++)
			{
				Element ed = (Element) nl.item(i);

				String[] edNos = ed.getAttribute("EDNo").split(",");
				String resultCode = ed.getAttribute("ResultCode");
				String ctrlCode = ed.getAttribute("CtrlCode");
				String annotation = ed.getAttribute("Annotation"); 

				for(String edNo:edNos)
					eList.add(new Code(edNo, resultCode, ctrlCode, annotation));
			}			

		}

		public static String getResultCodeByNo(int edNo)
		{
			for(Code c:eList)
				if(c.edNo.equals(Integer.toString(edNo)))
					return c.resultCode;
			return "";
		}

		public static String getCtrlCodeByNo(int edNo)
		{
			for(Code c:eList)
				if(c.edNo.equals(Integer.toString(edNo)))
					return c.ctrlCode;
			return "";
		}

		public static String getAnnotationByNo(int edNo)
		{
			for(Code c:eList)
				if(c.edNo.equals(Integer.toString(edNo)))
					return c.annotation;
			return "";
		}


		public static class Code
		{
			String edNo;
			String resultCode;
			String ctrlCode;
			String annotation;

			public Code(String edNo, String resultCode, String ctrlCode, String annotation)
			{
				this.edNo = edNo;
				this.resultCode = resultCode;
				this.ctrlCode = ctrlCode;
				this.annotation = annotation;
			}
		}
	}
}
