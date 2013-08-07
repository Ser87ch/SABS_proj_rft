package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED744_VER extends Holder<ED244> implements Generate<ED743_VER> {

	@Override
	public boolean generateFrom(ED743_VER source) {
		edNo = source.edNo + 2000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdNo = source.edNo;
		iEdDate = source.edDate;
		iEdAuthor = source.edAuthor;
		
		ed = new ED244();
		ed.generateFrom(source.ed);
		return true;
	}

	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED744_VER";
	}

	@Override
	public void insertIntoDB() {
		// TODO вставка

	}

}
