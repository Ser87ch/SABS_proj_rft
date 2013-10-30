package ru.sabstest;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class VERReturnPayt implements Comparable<VERReturnPayt> {
    public ED101 po;

    // реквизиты исходного ЭД
    public int iEdNo; // Номер ЭД в течение опердня
    public Date iEdDate; // Дата составления ЭД
    public String iEdAuthor; // Уникальный идентификатор составителя ЭД (УИС)

    @Override
    public int compareTo(VERReturnPayt o) {
	return po.compareTo(o.po);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
	result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
	result = prime * result + ((po == null) ? 0 : po.hashCode());
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
	VERReturnPayt other = (VERReturnPayt) obj;
	if (iEdAuthor == null) {
	    if (other.iEdAuthor != null)
		return false;
	} else if (!iEdAuthor.equals(other.iEdAuthor))
	    return false;
	if (iEdDate == null) {
	    if (other.iEdDate != null)
		return false;
	} else if (!iEdDate.equals(other.iEdDate))
	    return false;
	if (po == null) {
	    if (other.po != null)
		return false;
	} else if (!po.equals(other.po))
	    return false;
	return true;
    }

    @Deprecated
    public Element createED(Document doc) {
	Element el = doc.createElement("VERReturnPayt");
	el.appendChild(po.createED(doc));

	Element ied = doc.createElement("EDRefID");
	ied.setAttribute("EDNo", Integer.toString(iEdNo));
	ied.setAttribute("EDDate",
		new SimpleDateFormat("yyyy-MM-dd").format(iEdDate));
	ied.setAttribute("EDAuthor", iEdAuthor);

	el.appendChild(ied);

	return el;
    }

    public void readED(Element doc) {
	po = new ED101();
	po.readED((Element) doc.getElementsByTagNameNS("*", "ED101").item(0));

	Element el = (Element) doc.getElementsByTagNameNS("*", "EDRefID").item(
		0);
	iEdNo = Integer.parseInt(el.getAttribute("EDNo"));
	iEdDate = Date.valueOf(el.getAttribute("EDDate"));
	iEdAuthor = el.getAttribute("EDAuthor");
    }

    public void generateFromPaymentDocument(PaymentDocument pd, String author,
	    String resultCode) {
	iEdAuthor = pd.edAuthor;
	iEdDate = pd.edDate;
	iEdNo = pd.edNo;

	po = new ED101();

	po.generateReturnDocument(pd, author, resultCode);
    }

    public void insertIntoDbVer(int idPacet, String filename) {
	po.insertIntoDbVer(idPacet, filename, iEdNo, iEdDate, iEdAuthor);
    }
}
