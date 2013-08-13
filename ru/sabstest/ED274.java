package ru.sabstest;

import java.io.File;
import java.sql.Date;

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
	
	@Override
	public boolean generateFrom(ED273 source) {
		// TODO генерация 274
		return false;
	}

	@Override
	public void setFileName() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void insertIntoDB() {
		throw new UnsupportedOperationException();

	}

	@Override
	public int compareTo(ReadED arg0) {
		return super.compareTo((Packet) arg0);
	}

	@Override
	public void readEncodedFile(File src, boolean isUTF) {
		throw new UnsupportedOperationException();
		
	}

}
