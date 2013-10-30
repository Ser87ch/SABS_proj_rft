package ru.sabstest;

import java.sql.Date;

import org.w3c.dom.Element;

public class ED206 implements Comparable<ED206> {
    // реквизиты ЭД
    public int edNo; // Номер ЭД в течение опердня
    public Date edDate; // Дата составления ЭД
    public String edAuthor; // Уникальный идентификатор составителя ЭД (УИС)
    public String edReceiver; // УИС составителя

    public String acc; // счет
    public String bicCorr; // бик
    public int sum; // сумма
    public Date transDate; // дата операции
    public String transTime; // время операции
    public String corrAcc; // кор. счет
    public String dc; // признак дебета/кредита

    // реквизиты исходного расчетного документа
    public int accDocNo; // номер расчетного документа
    public Date accDocDate; // Дата выписки расчетного документа

    // реквизиты исходного ЭД
    public int iEdNo; // Номер ЭД в течение опердня
    public Date iEdDate; // Дата составления ЭД
    public String iEdAuthor; // Уникальный идентификатор составителя ЭД (УИС)

    @Override
    public int compareTo(ED206 o) {
	if (sum < o.sum)
	    return -1;
	else if (sum > o.sum)
	    return 1;
	else
	    return 0;
    }

    /**
     * считываются основные реквизиты платежного документа
     * 
     * @param doc
     *            элемент
     */
    public void readED(Element doc) {
	edNo = Integer.parseInt(doc.getAttribute("EDNo"));
	edDate = Date.valueOf(doc.getAttribute("EDDate"));
	edAuthor = doc.getAttribute("EDAuthor");
	edReceiver = doc.getAttribute("EDReceiver");

	acc = doc.getAttribute("Acc");
	bicCorr = doc.getAttribute("BICCorr");
	sum = Integer.parseInt(doc.getAttribute("Sum"));
	transDate = Date.valueOf(doc.getAttribute("TransDate"));
	corrAcc = doc.getAttribute("CorrAcc");
	dc = doc.getAttribute("DC");

	Element ref = (Element) doc.getElementsByTagNameNS("*", "EDRefID")
		.item(0);
	iEdNo = Integer.parseInt(ref.getAttribute("EDNo"));
	iEdDate = Date.valueOf(ref.getAttribute("EDDate"));
	iEdAuthor = ref.getAttribute("EDAuthor");

	Element accDoc = (Element) doc.getElementsByTagNameNS("*", "AccDoc")
		.item(0);
	accDocNo = Integer.parseInt(accDoc.getAttribute("AccDocNo"));
	accDocDate = Date.valueOf(accDoc.getAttribute("AccDocDate"));

    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((acc == null) ? 0 : acc.hashCode());
	result = prime * result
		+ ((accDocDate == null) ? 0 : accDocDate.hashCode());
	result = prime * result + ((bicCorr == null) ? 0 : bicCorr.hashCode());
	result = prime * result + ((corrAcc == null) ? 0 : corrAcc.hashCode());
	result = prime * result + ((dc == null) ? 0 : dc.hashCode());
	result = prime * result
		+ ((edAuthor == null) ? 0 : edAuthor.hashCode());
	result = prime * result + ((edDate == null) ? 0 : edDate.hashCode());
	result = prime * result
		+ ((edReceiver == null) ? 0 : edReceiver.hashCode());
	result = prime * result
		+ ((iEdAuthor == null) ? 0 : iEdAuthor.hashCode());
	result = prime * result + ((iEdDate == null) ? 0 : iEdDate.hashCode());
	result = prime * result + sum;
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
	ED206 other = (ED206) obj;
	if (acc == null) {
	    if (other.acc != null)
		return false;
	} else if (!acc.equals(other.acc))
	    return false;
	if (accDocDate == null) {
	    if (other.accDocDate != null)
		return false;
	} else if (!accDocDate.equals(other.accDocDate))
	    return false;
	if (bicCorr == null) {
	    if (other.bicCorr != null)
		return false;
	} else if (!bicCorr.equals(other.bicCorr))
	    return false;
	if (corrAcc == null) {
	    if (other.corrAcc != null)
		return false;
	} else if (!corrAcc.equals(other.corrAcc))
	    return false;
	if (dc == null) {
	    if (other.dc != null)
		return false;
	} else if (!dc.equals(other.dc))
	    return false;
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
	if (edReceiver == null) {
	    if (other.edReceiver != null)
		return false;
	} else if (!edReceiver.equals(other.edReceiver))
	    return false;
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
	if (sum != other.sum)
	    return false;
	return true;
    }
}
