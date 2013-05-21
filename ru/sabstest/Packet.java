package ru.sabstest;

import org.w3c.dom.Element;

abstract public class Packet {
	public Type packetType;
	public String filename;
	
	public enum Type{PacketEPD, PacketEPDVER, PacketESIDVER, PacketEPDVER_B};
	
	public abstract void createFile(String folder);
	
	public abstract void insertIntoDb();
}
