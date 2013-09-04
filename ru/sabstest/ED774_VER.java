package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED774_VER extends Holder<ED274> implements Generate<ED773_VER> {

	@Override
	public boolean generateFrom(ED773_VER source) {
		edNo = source.edNo + 2500;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdNo = source.edNo;
		iEdDate = source.edDate;
		iEdAuthor = source.edAuthor;
		
		ed = new ED274();
		ed.generateFrom(source.ed);
		return true;
	}

	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED774_VER";
		
	}

	@Override
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			int idPacet = insertIntoDBPacket(db, 0, "20");
			
			String query =  "INSERT INTO [dbo].[epay_EDVer]([ID_PACKET], [ID_DEPART], [ID_ARM], [InOutMode],\r\n" + 
					" [EDNo], [EDDate], [EDAuthor], [EDReceiver],\r\n" + 
					" [ini_EdNo], [ini_EdDate], [ini_EdAutor], [PacetCode], [EDQuantity], [Summa],\r\n" + 
					" [VerificationDate], [VerificationCode], [MSGID], [Annotation], [CtrlCode],\r\n" + 
					" [CtrlTime], [ErrorDiagnostic], [VCtrlCode])\r\n" +					 
			"VALUES(" + DB.toString(idPacet) + ", '', '0', '0',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ",\r\n" +
			DB.toString(iEdNo) + ", " + DB.toString(iEdDate) + ", " + DB.toString(iEdAuthor) + ", '', " + DB.toString(ed.iEdNo) + ", " + DB.toString(ed.edAuthor) + ",\r\n" + 
			DB.toString(ed.iEdDate) + ", "  + DB.toString(ed.edNo) + ", '', " + DB.toString(ed.annotation) + ", '20 ', \r\n" +  
			DB.toString("") + ", '', " + DB.toString("") + ")";			
			db.st.executeUpdate(query);
			
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
		
	}

	@Override
	ED274 createHolderObject() {
		return new ED274();
	}

}
