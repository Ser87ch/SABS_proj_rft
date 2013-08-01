package ru.sabstest;

import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GenerateFromXMLList extends GenerateList<Element> {

	public void generateFromXML(String fl)
	{
		Element root = XML.getXMLRootElement(fl);

		if(root.getNodeName().equals("Generation"))
		{
			pList = new ArrayList<Generate<Element>> ();

			NodeList nl = root.getChildNodes();
			for(int i = 0; i < nl.getLength(); i++)
			{
				if(nl.item(i).getNodeType() == Node.ELEMENT_NODE)
				{
					Generate<Element> gen = Packet.createGenFromXMLByFile(nl.item(i).getNodeName());	
					if(gen != null)
						if(gen.generateFrom((Element) nl.item(i)))
							pList.add(gen);
				}
			}	
			//Collections.sort(pList);
		}
	}
}
