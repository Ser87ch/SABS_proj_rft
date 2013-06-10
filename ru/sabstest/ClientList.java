package ru.sabstest;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ClientList {
	private static Map<String, Client> clList;
	
	public static void readFile(String src)
	{
		Element el = XML.getXMLRootElement(src);
		
		clList = new HashMap<String, Client>();
		
		NodeList nl = el.getElementsByTagName("Client");
		for(int i = 0; i < nl.getLength(); i++)
		{
			String id, bic, ls;
			Element clEl = (Element) nl.item(i);
			
			id = clEl.getAttribute("Id");
			bic = clEl.getAttribute("BIC");
			ls = clEl.getAttribute("LS");
			
			Client cl = Client.createClientFromBICPersonalAcc(bic, ls);
			
			clList.put(id, cl);
		}		
	}
	
	public static Client getClient(String id)
	{
		return clList.get(id);
	}
}
