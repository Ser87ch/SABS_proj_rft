package ru.sabstest;


import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Класс настройки
 * @author Admin
 *
 */
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
	public static String rkc = "";
	//DM+
	public static String dumpname = "";
	//public static String mainwindowname = "";

	public static final String pervfolder = "perv"; 
	public static final String obrfolder = "obr";


	/**
	 * загрузка настроек из БД
	 */
	public static void loadFromDB()
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			//получение папки сабс
			ResultSet rs = db.st.executeQuery("select VALUE from dbo.XDM_CONF_SETTINGS where ID_PARAM = 1004");
			rs.next();
			Settings.path = rs.getString("VALUE");
			if(!Settings.path.substring(Settings.path.length()-1).equals("\\"))
				Settings.path = Settings.path + "\\";
			Log.msg("Папка САБС " + Settings.path);

			rs = db.st.executeQuery("select X.BIK as BIK, X.NAME AS NAME, isnull(B.KSNP,'') as KSNP, B.RKC RKC from dbo.XDM_DEPARTMENT X inner join dbo.BNKSEEK B on B.NEWNUM = X.BIK");
			rs.next();
			Settings.bik = rs.getString("BIK");
			Settings.rkc = rs.getString("RKC");
			//	Settings.ks = rs.getString("KSNP");

			///DM*
			///Settings.mainwindowname = rs.getString("NAME");
			///Log.msg("БИК ПУ: " + Settings.bik + ", имя ПУ: "+Settings.mainwindowname);


			rs = db.st.executeQuery("SELECT top 1 OPER_DATE as dt FROM XDM_OPERDAY_PROP WHERE VALUE = 0");
			rs.next();
			Settings.operDate = rs.getDate("dt");
			//DM
			Log.msg("Дата опер.дня: " + new SimpleDateFormat("dd.MM.yyyy").format(Settings.operDate));		

			File dir = new File(Settings.testProj + "data\\");

			String[] children = dir.list();
			if(children.length == 0)
			{
				String filename = children[children.length - 1];
				Settings.datafolder = Settings.testProj + "data\\" + filename + "\\";
				Log.msg("Данные для теста находятся в " + Settings.testProj + "data\\" + filename + "\\");
			}
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

	/**
	 * создание файла с настройками
	 * @param fl полный путь к файлу
	 */
	public static void createXML(String fl)
	{		
		Document doc = XML.createNewDoc();
		Element rootElement = doc.createElement("general");
		doc.appendChild(rootElement);

		XML.createNode(doc, rootElement, "server", Settings.server);			
		XML.createNode(doc, rootElement, "db", Settings.db);
		XML.createNode(doc, rootElement, "user", Settings.user);
		XML.createNode(doc, rootElement, "pwd", Settings.pwd);

		//DM+
		XML.createNode(doc, rootElement, "dumpname", Settings.dumpname);

		XML.createXMLFile(doc, fl);
		Log.msg("XML c общими настройками " + fl + " создан.");
		//	XML.validate(Settings.testProj + "XMLSchema\\settings\\general.xsd", fl);
	}

	/**
	 * считывает настройки из файла
	 * @param src полный путь к файлу
	 */
	public static void readXML(String src)
	{
		//XML.validate(Settings.testProj + "XMLSchema\\settings\\general.xsd",src);

		Element eElement = XML.getXMLRootElement(src);		
		server = XML.getChildValueString("server", eElement);	
		db = XML.getChildValueString("db", eElement);	
		user = XML.getChildValueString("user", eElement);	
		pwd = XML.getChildValueString("pwd", eElement);

		//DM+
		dumpname = XML.getChildValueString("dumpname", eElement);


		Settings.loadFromDB();
		Log.msg("XML c общими настройками " + src + " загружен в программу.");		
	}





	/**
	 * настройка генерации данных
	 * @author Admin
	 *
	 */
	public static class GenDoc{
		public static int numBIK = 0;
		public static int numDoc = 0;
		public static int firstDoc = 0;

		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\gendoc.xml");
		}

		/**
		 * создание файла с настройками
		 * @param fl полный путь к файлу
		 */
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
			//	XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\gendoc.xsd", fl);		
		}

		/**
		 * считывает настройки из файла
		 * @param src полный путь к файлу
		 */
		public static void readXML(String src)
		{
			//	XML.validate(Settings.testProj + "XMLSchema\\settings\\gen\\gendoc.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			numBIK = XML.getChildValueInt("numbik", eElement);	
			numDoc = XML.getChildValueInt("numdoc", eElement);	
			firstDoc = XML.getChildValueInt("firstdoc", eElement);

			Log.msg("XML с настройками для генерации входящих документов " + src + " загружен в программу.");
		}
	}

	public static class Login
	{
		public static LoginInfo pervvod;
		public static LoginInfo contrvvod;
		public static LoginInfo formes; 
		public static LoginInfo contres;

		//DM+
		public static LoginInfo eocontr;
		public static LoginInfo eootvet;

		//DM+!!!
		public static LoginInfo eoadmin;


		public static void createXML()
		{
			createXML(Settings.testProj + "settings\\login.xml");
		}

		/**
		 * создание файла с настройками
		 * @param fl полный путь к файлу
		 */
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


			//DM+
			login = doc.createElement("eocontr");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", eocontr.user);	
			XML.createNode(doc, rootElement, "pwd", eocontr.pwd);	
			XML.createNode(doc, rootElement, "sign", eocontr.sign);	
			XML.createNode(doc, rootElement, "key", eocontr.key);	

			//DM+
			login = doc.createElement("eootvet");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", eootvet.user);	
			XML.createNode(doc, rootElement, "pwd", eootvet.pwd);	
			XML.createNode(doc, rootElement, "sign", eootvet.sign);	
			XML.createNode(doc, rootElement, "key", eootvet.key);	

			//DM+!!!
			login = doc.createElement("eoadmin");
			rootElement.appendChild(login);
			XML.createNode(doc, rootElement, "user", eoadmin.user);	
			XML.createNode(doc, rootElement, "pwd", eoadmin.pwd);	
			XML.createNode(doc, rootElement, "sign", eoadmin.sign);	
			XML.createNode(doc, rootElement, "key", eoadmin.key);	



			XML.createXMLFile(doc, fl);
			Log.msg("XML с настройками пользователей " + fl + " создан.");				

			//	XML.validate(Settings.testProj + "XMLSchema\\settings\\login.xsd",fl);
		}

		/**
		 * считывает настройки из файла
		 * @param src полный путь к файлу
		 */
		public static void readXML(String src)
		{
			//XML.validate(Settings.testProj + "XMLSchema\\settings\\login.xsd",src);

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

			//DM+
			login = (Element) root.getElementsByTagName("eocontr").item(0);
			eocontr = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));	

			//DM+
			login = (Element) root.getElementsByTagName("eootvet").item(0);
			eootvet = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));	

			//DM+!!
			login = (Element) root.getElementsByTagName("eoadmin").item(0);
			eoadmin = new LoginInfo(XML.getChildValueString("user", login), XML.getChildValueString("pwd", login),
					XML.getChildValueString("sign", login), XML.getChildValueString("key", login));	

			Log.msg("XML с настройками пользователей " + src + " загружен в программу.");
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

		/**
		 * создание файла с настройками
		 * @param fl полный путь к файлу
		 */
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

			Log.msg("XML с настройками для генерации R-пакета " + fl + " создан.");
			//XML.validate(Settings.testProj + "XMLSchema\\settings\\sign.xsd",fl);
		}

		/**
		 * считывает настройки из файла
		 * @param src полный путь к файлу
		 */
		public static void readXML(String src)
		{
			//XML.validate(Settings.testProj + "XMLSchema\\settings\\sign.xsd",src);

			Element eElement = XML.getXMLRootElement(src);

			signobr = XML.getChildValueString("signobr", eElement);		
			keyobr = XML.getChildValueString("keyobr", eElement);	
			signcontr = XML.getChildValueString("signcontr", eElement);		
			keycontr = XML.getChildValueString("keycontr", eElement);		

			Log.msg("XML с настройками для генерации R-пакета " + src + " загружен в программу.");
		}


	}
}


