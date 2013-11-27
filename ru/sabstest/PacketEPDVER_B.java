package ru.sabstest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * ЭПД по возврату платежей
 * 
 * @author Admin
 * 
 */
public class PacketEPDVER_B extends Packet implements ReadED,
	Generate<PacketEPD> {
    private List<VERReturnPayt> rdList;

    public int edQuantity;
    public int sum;

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + edQuantity;
	result = prime * result + ((rdList == null) ? 0 : rdList.hashCode());
	result = prime * result + sum;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PacketEPDVER_B other = (PacketEPDVER_B) obj;
	if (edQuantity != other.edQuantity)
	    return false;
	if (rdList == null) {
	    if (other.rdList != null)
		return false;
	} else if (!rdList.equals(other.rdList))
	    return false;
	if (sum != other.sum)
	    return false;
	return true;
    }

    public PacketEPDVER_B() {
	rdList = new ArrayList<VERReturnPayt>();
    }

    @Override
    public void setFileName() {
	filename = Settings.kcoi
		+ new SimpleDateFormat("yyyyMMdd").format(edDate)
		+ String.format("%09d", edNo) + ".PacketEPDVER_B";
    }

    @Override
    public boolean generateFrom(PacketEPD pdl) {

	secondSign = new Sign(Settings.Sign.keycontr, Settings.Sign.signcontr);
	firstSign = new Sign(Settings.Sign.keyobr, Settings.Sign.signobr);

	rdList = new ArrayList<VERReturnPayt>();

	edNo = pdl.edNo + 1000;
	edDate = pdl.edDate;
	edAuthor = Settings.rkc.substring(2) + "000";// pdl.edReceiver;
	edReceiver = Settings.bik.substring(2) + "000";

	setFileName();

	for (int i = 0; i < pdl.size(); i++) {
	    PaymentDocument pd = pdl.get(i);
	    String resultCode = PacketESIDVER.CodeList
		    .getResultCodeByBIC(pd.payee.bic);
	    if (ErrorCode.contains(resultCode)) {
		VERReturnPayt rd = new VERReturnPayt();
		rd.generateFromPaymentDocument(pd, edAuthor, resultCode);
		rdList.add(rd);
	    }

	}
	Collections.sort(rdList);

	if (rdList == null || rdList.size() == 0)
	    return false;
	else {
	    edQuantity = size();
	    sum = sum();
	    return true;
	}
    }

    @Deprecated
    public void createFile(String folder) {
	Document doc = XML.createNewDoc();
	Element root = doc.createElement("PacketEPDVER_B");
	doc.appendChild(root);

	root.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");

	root.setAttribute("EDNo", Integer.toString(edNo));
	root.setAttribute("EDDate",
		new SimpleDateFormat("yyyy-MM-dd").format(edDate));
	root.setAttribute("EDAuthor", edAuthor);
	root.setAttribute("EDReceiver", edReceiver);
	root.setAttribute("EDQuantity", Integer.toString(edQuantity));
	root.setAttribute("Sum", Integer.toString(sum));

	ListIterator<VERReturnPayt> iter = rdList.listIterator();
	while (iter.hasNext()) {
	    VERReturnPayt rd = iter.next();
	    root.appendChild(rd.createED(doc));
	}

	XML.createXMLFile(doc, folder + filename);

    }

    @Override
    public void readXML(Element root) {
	if (!root.getLocalName().equals("PacketEPDVER_B"))
	    return;

	rdList = new ArrayList<VERReturnPayt>();

	super.readXML(root);
	edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
	sum = Integer.parseInt(root.getAttribute("Sum"));

	NodeList nl = root.getElementsByTagNameNS("*", "VERReturnPayt");

	for (int i = 0; i < nl.getLength(); i++) {
	    VERReturnPayt rd = new VERReturnPayt();
	    rd.readED((Element) nl.item(i));
	    rdList.add(rd);
	}
	Collections.sort(rdList);
    }

    /**
     * @return количество документов в пакете
     */
    public int size() {
	return rdList.size();
    }

    /**
     * @return сумма документов в пакете
     */
    public int sum() {
	int sum = 0;
	ListIterator<VERReturnPayt> iter = rdList.listIterator();
	while (iter.hasNext()) {
	    int i = iter.next().po.sum;
	    sum = sum + i;
	}

	return sum;
    }

    @Override
    /**
     * вставка пакета в БД ВЭР
     * @param filename полный путь к файлу
     */
    public void insertIntoDB() {
	try {
	    DB db = new DB(Settings.server, Settings.db, Settings.user,
		    Settings.pwd);
	    db.connect();

	    String uic = Settings.bik.substring(2) + "000" + "00";
	    String uicRKC = Settings.kcoi + "00";

	    String query = "INSERT INTO [dbo].[epay_Packet]\r\n"
		    + "([ID_Depart], [ID_ARM], [User_Insert], [InOutMode],\r\n"
		    + " [Date_Oper], [EDNo], [EDDate], [EDAuthor], [EDReceiver], [EDQuantity],\r\n"
		    + " [Summa], [SystemCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n"
		    + " [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n"
		    + " [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n"
		    + " [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate], \r\n"
		    + "[IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n"
		    + "VALUES(null, 0, null, 0,\r\n"
		    + DB.toString(Settings.operDate)
		    + ", "
		    + DB.toString(edNo)
		    + ", "
		    + DB.toString(edDate)
		    + ", "
		    + DB.toString(edAuthor)
		    + ", "
		    + DB.toString(edReceiver)
		    + ", "
		    + DB.toString(edQuantity)
		    + ",\r\n"
		    + DB.toString(sum)
		    + ", null, null, 4, "
		    + DB.toString(filename)
		    + ", 0, 0, \r\n"
		    + " 0, null, null, null, "
		    + DB.toString(uic)
		    + ", "
		    + DB.toString(uicRKC)
		    + ", 1, \r\n"
		    + // Mesto MesFrom?
		    " 5, null, '20120202', 3, 1, 1, 1,\r\n"
		    + " 0, 4, 3, 0, 1, null, null,\r\n"
		    + " null, null, null, 20, '', NULL, NULL, NULL, NULL)";
	    db.st.executeUpdate(query);
	    db.close();

	    int idPacet = Integer
		    .parseInt(DB
			    .selectFirstValueSabsDb("select max(ID_PACKET) from dbo.epay_Packet"));

	    ListIterator<VERReturnPayt> iter = rdList.listIterator();
	    while (iter.hasNext())
		iter.next().insertIntoDbVer(idPacet, filename);

	} catch (Exception e) {
	    e.printStackTrace();
	    Log.msg(e);
	}
    }

    @Override
    public void readEncodedFile(File src, boolean isUTF) {
	readXML(getEncodedElement(src.getAbsolutePath(), isUTF));
	filename = src.getName();
    }

    @Override
    public int compareTo(ReadED o) {
	return compareTo((Packet) o);
    }
}
