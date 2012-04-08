package ru.sabstest;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;


public class DB implements Closeable{
	private String server;
	private String db;
	private String user;
	private String pwd;
	private Connection con;
	public Statement st;

	DB(String server, String db, String user, String pwd)
	{
		this.server = server;
		this.db = db;
		this.user = user;
		this.pwd = pwd;
	}

	public void connect() throws Exception 
	{
		try {
			if(con != null)	if (con.isValid(10)) throw new SQLException("Connection have been already opened.");

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();

			String URL = "jdbc:sqlserver://" + server + ";databaseName=" + db;

			con = DriverManager.getConnection(URL, user, pwd);


			if (con==null) System.exit(0);
			Log.msg("Подключение к " + server + " БД " + db + " выполнено.");
			st = con.createStatement();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	public boolean isConnected() throws SQLException
	{		
		if(con != null)	return con.isValid(10);
		else return false;	

	}

	@Override
	public void close()
	{
		try {
			if(con != null)
			{
				if(con.isValid(10))

				{
					st.close();
					con.close();
					Log.msg("Подключение к " + server + " БД " + db + " закрыто.");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	public static String selectFirstValueSabsDb(String s)
	{
		return selectFirstValueSabsDb(s,1);
	}

	public static String selectFirstValueSabsDb(String s, int row)
	{
		if(row == 0)
			row = 1;
		String value = "";
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			ResultSet rs = db.st.executeQuery(s);
			int i = 1;
			while(rs.next() && i<=row)
			{
				if(i==row)
					value = rs.getString(1);
				i++;
			}			

			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
		return value;
	}

	public static void insertPacetForReadUfebs(String filename)
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String query = "INSERT INTO [dbo].[UFEBS_Pacet]\r\n" + 
			"( [ID_Depart], [ID_ARM], [User_Insert], [InOutMode], [FileName], [Mesto], [KodObr]) \r\n" +					
			"VALUES(null, 2, null, 0, " + DB.toString(filename) + ", 'R', 0)";			
			db.st.executeUpdate(query);
			db.close();			

		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}

	}

	public static void insertPacetForReadVer(String filename)
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();
			String query = "INSERT INTO [dbo].[epay_Pacet]\r\n" + 
			"( [ID_Depart], [ID_ARM], [User_Insert], [InOutMode], [FileName], [Mesto], [KodObr]) \r\n" +					
			"VALUES(null, 2, null, 0, " + DB.toString(filename) + ", 'R', 0)";			
			db.st.executeUpdate(query);
			db.close();	
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}

	}

	public static String toString(String s)
	{
		if(s == null || s.equals(""))
			return "null";
		else
			return "'" + s + "'";
	}

	public static String toString(int s)
	{
		if(s == 0)
			return "null";
		else
			return Integer.toString(s);
	}

	public static String toString(Date s)
	{
		if(s == null)
			return "null";
		else
			return "'" + new SimpleDateFormat("yyyy-MM-dd").format(s) + "'";
	}

}
