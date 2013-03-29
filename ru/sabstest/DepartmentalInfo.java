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
		if(di.getNodeName().equals("DepartmentalInfo"))
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
		
		XML.setOptinalAttr(rootElement, "DrawerStatus", drawerStatus);
		XML.setOptinalAttr(rootElement, "CBC", cbc);
		XML.setOptinalAttr(rootElement, "OKATO", okato);
		XML.setOptinalAttr(rootElement, "PaytReason", paytReason);
		XML.setOptinalAttr(rootElement, "TaxPeriod", taxPeriod);
		XML.setOptinalAttr(rootElement, "DocNo", docNo);
		XML.setOptinalAttr(rootElement, "DocDate", docDate);
		XML.setOptinalAttr(rootElement, "TaxPaytKind", taxPaytKind);
				
		return rootElement;
	}
}
