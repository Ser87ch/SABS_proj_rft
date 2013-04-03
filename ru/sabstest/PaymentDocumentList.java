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


public class PaymentDocumentList {
	private List<PaymentDocument> pdList;

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

	

	public int length()
	{
		return pdList.size();
	}

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
		pdList = new ArrayList<PaymentDocument>();

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
			pdList.add(pd);
		}

		nl = root.getElementsByTagName("ED103");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentRequest();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagName("ED104");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new CollectionOrder();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagName("ED105");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentWarrant();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
		}

		nl = root.getElementsByTagName("ED108");

		for(int i = 0; i < nl.getLength(); i++)
		{
			PaymentDocument pd = new PaymentOrderRegister();
			pd.readED((Element) nl.item(i));
			pdList.add(pd);
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

		ListIterator <PaymentDocument> iter = pdList.listIterator();
		while(iter.hasNext())
		{
			PaymentDocument pd = iter.next();
			root.appendChild(pd.createED(doc));
		}

		XML.createXMLFile(doc, fl);
	}

	public void add(PaymentDocument pd)
	{
		pdList.add(pd);
	}

	
	public void generateFromXML(String src)
	{
		Element root = XML.getXMLRootElement(src);
		
		if(root.getNodeName().equals("PacketEPD"))
		{
			pdList = new ArrayList<PaymentDocument>();

			edNo = Integer.parseInt(root.getAttribute("EPDNo"));
			edDate = Settings.operDate;
			edAuthor = root.getAttribute("EDAuthor");
			systemCode = "01";


			int edNo = Integer.parseInt(root.getAttribute("EDFirstNo"));

			NodeList nl = root.getElementsByTagName("ED101");

			for(int i = 0; i < nl.getLength(); i++)
			{
				Element ed = (Element) nl.item(i);
				
				int quantity = Integer.parseInt(ed.getAttribute("Quantity"));
				for(int j = 0; j < quantity; j++)
				{
					PaymentDocument pd = new PaymentOrder();
					pd.generateFromXML(ed, edNo, edAuthor);
					edNo++;
					pdList.add(pd);
				}
			}

			edQuantity = length();
			sum = sumAll();

		}
	}
}


