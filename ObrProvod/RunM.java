package ObrProvod;
import java.io.File;
import java.io.IOException;

import resources.ObrProvod.RunMHelper;
import ru.sabstest.DeltaDB;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.Settings;

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
import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;
import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

public class RunM extends RunMHelper
{

	public void testMain(Object[] args) 
	{
		boolean isreset = true, iscontrspack = true, isobrspack = true, iscontrrpack = true, isdeltadb = true;

		Log.msg("������ ���������� ����� '�������� ������ S ������ � � ��� ������'.");

		try {
			Settings.testProj = TSSUtility.getScriptOption("projFolder");		
		} catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}

		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];

		Init.load();
		Settings.readXML(Settings.fullfolder + "settings\\general.xml");	

		if(isreset)
			callScript("SABS.Reset");

		String sfilename = Pack.getPackNameFolder(Settings.datafolder + "\\input\\005\\", "s");
		try {
			Pack.copyFile(Settings.datafolder + "\\input\\005\\" + sfilename, Settings.path + "post\\kPuI\\" + sfilename);
		} catch (IOException e) {
			e.printStackTrace();
			Log.msg(e);
		}
		logInfo("S ����� " +  sfilename+ " ���������� � ����.");

		//�������� ������ � ��������� ��� ����
		if(isdeltadb)
		{
			Log.msg("������ ����������� ��������� � ��.");
			DeltaDB.readXMLSettings(Settings.fullfolder + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}


		//�������� S-������
		if(iscontrspack)
		{
			Log.msg("������� ������ �� �������� S ������.");
			Settings.ContrES.readXML(Settings.fullfolder + "settings\\" + Settings.pervfolder + "\\contres.xml");
			callScript("SABS.VFD",new String[]{Settings.ContrES.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrES.user, Settings.ContrES.pwd, Settings.ContrES.sign});
			callScript("ObrProvod.Vvod.ContrSpack");
			callScript("SABS.CloseSABS");
		}

		//��������� S-������
		if(isobrspack)
		{
			Log.msg("������� ������ �� ��������� S ������.");
			Settings.FormES.readXML(Settings.fullfolder + "settings\\" + Settings.pervfolder + "\\formes.xml");
			callScript("SABS.VFD",new String[]{Settings.FormES.key});
			callScript("SABS.StartSABS",new String[]{Settings.FormES.user, Settings.FormES.pwd, Settings.FormES.sign});
			callScript("ObrProvod.Vvod.ObrSpack");			
			callScript("SABS.CloseSABS");
		}


		//�������� R-������
		if(iscontrrpack)
		{
			Log.msg("������� ������ �� �������� R ������.");
			Settings.ContrES.readXML(Settings.fullfolder + "settings\\" + Settings.pervfolder + "\\contres.xml");
			callScript("SABS.VFD",new String[]{Settings.ContrES.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrES.user, Settings.ContrES.pwd, Settings.ContrES.sign});
			callScript("ObrProvod.Vvod.ContrRpack");
			callScript("SABS.CloseSABS");	

			String rpack = Pack.copyRPack("005");
			if(rpack != "")
				logInfo("R ����� " + rpack + " ���������� � " + Settings.fullfolder + "output\\005\\");

			String rpet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\005\\", "r");

			if(rpack != "" && rpet != "")
			{
				if(Pack.compareRPack(Settings.datafolder + "etalon\\005\\" + rpet, Settings.fullfolder + "output\\005\\" + rpack))							
					logTestResult("R ����� " + Settings.fullfolder + "output\\005\\" + rpack + " ��������� � ��������� R ������� " + Settings.datafolder + "etalon\\005\\" + rpet + " �� ����� rpack.msk .", true);
				else
					logTestResult("R ����� " + Settings.fullfolder + "output\\005\\" + rpack + " �� ��������� � ��������� R ������� " + Settings.datafolder + "etalon\\005\\" + rpet + " �� ����� rpack.msk .", false);

			}
			else if(rpack == "")
			{
				Log.msgCMP("���������� R ����� � " +  Settings.fullfolder + "output\\005\\" + rpack + " ��� ���������.");
				logTestResult("���������� R ����� � " +  Settings.fullfolder + "output\\005\\" + rpack + " ��� ���������.", false);
			}
			else if(rpet == "")
			{
				Log.msg("���������� ��������� R ����� � " +  Settings.datafolder + "etalon\\005\\" + rpet + " ��� ���������.");
				logInfo("���������� ��������� R ����� � " +  Settings.datafolder + "etalon\\005\\" + rpet + " ��� ���������.");
			}

			String bpack = Pack.copyBPack("005");
			if(bpack != "")
				logInfo("B ����� " + bpack + " ���������� � " + Settings.fullfolder + "output\\005\\");

			String bpet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\005\\", "b");

			if(bpack != "" && bpet != "")
			{
				if(Pack.compareBPack(Settings.datafolder + "etalon\\005\\" + bpet, Settings.fullfolder + "output\\005\\" + bpack))							
					logTestResult("B ����� " + Settings.fullfolder + "output\\005\\" + bpack + " ��������� � ��������� B ������� " + Settings.datafolder + "etalon\\005\\" + bpet + " �� ����� bpack.msk .", true);
				else
					logTestResult("B ����� " + Settings.fullfolder + "output\\005\\" + bpack + " �� ��������� � ��������� B ������� " + Settings.datafolder + "etalon\\005\\" + bpet + " �� ����� bpack.msk .", false);

			}
			else if(bpack == "")
			{
				Log.msgCMP("���������� B ����� � " +  Settings.fullfolder + "output\\005\\" + bpack + " ��� ���������.");
				logTestResult("���������� B ����� � " +  Settings.fullfolder + "output\\005\\" + bpack + " ��� ���������.", false);
			}
			else if(bpet == "")
			{
				Log.msg("���������� ��������� B ����� � " +  Settings.datafolder + "etalon\\005\\" + bpet + " ��� ���������.");
				logInfo("���������� ��������� B ����� � " +  Settings.datafolder + "etalon\\005\\" + bpet + " ��� ���������.");
			}

		}

		//�������� xml � ���������
		if(isdeltadb)
		{
			Log.msg("��������� ����������� ��������� � ��.");
			DeltaDB.createXML("005\\spack.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\005\\spack.xml").exists() && new File(Settings.fullfolder + "output\\005\\spack.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\005\\spack.xml", Settings.fullfolder + "output\\005\\spack.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\005\\spack.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\005\\spack.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\005\\spack.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\005\\spack.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\005\\spack.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\005\\spack.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\005\\spack.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\005\\spack.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\005\\spack.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\005\\spack.xml ��� ���������.");
			}

		}



		Log.msg("���������� ����� '�������� ������ S ������ � � ��� ������' ���������.");
		Log.close();

	}
}

