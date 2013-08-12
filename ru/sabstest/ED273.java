package ru.sabstest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.w3c.dom.Element;

public class ED273 extends Packet implements ReadED, Generate<Element> {

	public List<PaymentDocument> pd;
	
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
	public boolean generateFrom(Element source) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}

}
