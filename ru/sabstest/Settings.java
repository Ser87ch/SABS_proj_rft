package ru.sabstest;


import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

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

			//��������� ����� ����
			ResultSet rs = db.st.executeQuery("select VALUE from dbo.XDM_CONF_SETTINGS where ID_PARAM = 1004");
			rs.next();
			Settings.path = rs.getString("VALUE");
			Log.msg("����� ���� " + Settings.path);

			rs = db.st.executeQuery("select X.BIK as BIK , isnull(B.KSNP,'') as KSNP from dbo.XDM_DEPARTMENT X inner join dbo.BNKSEEK B on B.NEWNUM = X.BIK");
			rs.next();
			Settings.bik = rs.getString("BIK");
			//	Settings.ks = rs.getString("KSNP");
			Log.msg("��� �� " + Settings.bik);

			rs = db.st.executeQuery("SELECT top 1 OPER_DATE as dt FROM XDM_OPERDAY_PROP WHERE VALUE = 0");
			rs.next();
			Settings.operDate = rs.getDate("dt");
			Log.msg("����. ���� " + new SimpleDateFormat("dd.MM.yyyy").format(Settings.operDate));		

			File dir = new File(Settings.testProj + "data\\");

			String[] children = dir.list();
			String filename = children[children.length - 1];
			Settings.datafolder = Settings.testProj + "data\\" + filename + "\\";
			Log.msg("������ ��� ����� ��������� � " + Settings.testProj + "data\\" + filename + "\\");

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
		Log.msg("XML c ������ ����������� " + fl + " ������.");
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
		Log.msg("XML c ������ ����������� " + src + " �������� � ���������.");		
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
			Log.msg("XML � ����������� ��� ��������� �������� ���������� " + fl + " ������.");
			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\gendoc.xsd", fl);		
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\gendoc.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			numBIK = XML.getChildValueInt("numbik", eElement);	
			numDoc = XML.getChildValueInt("numdoc", eElement);	
			firstDoc = XML.getChildValueInt("firstdoc", eElement);

			Log.msg("XML � ����������� ��� ��������� �������� ���������� " + src + " �������� � ���������.");
		}
	}

	public static class Login
	{
		public static LoginInfo pervvod;
		public static LoginInfo contrvvod;
		public static LoginInfo formes; 
		public static LoginInfo contres;
		
		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\login.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("login");
			doc.appendChild(rootElement);
			
			Element login = doc.createElement("pervvod");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", pervvod.user);	
			XML.createNode(doc, rootElement, "pwd", pervvod.pwd);	
			XML.createNode(doc, rootElement, "sign", pervvod.sign);	
			XML.createNode(doc, rootElement, "key", pervvod.key);	
			
			login = doc.createElement("contrvvod");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", contrvvod.user);	
			XML.createNode(doc, rootElement, "pwd", contrvvod.pwd);	
			XML.createNode(doc, rootElement, "sign", contrvvod.sign);	
			XML.createNode(doc, rootElement, "key", contrvvod.key);	
			
			login = doc.createElement("formes");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", formes.user);	
			XML.createNode(doc, rootElement, "pwd", formes.pwd);	
			XML.createNode(doc, rootElement, "sign", formes.sign);	
			XML.createNode(doc, rootElement, "key", formes.key);	
			
			login = doc.createElement("contres");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", contres.user);	
			XML.createNode(doc, rootElement, "pwd", contres.pwd);	
			XML.createNode(doc, rootElement, "sign", contres.sign);	
			XML.createNode(doc, rootElement, "key", contres.key);	

			XML.createXMLFile(doc, fl);
			Log.msg("XML � ����������� ������������� " + fl + " ������.");				

			XML.validate(Settings.testProj + "XMLSchema\\settings\\login.xsd",fl);
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\login.xsd",src);

			Element root = XML.getXMLRootElement(src);

			Element login = (Element) root.getElementsByTagName("pervvod").item(0);
			pervvod = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));
			
			login = (Element) root.getElementsByTagName("contrvvod").item(0);
			contrvvod = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));	
			
			login = (Element) root.getElementsByTagName("formes").item(0);
			formes = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));	
			
			login = (Element) root.getElementsByTagName("contres").item(0);
			contres = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));	
			
			Log.msg("XML � ����������� ������������� " + src + " �������� � ���������.");
		}
		
		public static class LoginInfo
		{
			public String user;
			public String pwd;
			public String sign;
			public String key;

			LoginInfo(String user, String pwd, String sign, String key)
			{
				this.user = user;
				this.pwd = pwd;
				this.sign = sign;
				this.key = key;
			}
		}
	}




	public static class Sign{
		public static String signobr = "";
		public static String keyobr = "";
		public static String signcontr = "";
		public static String keycontr = "";


		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\sign.xml");
		}

		public static void createXML(String fl)
		{
			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("sign");
			doc.appendChild(rootElement);

			XML.createNode(doc, rootElement, "signobr", signobr);	
			XML.createNode(doc, rootElement, "keyobr", keyobr);	
			XML.createNode(doc, rootElement, "signcontr", signcontr);	
			XML.createNode(doc, rootElement, "keycontr", keycontr);	

			XML.createXMLFile(doc, fl);

			Log.msg("XML � ����������� ��� ��������� R-������ " + fl + " ������.");
			XML.validate(Settings.testProj + "XMLSchema\\settings\\sign.xsd",fl);
		}

		public static void readXML(String src)
		{
			XML.validate(Settings.testProj + "XMLSchema\\settings\\sign.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			signobr = XML.getChildValueString("signobr", eElement);		
			keyobr = XML.getChildValueString("keyobr", eElement);	
			signcontr = XML.getChildValueString("signcontr", eElement);		
			keycontr = XML.getChildValueString("keycontr", eElement);		

			Log.msg("XML � ����������� ��� ��������� R-������ " + src + " �������� � ���������.");
		}
	}

//	public static class GenSpack{
//
//		public static int numBIK = 0;
//		public static int numDoc = 0;
//		public static int firstDoc = 0;
//
//		public static void createXML()
//		{
//			createXML(Settings.testProj + "settings\\" + Settings.obrfolder + "\\genspack.xml");
//		}
//
//		public static void createXML(String fl)
//		{
//			Document doc = XML.createNewDoc();
//			Element rootElement = doc.createElement("genspack");
//			doc.appendChild(rootElement);
//
//			XML.createNode(doc, rootElement, "numbik", numBIK);	
//			XML.createNode(doc, rootElement, "numdoc", numDoc);	
//			XML.createNode(doc, rootElement, "firstdoc", firstDoc);
//
//			XML.createXMLFile(doc, fl);
//			Log.msg("XML � ����������� ��� ��������� S-������ " + fl + " ������.");
//			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\genspack.xsd",fl);
//		}
//
//		public static void readXML(String src)
//		{			
//			XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\genspack.xsd",src);				
//
//			Element eElement = XML.getXMLRootElement(src);
//
//			numBIK = XML.getChildValueInt("numbik", eElement);	
//			numDoc = XML.getChildValueInt("numdoc", eElement);	
//			firstDoc = XML.getChildValueInt("firstdoc", eElement);
//
//			Log.msg("XML � ����������� ��� ��������� S-������ " + src + " �������� � ���������.");
//		}
//	}
}


