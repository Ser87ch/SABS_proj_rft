package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED744_VER extends Holder<ED244> implements Generate<ED743_VER> {

	@Override
	public boolean generateFrom(ED743_VER source) {
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
		// TODO Auto-generated method stub

	}

}
