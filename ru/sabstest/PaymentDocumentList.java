package ru.sabstest;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 *  ласс пакет Ёѕƒ
 * @author Admin
 *
 */
public class PaymentDocumentList extends Packet{
	private List<PaymentDocument> pdList;

	public int edNo;
	public Date edDate;
	public String edAuthor;
	public String edReceiver;
	public int edQuantity;
	public int sum;
	public String systemCode;

	public PaymentDocumentList() 
	{

	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
		result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
		result = prime * result + edQuantity;
		result = prime * result
				+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
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
		PaymentDocumentList other = (PaymentDocumentList) obj;
		if (edAuthor == null) {
			if (other.edAuthor != null)
				return false;
		} else if (!edAuthor.equals(other.edAuthor))
			return false;
		if (edDate == null) {
			if (other.edDate != null)
				return false;
		} else if (!edDate.equals(other.edDate))
			return false;
		if (edQuantity != other.edQuantity)
			return false;
		if (edReceiver == null) {
			if (other.edReceiver != null)
				return false;
		} else if (!edReceiver.equals(other.edReceiver))
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
	public void setFileName()
	{
		if(isVER())
			filename = edAuthor + new SimpleDateFormat("yyyyMMdd").format(edDate) + String.format("%09d", edNo) + ".PacketEPDVER";
		else
		{
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
	}

	public boolean isVER()
	{
		if(packetType == Packet.Type.PacketEPDVER)
			return true;
		return false;
	}

	/**
	 * @return количество документов в пакете
	 */
	public int size()
	{
		return pdList.size();
	}

	/**
	 * @return сумма документов в пакете
	 */
	public int sumAll() 
	{
		int sum = 0;
		ListIterator <PaymentDocument> iter = pdList.listIterator();
		while(iter.hasNext())
		{
			int i = iter.next().sum;
			sum = sum + i;

		}

		return sum;
	}

	/**
	 * @param i номер документа
	 * @return документ под номером i
	 */
	public PaymentDocument get(int i)
	{
		return (PaymentDocument) pdList.get(i);
	}

	@Override
	public String toString()
	{
		String str = "";
		ListIterator <PaymentDocument> iter = pdList.listIterator();
		while(iter.hasNext())
		{
			str = str + iter.next().toString() + "\n";
		}

		return str;
	}




	/**
	 * создает s пакет
	 * @param fl полный путь к файлу
	 */
	public void createSpack(String fl)
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);

			db.connect();
			ResultSet rs = db.st.executeQuery("select b.RKC rkc, b.NAMEP name, r.namep rname  from bnkseek b inner join bnkseek r on b.rkc = r.newnum where b.newnum = '" + Settings.bik + "'");
			rs.next();
			String rkcbik = rs.getString("RKC");
			String rkcname = rs.getString("rname");
			String bnkname = rs.getString("name");

			File sfile = new File(fl);
			FileOutputStream s = new FileOutputStream(sfile);
			DataOutputStream sd = new DataOutputStream(s);

			String sf = rkcbik + String.format("%18s", "") + new SimpleDateFormat("ddMMyyyy").format(Settings.operDate) +
			String.format("%3s", "") + Settings.bik + String.format("%10s", "") + String.format("%09d", this.size()) + 
			String.format("%9s", "") + String.format("%018d", this.sumAll() ) + String.format("%8s", "") + 
			String.format("%-210s", "ЁЋ≈ “–ќЌЌџ≈ ѕЋј“≈∆»") + String.format("%-80s", rkcname) +
			String.format("%-80s", bnkname) + String.format("%259s", "");			

			byte[] b = new byte[730];
			b = sf.getBytes("cp866"); 
			sd.write(b);			

			Log.msg("—лужебна€ запись записана в S пакет.");

			int i = 1;
			ListIterator <PaymentDocument> iter = pdList.listIterator();
			while(iter.hasNext())
			{
				sd.writeBytes("\r\n");
				PaymentDocument pd = iter.next();

				sf = String.format("%06d", pd.edNo) + new SimpleDateFormat("ddMMyyyy").format(pd.accDocDate) +
				pd.payer.bic.substring(2,9) + "000" + pd.payer.bic + String.format("%20s", pd.payer.personalAcc) + String.format("%20s", pd.payer.correspAcc) +
				String.format("%03d", pd.accDocNo) + new SimpleDateFormat("ddMMyyyy").format(pd.accDocDate) + pd.transKind +
				pd.payee.bic + String.format("%20s", pd.payee.personalAcc) + String.format("%20s", pd.payee.correspAcc) +
				String.format("%018d", pd.sum) + pd.priority + 
				String.format("%12s", pd.payer.inn) + String.format("%9s", pd.payer.kpp) +
				String.format("%-160s", pd.payer.name) + 	String.format("%12s", pd.payee.inn) + 
				String.format("%9s", pd.payee.kpp) + String.format("%-160s", pd.payee.name) + 
				String.format("%-210s", pd.purpose) + new SimpleDateFormat("ddMMyyyy").format(pd.receiptDate) + 
				String.format("%8s", "") + new SimpleDateFormat("ddMMyyyy").format(pd.chargeOffDate) +
				new SimpleDateFormat("ddMMyyyy").format(pd.accDocDate) + String.format("%2s", pd.tax.drawerStatus) + 
				String.format("%20s", pd.tax.cbc) + String.format("%11s", pd.tax.okato) + String.format("%2s", pd.tax.paytReason) +
				String.format("%10s", pd.tax.taxPeriod) + String.format("%15s", pd.tax.docNo) + String.format("%10s", pd.tax.docDate) +
				String.format("%2s", pd.tax.taxPaytKind) + String.format("%50s", "");

				b = new byte[880];
				b = sf.getBytes("cp866"); 
				sd.write(b);

				Log.msg("ƒокумент є" + i + " записан в S пакет.");
				i++;
			}

			Log.msg("S пакет " + fl + " создан.");
			s.close();
			sd.close();
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}		
	}
	public void createSpack()
	{
		createSpack(Settings.testProj + "tests\\" + Settings.folder + "\\input\\spack.txt");
	}

	/**
	 * считывает Ёѕƒ из xml
	 * @param src полный путь к файлу
	 */
	public void readFile(String src)
	{
		Element root = XML.getXMLRootElement(src);

		readXML(root);
	}

	public void readEncodedFile(String src, boolean isUTF)
	{
		Element root;
		if(isUTF)
			root = XML.getXMLRootElementFromStringUTF(XML.decodeBase64(src));
		else
			root = XML.getXMLRootElementFromString1251(XML.decodeBase64(src));
		
		readXML(root);
	}

	public void readXML(Element root)
	{


		if(root.getLocalName().equals("PacketEPDVER"))
			packetType = Packet.Type.PacketEPDVER;
		else if(root.getLocalName().equals("PacketEPD"))
			packetType = Packet.Type.PacketEPD;
		else
			return;

		pdList = new ArrayList<PaymentDocument>();

		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		systemCode = root.getAttribute("SystemCode");

		NodeList nl = root.getElementsByTagNameNS("*","ED101");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentOrder();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagNameNS("*","ED103");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentRequest();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagNameNS("*","ED104");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new CollectionOrder();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagNameNS("*","ED105");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentWarrant();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagNameNS("*","ED108");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentOrderRegister();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}
		//System.out.println(toString());
	}

	@Override
	/** 
	 * создает XML Ёѕƒ
	 * @param fl полный путь к файлу
	 */
	public void createFile(String folder)
	{
		Document doc = XML.createNewDoc();
		Element root = null;

		if(isVER())
			root = doc.createElement("PacketEPDVER");
		else
			root = doc.createElement("PacketEPD");

		doc.appendChild(root);

		root.setAttribute("xmlns", "urn:cbr-ru:ed:v2.0");

		root.setAttribute("EDNo", Integer.toString(edNo));
		root.setAttribute("EDDate", new SimpleDateFormat("yyyy-MM-dd").format(edDate));
		root.setAttribute("EDAuthor", edAuthor);
		if(edReceiver != null && !edReceiver.equals(""))
			root.setAttribute("EDReceiver", edReceiver);
		root.setAttribute("EDQuantity", Integer.toString(edQuantity));
		root.setAttribute("Sum", Integer.toString(sum));
		root.setAttribute("SystemCode", systemCode);

		ListIterator <PaymentDocument> iter = pdList.listIterator();
		while(iter.hasNext())
		{
			PaymentDocument pd = iter.next();
			root.appendChild(pd.createED(doc));
		}

		XML.createXMLFile(doc, folder + filename);
	}

	/**
	 * добавл€ет документ в Ёѕƒ
	 * @param pd платежный документ
	 */
	public void add(PaymentDocument pd)
	{
		pdList.add(pd);
	}


	/**
	 * генерирует Ёѕƒ из xml
	 * @param src полный путь к файлу
	 */
	public void generateFromXML(Element root)
	{

		//	XML.validate(Settings.testProj + "\\XMLschema\\settings\\generation.xsd", src);
		//	Element root = XML.getXMLRootElement(src);

		if(root.getLocalName().equals("PacketEPDVER"))
		{
			packetType = Packet.Type.PacketEPDVER;			
			secondSign = new Sign(Settings.Sign.keycontr,Settings.Sign.signcontr);
			firstSign = new Sign(Settings.Sign.keyobr,Settings.Sign.signobr);

		}
		else if(root.getLocalName().equals("PacketEPD"))
		{
			packetType = Packet.Type.PacketEPD;		
			firstSign = new Sign(root.getAttribute("key1"),root.getAttribute("profile1"));
			secondSign = new Sign(root.getAttribute("key2"),root.getAttribute("profile2"));
		}
		else
			return;



		pdList = new ArrayList<PaymentDocument>();

		edNo = Integer.parseInt(root.getAttribute("EPDNo"));
		edDate = Settings.operDate;
		
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		systemCode = "0";

		setFileName();

		int edNo = Integer.parseInt(root.getAttribute("EDFirstNo"));
		int sum = XML.getOptionalIntAttr("Sum", root);

		NodeList nl = root.getElementsByTagName("ED");

		for(int i = 0; i < nl.getLength(); i++)
		{
			Element ed = (Element) nl.item(i);

			String[] typeList = ed.getAttribute("Type").split(",");
			int quantity = Integer.parseInt(ed.getAttribute("Quantity"));

			for(String type:typeList)
			{
				for(int j = 0; j < quantity; j++)
				{
					PaymentDocument pd = PaymentDocument.createByType(type);					

					pd.generateFromXML(ed, edNo, edAuthor, sum);
					edNo++;
					if(sum != 0)
						sum++;
					pdList.add(pd);
				}
			}
		}		

		this.edQuantity = size();
		this.sum = sumAll();

	}


	/**
	 * вставка пакета в Ѕƒ ”Ёќ
	 * @param filename полный путь к файлу
	 */
	public void insertIntoDbUfebs(String filename)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String query = "INSERT INTO [dbo].[UFEBS_Pacet]\r\n" + 
			"( [ID_Depart], [ID_ARM], [User_Insert], [InOutMode], \r\n" + 
			"[Date_Oper], [pacno], [pacdate], [Author], [Receiver], [Quantity],\r\n" + 
			" [SumPac], [SystCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n" + 
			" [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n" + 
			" [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n" + 
			" [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate],\r\n" + 
			" [IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n" + 
			"VALUES(null, 0, null, 0,\r\n" + 
			DB.toString(Settings.operDate) + ", " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(edQuantity) + ",\r\n" + 
			DB.toString(sum) + ", " + DB.toString(systemCode) + ", null, 0, " + DB.toString(filename) + ", 0, 0, \r\n" + 
			" 0, null, null, null, " + DB.toString(edReceiver) + ", " + DB.toString(edAuthor) + ", 1, \r\n" +  //Mesto MesFrom?
			" 5, null, '20120202', 3, 1, 1, 1,\r\n" + 
			" 0, 1, 3, 0, 1, null, null,\r\n" + 
			" null, null, null, 20, '', NULL, NULL, NULL, NULL)";			
			db.st.executeUpdate(query);
			db.close();

			int idPacet = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_PACET) from dbo.UFEBS_Pacet"));

			ListIterator <PaymentDocument> iter = pdList.listIterator();
			while(iter.hasNext())
			{
				iter.next().insertIntoDbUfebs(idPacet, edNo, edDate, edAuthor, filename);
			}			

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}

	/**
	 * вставка пакета в Ѕƒ ¬Ё–
	 * @param filename полный путь к файлу
	 */
	public void insertIntoDbVer(String filename)
	{
		try
		{
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			String uic = Settings.bik.substring(2) + "000";

			String query =  "INSERT INTO [dbo].[epay_Packet]\r\n" + 
			"([ID_Depart], [ID_ARM], [User_Insert], [InOutMode],\r\n" + 
			" [Date_Oper], [EDNo], [EDDate], [EDAuthor], [EDReceiver], [EDQuantity],\r\n" + 
			" [Summa], [SystemCode], [sTime], [Type], [FileName], [KodErr], [KodObr],\r\n" + 
			" [KodDocum], [Time_Inp], [MSGID], [ErrorTxt], [Mesto], [MesFrom], [MesType],\r\n" + 
			" [MesPrior], [MesFName], [MesTime], [SlKonv], [Pridenti], [Shifr], [Upakovka],\r\n" + 
			" [OID], [WriteSig], [Verify], [Pr_ufebs], [Forme221], [IEdNo], [IEdDate], \r\n" + 
			"[IEdAuth], [Esc_Key], [Esc_key2], [Seanc], [FilePath], [ManName], [QueName], [KcoiKgur], [TypeObr])\r\n" + 
			"VALUES(null, 0, null, 0,\r\n" + 
			DB.toString(Settings.operDate) + ", " + DB.toString(edNo) + ", " + DB.toString(edDate) + ", " + DB.toString(edAuthor) + ", " + DB.toString(edReceiver) + ", " + DB.toString(edQuantity) + ",\r\n" + 
			DB.toString(sum) + ", " + DB.toString(systemCode) + ", null, 0, " + DB.toString(filename) + ", 0, 0, \r\n" + 
			" 0, null, null, null, " + DB.toString(uic) + ", " + DB.toString(uic) + ", 1, \r\n" +  //Mesto MesFrom?
			" 5, null, '20120202', 3, 1, 0, 0,\r\n" + 
			" 0, 4, 3, 0, 1, null, null,\r\n" + 
			" null, null, null, 20, '', NULL, NULL, NULL, NULL)";			
			db.st.executeUpdate(query);
			db.close();			 

			int idPacet = Integer.parseInt(DB.selectFirstValueSabsDb("select max(ID_PACKET) from dbo.epay_Packet"));

			ListIterator <PaymentDocument> iter = pdList.listIterator();
			while(iter.hasNext())
			{
				iter.next().insertIntoDbVer(idPacet, filename);
			}			

		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);			
		}
	}

	@Override
	/**
	 * вставка пакета в Ѕƒ ¬Ё–
	 * @param filename полный путь к файлу
	 */
	public void insertIntoDB()
	{
		if(packetType == Packet.Type.PacketEPD)
			insertIntoDbUfebs(filename);
		else if(packetType == Packet.Type.PacketEPDVER)
			insertIntoDbVer(filename);
	}

}


