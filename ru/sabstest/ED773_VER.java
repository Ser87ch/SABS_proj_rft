package ru.sabstest;

import java.text.SimpleDateFormat;
import java.util.ListIterator;

import org.w3c.dom.Element;

public class ED773_VER extends Holder<ED273>  implements Generate<Element> {

	@Override
	public boolean generateFrom(Element root) {
		edNo = Integer.parseInt(root.getAttribute("EPDNo"));
		edDate = Settings.operDate;

		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		
		ed = new ED273();
		
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
		filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".ED773_VER";
		
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
			"'', " + DB.toString("") + ", " + DB.toString("") + ", " + DB.toString("") + ", '19', \r\n" +  
			DB.toString("") + ", " + DB.toString("") + ", '')";			
			db.st.executeUpdate(query);
			
			ListIterator <PaymentDocument> iter = ed.pdList.listIterator();
			while(iter.hasNext())
			{
				iter.next().insertIntoDbVer(idPacet, filename);
			}
			
			db.close();		

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
		
	}
	
	@Override
	ED273 createHolderObject() {
		return new ED273();
	}

}
