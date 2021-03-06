package ru.sabstest;

import java.text.SimpleDateFormat;

public class ED708_VER extends Holder<ED208> implements Generate<ED743_VER> {

    int ed773No;

    public ED708_VER(int i) {
	ed773No = i;
    }

    public ED708_VER() {

    }

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

	setFileName();

	return ed.generateFrom(source.ed);
    }

    @Override
    public void setFileName() {
	filename = Settings.kcoi
		+ new SimpleDateFormat("yyyyMMdd").format(edDate)
		+ String.format("%09d", edNo) + ".ED708_VER";
    }

    @Override
    public void insertIntoDB() {
	try {
	    DB db = new DB(Settings.server, Settings.db, Settings.user,
		    Settings.pwd);
	    db.connect();

	    int idPacet = insertIntoDBPacket(db, 0, "16");

	    String query = "INSERT INTO [dbo].[epay_EDVer]([ID_PACKET], [ID_DEPART], [ID_ARM], [InOutMode],\r\n"
		    + " [EDNo], [EDDate], [EDAuthor], [EDReceiver],\r\n"
		    + " [ini_EdNo], [ini_EdDate], [ini_EdAutor], [PacetCode], [EDQuantity], [Summa],\r\n"
		    + " [VerificationDate], [VerificationCode], [MSGID], [Annotation], [CtrlCode],\r\n"
		    + " [CtrlTime], [ErrorDiagnostic], [VCtrlCode])\r\n"
		    + "VALUES("
		    + DB.toString(idPacet)
		    + ", '', '0', '0',\r\n"
		    + DB.toString(edNo)
		    + ", "
		    + DB.toString(edDate)
		    + ", "
		    + DB.toString(edAuthor)
		    + ", "
		    + DB.toString(edReceiver)
		    + ",\r\n"
		    + DB.toString(iEdNo)
		    + ", "
		    + DB.toString(iEdDate)
		    + ", "
		    + DB.toString(iEdAuthor)
		    + ", '', "
		    + DB.toString(ed.iEdNo)
		    + ", "
		    + DB.toString(ed.iEdAuthor)
		    + ",\r\n"
		    + DB.toString(ed.iEdDate)
		    + ", "
		    + DB.toString(ed.edNo)
		    + ", '', "
		    + DB.toString(ed.annotation)
		    + ", '16', \r\n"
		    + DB.toString(ed.resultCode)
		    + ", '', "
		    + DB.toString(ed.ctrlCode) + ")";
	    db.st.executeUpdate(query);

	    db.close();

	} catch (Exception e) {
	    e.printStackTrace();
	    Log.msg(e);
	}

    }

    public Generate<ED773_VER> getGenerateED773() {
	return new Generate<ED773_VER>() {

	    @Override
	    public void setFileName() {
		ED708_VER.this.setFileName();

	    }

	    @Override
	    public boolean isVER() {
		return ED708_VER.this.isVER();
	    }

	    @Override
	    public void insertIntoDB() {
		ED708_VER.this.insertIntoDB();

	    }

	    @Override
	    public void insertForRead() {
		ED708_VER.this.insertForRead();

	    }

	    @Override
	    public Sign[] getSigns() {
		return ED708_VER.this.getSigns();
	    }

	    @Override
	    public boolean generateFrom(ED773_VER source) {
		edNo = source.ed.pdList.get(ed773No).edNo + 4000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		iEdNo = source.ed.pdList.get(ed773No).edNo + 4500;
		iEdDate = source.ed.edDate;
		iEdAuthor = source.ed.edReceiver;

		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];

		ed = new ED208(ed773No);

		setFileName();

		return ed.generateFrom(source.ed);

	    }
	};
    }

    public Generate<ED744_VER> getGenerateED744() {
	return new Generate<ED744_VER>() {

	    @Override
	    public void setFileName() {
		ED708_VER.this.setFileName();

	    }

	    @Override
	    public boolean isVER() {
		return ED708_VER.this.isVER();
	    }

	    @Override
	    public void insertIntoDB() {
		ED708_VER.this.insertIntoDB();

	    }

	    @Override
	    public void insertForRead() {
		ED708_VER.this.insertForRead();

	    }

	    @Override
	    public Sign[] getSigns() {
		return ED708_VER.this.getSigns();
	    }

	    @Override
	    public boolean generateFrom(ED744_VER source) {
		edNo = source.edNo + 4000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		iEdNo = source.ed.edNo + 4500;
		iEdDate = source.ed.edDate;
		iEdAuthor = source.ed.edReceiver;

		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];

		ed = new ED208();

		setFileName();

		return ed.generateFrom(source.ed);

	    }
	};
    }

    public Generate<ED774_VER> getGenerateED774() {
	return new Generate<ED774_VER>() {

	    @Override
	    public void setFileName() {
		ED708_VER.this.setFileName();

	    }

	    @Override
	    public boolean isVER() {
		return ED708_VER.this.isVER();
	    }

	    @Override
	    public void insertIntoDB() {
		ED708_VER.this.insertIntoDB();

	    }

	    @Override
	    public void insertForRead() {
		ED708_VER.this.insertForRead();

	    }

	    @Override
	    public Sign[] getSigns() {
		return ED708_VER.this.getSigns();
	    }

	    @Override
	    public boolean generateFrom(ED774_VER source) {
		edNo = source.edNo + 5000;
		edDate = source.edDate;
		edAuthor = source.edReceiver;
		edReceiver = source.edAuthor;

		iEdNo = source.ed.edNo + 5500;
		iEdDate = source.ed.edDate;
		iEdAuthor = source.ed.edReceiver;

		Sign[] s = ClientList.getSignByUIC(edAuthor);
		firstSign = s[0];
		secondSign = s[1];

		ed = new ED208();

		setFileName();

		return ed.generateFrom(source.ed);

	    }
	};
    }

    @Override
    ED208 createHolderObject() {
	return new ED208();
    }

}
