package ru.sabstest;

import java.text.SimpleDateFormat;

import org.w3c.dom.Element;

public class ED743_VER extends Holder<ED243> implements Generate<Element> {

	static int i = 300;
	
	public ED743_VER()
	{
		i++;
	}
	
	@Override
	ED243 createHolderObject() {
		return new ED243();
	}

	@Override
	public boolean generateFrom(Element root) {
		
		edNo = i;
		edDate = Settings.operDate;
		edAuthor = Settings.bik.substring(2) + "000";
		edReceiver = Settings.bik.substring(2) + "000";		
		
		ed = new ED243();
		
		ed.generateFrom(root);	
		
		iEdNo = ed.edNo;
		iEdDate = ed.edDate;
		iEdAuthor = ed.edAuthor;

		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];	

		setFileName();

		return true;
	}

	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED743_VER";
		
	}

	@Override
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			int idPacet = insertIntoDBPacket(db, 0, "17");
			
			String query =  "INSERT INTO [dbo].[epay_EDVer]([ID_PACKET], [ID_DEPART], [ID_ARM], [InOutMode],\r\n" + 
					" [EDNo], [EDDate], [EDAuthor], [EDReceiver],\r\n" + 
					" [ini_EdNo], [ini_EdDate], [ini_EdAutor], [PacetCode], [EDQuantity], [Summa],\r\n" + 
					" [VerificationDate], [VerificationCode], [MSGID], [Annotation], [CtrlCode],\r\n" + 
					" [CtrlTime], [ErrorDiagnostic], [VCtrlCode])\r\n" +					 
			"VALUES(" + DB.toString(idPacet) + ", '', '0', '0',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ",\r\n" +
			DB.toString(iEdNo) + ", " + DB.toString(iEdDate) + ", " + DB.toString(iEdAuthor) + ", 0, 0, '', \r\n" + 
			"'', " + DB.toString(ed.iEdNo) + ", " + DB.toString(ed.iEdAuthor) + ", " + DB.toString(ed.edReceiver) + ", '17', \r\n" +  
			DB.toString(ed.edDefineRequestCode) + ", " + DB.toString(ed.iEdDate) + ", '')";			
			db.st.executeUpdate(query);
			
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
		
	}

}
