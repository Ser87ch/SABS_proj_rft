package ru.sabstest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DepartmentalInfo {
	public String drawerStatus; //статус составителя
	public String cbc; //КБК
	public String okato; //ОКАТО
	public String paytReason; //основание налогового платежа
	public String taxPeriod; //налоговый период
	public String docNo; //номер налогового документа
	public String docDate; //дата налогового документа
	public String taxPaytKind; //тип налогового документа
	
	
	public DepartmentalInfo()
	{
		
		this.drawerStatus = "";
		this.cbc = "";
		this.okato = "";
		this.paytReason = "";
		this.taxPeriod = "";
		this.docNo = "";
		this.docDate = "";
		this.taxPaytKind = "";
	}
	
	public DepartmentalInfo(String drawerStatus, String cbc, String okato,
			String paytReason, String taxPeriod, String docNo,
			String docDate, String taxPaytKind)
	{
		
		this.drawerStatus = drawerStatus;
		this.cbc = cbc;
		this.okato = okato;
		this.paytReason = paytReason;
		this.taxPeriod = taxPeriod;
		this.docNo = docNo;
		this.docDate = docDate;
		this.taxPaytKind = taxPaytKind;
	}
	
	public void readED(Element di)
	{
		if(di.getNodeName() == "DepartmentalInfo")
		{
			drawerStatus = di.getAttribute("DrawerStatus");
			cbc = di.getAttribute("CBC");
			okato = di.getAttribute("OKATO");
			paytReason = di.getAttribute("PaytReason");
			taxPeriod = di.getAttribute("TaxPeriod");
			docNo = di.getAttribute("DocNo");
			docDate = di.getAttribute("DocDate");
			taxPaytKind = di.getAttribute("TaxPaytKind");
		}
	}
	public Element createXMLElement(Document doc)
	{
		Element rootElement = doc.createElement("DepartmentalInfo");
		
		if(drawerStatus != null && !drawerStatus.equals(""))
			rootElement.setAttribute("DrawerStatus", drawerStatus);
		
		if(cbc != null && !cbc.equals(""))
			rootElement.setAttribute("CBC", cbc);
		
		if(okato != null && !okato.equals(""))
			rootElement.setAttribute("OKATO", okato);
		
		if(paytReason != null && !paytReason.equals(""))
			rootElement.setAttribute("PaytReason", paytReason);
		
		if(taxPeriod != null && !taxPeriod.equals(""))
			rootElement.setAttribute("TaxPeriod", taxPeriod);
		
		if(docNo != null && !docNo.equals(""))
			rootElement.setAttribute("DocNo", docNo);
		
		if(docDate != null && !docDate.equals(""))
			rootElement.setAttribute("DocDate", docDate);
		
		if(taxPaytKind != null && !taxPaytKind.equals(""))
			rootElement.setAttribute("TaxPaytKind", taxPaytKind);
		
		return rootElement;
	}
}
