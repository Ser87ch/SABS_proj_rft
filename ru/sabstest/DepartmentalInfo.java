package ru.sabstest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Класс Ведомственная информация
 * @author Admin
 *
 */
public class DepartmentalInfo {
	public String drawerStatus; //статус составителя
	public String cbc; //КБК
	public String okato; //ОКАТО
	public String paytReason; //основание налогового платежа
	public String taxPeriod; //налоговый период
	public String docNo; //номер налогового документа
	public String docDate; //дата налогового документа
	public String taxPaytKind; //тип налогового документа
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cbc == null) ? 0 : cbc.hashCode());
		result = prime * result + ((docDate == null) ? 0 : docDate.hashCode());
		result = prime * result + ((docNo == null) ? 0 : docNo.hashCode());
		result = prime * result
				+ ((drawerStatus == null) ? 0 : drawerStatus.hashCode());
		result = prime * result + ((okato == null) ? 0 : okato.hashCode());
		result = prime * result
				+ ((paytReason == null) ? 0 : paytReason.hashCode());
		result = prime * result
				+ ((taxPaytKind == null) ? 0 : taxPaytKind.hashCode());
		result = prime * result
				+ ((taxPeriod == null) ? 0 : taxPeriod.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DepartmentalInfo other = (DepartmentalInfo) obj;
		if (cbc == null) {
			if (other.cbc != null)
				return false;
		} else if (!cbc.equals(other.cbc))
			return false;
		if (docDate == null) {
			if (other.docDate != null)
				return false;
		} else if (!docDate.equals(other.docDate))
			return false;
		if (docNo == null) {
			if (other.docNo != null)
				return false;
		} else if (!docNo.equals(other.docNo))
			return false;
		if (drawerStatus == null) {
			if (other.drawerStatus != null)
				return false;
		} else if (!drawerStatus.equals(other.drawerStatus))
			return false;
		if (okato == null) {
			if (other.okato != null)
				return false;
		} else if (!okato.equals(other.okato))
			return false;
		if (paytReason == null) {
			if (other.paytReason != null)
				return false;
		} else if (!paytReason.equals(other.paytReason))
			return false;
		if (taxPaytKind == null) {
			if (other.taxPaytKind != null)
				return false;
		} else if (!taxPaytKind.equals(other.taxPaytKind))
			return false;
		if (taxPeriod == null) {
			if (other.taxPeriod != null)
				return false;
		} else if (!taxPeriod.equals(other.taxPeriod))
			return false;
		return true;
	}

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
	
	/**
	 * считывает информация из элемента
	 * @param di элемент
	 */
	public void readED(Element di)
	{
		if(di.getLocalName().equals("DepartmentalInfo"))
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
	
	/**
	 * создает в документе элемент
	 * @param doc документ
	 * @return элемент
	 */
	@Deprecated
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
