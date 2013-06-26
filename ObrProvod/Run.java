package ObrProvod;
import java.io.File;
import java.io.IOException;

import resources.ObrProvod.RunHelper;
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

public class Run extends RunHelper
{

	public void testMain(Object[] args) 
	{
		boolean isreset = true, iscontrspack = true, isobrspack = true, iscontrrpack = true, isdeltadb = true;

		Log.msg("������ ���������� ����� '�������� ������ S ������ ��� ������'.");

		try {
			Settings.testProj = TSSUtility.getScriptOption("projFolder");		
		} catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}

		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];

		Init.load();
		Settings.readXML(Settings.testProj + "settings\\general.xml");	

		if(isreset)
			callScript("SABS.Reset");
		
		String sfilename = Pack.getPackNameFolder(Settings.datafolder + "\\input\\003\\", "s");
		
		Pack.copyFile(Settings.datafolder + "\\input\\003\\" + sfilename, Settings.path + "post\\kPuI\\" + sfilename);
		
		logInfo("S ����� " +  sfilename+ " ���������� � ����.");

		//�������� ������ � ��������� ��� ����
		if(isdeltadb)
		{
			Log.msg("������ ����������� ��������� � ��.");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}


		//�������� S-������
		if(iscontrspack)
		{
			Log.msg("������� ������ �� �������� S ������.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ObrProvod.Vvod.ContrSpack");
			callScript("SABS.CloseSABS");
		}

		//��������� S-������
		if(isobrspack)
		{
			Log.msg("������� ������ �� ��������� S ������.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("ObrProvod.Vvod.ObrSpack");			
			callScript("SABS.CloseSABS");
		}


		//�������� R-������
		if(iscontrrpack)
		{
			Log.msg("������� ������ �� �������� R ������.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ObrProvod.Vvod.ContrRpack");
			callScript("SABS.CloseSABS");	
			
			String rpack = Pack.copyRPack("003");
			if(!rpack.equals(""))
				logInfo("R ����� " + rpack + " ���������� � " + Settings.fullfolder + "output\\003\\");
			
			String rpet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\003\\", "r");
			
			if(!rpack.equals("") && !rpet.equals(""))
			{
				if(Pack.compareRPack(Settings.datafolder + "etalon\\003\\" + rpet, Settings.fullfolder + "output\\003\\" + rpack))							
					logTestResult("R ����� " + Settings.fullfolder + "output\\003\\" + rpack + " ��������� � ��������� R ������� " + Settings.datafolder + "etalon\\003\\" + rpet + " �� ����� rpack.msk .", true);
				else
					logTestResult("R ����� " + Settings.fullfolder + "output\\003\\" + rpack + " �� ��������� � ��������� R ������� " + Settings.datafolder + "etalon\\003\\" + rpet + " �� ����� rpack.msk .", false);

			}
			else if(rpack.equals(""))
			{
				Log.msgCMP("���������� R ����� � " +  Settings.fullfolder + "output\\003\\" + rpack + " ��� ���������.");
				logTestResult("���������� R ����� � " +  Settings.fullfolder + "output\\003\\" + rpack + " ��� ���������.", false);
			}
			else if(rpet.equals(""))
			{
				Log.msg("���������� ��������� R ����� � " +  Settings.datafolder + "etalon\\003\\" + rpet + " ��� ���������.");
				logInfo("���������� ��������� R ����� � " +  Settings.datafolder + "etalon\\003\\" + rpet + " ��� ���������.");
			}

		}

		//�������� xml � ���������
		if(isdeltadb)
		{
			Log.msg("��������� ����������� ��������� � ��.");
			DeltaDB.createXML("003\\spack.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\003\\spack.xml").exists() && new File(Settings.fullfolder + "output\\003\\spack.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\003\\spack.xml", Settings.fullfolder + "output\\003\\spack.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\003\\spack.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\003\\spack.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\003\\spack.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\003\\spack.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\003\\spack.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\003\\spack.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\003\\spack.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\003\\spack.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\003\\spack.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\003\\spack.xml ��� ���������.");
			}

		}

		

		Log.msg("���������� ����� '�������� ������ S ������ ��� ������' ���������.");
		Log.close();
	}
}

