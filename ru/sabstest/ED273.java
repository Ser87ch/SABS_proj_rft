package ru.sabstest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ED273 extends Packet implements ReadED, Generate<Element> {

	public List<PaymentDocument> pdl;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((pdl == null) ? 0 : pdl.hashCode());
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
		if (pdl == null) {
			if (other.pdl != null)
				return false;
		} else if (!pdl.equals(other.pdl))
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
		
		pdl = new ArrayList<PaymentDocument>();
		NodeList nl = root.getChildNodes();
		for(int i = 0; i < nl.getLength(); i++)
			if(nl.item(i).getNodeType() == Node.ELEMENT_NODE)
			{
				PaymentDocument pd = PaymentDocument.createDocFromXML((Element) nl.item(i));
				if(pd != null)
					pdl.add(pd);
			}
		
		Collections.sort(pdl);
	}

	@Override
	public boolean generateFrom(Element source) {
		// TODO генерация из xml
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
		// TODO вставка в БД
		
	}

}
