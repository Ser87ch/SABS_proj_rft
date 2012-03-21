package PrProvod;
import java.io.File;
import java.io.IOException;

import resources.PrProvod.RunHelper;
import ru.sabstest.*;

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
		boolean isreset = true, isdeltadb = true, ispervvod = true, 
		iscontrvvod = true, isformes = true, 
		iscontres = true, isgenrpack = true, isdeltadbr = true,
		iscontrrpack = true, isobrrpack = true;

		Log.msg("������ ���������� ����� '��������� ������ � ���������� R ������'.");

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

		//�������� ������ � ��������� ��� ����
		if(isdeltadb)
		{
			Log.msg("������ ����������� ��������� � ��.");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}

		PayDocList pl = new PayDocList();
		if(ispervvod || iscontrvvod)
		{			
			pl.readXML(Settings.datafolder + "input\\001\\paydocs.xml");
			//System.out.println(pl.toString());
		}

		//��������� ����
		if(ispervvod)
		{
			Log.msg("������� ������ �� ���������� ����� ����������.");
			Settings.PerVvod.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\pervvod.xml");
			callScript("SABS.VFD",new String[]{Settings.PerVvod.key});
			callScript("SABS.StartSABS",new String[]{Settings.PerVvod.user, Settings.PerVvod.pwd, Settings.PerVvod.sign});		
			callScript("PrProvod.PervVvod.Vvod", new Object[]{ pl});
			callScript("SABS.CloseSABS");
		}


		//����������� ����
		if(iscontrvvod)
		{
			Log.msg("������� ������ �� ������������ ����� ����������.");
			Settings.ContrVvod.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contrvvod.xml");
			callScript("SABS.VFD",new String[]{Settings.ContrVvod.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrVvod.user, Settings.ContrVvod.pwd, Settings.ContrVvod.sign});
			callScript("PrProvod.PervVvod.ContrVvod", new Object[]{ pl});
			callScript("SABS.CloseSABS");
		}

		//�������� xml � ���������
		if(isdeltadb)
		{
			Log.msg("��������� ����������� ��������� � ��.");
			DeltaDB.createXML("001\\vvod.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\001\\vvod.xml").exists() && new File(Settings.fullfolder + "output\\001\\vvod.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\001\\vvod.xml", Settings.fullfolder + "output\\001\\vvod.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\001\\vvod.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\001\\vvod.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\001\\vvod.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\001\\vvod.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\001\\vvod.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\001\\vvod.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\001\\vvod.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\001\\vvod.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\001\\vvod.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\001\\vvod.xml ��� ���������.");
			}
		}
		//������������ ��
		if(isformes)
		{
			Log.msg("������� ������ �� ������������ ��.");
			Settings.FormES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\formes.xml");
			callScript("SABS.VFD",new String[]{Settings.FormES.key});
			callScript("SABS.StartSABS",new String[]{Settings.FormES.user, Settings.FormES.pwd, Settings.FormES.sign});
			callScript("PrProvod.PervVvod.FormES");
			callScript("SABS.CloseSABS");
		}

		//�������� ��
		if(iscontres)
		{
			Log.msg("������� ������ �� �������� ��.");
			Settings.ContrES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contres.xml");
			callScript("SABS.VFD",new String[]{Settings.ContrES.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrES.user, Settings.ContrES.pwd, Settings.ContrES.sign});
			callScript("PrProvod.PervVvod.ContrES");
			callScript("SABS.CloseSABS");


			String spack = Pack.copySPack("001");
			if(spack != "")
				logInfo("S ����� " + spack + " ���������� � " + Settings.fullfolder + "output\\001\\");
			String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\001\\", "s");


			if(spack != "" && spet != "")
			{
				if(Pack.compareSPack(Settings.datafolder + "etalon\\001\\" + spet, Settings.fullfolder + "output\\001\\" + spack))							
					logTestResult("S ����� " + Settings.fullfolder + "output\\001\\" + spack + " ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\001\\" + spet + " �� ����� spack.msk .", true);
				else
					logTestResult("S ����� " + Settings.fullfolder + "output\\001\\" + spack + " �� ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\001\\" + spet + " �� ����� spack.msk .", false);

			}
			else if(spack == "")
			{
				Log.msgCMP("���������� S ����� � " +  Settings.fullfolder + "output\\001\\" + spack + " ��� ���������.");
				logTestResult("���������� S ����� � " +  Settings.fullfolder + "output\\001\\" + spack + " ��� ���������.", false);
			}
			else if(spet == "")
			{
				Log.msg("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\001\\" + spet + " ��� ���������.");
				logInfo("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\001\\" + spet + " ��� ���������.");
			}
		}		



		//�������� R-������
		if(isgenrpack)
		{
			Log.msg("������� ������ �� �������� R ������.");
			Settings.GenRpack.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\genrpack.xml");

			Pack.createRpack();

			String rfilename = Pack.getRPackName();
			callScript("SABS.VFD",new String[]{Settings.GenRpack.keyobr});
			callScript("SABS.SignFile",new String[]{Settings.GenRpack.signobr, Settings.fullfolder + "\\input\\001\\rpack.txt", Settings.fullfolder + "\\input\\001\\rpack_1sgn.txt"});
			callScript("SABS.VFD",new String[]{Settings.GenRpack.keycontr});			
			callScript("SABS.SignFile",new String[]{Settings.GenRpack.signcontr, Settings.fullfolder + "\\input\\001\\rpack_1sgn.txt", Settings.fullfolder + "\\input\\001\\" + rfilename});
			logInfo("������ R ����� " +  rfilename + " .");

			try {
				Pack.copyFile(Settings.fullfolder + "\\input\\001\\" + rfilename, Settings.path + "post\\kPuI\\" + rfilename);
			} catch (IOException e) {
				e.printStackTrace();
				Log.msg(e);
			}

		}
		if(isdeltadbr)
		{
			Log.msg("������ ����������� ��������� � ��.");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}
		//�������� R-������
		if(iscontrrpack)
		{
			Log.msg("������� ������ �� �������� R ������.");
			Settings.ContrES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contres.xml");
			sleep(5);
			callScript("SABS.VFD",new String[]{Settings.ContrES.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrES.user, Settings.ContrES.pwd, Settings.ContrES.sign});
			callScript("PrProvod.PervVvod.ContrRpack");
			callScript("SABS.CloseSABS");
		}

		//��������� R-������
		if(isobrrpack)
		{
			Log.msg("������� ������ �� ��������� R ������.");
			Settings.FormES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\formes.xml");
			callScript("SABS.VFD",new String[]{Settings.FormES.key});
			callScript("SABS.StartSABS",new String[]{Settings.FormES.user, Settings.FormES.pwd, Settings.FormES.sign});
			callScript("PrProvod.PervVvod.ObrRpack");			
			callScript("SABS.CloseSABS");
		}
		if(isdeltadbr)
		{
			Log.msg("��������� ����������� ��������� � ��.");
			DeltaDB.createXML("001\\rpack.xml");
			DeltaDB.deleteDBLog();


			if(new File(Settings.datafolder + "etalon\\001\\rpack.xml").exists() && new File(Settings.fullfolder + "output\\001\\rpack.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\001\\rpack.xml", Settings.fullfolder + "output\\001\\rpack.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\001\\rpack.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\001\\rpack.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\001\\rpack.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\001\\rpack.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\001\\rpack.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\001\\rpack.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\001\\rpack.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\001\\rpack.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\001\\rpack.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\001\\rpack.xml ��� ���������.");
			}
		}
		Log.msg("���������� ����� '��������� ������ � ���������� R ������' ���������.");
		Log.close();
	}
}

