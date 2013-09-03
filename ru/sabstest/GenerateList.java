package ru.sabstest;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenerateList<T> {
	public List<Generate<T>> pList;

	public GenerateList()
	{
		pList = new ArrayList<Generate<T>>();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pList == null) ? 0 : pList.hashCode());
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
		GenerateList<?> other = (GenerateList<?>) obj;
		if (pList == null) {
			if (other.pList != null)
				return false;
		} else if (!pList.equals(other.pList))
			return false;
		return true;
	}	

	public void sortBySign()
	{
		Collections.sort(pList, new Comparator<Generate<T>>() {

			@Override
			public int compare(Generate<T> o1, Generate<T> o2) {
				Sign[] sg1 = o1.getSigns();
				Sign[] sg2 = o2.getSigns();
				String s1 = sg1[0].profile;
				String s2 = sg2[0].profile;

				return s1.compareTo(s2);
			}
		});
	}

	public List<List<Generate<?>>> getSubListBySign()
	{
		sortBySign();
		List<List<Generate<?>>> llG = new ArrayList<List<Generate<?>>>();

		List<Generate<?>> lstGen = new ArrayList<Generate<?>>();
		lstGen.add(pList.get(0));
		
		for(int i = 1; i < pList.size(); i++)
		{
			if(pList.get(i).getSigns()[0].profile.equals(pList.get(i - 1).getSigns()[0].profile))
				lstGen.add(pList.get(i));
			else
			{
				llG.add(lstGen);
				lstGen = new ArrayList<Generate<?>>();
				lstGen.add(pList.get(i));
			}
		}
		llG.add(lstGen);
		return llG;
	}

	//	public Packet get(int i)
	//	{
	//		return pList.get(i);
	//	}

	//	public int getSize()
	//	{
	//		return pList.size();
	//	}

	public void insertIntoDB()
	{
		Iterator<Generate<T>> it = pList.iterator();

		while(it.hasNext())
		{
			Generate<T> p = it.next();
			p.insertIntoDB();
		}
	}

	public void insertForRead()
	{
		Iterator<Generate<T>> it = pList.iterator();

		while(it.hasNext())
		{
			Generate<T> p = it.next();

			p.insertForRead();
		}
	}

	//	public void generateEsidFromEPD(ReadEDList pl)
	//	{
	//		Iterator<Packet> it = pl.pList.iterator();
	//
	//		pList = new ArrayList<Packet>();
	//
	//		while(it.hasNext())
	//		{
	//			PacketEPD epd = (PacketEPD) it.next();
	//
	//			PacketESIDVER rpack = new PacketESIDVER();
	//			if(rpack.generateFrom(epd))
	//				pList.add(rpack);
	//
	//			PacketEPDVER_B bpack = new PacketEPDVER_B();
	//			if(bpack.generateFrom(epd))
	//				pList.add(bpack);		
	//
	//			Collections.sort(pList);
	//
	//		}
	//	}


	public void add(Generate<T> p)
	{
		pList.add(p);
	}	
}
