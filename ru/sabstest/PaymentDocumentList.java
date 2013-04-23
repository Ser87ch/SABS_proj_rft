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
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PaymentDocumentList {
	public List<PaymentDocument> pdl;

	int edNo;
	Date edDate;
	String edAuthor;
	String edReceiver;
	int edQuantity;
	int sum;
	String systemCode;

	public PaymentDocumentList() 
	{

	}

	public void generate()
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			ResultSet rs = db.st.executeQuery("select top 1 NUM_ACC from dbo.Account where rest = (select min(rest) from dbo.Account where substring(NUM_ACC,1,1) = '4' and substring(NUM_ACC,1,5) <> '40101')");
			rs.next();
			String ls = rs.getString("NUM_ACC");

			Client plat = new Client(Settings.bik, ls);

			pdl = new ArrayList<PaymentDocument>();
			ResultSet rsbik = null;

			rsbik = db.st.executeQuery("select top " + Settings.GenDoc.numBIK + " NEWNUM, isnull(KSNP,'') ksnp from dbo.BNKSEEK where substring(NEWNUM,1,4) = '" + Settings.bik.substring(0, 4) + "' and UER in ('2','3','4','5') and NEWNUM <> '" + Settings.bik + "'");

			int i = 1;
			while(rsbik.next()) {
				String bikpol = rsbik.getString("NEWNUM");
				String kspol = rsbik.getString("ksnp");
				String lspol = "40702810000000000005";

				Client pol = new Client(bikpol, kspol, lspol, "111111111111", "222222222", "ЗАО Тест");
				pol.contrrazr();

				for(int j = 0; j < Settings.GenDoc.numDoc; j++)
				{
					PaymentDocument pd = new PaymentOrder();
					pd.accDocNo = Settings.GenDoc.firstDoc + j;
					pd.accDocDate = Settings.operDate;
					pd.transKind = "01";
					pd.sum =  (int) (new Random().nextFloat() * 10000);				
					pd.paytKind = "1";
					pd.payer = plat;
					pd.payee = pol;
					pd.priority = "6";
					pd.tax.drawerStatus = "";
					pd.purpose = "Оплата теста";
					pd.chargeOffDate = Settings.operDate;
					pd.receiptDate = Settings.operDate;

					pdl.add(pd);
					Log.msg("Документ №" + Integer.toString(i) + " сгенерирован.");
					i++;
				}

			}
			Log.msg("Генерация документов завершена.");
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	public void generateB()
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();

			ResultSet rs = db.st.executeQuery("select top 1 NUM_ACC from dbo.Account where rest = (select min(rest) from dbo.Account where substring(NUM_ACC,1,1) = '4' and substring(NUM_ACC,1,5) <> '40101')");
			rs.next();
			String ls = rs.getString("NUM_ACC");

			Client plat = new Client(Settings.bik, ls);

			pdl = new ArrayList<PaymentDocument>();
			ResultSet rsbik = null;

			rsbik = db.st.executeQuery("select RKC NEWNUM, '' ksnp from dbo.BNKSEEK where NEWNUM = '" + Settings.bik + "'");
			int i = 1;
			while(rsbik.next()) {
				String bikpol = rsbik.getString("NEWNUM");
				String kspol = rsbik.getString("ksnp");
				String lspol = "40702810000000000005";

				Client pol = new Client(bikpol, kspol, lspol, "111111111111", "222222222", "ЗАО Тест");
				pol.contrrazr();

				for(int j = 0; j < Settings.GenDoc.numDoc; j++)
				{
					PaymentDocument pd = new PaymentOrder();
					pd.accDocNo = Settings.GenDoc.firstDoc + j;
					pd.accDocDate = Settings.operDate;
					pd.transKind = "01";
					pd.sum =  (int) (new Random().nextFloat() * 10000);				
					pd.paytKind = "1";
					pd.payer = plat;
					pd.payee = pol;
					pd.priority = "6";
					pd.tax.drawerStatus = "";
					pd.purpose = "Оплата теста";
					pd.chargeOffDate = Settings.operDate;
					pd.receiptDate = Settings.operDate;

					pdl.add(pd);
					Log.msg("Документ №" + Integer.toString(i) + " сгенерирован.");
					i++;
				}

			}
			Log.msg("Генерация документов завершена.");
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	public void generateS()
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();


			DB db2 = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db2.connect();

			ResultSet rs = db.st.executeQuery("select top 1 NUM_ACC from dbo.Account where rest = (select min(rest) from dbo.Account where substring(NUM_ACC,1,1) = '4' and substring(NUM_ACC,1,5) <> '40101')");
			rs.next();
			String ls = rs.getString("NUM_ACC");

			Client plat = new Client(Settings.bik, "", ls, "111111111111", "222222222", "ЗАО Получатель");

			pdl = new ArrayList<PaymentDocument>();

			ResultSet rsbik = db.st.executeQuery("select top " + Settings.GenSpack.numBIK + " NEWNUM, isnull(KSNP,'') ksnp from dbo.BNKSEEK where substring(NEWNUM,1,4) = '" + Settings.bik.substring(0, 4) + "' and UER in ('2','3','4','5') and NEWNUM <> '" + Settings.bik + "'");
			int i = 1;
			while(rsbik.next()) {
				String bikpol = rsbik.getString("NEWNUM");
				String kspol = rsbik.getString("ksnp");
				String lspol = "40702810000000000005";



				String s = "select isnull(max(elnum),0) as el, isnull(max(n_doc),0) as ndoc from dbo.DOCUMENT_BON where date_doc = '" + new SimpleDateFormat("yyyy-MM-dd").format(Settings.operDate) + "' and uic = '" + bikpol.substring(2,9) + "000" + "'";		
				ResultSet rsel = db2.st.executeQuery(s);
				int elnum = 0, ndoc = 0;
				rsel.next();
				elnum = rsel.getInt("el");
				ndoc = rsel.getInt("ndoc");

				Client pol = new Client(bikpol, kspol, lspol, "222222222222", "111111111", "ЗАО Плательщик");
				pol.contrrazr();

				for(int j = 0; j < Settings.GenSpack.numDoc; j++)
				{
					PaymentDocument pd = new PaymentOrder();
					pd.accDocNo = ndoc + 1 + j;
					pd.accDocDate = Settings.operDate;
					pd.transKind = "01";
					pd.sum =  (int) (new Random().nextFloat() * 10000);					
					pd.paytKind = "1";
					pd.payee = plat;
					pd.payer = pol;
					pd.priority = "6";
					pd.tax.drawerStatus = "";
					pd.purpose = "Оплата теста";
					pd.chargeOffDate = Settings.operDate;
					pd.receiptDate = Settings.operDate;
					pd.edNo = elnum + 1 + j;
					pdl.add(pd);
					Log.msg("Документ №" + Integer.toString(i) + " для S пакета сгенерирован.");
					i++;
				}

			}
			Log.msg("Генерация документов для S пакета завершена.");
			db.close();
			db2.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

	public void generateSwB(int numd)
	{
		try {
			DB db = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db.connect();


			DB db2 = new DB(Settings.server, Settings.db, Settings.user, Settings.pwd);
			db2.connect();

			ResultSet rs = db.st.executeQuery("select top 1 NUM_ACC from dbo.Account where rest = (select min(rest) from dbo.Account where substring(NUM_ACC,1,1) = '4' and substring(NUM_ACC,1,5) <> '40101')");
			rs.next();
			String ls = rs.getString("NUM_ACC");

			Client plat = new Client(Settings.bik, "", ls, "111111111111", "222222222", "ЗАО Получатель");

			pdl = new ArrayList<PaymentDocument>();

			ResultSet rsbik = db.st.executeQuery("select top 1 NEWNUM, isnull(KSNP,'') ksnp from dbo.BNKSEEK where substring(NEWNUM,1,4) = '" + Settings.bik.substring(0, 4) + "' and UER in ('2','3','4','5') and NEWNUM <> '" + Settings.bik + "'");
			int i = 1;
			while(rsbik.next()) {
				String bikpol = rsbik.getString("NEWNUM");
				String kspol = rsbik.getString("ksnp");
				String lspol = "40702810000000000005";



				String s = "select isnull(max(elnum),0) as el, isnull(max(n_doc),0) as ndoc from dbo.DOCUMENT_BON where date_doc = '" + new SimpleDateFormat("yyyy-MM-dd").format(Settings.operDate) + "' and uic = '" + bikpol.substring(2,9) + "000" + "'";		
				ResultSet rsel = db2.st.executeQuery(s);
				int elnum = 0, ndoc = 0;
				rsel.next();
				elnum = rsel.getInt("el");
				ndoc = rsel.getInt("ndoc");

				Client pol = new Client(bikpol, kspol, lspol, "222222222222", "111111111", "ЗАО Плательщик");
				pol.contrrazr();

				for(int j = 0; j < numd; j++)
				{
					PaymentDocument pd = new PaymentOrder();
					pd.accDocNo = ndoc + 1 + j;
					pd.accDocDate = Settings.operDate;
					pd.transKind = "01";
					pd.sum =  (int) (new Random().nextFloat() * 10000);					
					pd.paytKind = "1";
					pd.payee = plat;
					if(j == 0) //49 несуществующий счет получателя
					{
						Client poler = new Client(Settings.bik, "", "40116810499999999999", "111111111111", "222222222", "ЗАО Получатель");
						pol.contrrazr();
						pd.payee = poler;
					}

					//					if(j == 3) //40 несуществующий корсчет ко получателя 40116810600000000037
					//					{
					//						Client poler = new Client("044525159", "30101810000000000159", "40116810600000000037", "111111111111", "222222222", "ЗАО Получатель");
					//						//poler.contrrazr();
					//						pd.pol = poler;
					//					}

					if(j == 1) //39 у ко получотозвана лицензия
					{
						Client poler = new Client("044552989", "30101810000000000989", "40116810300000000037", "111111111111", "222222222", "ЗАО Получатель");
						poler.contrrazr();
						pd.payee = poler;
					}

					pd.payer = pol;
					if(j == 2) //43 недопустимый номер бс второ порядка лиц счета плат
					{
						Client plater = new Client(bikpol, kspol, "55555810200000000005", "111111111111", "222222222", "ЗАО Получатель");
						plater.contrrazr();
						pd.payer = plater;
					}

					pd.priority = "6";
					pd.tax.drawerStatus = "";
					pd.purpose = "Оплата теста";
					pd.chargeOffDate = Settings.operDate;
					pd.receiptDate = Settings.operDate;
					pd.edNo = elnum + 1 + j;
					pdl.add(pd);
					Log.msg("Документ №" + Integer.toString(i) + " для S пакета сгенерирован.");
					i++;
				}

			}
			Log.msg("Генерация документов для S пакета завершена.");
			db.close();
			db2.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}
	public void generateSerr()
	{
		generateSwB(3);
	}

	public int length()
	{
		return pdl.size();
	}

	public int sumAll() 
	{
		int sum = 0;
		ListIterator <PaymentDocument> iter = pdl.listIterator();
		while(iter.hasNext())
		{
			int i = iter.next().sum;
			sum = sum + i;

		}

		return sum;
	}

	public PaymentDocument get(int i)
	{
		return (PaymentDocument) pdl.get(i);
	}

	@Override
	public String toString()
	{
		String str = "";
		ListIterator <PaymentDocument> iter = pdl.listIterator();
		while(iter.hasNext())
		{
			str = str + iter.next().toString() + "\n";
		}

		return str;
	}

	public void createXML(String fl)
	{
		try {

			Document doc = XML.createNewDoc();
			Element rootElement = doc.createElement("paydocs");
			doc.appendChild(rootElement);

			rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			rootElement.setAttribute("xsi:noNamespaceSchemaLocation", Settings.testProj + "XMLSchema\\input\\paydocs.xsd");
			ListIterator <PaymentDocument> iter = pdl.listIterator();
			int i = 1;
			Log.msg("Начало создания XML с входящими документами.");
			while(iter.hasNext())
			{

				Element paydoc = doc.createElement("doc");
				rootElement.appendChild(paydoc);
				paydoc.setAttribute("id", Integer.toString(i));


				PaymentDocument pd = iter.next();

				XML.createNode(doc, paydoc, "num", pd.accDocNo);
				XML.createNode(doc, paydoc, "date", pd.accDocDate);
				XML.createNode(doc, paydoc, "vidop", pd.transKind);
				XML.createNode(doc, paydoc, "sum", pd.sum);
				XML.createNode(doc, paydoc, "vidpl", pd.paytKind);

				//плательщик
				Element plat = doc.createElement("plat");
				paydoc.appendChild(plat);

				XML.createNode(doc, plat, "bik", pd.payer.bic);
				XML.createNode(doc, plat, "ks", pd.payer.correspAcc);
				XML.createNode(doc, plat, "ls", pd.payer.personalAcc);
				XML.createNode(doc, plat, "inn", pd.payer.inn);
				XML.createNode(doc, plat, "kpp", pd.payer.kpp);
				XML.createNode(doc, plat, "name", pd.payer.name);

				//получатель
				Element pol = doc.createElement("pol");
				paydoc.appendChild(pol);				

				XML.createNode(doc, pol, "bik", pd.payee.bic);
				XML.createNode(doc, pol, "ks", pd.payee.correspAcc);
				XML.createNode(doc, pol, "ls", pd.payee.personalAcc);
				XML.createNode(doc, pol, "inn", pd.payee.inn);
				XML.createNode(doc, pol, "kpp", pd.payee.kpp);
				XML.createNode(doc, pol, "name", pd.payee.name);

				XML.createNode(doc, paydoc, "ocher", pd.priority);
				XML.createNode(doc, paydoc, "status", pd.tax.drawerStatus);

				if(pd.tax.drawerStatus != "")
				{
					XML.createNode(doc, paydoc, "kbk", pd.tax.cbc);
					XML.createNode(doc, paydoc, "okato", pd.tax.okato);
					XML.createNode(doc, paydoc, "osn", pd.tax.paytReason);
					XML.createNode(doc, paydoc, "nalper", pd.tax.taxPeriod);
					XML.createNode(doc, paydoc, "numdoc", pd.tax.docNo);
					XML.createNode(doc, paydoc, "datedoc", pd.tax.docDate);
					XML.createNode(doc, paydoc, "typepl", pd.tax.taxPaytKind);
				}

				XML.createNode(doc, paydoc, "naznach", pd.purpose);
				XML.createNode(doc, paydoc, "datesp", pd.chargeOffDate);
				XML.createNode(doc, paydoc, "datepost", pd.receiptDate);

				Log.msg("Документ №" + i + " записан в XML.");
				i++;
			}			
			XML.createXMLFile(doc, fl);
			Log.msg("XML с входящими документами " + fl + " создан.");			

			XML.validate(Settings.testProj + "XMLSchema\\input\\paydocs.xsd", fl);			

		} catch (Exception tfe) {
			tfe.printStackTrace();
			Log.msg(tfe);
		}
	}

	public void createXML()
	{
		createXML(Settings.testProj + "\\tests\\" + Settings.folder + "\\input\\paydocs.xml");
	}

	public void readXML(String src)
	{
		try {
			pdl = new ArrayList<PaymentDocument>();

			Element root = XML.getXMLRootElement(src);
			XML.validate(Settings.testProj + "XMLSchema\\input\\paydocs.xsd",src);

			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = root.getElementsByTagName("doc");


			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					PaymentDocument pd = new PaymentOrder();
					pd.accDocNo = XML.getChildValueInt("num", eElement);	
					pd.accDocDate = XML.getChildValueDate("date", eElement);	
					pd.transKind = XML.getChildValueString("vidop", eElement);
					pd.sum = XML.getChildValueInt("sum", eElement);
					pd.paytKind = XML.getChildValueString("vidpl", eElement);

					NodeList nlList = eElement.getElementsByTagName("plat");
					Element clElement = (Element) nlList.item(0);

					pd.payer = new Client(XML.getChildValueString("bik", clElement), XML.getChildValueString("ks", clElement),
							XML.getChildValueString("ls", clElement), XML.getChildValueString("inn", clElement),
							XML.getChildValueString("kpp", clElement), XML.getChildValueString("name", clElement));

					nlList = eElement.getElementsByTagName("pol");
					clElement = (Element) nlList.item(0);
					pd.payee = new Client(XML.getChildValueString("bik", clElement), XML.getChildValueString("ks", clElement),
							XML.getChildValueString("ls", clElement), XML.getChildValueString("inn", clElement),
							XML.getChildValueString("kpp", clElement), XML.getChildValueString("name", clElement));

					pd.priority = XML.getChildValueString("ocher", eElement);
					pd.tax.drawerStatus = XML.getChildValueString("status", eElement);
					pd.tax.cbc = XML.getChildValueString("kbk", eElement);
					pd.tax.okato = XML.getChildValueString("okato", eElement);
					pd.tax.paytReason = XML.getChildValueString("osn", eElement);
					pd.tax.taxPeriod = XML.getChildValueString("nalper", eElement);
					pd.tax.docNo = XML.getChildValueString("numdoc", eElement);
					pd.tax.docDate = XML.getChildValueString("datedoc", eElement);
					pd.tax.taxPaytKind = XML.getChildValueString("typepl", eElement);
					pd.purpose = XML.getChildValueString("naznach", eElement);
					pd.chargeOffDate = XML.getChildValueDate("datesp", eElement);
					pd.receiptDate = XML.getChildValueDate("datepost", eElement);

					pdl.add(pd);
					Log.msg("Документ №" + Integer.toString(temp + 1) + " загружен в программу.");
				}
			}
			Log.msg("XML с входящими документами " + src + " загружен в программу.");
		} catch (Exception e) {
			e.printStackTrace();
			Log.msg(e);
		}
	}

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
			String.format("%3s", "") + Settings.bik + String.format("%10s", "") + String.format("%09d", this.length()) + 
			String.format("%9s", "") + String.format("%018d", this.sumAll() ) + String.format("%8s", "") + 
			String.format("%-210s", "ЭЛЕКТРОННЫЕ ПЛАТЕЖИ") + String.format("%-80s", rkcname) +
			String.format("%-80s", bnkname) + String.format("%259s", "");			

			byte[] b = new byte[730];
			b = sf.getBytes("cp866"); 
			sd.write(b);			

			Log.msg("Служебная запись записана в S пакет.");

			int i = 1;
			ListIterator <PaymentDocument> iter = pdl.listIterator();
			while(iter.hasNext())
			{
				sd.writeBytes("\r\n");
				PaymentDocument pd = iter.next();

				sf = String.format("%06d", pd.edNo) + new SimpleDateFormat("ddMMyyyy").format(pd.accDocDate) +
				pd.payer.bic.substring(2,9) + "000" + pd.payer.bic + String.format("%20s", pd.payer.personalAcc) + String.format("%20s", pd.payer.correspAcc) +
				String.format("%03d", pd.accDocNo) + new SimpleDateFormat("ddMMyyyy").format(pd.accDocDate) + pd.transKind +
				pd.payee.bic + String.format("%20s", pd.payee.personalAcc) + String.format("%20s", pd.payee.correspAcc) +
				String.format("%018d", pd.sum) + String.format("%01d", pd.priority) + 
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

				Log.msg("Документ №" + i + " записан в S пакет.");
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

	public void readEPD(String src)
	{
		Element root = XML.getXMLRootElement(src);
		pdl = new ArrayList<PaymentDocument>();

		edNo = Integer.parseInt(root.getAttribute("EDNo"));
		edDate = Date.valueOf(root.getAttribute("EDDate"));
		edAuthor = root.getAttribute("EDAuthor");
		edReceiver = root.getAttribute("EDReceiver");
		edQuantity = Integer.parseInt(root.getAttribute("EDQuantity"));
		sum = Integer.parseInt(root.getAttribute("Sum"));
		systemCode = root.getAttribute("SystemCode");

		NodeList nl = root.getElementsByTagName("ED101");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentOrder();
			pd.readED((Element) nl.item(i));
			pdl.add(pd);
		}

		nl = root.getElementsByTagName("ED103");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentRequest();
			pd.readED((Element) nl.item(i));
			pdl.add(pd);
		}

		nl = root.getElementsByTagName("ED104");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new CollectionOrder();
			pd.readED((Element) nl.item(i));
			pdl.add(pd);
		}

		nl = root.getElementsByTagName("ED105");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentWarrant();
			pd.readED((Element) nl.item(i));
			pdl.add(pd);
		}
		//System.out.println(toString());
	}

	public void createEPD(String fl)
	{
		Document doc = XML.createNewDoc();
		Element root = doc.createElement("PacketEPD");
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

		ListIterator <PaymentDocument> iter = pdl.listIterator();
		while(iter.hasNext())
		{
			PaymentDocument pd = iter.next();
			root.appendChild(pd.createED(doc));
		}

		XML.createXMLFile(doc, fl);
	}
}


