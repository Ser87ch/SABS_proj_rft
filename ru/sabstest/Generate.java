package ru.sabstest;

public interface Generate<T> {
	public boolean generateFrom(T source);
	public void setFileName();
	public void insertIntoDB();
	public void insertForRead();	
	public Sign[] getSigns();
	public boolean isVER();
}
