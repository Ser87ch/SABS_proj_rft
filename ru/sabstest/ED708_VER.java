package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED708_VER extends Holder<ED208> implements Generate<ED743_VER> {

	@Override
	public boolean generateFrom(ED743_VER source) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED708_VER";		
	}

	@Override
	public void insertIntoDB() {
		// TODO Auto-generated method stub
		
	}

}
