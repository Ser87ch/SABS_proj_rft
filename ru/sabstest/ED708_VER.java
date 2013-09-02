package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED708_VER extends Holder<ED208> implements Generate<ED743_VER> {

	@Override
	public boolean generateFrom(ED743_VER source) {
		edNo = source.edNo + 3000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;
		
		iEdNo = source.ed.edNo + 3500;
		iEdDate = source.ed.edDate;
		iEdAuthor = source.ed.edReceiver;
		
		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];
		
		ed = new ED208();
		
		return ed.generateFrom(source.ed);
	}
	
	@Override
	public void setFileName() {
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED708_VER";		
	}

	@Override
	public void insertIntoDB() {
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			int idPacet = insertIntoDBPacket(db, 0, true);
			
			String query =  "INSERT INTO [dbo].[epay_EDVer]([ID_PACKET], [ID_DEPART], [ID_ARM], [InOutMode],\r\n" + 
					" [EDNo], [EDDate], [EDAuthor], [EDReceiver],\r\n" + 
					" [ini_EdNo], [ini_EdDate], [ini_EdAutor], [PacetCode], [EDQuantity], [Summa],\r\n" + 
					" [VerificationDate], [VerificationCode], [MSGID], [Annotation], [CtrlCode],\r\n" + 
					" [CtrlTime], [ErrorDiagnostic], [VCtrlCode])\r\n" +					 
			"VALUES(" + DB.toString(idPacet) + ", null, '0', '0',\r\n" +
			DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ",\r\n" +
			DB.toString(iEdNo) + ", " + DB.toString(iEdDate) + ", " + DB.toString(iEdAuthor) + ", null, " + DB.toString(ed.iEdNo) + ", " + DB.toString(ed.edAuthor) + ",\r\n" + 
			DB.toString(ed.iEdDate) + ", "  + DB.toString(ed.edNo) + ", null, " + DB.toString(ed.annotation) + ", '16', \r\n" +  
			DB.toString(ed.resultCode) + ", null, " + DB.toString(ed.ctrlCode) + ")";			
			db.st.executeUpdate(query);
			
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
		
	}
	
	public Generate<ED773_VER> getGenerateED773()
	{
		return new Generate<ED773_VER>() {
			
			@Override
			public void setFileName() {
				this.setFileName();
				
			}
			
			@Override
			public boolean isVER() {
				return this.isVER();
			}
			
			@Override
			public void insertIntoDB() {
				this.insertIntoDB();
				
			}
			
			@Override
			public void insertForRead() {
				this.insertForRead();
				
			}
			
			@Override
			public Sign[] getSigns() {
				return this.getSigns();
			}
			
			@Override
			public boolean generateFrom(ED773_VER source) {
				// TODO генерация из ED773
				return false;
			}
		};
	}

	@Override
	ED208 createHolderObject() {
		return new ED208();
	}

}
