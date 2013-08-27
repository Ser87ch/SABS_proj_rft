package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED744_VER extends Holder<ED244> implements Generate<ED743_VER> {

	@Override
	public boolean generateFrom(ED743_VER source) {
		edNo = source.edNo + 2000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdNo = source.ed.edNo + 2500;
		iEdDate = source.ed.edDate;
		iEdAuthor = source.ed.edReceiver;
		
		ed = new ED244();
		
		setFileName();
		
		return ed.generateFrom(source.ed);
	}

	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED744_VER";
	}

	@Override
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			int idPacet = insertIntoDBPacket(db, 0, true);
			
			String query =  "INSERT INTO [dbo].[epay_EDVer]([[ID_PACKET], [ID_DEPART], [ID_ARM], [InOutMode],\r\n" + 
					" [EDNo], [EDDate], [EDAuthor], [EDReceiver],\r\n" + 
					" [ini_EdNo], [ini_EdDate], [ini_EdAutor], [PacetCode], [EDQuantity], [Summa],\r\n" + 
					" [VerificationDate], [VerificationCode], [MSGID], [Annotation], [CtrlCode],\r\n" + 
					" [CtrlTime], [ErrorDiagnostic], [VCtrlCode])\r\n" +					 
			"VALUES(" + DB.toString(idPacet) + ", null, '0', '0',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ",\r\n" +
			DB.toString(iEdNo) + ", " + DB.toString(iEdDate) + ", " + DB.toString(iEdAuthor) + ", null, " + DB.toString(ed.iEdNo) + ", " + DB.toString(ed.edAuthor) + ",\r\n" + 
			DB.toString(ed.iEdDate) + ", "  + DB.toString(ed.oEdNo) + ", " + DB.toString(ed.oEdAuthor) +  ", " + DB.toString(ed.edReceiver) + ", '18', \r\n" +  
			DB.toString(ed.edDefineRequestCode) + ", " + DB.toString(ed.oEdDate) + ", " + DB.toString(ed.edDefineAnswerCode) + ")";			
			db.st.executeUpdate(query);
			
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}

	@Override
	ED244 createHolderObject() {
		return new ED244();
	}

}
