package ru.sabstest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * ����� ����� ���
 * 
 * @author Admin
 * 
 */
public class PacketEPD extends Packet implements Generate<Element>, ReadED {

    public int edQuantity;
    public int sum;
    public String systemCode;

    public List<PaymentDocument> pdList;

    public PacketEPD() {

    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + edQuantity;
	result = prime * result + ((pdList == null) ? 0 : pdList.hashCode());
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
	PacketEPD other = (PacketEPD) obj;
	if (edQuantity != other.edQuantity)
	    return false;
	if (pdList == null) {
	    if (other.pdList != null)
		return false;
	} else if (!pdList.equals(other.pdList))
	    return false;
	if (sum != other.sum)
	    return false;
	return true;
    }

    @Override
    public void setFileName() {
	if (isVER())
	    filename = Settings.kcoi
		    + new SimpleDateFormat("yyyyMMdd").format(edDate)
		    + String.format("%09d", edNo) + ".PacketEPDVER";
	else {
	    if (edAuthor.substring(0, 7).equals(Settings.bik.substring(2))) // ������
									    // ������
		filename = "K";
	    else
		filename = "B";

	    filename = filename
		    + Integer.toString(Integer.parseInt(new SimpleDateFormat(
			    "MM").format(edDate)), 36)
		    + Integer.toString(Integer.parseInt(new SimpleDateFormat(
			    "dd").format(edDate)), 36);

	    if (edAuthor.substring(0, 7).equals(Settings.bik.substring(2))) // ������
									    // ������
		filename = filename + edAuthor.substring(2, 4)
			+ edAuthor.substring(7, 10) + "."
			+ String.format("%03d", edNo);
	    else
		filename = filename + edAuthor.substring(2, 7) + "."
			+ String.format("%03d", edNo);
	}
    }

    /**
     * @return ���������� ���������� � ������
     */
    public int size() {
	return pdList.size();
    }

    /**
     * @return ����� ���������� � ������
     */
    public int sumAll() {
	int sum = 0;
	ListIterator<PaymentDocument> iter = pdList.listIterator();
	while (iter.hasNext()) {
	    int i = iter.next().sum;
	    sum = sum + i;

	}

	return sum;
    }

    /**
     * @param i
     *            ����� ���������
     * @return �������� ��� ������� i
     */
    public PaymentDocument get(int i) {
	return pdList.get(i);
    }

    @Override
    public String toString() {
	String str = "";
	ListIterator<PaymentDocument> iter = pdList.listIterator();
	while (iter.hasNext())
	    str = str + iter.next().toString() + "\n";

	return str;
    }

    @Override
    public void readXML(Element root) {

	if (root.getLocalName().equals("PacketEPDVER"))
	    isVER = true;
	else if (root.getLocalName().equals("PacketEPD"))
	    isVER = false;
	else
	    return;

	pdList = new ArrayList<PaymentDocument>();

	super.readXML(root);
	edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
	sum = Integer.parseInt(root.getAttribute("Sum"));
	systemCode = root.getAttribute("SystemCode");

	NodeList nl = root.getChildNodes();
	for (int i = 0; i < nl.getLength(); i++)
	    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
		PaymentDocument pd = PaymentDocument
			.createDocFromXML((Element) nl.item(i));
		if (pd != null)
		    pdList.add(pd);
	    }
	// System.out.println(toString());

	Collections.sort(pdList);
    }

    /**
     * ������� XML ���
     * 
     * @param fl
     *            ������ ���� � �����
     */
    @Deprecated
    public void createFile(String folder) {
	Document doc = XML.createNewDoc();
	Element root = null;

	if (isVER())
	    root = doc.createElement("PacketEPDVER");
	else
	    root = doc.createElement("PacketEPD");

	doc.appendChild(root);

	root.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");

	root.setAttribute("EDNo", Integer.toString(edNo));
	root.setAttribute("EDDate",
		new SimpleDateFormat("yyyy-MM-dd").format(edDate));
	root.setAttribute("EDAuthor", edAuthor);
	if (edReceiver != null && !edReceiver.equals(""))
	    root.setAttribute("EDReceiver", edReceiver);
	root.setAttribute("EDQuantity", Integer.toString(edQuantity));
	root.setAttribute("Sum", Integer.toString(sum));
	root.setAttribute("SystemCode", systemCode);

	ListIterator<PaymentDocument> iter = pdList.listIterator();
	while (iter.hasNext()) {
	    PaymentDocument pd = iter.next();
	    root.appendChild(pd.createED(doc));
	}

	XML.createXMLFile(doc, folder + filename);
    }

    /**
     * ��������� �������� � ���
     * 
     * @param pd
     *            ��������� ��������
     */
    public void add(PaymentDocument pd) {
	pdList.add(pd);
    }

    /**
     * ���������� ��� �� xml
     * 
     * @param src
     *            ������ ���� � �����
     */
    @Override
    public boolean generateFrom(Element root) {
	if (root.getLocalName().equals("PacketEPDVER"))
	    isVER = true;
	else if (root.getLocalName().equals("PacketEPD"))
	    isVER = false;
	else
	    return false;

	pdList = new ArrayList<PaymentDocument>();

	edNo = Integer.parseInt(root.getAttribute("EPDNo"));
	edDate = Settings.operDate;

	edAuthor = root.getAttribute("EDAuthor");
	edReceiver = root.getAttribute("EDReceiver");
	systemCode = "0";

	Sign[] s = ClientList.getSignByUIC(edAuthor);
	firstSign = s[0];
	secondSign = s[1];

	setFileName();

	int edNo = Integer.parseInt(root.getAttribute("EDFirstNo"));
	int sum = XML.getOptionalIntAttr("Sum", root);

	NodeList nl = root.getElementsByTagName("ED");

	for (int i = 0; i < nl.getLength(); i++) {
	    Element ed = (Element) nl.item(i);

	    String[] typeList = ed.getAttribute("Type").split(",");
	    int quantity = Integer.parseInt(ed.getAttribute("Quantity"));

	    for (String type : typeList)
		for (int j = 0; j < quantity; j++) {
		    PaymentDocument pd = PaymentDocument.createByType(type);

		    pd.generateFromXML(ed, edNo, edAuthor, sum);
		    edNo++;
		    if (sum != 0)
			sum++;
		    pdList.add(pd);
		}
	}

	if (pdList == null || pdList.size() == 0)
	    return false;
	else {
	    this.edQuantity = size();
	    this.sum = sumAll();
	    Collections.sort(pdList);
	    return true;
	}
    }

    /**
     * ������� ������ � �� ���
     * 
     * @param filename
     *            ������ ���� � �����
     */
    public void insertIntoDbUfebs(String filename) {
	try {
	    DB db = new DB(Settings.server, Settings.db, Settings.user,
		    Settings.pwd);
	    db.connect();

	    String query = "INSERT INTO [dbo].[UFEBS_Pacet]\r\n"
		    + "( [ID_Depart], [ID_ARM], [User_Insert], [InOutMode], \r\n"
		    + "[Date_Oper], [pacno], [pacdate], [Author], [Receiver], [Quantity],\r\n"
		    + " [SumPac], [SystCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n"
		    + " [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n"
		    + " [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n"
		    + " [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate],\r\n"
		    + " [IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n"
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
		    + ", "
		    + DB.toString(systemCode)
		    + ", null, 0, "
		    + DB.toString(filename)
		    + ", 0, 0, \r\n"
		    + " 0, null, null, null, "
		    + DB.toString(edReceiver)
		    + ", "
		    + DB.toString(edAuthor)
		    + ", 1, \r\n"
		    + // Mesto MesFrom?
		    " 5, null, '20120202', 3, 1, 1, 1,\r\n"
		    + " 0, 1, 3, 0, 1, null, null,\r\n"
		    + " null, null, null, 20, '', NULL, NULL, NULL, NULL)";
	    db.st.executeUpdate(query);
	    db.close();

	    int idPacet = Integer
		    .parseInt(DB
			    .selectFirstValueSabsDb("select max(ID_PACET) from dbo.UFEBS_Pacet"));

	    ListIterator<PaymentDocument> iter = pdList.listIterator();
	    while (iter.hasNext())
		iter.next().insertIntoDbUfebs(idPacet, edNo, edDate, edAuthor,
			filename);

	} catch (Exception e) {
	    e.printStackTrace();
	    Log.msg(e);
	}
    }

    /**
     * ������� ������ � �� ���
     * 
     * @param filename
     *            ������ ���� � �����
     */
    public void insertIntoDbVer(String filename) {
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
		    + ", "
		    + DB.toString(systemCode)
		    + ", null, 0, "
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

	    ListIterator<PaymentDocument> iter = pdList.listIterator();
	    while (iter.hasNext())
		iter.next().insertIntoDbVer(idPacet, filename);

	} catch (Exception e) {
	    e.printStackTrace();
	    Log.msg(e);
	}
    }

    @Override
    /**
     * ������� ������ � �� ���
     * @param filename ������ ���� � �����
     */
    public void insertIntoDB() {
	if (!isVER())
	    insertIntoDbUfebs(filename);
	else if (isVER())
	    insertIntoDbVer(filename);
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
