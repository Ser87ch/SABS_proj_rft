package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED774_VER extends Holder<ED274> implements Generate<ED773_VER> {

	@Override
	public boolean generateFrom(ED773_VER source) {
		edNo = source.edNo + 2500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdNo = source.edNo;
		iEdDate = source.edDate;
		iEdAuthor = source.edAuthor;
		
		ed = new ED274();
		ed.generateFrom(source.ed);
		return true;
	}

	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED774_VER";
		
	}

	@Override
	public void insertIntoDB() {
		// TODO вставка в базу
		
	}

	@Override
	ED274 createHolderObject() {
		return new ED274();
	}

}
