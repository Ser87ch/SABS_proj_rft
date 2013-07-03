package ru.sabstest;

import java.sql.Date;

public class ED206 {
	//реквизиты ЭД
	public int edNo; //Номер ЭД в течение опердня
	public Date edDate; //Дата составления ЭД
	public String edAuthor; //Уникальный идентификатор составителя ЭД (УИС)
	public String edReceiver; //УИС составителя
	
	public String acc; //счет
	public String bicCorr; //бик	
	public int sum; //сумма
	public Date transDate; //дата операции
	public String transTime; //время операции
	public String corrAcc; //кор. счет
	public String dc; //признак дебета/кредита
	
	//реквизиты исходного расчетного документа
	public int accDocNo; //номер расчетного документа
	public Date accDocDate; //Дата выписки расчетного документа
	
	//реквизиты исходного ЭД
	public int iEdNo; //Номер ЭД в течение опердня
	public Date iEdDate; //Дата составления ЭД
	public String iEdAuthor; //Уникальный идентификатор составителя ЭД (УИС)
}
