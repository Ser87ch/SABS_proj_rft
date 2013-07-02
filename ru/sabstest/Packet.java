package ru.sabstest;


abstract public class Packet implements Comparable<Packet>{
	public Type packetType;
	public String filename;

	public Sign firstSign;
	public Sign secondSign;

	public enum Type{PacketEPD, PacketEPDVER, PacketESIDVER, PacketEPDVER_B};

	@Override
	public int compareTo(Packet o) {
		return filename.compareTo(o.filename);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((packetType == null) ? 0 : packetType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Packet other = (Packet) obj;
		if (packetType != other.packetType)
			return false;
		return true;
	}

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
