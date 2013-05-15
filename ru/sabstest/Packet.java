package ru.sabstest;

import org.w3c.dom.Element;

abstract public class Packet {
	public Type packetType;
	
	public enum Type{PacketEPD, PacketEPDVER, PacketESIDVER, PacketEPDVER_B};
	
	abstract void createFile(String fl);
}
