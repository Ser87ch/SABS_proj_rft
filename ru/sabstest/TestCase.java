package ru.sabstest;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ru.sabstest.ModuleList.Module;

public class TestCase {
	private List<Step> stList;
	
	public void readFile(String src)
	{
		Element el = XML.getXMLRootElement(src);
		
		stList = new ArrayList<Step>();
		
		NodeList nl = el.getElementsByTagName("Step");
		for(int i = 0; i < nl.getLength(); i++)
		{
			int id;
			String script;
			String[] options;
			Element clEl = (Element) nl.item(i);
			
			id = Integer.parseInt(clEl.getAttribute("Id"));
			script = ModuleList.getScript(clEl.getAttribute("Module"));			
			options = clEl.getAttribute("Options").split(",");
			
			Step st = new Step(id, script, options);			
			stList.add(st);
		}	
	}
	
	public String getScript(int i)
	{
		return stList.get(i).script;
	}
	
	public Step getStep(int i)
	{
		return stList.get(i);
	}
	
	public boolean containsOption(int i, String option)
	{
		boolean ctn = false;
		for(String op:stList.get(i).options)
			if(op.equals(option))
				ctn = true;
		return ctn;
	}
	
	public int getSize()
	{
		return stList.size();
	}
	
	public static class Step
	{
		int id;
		public String script;
		public String[] options;
		
		Step(int id, String script, String[] options)
		{
			this.id = id;
			this.script = script;
			this.options = options;
		}
		
		public boolean containsOption(String option)
		{
			boolean ctn = false;
			for(String op:options)
				if(op.equals(option))
					ctn = true;
			return ctn;
		}
	}
}
