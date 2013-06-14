package ru.sabstest;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;

import javax.xml.crypto.KeySelector.Purpose;

public class ErrorCode {
	private static final String[] codes = {"39","49"};

	public static boolean contains(String s)
	{
		return Arrays.asList(codes).contains(s);
	}

	public static void addError(PaymentDocument pd, int errorCode)
	{
		switch(errorCode)
		{
		case 12:
			Calendar c = Calendar.getInstance(); 
			c.setTime(pd.edDate); 
			c.add(Calendar.DATE, 1);
			pd.edDate =  new Date(c.getTimeInMillis());			
			break;
		case 13:
			c = Calendar.getInstance(); 
			c.setTime(pd.accDocDate); 
			c.add(Calendar.DATE, 1);
			pd.accDocDate =  new Date(c.getTimeInMillis());			
			break;
		case 15:
			pd.priority="0";
			break;
		case 16:
			c = Calendar.getInstance(); 
			c.setTime(pd.chargeOffDate); 
			c.add(Calendar.DATE, -1);
			pd.chargeOffDate =  new Date(c.getTimeInMillis());		
			break;
		case 17://
			pd.transKind = "09";
			break;
		case 18://
			pd.payer.inn = "        ";
			break;
		case 19://
			pd.payee.inn = "         ";
			break;
		case 20://
			pd.payer.name = "        ";
			break;
		case 21://
			pd.payee.name = "        ";
			break;
		case 22://
			pd.purpose = "        ";
			break;
		case 23:
			pd.edAuthor = "!@abc";
			break;
		case 31:
			pd.payer.bic = "999999999";
			break;
		case 32:
			pd.payer.correspAcc = "99999810300000000001";
			break;
		case 33:
			pd.payer.correspAcc = "";
			break;
		case 34:
			pd.payee.bic = "999999999";
			break;
		case 35:
			pd.payee.correspAcc = "99999810300000000001";
			break;
		case 36:
			pd.payee.correspAcc = "";
			break;
			
			
		}
	}
}
