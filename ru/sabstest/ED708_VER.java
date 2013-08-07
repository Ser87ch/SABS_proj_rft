package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED708_VER extends Holder<ED208> implements Generate<ED743_VER> {

	@Override
	public boolean generateFrom(ED743_VER source) {
		edNo = source.edNo + 3000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdNo = source.edNo;
		iEdDate = source.edDate;
		iEdAuthor = source.edAuthor;
		
		ed = new ED208();
		ed.generateFrom(source.ed);
		return true;
	}
	
	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED708_VER";		
	}

	@Override
	public void insertIntoDB() {
		// TODO вставка
		
	}

}
