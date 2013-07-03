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
import ru.sabstest.PacketList;
import ru.sabstest.Settings;
import ru.sabstest.Log;

public class CompareOutput extends CompareOutputHelper
{

	public void testMain(Object[] args) 
	{
		//List<String> st = Arrays.asList((String[]) args[0]);
		String num = (String) args[1];

		PacketList plOut = new PacketList();
		plOut.readFolder(Settings.fullfolder + "\\output\\" + num);
		PacketList plEt = new PacketList();
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
	}
}

