package ru.sabstest;


import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;





/**
 * Класс Изменения в БД
 * @author Admin 
 */
public class DeltaDB {
	static private List<TableMeta> tables;
	static private int count = 1;
	
	
	
	/**
	 * создает файл с изменениями в БД
	 * @param filename полный путь к файлу
	 */
	public static void createXML(String num)
	{
		try 
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);

			db.connect();

			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("deltadb");
			doc.appendChild(rootElement);			
			ListIterator <TableMeta> iter = tables.listIterator();

			Log.msg("Начало создания XML с изменениями в БД.");
			while(iter.hasNext())
			{

				Element tbl = doc.createElement("table");
				rootElement.appendChild(tbl);
				String table = iter.next().name + "_log";
				tbl.setAttribute("name", table);
				ResultSet rs = db.st.executeQuery("select * from "+ table + " order by changedate,action");

				while(rs.next()) 
				{
					Element row = doc.createElement("row");
					row.setAttribute("id", Integer.toString(rs.getRow()));
					row.setAttribute("action", rs.getString("action"));
					row.setAttribute("changedate", rs.getString("changedate"));
					tbl.appendChild(row);

					ResultSetMetaData rsMetaData = rs.getMetaData();

					int numberOfColumns = rsMetaData.getColumnCount();
					for(int i = 1; i <= numberOfColumns; i++)
					{	
						if(!(rsMetaData.getColumnName(i).equals("action")) && !(rsMetaData.getColumnName(i).equals("changedate")))
						{
							Element col = doc.createElement("column");
							col.setAttribute("name", rsMetaData.getColumnName(i));
							String s = rs.getString(i)!= null ? rs.getString(i) : "";
							col.appendChild(doc.createTextNode(s));
							row.appendChild(col);	
						}
					}

				}
				Log.msg("Таблица с измениями в БД '" + table + "' записана в XML.");
			}

			XML.createXMLFile(doc, Settings.testProj + "\\tests\\" + Settings.folder + "\\output\\" + num + "\\deltadb" + String.format("%03d", count) + ".xml");
			Log.msg("XML с изменениями в БД " + Settings.testProj + "\\tests\\" + Settings.folder + "\\output\\" + num + "\\deltadb" + String.format("%03d", count) + ".xml" + " создан.");			

			//XML.validate(Settings.testProj + "XMLSchema\\output\\deltadb.xsd",Settings.testProj + "\\tests\\" +  Settings.folder + "\\output\\" + filename);			

			count++;

			db.close();
		}catch (Exception e) {
			Log.msg(e);
			e.printStackTrace();
		}
	}

	/**
	 * создает таблицу для логирования изменений в БД 
	 * @param t описание таблицы
	 * @return создалась ли таблица
	 */
	private static boolean createTableLog(TableMeta t)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			String s = "select c.name cname, t.name tname, c.length, c.xprec, c.xscale  from dbo.syscolumns c \r\n" + 
			"	inner join dbo.systypes t on c.xtype = t.xtype\r\n" + 
			"	inner join dbo.sysobjects tb on tb.id = c.id and tb.xtype = 'u' and not (tb.name like '%_log')\r\n" + 
			"	where tb.name = '" + t.name + "' and t.name <> 'NCIID'\r\n and c.name in("+ t.toStr() +")\r\n";

			ResultSet rs = db.st.executeQuery(s);
			if (rs.next()) 
			{  
				s = "if  exists (select * from dbo.sysobjects where id = object_id('" + t.name + "_log'))\r\n" + 
				"drop table " + t.name + "_log\r\n" + 
				"\r\n" + 
				"create table dbo." + t.name + "_log(\r\n";
				do 
				{  
					s = s + rs.getString("cname") + " " + rs.getString("tname");
					if(rs.getString("tname").equals("char")|| rs.getString("tname").equals("varchar") || rs.getString("tname").equals("nchar")  || rs.getString("tname").equals("nvarchar"))
						s = s + "(" + rs.getString("length") + ")";					
					else if(rs.getString("tname").equals("decimal"))
						s = s + "(" + rs.getString("xprec") + ", " + rs.getString("xscale") + ")";
					s = s + ",\r\n";


				} while (rs.next());  
			} else {  
				return false;
			}
			s = s + "changedate datetime,\r\n" + 
			"action varchar(2))\r\n";	
			db.st.executeUpdate(s);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
			return false;
		}

	}

	/**
	 * создает тригер для отслеживания вставок в БД
	 * @param t описание таблицы
	 * @return создался ли тригер
	 */
	private static boolean createTriggerIns(TableMeta t)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			String s = "select c.name cname, t.name tname, c.length, c.xprec, c.xscale  from dbo.syscolumns c \r\n" + 
			"	inner join dbo.systypes t on c.xtype = t.xtype\r\n" + 
			"	inner join dbo.sysobjects tb on tb.id = c.id and tb.xtype = 'u' and not (tb.name like '%_log')\r\n" + 
			"	where tb.name = '" + t.name + "' and t.name <> 'NCIID'\r\n and c.name in("+ t.toStr() +")\r\n";
			ResultSet rs = db.st.executeQuery(s);
			if (rs.next()) 
			{
				String drop = "if  exists (select * from dbo.sysobjects where id = object_id('" + t.name + "_ins_trg'))\r\n drop trigger " + t.name +"_ins_trg\r\n";

				s = "create trigger dbo." + t.name + "_ins_trg on dbo." + t.name + "\r\n" + 
				"for insert\r\n" + 
				"as\r\n" + 
				"begin\r\n insert into " + t.name + "_log\r\n" + 
				"(\r\n";

				String a = "";

				do 
				{
					a = a + rs.getString("cname") + ", ";
				} while (rs.next());  
				s = s + a + "changedate, action\r\n" +
				")\r\n" + 
				"select " + a + "getdate(), 'i'\r\n" + 
				"from Inserted\r\n" + 
				"end\r\n";
				db.st.executeUpdate(drop);

			} else {  
				return false;
			}

			db.st.executeUpdate(s);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
			return false;
		}
	}

	/**
	 * создает тригер для отслеживания удалений в БД
	 * @param t описание таблицы
	 * @return создался ли тригер
	 */
	private static boolean createTriggerDel(TableMeta t)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			String s = "select c.name cname, t.name tname, c.length, c.xprec, c.xscale  from dbo.syscolumns c \r\n" + 
			"	inner join dbo.systypes t on c.xtype = t.xtype\r\n" + 
			"	inner join dbo.sysobjects tb on tb.id = c.id and tb.xtype = 'u' and not (tb.name like '%_log')\r\n" + 
			"	where tb.name = '" + t.name + "' and t.name <> 'NCIID'\r\n and c.name in("+ t.toStr() +")\r\n";
			ResultSet rs = db.st.executeQuery(s);
			if (rs.next()) 
			{
				String drop = "if  exists (select * from dbo.sysobjects where id = object_id('" + t.name + "_del_trg'))\r\n drop trigger " + t.name +"_del_trg\r\n";

				s = "create trigger dbo." + t.name + "_del_trg on dbo." + t.name + "\r\n" + 
				"for delete\r\n" + 
				"as\r\n" + 
				"begin\r\n insert into " + t.name + "_log\r\n" + 
				"(\r\n";

				String a = "";

				do 
				{
					a = a + rs.getString("cname") + ", ";
				} while (rs.next());  
				s = s + a + "changedate, action\r\n" +
				")\r\n" + 
				"select " + a + "getdate(), 'd'\r\n" + 
				"from Deleted\r\n" + 
				"end\r\n";
				db.st.executeUpdate(drop);

			} else {  
				return false;
			}

			db.st.executeUpdate(s);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
			return false;
		}
	}

	/**
	 * создает тригер для отслеживания обновлений в БД
	 * @param t описание таблицы
	 * @return создался ли тригер
	 */
	private static boolean createTriggerUpd(TableMeta t)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			String s = "select c.name cname, t.name tname, c.length, c.xprec, c.xscale  from dbo.syscolumns c \r\n" + 
			"	inner join dbo.systypes t on c.xtype = t.xtype\r\n" + 
			"	inner join dbo.sysobjects tb on tb.id = c.id and tb.xtype = 'u' and not (tb.name like '%_log')\r\n" + 
			"	where tb.name = '" + t.name + "' and t.name <> 'NCIID'\r\n and c.name in("+ t.toStr() +")\r\n";
			ResultSet rs = db.st.executeQuery(s);
			if (rs.next()) 
			{
				String drop = "if  exists (select * from dbo.sysobjects where id = object_id('" + t.name + "_upd_trg'))\r\n drop trigger " + t.name +"_upd_trg\r\n";

				s = "create trigger dbo." + t.name + "_upd_trg on dbo." + t.name + "\r\n" + 
				"for update\r\n" + 
				"as\r\n" + 
				"begin\r\n" ;

				String a = "", b = "";				

				do 
				{
					a = a + rs.getString("cname") + ", ";
					b = b + "i." + rs.getString("cname") + " = d." + rs.getString("cname") + " and ";
				} while (rs.next());  


				String prkeysql = "Select o2.name As TableName, o.name As PrimaryKeyName, tc.name As ColumnName\r\n" + 
				"From sysconstraints c\r\n" + 
				"Inner Join sysobjects o On c.constid = o.id\r\n" + 
				"Inner Join sysindexes i On o.name = i.name And o.parent_obj = i.id\r\n" + 
				"Inner Join sysindexkeys ik On i.id = ik.id And i.indid = ik.indid\r\n" + 
				"Inner Join syscolumns tc On ik.id = tc.id And ik.colid = tc.colid\r\n" + 
				"Inner Join sysobjects o2 On ik.id = o2.id\r\n" + 
				"Where o.xtype = 'PK' And o2.xtype = 'U' and o2.name = '" + t.name + "'\r\n";

				rs = db.st.executeQuery(prkeysql);
				rs.next();
				String prkey = rs.getString("ColumnName");
				b = b + "i." + prkey + " = d." + prkey;

				s = s + "insert into " + t.name + "_log\r\n" + 
				"(\r\n" +
				a + "changedate, action\r\n" +
				")\r\n" + 
				"select " + a + "getdate(), 'ud'\r\n" + 
				"from Deleted d\r\n" + 
				"where not exists(select 1 from Inserted i where " + b + ")\r\n" + 
				"insert into " + t.name + "_log\r\n" + 
				"(\r\n" + a + "changedate, action\r\n" +
				")\r\n" + 
				"select " + a + "getdate(), 'ui'\r\n" + 
				"from Inserted i\r\n" +		
				"where not exists(select 1 from Deleted d where " + b + ")\r\n" + 
				"end\r\n";

				db.st.executeUpdate(drop);

			} else {  
				return false;
			}

			db.st.executeUpdate(s);
			db.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
			return false;
		}
	}

	
	/**
	 * создает таблицы и тригера для отслеживания изменений в БД для таблиц и колонок, указанных в настройках
	 */
	public static void createDBLog()
	{
		try 
		{
			ListIterator <TableMeta> iter = tables.listIterator();

			while(iter.hasNext()) 
			{
				TableMeta t = iter.next();
				createTableLog(t);
				createTriggerIns(t);
				createTriggerDel(t);
				createTriggerUpd(t);
			}
			Log.msg("Таблицы для записи лога для таблиц " + toStr() + " созданы.");
			Log.msg("Триггера для записи лога для таблиц " + toStr() + " созданы.");

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	
	/**
	 * удаляет таблицы и тригера, которые использовались для логирования
	 */
	public static void deleteDBLog()
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			String s = "declare cur cursor for  \r\n" + 
			"select name,id from dbo.sysobjects where xtype = 'u' and not (name like '%_log')and name in("+ DeltaDB.toStr() +")\r\n" + 
			"\r\n" + 
			"declare @tblname varchar(128), @tblid int, @str varchar(8000)\r\n" + 
			"\r\n" + 
			" \r\n" + 
			"\r\n" + 
			"open cur\r\n" + 
			"fetch next from cur into @tblname, @tblid  \r\n" + 
			"\r\n" + 
			"while @@fetch_status = 0   \r\n" + 
			"begin	\r\n" + 
			"	set @str = 'if  exists (select * from dbo.sysobjects where id = object_id(''' + @tblname + '_ins_trg''))\r\n" + 
			"drop trigger ' + @tblname + '_ins_trg'\r\n" + 
			"	print @str\r\n" + 
			"	exec(@str)\r\n" + 
			"\r\n" + 
			"	set @str = 'if  exists (select * from dbo.sysobjects where id = object_id(''' + @tblname + '_upd_trg''))\r\n" + 
			"drop trigger ' + @tblname + '_upd_trg'\r\n" + 
			"	print @str\r\n" + 
			"	exec(@str)\r\n" + 
			"\r\n" + 
			"	set @str = 'if  exists (select * from dbo.sysobjects where id = object_id(''' + @tblname + '_del_trg''))\r\n" + 
			"drop trigger ' + @tblname + '_del_trg'\r\n" + 
			"	print @str\r\n" + 
			"	exec(@str)\r\n" + 
			"\r\n" + 
			"	set @str = 'if  exists (select * from dbo.sysobjects where id = object_id(''' + @tblname + '_log''))\r\n" + 
			"drop table ' + @tblname + '_log'\r\n" + 
			"	print @str\r\n" + 
			"	exec(@str)\r\n" + 
			"	\r\n" + 
			"	fetch next from cur into @tblname, @tblid    \r\n" + 
			"end   \r\n" + 
			"\r\n" + 
			"close cur   \r\n" + 
			"deallocate cur";
			db.st.executeUpdate(s);

			Log.msg("Таблицы и триггера для записи лога для таблиц " + toStr() + " удалены.");
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	
	/**
	 * @return
	 * возращает в виде строки все таблицы, для которых записываются изменения
	 */
	public static String toStr()
	{
		ListIterator <TableMeta> iter = tables.listIterator();
		String s = "";
		int i = 0;
		while(iter.hasNext()) 
		{
			s = s + ((i==0)?"":", ") + "'" + iter.next().name + "'";
			i++;
		}


		return s;

	}
	
	/**
	 * создание xml с таблицами и колонками для отслеживания изменений
	 * @param fl полный путь к файлу
	 */
	public static void createXMLSettings(String fl)
	{		
		Document doc = XML.createNewDoc();
		Element rootElement = doc.createElement("deltadb");
		doc.appendChild(rootElement);

		ListIterator <TableMeta> iter = tables.listIterator();
		while(iter.hasNext())
		{
			TableMeta t = iter.next();				
			Element tbl = doc.createElement("table");
			tbl.setAttribute("name", t.name);
			rootElement.appendChild(tbl);				
			ListIterator <String> sitr = t.columns.listIterator();
			while(sitr.hasNext())
			{
				XML.createNode(doc, tbl, "column", sitr.next());
			}
		}		


		XML.createXMLFile(doc, fl);

		Log.msg("XML с настройками для подсчета изменений в БД " + fl + " создан.");

		//XML.validate(Settings.testProj + "XMLSchema\\settings\\deltadb.xsd",fl);

	}

	public static void createXMLSettings()
	{
		createXMLSettings(Settings.testProj + "settings\\deltadb.xml");
	}

	/**
	 * считывает из xml таблицы и колонки в них для отслеживания изменений
	 * @param src полный путь к файлу
	 */
	public static void readXMLSettings(String src)
	{		
		Element root = XML.getXMLRootElement(src);
		NodeList nList = root.getElementsByTagName("table");		

		tables = new ArrayList<TableMeta>();

		for (int temp = 0; temp < nList.getLength(); temp++)
		{
			Element NmElmnt = (Element) nList.item(temp);
			String s = NmElmnt.getAttribute("name");			
			TableMeta t = new TableMeta(s);

			NodeList nlList = NmElmnt.getElementsByTagName("column");
			for (int i = 0; i < nlList.getLength(); i++)
			{
				Element Elmnt = (Element) nlList.item(i);					
				NodeList Nm = Elmnt.getChildNodes();   
				s = ((Node) Nm.item(0)).getNodeValue();	
				t.add(s);
			}				
			tables.add(t);
		}
		Log.msg("XML с настройками для подсчета изменений в БД " + src + " загружен в программу.");		
	}

	public static File[] getDeltaDBFiles(String fld)
	{
		FileFilter filter = new FileFilter() {			
			@Override
			public boolean accept(File pathname) {
				if(pathname.getName().startsWith("deltadb"))
					return true;
				else
					return false;
			}
		};
		
		return new File(fld).listFiles(filter);	
	}
	
	public static boolean cmpDeltaDBfld(File[] fset, File[] fssrc)
	{
		if(fset.length != fssrc.length)
			return false;
		
		for(int i = 0; i < fset.length; i++)
			if(!cmpDeltaDB(fset[i].getAbsolutePath(), fssrc[i].getAbsolutePath()))
				return false;
		
		return true;
	}
	
	/**
	 * @param et первый xml с изменениями в БД
	 * @param src второй xml с изменениями в БД
	 * @return совпадают ли изменения
	 */
	public static boolean cmpDeltaDB(String et, String src)
	{
		Delta etd = new Delta();
		Delta srcd = new Delta();
		etd.readXML(et);
		srcd.readXML(src);
		return srcd.equals(etd);	
	}


	/**
	 * Класс Изменения в БД
	 * @author Admin
	 *
	 */
	private static class Delta
	{
		List<Table> tables;

		private Delta()
		{
			tables = new ArrayList<Table>();
		}

		/**
		 * считывает файл с изменениями в бд
		 * @param src полный путь к файлу
		 */
		private void readXML(String src)
		{
			

		//	XML.validate(Settings.testProj + "XMLSchema\\output\\deltadb.xsd",src);

			Element root = XML.getXMLRootElement(src);

			NodeList nList = root.getElementsByTagName("table");

			for (int temp = 0; temp < nList.getLength(); temp++) 
			{

				//Node nNode = nList.item(temp);
				Element NmElmnt = (Element) nList.item(temp);
				String s = NmElmnt.getAttribute("name");	

				Table t = new Table(s);

				NodeList nlList = NmElmnt.getElementsByTagName("row");
				for (int i = 0; i < nlList.getLength(); i++)
				{
					Element Elmnt = (Element) nlList.item(i);	
					Line l = new Line(Integer.parseInt(Elmnt.getAttribute("id")), Elmnt.getAttribute("action"));

					NodeList Nm = Elmnt.getElementsByTagName("column");   
					for (int j = 0; j < Nm.getLength(); j++)
					{
						Element El = (Element) Nm.item(j);	
						NodeList N = El.getChildNodes();   
						if(((Node) N.item(0)) == null)
							s = "";
						else
							s = ((Node) N.item(0)).getNodeValue();	

						if(!El.getAttribute("name").equals("changedate") && !El.getAttribute("name").equals("action"))
						{
							l.addCol(s);
							if(i == 0)
								t.tm.add(El.getAttribute("name"));
						}
					}				

					t.lines.add(l);
				}				

				tables.add(t);
			}		
		}

		
		/**
		 * @param et
		 * @return совпадают ли изменения в бд
		 */
		private boolean equals(Delta et)
		{
			if(tables.size() != et.tables.size())
			{
				Log.msgCMP("Количество таблиц в файлах изменений не совпадает.");
				return false;
			}

			ListIterator <Table> iterTbl = et.tables.listIterator();

			while(iterTbl.hasNext())
			{
				if(!contains(iterTbl.next()))
					return false;
			}
			return true;
		}
		
		/**
		 * @param t таблица
		 * @return содержатся ли полностью данные из таблицы в изменениях в бд
		 */
		private boolean contains(Table t)
		{

			ListIterator <Table> iterTbl = tables.listIterator();
			while(iterTbl.hasNext())
			{
				Table tbl = iterTbl.next();
				if(tbl.tm.equals(t.tm) && tbl.lines.size() == t.lines.size())
				{
					ListIterator <Line> iterLn = t.lines.listIterator();
					while(iterLn.hasNext())
					{
						if(!tbl.contains(iterLn.next()))
							return false;
					}
					return true;
				}				
			}		
			Log.msgCMP("Таблица из эталона " + t.tm.name + " не найдена в изменениях БД.");
			return false;
		}
	}


	/**
	 * Класс Описание таблицы
	 * @author Admin
	 *
	 */
	private static class TableMeta
	{
		String name;
		List<String> columns;


		TableMeta(String tbl)
		{
			name = tbl;
			columns = new ArrayList<String>();
		}

		/**
		 * добавляет колонку в описание таблицы
		 * @param col наименование колонки
		 */
		private void add(String col)
		{
			columns.add(col);
		}

		/**
		 * @return строку всех колонок в таблице
		 */
		private String toStr()
		{
			ListIterator <String> iter = columns.listIterator();
			String s = "";
			int i = 0;
			while(iter.hasNext()) 
			{
				s = s + ((i==0)?"":", ") + "'" + iter.next() + "'";
				i++;
			}
			return s;
		}	

		/**
		 * @param tm описание таблицы
		 * @return совпадают ли описания таблиц
		 */
		private boolean equals(TableMeta tm)
		{


			if(name.equals(tm.name) && (columns.size() == tm.columns.size()))
			{
				ListIterator<String> iter =  columns.listIterator();
				while(iter.hasNext()) 
				{
					if(!tm.columns.contains(iter.next()))
						return false;
				}				
			}	
			else
			{				
				return false;
			}
			return true;
		}

	}

	/**
	 * Класс Таблица
	 * @author Admin
	 *
	 */
	private static class Table
	{
		private TableMeta tm;
		private List<Line> lines;
		private Table(String TableName)
		{
			tm = new TableMeta(TableName);
			lines = new ArrayList<Line>();
		}

		/**
		 * @param ln строка
		 * @return содержит ли таблица полностью строку
		 */
		private boolean contains(Line ln)
		{

			ListIterator <Line> iterLn = lines.listIterator();
			while(iterLn.hasNext()) 
			{
				if(iterLn.next().equals(ln))
					return true;
			}
			Log.msgCMP("Строка с id " + Integer.toString(ln.i) + " из эталона не найдена в таблице " + tm.name + " ."); 
			return false;
		}
	}

	/**
	 * класс Строка
	 * @author Admin
	 *
	 */
	private static class Line
	{
		private List<String> columns;
		private int i;
		private String action;
		private Line(int i, String action)
		{
			columns = new ArrayList<String>();
			this.i = i;
			this.action = action;
		}
		/**
		 * добавляет значение в строку
		 * @param value 
		 */
		private void addCol(String value)
		{
			columns.add(value);
		}
		/**
		 * @param ln строка
		 * @return совпадают ли строки
		 */
		private boolean equals(Line ln)
		{


			if(columns.size() == ln.columns.size() && action.equals(ln.action))
			{
				ListIterator<String> iterCol =  columns.listIterator();
				while(iterCol.hasNext()) 
				{
					if(!ln.columns.contains(iterCol.next()))
						return false;
				}				
			}	
			else
			{				
				return false;
			}
			return true;
		}
	}
}
