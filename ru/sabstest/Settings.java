package ru.sabstest;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Settings{

	public static String server = "";
	public static String db = "";
	public static String user = "";
	public static String pwd = "";
	public static String path = "";
	public static String bik = "";
	//	public static String ks = "";
	public static Date operDate = new Date(0);
	public static String testProj= "";
	public static String folder = "";
	public static String fullfolder = "";
	public static String datafolder = ""; 

	public static final String pervfolder = "perv"; 
	public static final String obrfolder = "obr"; 


	public static void loadFromDB()
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			//получение папки сабс
			ResultSet rs = db.st.executeQuery("select VALUE from dbo.XDM_CONF_SETTINGS where ID_PARAM = 1004");
			rs.next();
			Settings.path = rs.getString("VALUE");
			Log.msg("Папка САБС " + Settings.path);

			rs = db.st.executeQuery("select X.BIK as BIK , isnull(B.KSNP,'') as KSNP from dbo.XDM_DEPARTMENT X inner join dbo.BNKSEEK B on B.NEWNUM = X.BIK");
			rs.next();
			Settings.bik = rs.getString("BIK");
			//	Settings.ks = rs.getString("KSNP");
			Log.msg("БИК ПУ " + Settings.bik);

			rs = db.st.executeQuery("SELECT top 1 OPER_DATE as dt FROM XDM_OPERDAY_PROP WHERE VALUE = 0");
			rs.next();
			Settings.operDate = rs.getDate("dt");
			Log.msg("Опер. день " + new SimpleDateFormat("dd.MM.yyyy").format(Settings.operDate));		

			File dir = new File(Settings.testProj + "data\\");

			String[] children = dir.list();
			String filename = children[children.length - 1];
			Settings.datafolder = Settings.testProj + "data\\" + filename + "\\";
			Log.msg("Данные для теста находятся в " + Settings.testProj + "data\\" + filename + "\\");

			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	public static void createXML()
	{
		createXML(Settings.testProj + "settings\\general.xml");
	}

	public static void createXML(String fl)
	{		
		Document doc = XML.createNewDoc();
		Element rootElement = doc.createElement("general");
		doc.appendChild(rootElement);

		XML.createNode(doc, rootElement, "server", Settings.server);			
		XML.createNode(doc, rootElement, "db", Settings.db);
		XML.createNode(doc, rootElement, "user", Settings.user);
		XML.createNode(doc, rootElement, "pwd", Settings.pwd);		

		XML.createXMLFile(doc, fl);
		Log.msg("XML c общими настройками " + fl + " создан.");
		XML.validate(Settings.testProj + "XMLSchema\\settings\\general.xsd", fl);
	}

	public static void readXML(String src)
	{
		XML.validate(Settings.testProj + "XMLSchema\\settings\\general.xsd",src);

		Element eElement = XML.getXMLRootElement(src);		
		server = XML.getChildValueString("server", eElement);	
		db = XML.getChildValueString("db", eElement);	
		user = XML.getChildValueString("user", eElement);	
		pwd = XML.getChildValueString("pwd", eElement);
					
		Settings.loadFromDB();
		Log.msg("XML c общими настройками " + src + " загружен в программу.");		
	}



	public static class GenDoc{
		public static int numBIK = 0;
		public static int numDoc = 0;
		public static int firstDoc = 0;

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\gendoc.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("gendoc");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "numbik", numBIK);	
			XML.createNode(doc, rootElement, "numdoc", numDoc);	
			XML.createNode(doc, rootElement, "firstdoc", firstDoc);	

			XML.createXMLFile(doc, fl);
			Log.msg("XML с настройками для генерации входящих документов " + fl + " создан.");
			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\gendoc.xsd", fl);		
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\gendoc.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			numBIK = XML.getChildValueInt("numbik", eElement);	
			numDoc = XML.getChildValueInt("numdoc", eElement);	
			firstDoc = XML.getChildValueInt("firstdoc", eElement);

			Log.msg("XML с настройками для генерации входящих документов " + src + " загружен в программу.");
		}
	}

	public static class PerVvod{
		public static String user = "";
		public static String pwd = "";
		public static String sign = "";
		public static String key = "";

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\pervvod.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("pervvod");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "user", user);	
			XML.createNode(doc, rootElement, "pwd", pwd);	
			XML.createNode(doc, rootElement, "sign", sign);	
			XML.createNode(doc, rootElement, "key", key);	

			XML.createXMLFile(doc, fl);
			Log.msg("XML с настройками для первичного ввода докуметов " + fl + " создан.");				

			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\pervvod.xsd",fl);
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\pervvod.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			user = XML.getChildValueString("user", eElement);	
			pwd = XML.getChildValueString("pwd", eElement);	
			sign = XML.getChildValueString("sign", eElement);		
			key = XML.getChildValueString("key", eElement);

			Log.msg("XML с настройками для первичного ввода документов " + src + " загружен в программу.");
		}
	}


	public static class ContrVvod{
		public static String user = "";
		public static String pwd = "";
		public static String sign = "";
		public static String key = "";

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contrvvod.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("contrvvod");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "user", user);	
			XML.createNode(doc, rootElement, "pwd", pwd);	
			XML.createNode(doc, rootElement, "sign", sign);	
			XML.createNode(doc, rootElement, "key", key);	

			XML.createXMLFile(doc, fl);

			Log.msg("XML с настройками для контрольго ввода докуметов " + fl + " создан.");

			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\contrvvod.xsd",fl);
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\contrvvod.xsd",src);				

			Element eElement = XML.getXMLRootElement(src);

			user = XML.getChildValueString("user", eElement);	
			pwd = XML.getChildValueString("pwd", eElement);	
			sign = XML.getChildValueString("sign", eElement);	
			key = XML.getChildValueString("key", eElement);	

			Log.msg("XML с настройками для контрольного ввода документов " + src + " загружен в программу.");			
		}
	}


	public static class FormES{
		public static String user = "";
		public static String pwd = "";
		public static String sign = "";
		public static String key = "";

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\formes.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("formes");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "user", user);	
			XML.createNode(doc, rootElement, "pwd", pwd);	
			XML.createNode(doc, rootElement, "sign", sign);	
			XML.createNode(doc, rootElement, "key", key);	

			XML.createXMLFile(doc, fl);
			Log.msg("XML с настройками для формирования ЭС " + fl + " создан.");

			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\formes.xsd",fl);			
		}

		public static void readXML(String src)
		{			
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\formes.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			user = XML.getChildValueString("user", eElement);	
			pwd = XML.getChildValueString("pwd", eElement);	
			sign = XML.getChildValueString("sign", eElement);		
			key = XML.getChildValueString("key", eElement);		

			Log.msg("XML с настройками для формирования ЭС " + src + " загружен в программу.");			
		}
	}

	public static class ContrES{
		public static String user = "";
		public static String pwd = "";
		public static String sign = "";
		public static String key = "";


		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contres.xml");	
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("contres");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "user", user);	
			XML.createNode(doc, rootElement, "pwd", pwd);	
			XML.createNode(doc, rootElement, "sign", sign);	
			XML.createNode(doc, rootElement, "key", key);	

			XML.createXMLFile(doc, fl);
			Log.msg("XML с настройками для контроля ЭС " + fl + " создан.");
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\contres.xsd", fl);
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\contres.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			user = XML.getChildValueString("user", eElement);	
			pwd = XML.getChildValueString("pwd", eElement);	
			sign = XML.getChildValueString("sign", eElement);		
			key = XML.getChildValueString("key", eElement);

			Log.msg("XML с настройками для контроля ЭС " + src + " загружен в программу.");			
		}
	}

	public static class GenRpack{
		public static String signobr = "";
		public static String keyobr = "";
		public static String signcontr = "";
		public static String keycontr = "";
		public static boolean isGenBpack = false;

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\genrpack.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("genrpack");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "signobr", signobr);	
			XML.createNode(doc, rootElement, "keyobr", keyobr);	
			XML.createNode(doc, rootElement, "signcontr", signcontr);	
			XML.createNode(doc, rootElement, "keycontr", keycontr);	
			XML.createNode(doc, rootElement, "isgenbpack", Boolean.toString(isGenBpack));

			XML.createXMLFile(doc, fl);

			Log.msg("XML с настройками для генерации R-пакета " + fl + " создан.");
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\genrpack.xsd",fl);
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\" + Settings.pervfolder + "\\genrpack.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			signobr = XML.getChildValueString("signobr", eElement);		
			keyobr = XML.getChildValueString("keyobr", eElement);	
			signcontr = XML.getChildValueString("signcontr", eElement);		
			keycontr = XML.getChildValueString("keycontr", eElement);		
			isGenBpack = Boolean.parseBoolean(XML.getChildValueString("isgenbpack", eElement));

			Log.msg("XML с настройками для генерации R-пакета " + src + " загружен в программу.");
		}
	}

	public static class GenSpack{

		public static int numBIK = 0;
		public static int numDoc = 0;
		public static int firstDoc = 0;
		public static String signobr = "";
		public static String keyobr = "";
		public static String signcontr = "";
		public static String keycontr = "";
		public static String error = "";

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.obrfolder + "\\genspack.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("genspack");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "numbik", numBIK);	
			XML.createNode(doc, rootElement, "numdoc", numDoc);	
			XML.createNode(doc, rootElement, "firstdoc", firstDoc);
			XML.createNode(doc, rootElement, "signobr", signobr);	
			XML.createNode(doc, rootElement, "keyobr", keyobr);	
			XML.createNode(doc, rootElement, "signcontr", signcontr);	
			XML.createNode(doc, rootElement, "keycontr", keycontr);	
			XML.createNode(doc, rootElement, "error", error);	

			XML.createXMLFile(doc, fl);
			Log.msg("XML с настройками для генерации S-пакета " + fl + " создан.");
			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\genspack.xsd",fl);
		}

		public static void readXML(String src)
		{			
			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\genspack.xsd",src);				

			Element eElement = XML.getXMLRootElement(src);

			numBIK = XML.getChildValueInt("numbik", eElement);	
			numDoc = XML.getChildValueInt("numdoc", eElement);	
			firstDoc = XML.getChildValueInt("firstdoc", eElement);
			signobr = XML.getChildValueString("signobr", eElement);		
			keyobr = XML.getChildValueString("keyobr", eElement);	
			signcontr = XML.getChildValueString("signcontr", eElement);		
			keycontr = XML.getChildValueString("keycontr", eElement);	
			error = XML.getChildValueString("error", eElement);

			Log.msg("XML с настройками для генерации S-пакета " + src + " загружен в программу.");
		}
	}
}


