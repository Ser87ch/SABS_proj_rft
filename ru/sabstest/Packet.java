package ru.sabstest;


abstract public class Packet {
	public Type packetType;
	public String filename;

	public Sign firstSign;
	public Sign secondSign;

	public enum Type{PacketEPD, PacketEPDVER, PacketESIDVER, PacketEPDVER_B};

	public abstract void createFile(String folder);

	public abstract void insertIntoDB();

	public abstract void setFileName();

	public void insertForRead()
	{
		if(packetType == Packet.Type.PacketEPD)
			DB.insertPacetForReadUfebs(filename);
		else
			DB.insertPacetForReadVer(filename);
	}
}
