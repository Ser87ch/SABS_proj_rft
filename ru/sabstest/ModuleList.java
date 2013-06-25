package ru.sabstest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ModuleList {
	static private List<Module> mlList;
	
	static public void readFile(String src)
	{
		Element el = XML.getXMLRootElement(src);
		
		mlList = new ArrayList<Module>();
		
		NodeList nl = el.getElementsByTagName("Module");
		for(int i = 0; i < nl.getLength(); i++)
		{
			int id;
			String name;
			String alias;
			String script;
			String options;
			Element clEl = (Element) nl.item(i);
			
			id = Integer.parseInt(clEl.getAttribute("Id"));
			name = clEl.getAttribute("Name");
			alias = clEl.getAttribute("Alias");
			script = clEl.getAttribute("Script");
			options = clEl.getAttribute("Options");
			
			Module ml = new Module(id, name, alias, script, options);			
			mlList.add(ml);
		}	
	}
	
	static public String getScript(String alias)
	{
		String scr = "";
		
		for(int i = 0; i < mlList.size(); i++)
		{
			if(mlList.get(i).alias.equals(alias))
					scr = mlList.get(i).script;
		}
		
		return scr;
	}
	
	static class Module
	{
		int id;
		String name;
		String alias;
		String script;
		String options;
		
		Module(int id, String name, String alias, String script, String options) 
		{
			this.id = id;
			this.name = name;
			this.alias = alias;
			this.script = script;
			this.options = options;
		}		
	}
}
