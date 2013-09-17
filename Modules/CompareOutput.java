package Modules;
import java.io.File;

import resources.Modules.CompareOutputHelper;
import com.rational.test.ft.*;
import com.rational.test.ft.object.interfaces.*;
import com.rational.test.ft.object.interfaces.SAP.*;
import com.rational.test.ft.object.interfaces.WPF.*;
import com.rational.test.ft.object.interfaces.dojo.*;
import com.rational.test.ft.object.interfaces.siebel.*;
import com.rational.test.ft.object.interfaces.flex.*;
import com.rational.test.ft.object.interfaces.generichtmlsubdomain.*;
import com.rational.test.ft.script.*;
import com.rational.test.ft.value.*;
import com.rational.test.ft.vp.*;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

import ru.sabstest.DeltaDB;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;
import ru.sabstest.Log;

public class CompareOutput extends CompareOutputHelper
{

	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];

		
		//��������� ��
		ReadEDList plOut = new ReadEDList();
		plOut.readFolder(Settings.fullfolder + "\\output\\" + num);
		ReadEDList plEt = new ReadEDList();
		plEt.readFolder(Settings.datafolder + "etalon\\" + num);

		if(plOut.getSize() != 0 && plEt.getSize() != 0)
		{
			if(plOut.equals(plEt))
			{
				Log.msgCMP("���������� ��������� � ���������.");
				logTestResult("���������� ��������� � ���������.", true);
			}
			else
			{
				Log.msgCMP("���������� �� ��������� � ���������.");
				logTestResult("���������� �� ��������� � ���������.", false);
			}
		}
		else if(plOut.getSize() == 0)
		{
			Log.msg("����������� �������� ������.");
			logTestResult("���������� �������� ������.", false);
		}
		else if(plEt.getSize() == 0)
		{
			Log.msg("����������� ��������� ������.");
			logInfo("���������� ��������� ������.");
		}		
		
		
		//��������� ��������� � ��
		File[] dbet = DeltaDB.getDeltaDBFiles(Settings.datafolder + "etalon\\" + num);
		File[] dbout = DeltaDB.getDeltaDBFiles(Settings.fullfolder + "\\output\\" + num);
		
		if(dbet!= null && dbout != null && dbet.length != 0 && dbout.length != 0)
		{
			if(DeltaDB.cmpDeltaDBfld(dbet, dbout))
			{
				Log.msgCMP("��������� � �� ��������� � ���������.");
				logTestResult("��������� � �� ��������� � ���������.", true);
			}
			else
			{
				Log.msgCMP("��������� � �� �� ��������� � ���������.");
				logTestResult("��������� � �� �� ��������� � ���������.", false);
			}
		}
		else if((dbout.length == 0 || dbout == null) && dbet.length != 0 && dbet != null)
		{
			Log.msg("����������� �������� ��������� � ��.");
			logTestResult("���������� �������� ��������� � ��.", false);
		}
		else if((dbet.length == 0 || dbet == null) && dbout.length != 0 && dbout != null)
		{
			Log.msg("����������� ��������� ��������� � ��.");
			logInfo("���������� ��������� ��������� � ��.");
		}		
		
	}
}

