package ru.sabstest;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ListIterator;

public class ED274 extends Packet implements Generate<ED273>, ReadED {

	public String infoCode;
	public String annotation;
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	//реквизиты исходного ЭПД
	public int refEdNo; //Номер ЭД в течение опердня
	public Date refEdDate; //Дата составления ЭД
	public String refEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	
	public int ed273No;
	
	public ED274(int i)
	{
		ed273No = i;
	}
	
	public ED274()
	{
		
	}
	
	@Override
	public boolean generateFrom(ED273 source) {
		edNo = source.pdList.get(ed273No).edNo + 5500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		infoCode = "8";
		annotation = "Положительные результаты всех видов контролей, предшествующих исполнению";
		refEdAuthor = source.edAuthor;
		refEdDate = source.edDate;
		refEdNo = source.edNo;
		
		iEdAuthor = source.pdList.get(ed273No).edAuthor;
		iEdDate = source.pdList.get(ed273No).edDate;
		iEdNo = source.pdList.get(ed273No).edNo;
		
		return true;		
	}

	@Override
	public void setFileName() {
		if(edAuthor.substring(0,7).equals(Settings.bik.substring(2))) //особый клиент
			filename = "K";
		else
			filename = "B";

		filename = filename + Integer.toString(Integer.parseInt(new SimpleDateFormat("MM").format(edDate)),36)
		+ Integer.toString(Integer.parseInt(new SimpleDateFormat("dd").format(edDate)),36);

		if(edAuthor.substring(0,7).equals(Settings.bik.substring(2))) //особый клиент
			filename = filename + edAuthor.substring(2,4) + edAuthor.substring(7,10) + "." + String.format("%03d", edNo);
		else
			filename = filename + edAuthor.substring(2,7) + "." + String.format("%03d", edNo);

	}

	@Override
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			int idPacet = insertIntoDBPacket(db, 0, "1");


			String query =  "INSERT [dbo].[UFEBS_Es201]([ID_PACET], [ID_DEPART], [EdNo], [EdDate],\r\n" + 
			" [EdAuthor], [EdReceiv], [CtrlCode], [CtrlTime], [Annotat],\r\n" + 
			" [MsgId], [IEdNo], [IEdDate], [IEdAuth], [FTime], [EsidCod],\r\n" + 
			" [PEpdNo], [PacDate], [PAuthor], [BeginDat], [EndDat], [BIC],\r\n" + 
			" [ACC], [Annotat1], [StopReas], [ID_ARM])\r\n" + 
			"VALUES(" + DB.toString(idPacet) + ", null, " + DB.toString(edNo) + ", " + DB.toString(edDate) + ",\r\n" +
			DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(infoCode) + ", null, " + DB.toString(annotation) + ",\r\n" +
			"null, '', '', '', null, '274',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", null, null, null,\r\n" +
			"null, null, null, '0')";			
			db.st.executeUpdate(query);		

			db.close();			
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}

	}

	@Override
	public int compareTo(ReadED arg0) {
		return super.compareTo((Packet) arg0);
	}

	@Override
	public void readEncodedFile(File src, boolean isUTF) {
		throw new UnsupportedOperationException();
		
	}

}
