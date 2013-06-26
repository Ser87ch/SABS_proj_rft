package PrProvod;
import java.io.File;
import java.io.IOException;

import resources.PrProvod.RunBHelper;
import ru.sabstest.DeltaDB;
import ru.sabstest.Init;
import ru.sabstest.Log;
import ru.sabstest.Pack;
import ru.sabstest.PaymentDocumentList;
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


public class RunB extends RunBHelper
{


	public void testMain(Object[] args) 
	{
		boolean isreset = true, isdeltadb = true, ispervvod = true, 
		iscontrvvod = true, isformes = true, 
		iscontres = true, isgenrpack = true, isdeltadbr = true,
		iscontrrpack = true, isobrrpack = true;

		Log.msg("������ ���������� ����� '��������� ������ � ���������� R � B ������'.");

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

		PaymentDocumentList pl = new PaymentDocumentList();
		if(ispervvod || iscontrvvod)
		{			
			pl.readFile(Settings.datafolder + "input\\002\\paydocs.xml");
			//System.out.println(pl.toString());
		}

		//��������� ����
		if(ispervvod)
		{
			Log.msg("������� ������ �� ���������� ����� ����������.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.pervvod.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.pervvod.user, Settings.Login.pervvod.pwd, Settings.Login.pervvod.sign});		
			callScript("PrProvod.PervVvod.Vvod", new Object[]{ pl});
			callScript("SABS.CloseSABS");
		}


		//����������� ����
		if(iscontrvvod)
		{
			Log.msg("������� ������ �� ������������ ����� ����������.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contrvvod.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contrvvod.user, Settings.Login.contrvvod.pwd, Settings.Login.contrvvod.sign});
			callScript("PrProvod.PervVvod.ContrVvod", new Object[]{ pl});
			callScript("SABS.CloseSABS");
		}

		//�������� xml � ���������
		if(isdeltadb)
		{
			Log.msg("��������� ����������� ��������� � ��.");
			DeltaDB.createXML("002\\vvod.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\002\\vvod.xml").exists() && new File(Settings.fullfolder + "output\\002\\vvod.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\002\\vvod.xml", Settings.fullfolder + "output\\002\\vvod.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\002\\vvod.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\002\\vvod.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\002\\vvod.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\002\\vvod.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\002\\vvod.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\002\\vvod.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\002\\vvod.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\002\\vvod.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\002\\vvod.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\002\\vvod.xml ��� ���������.");
			}
		}
		//������������ ��
		if(isformes)
		{
			Log.msg("������� ������ �� ������������ ��.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("PrProvod.PervVvod.FormES");
			callScript("SABS.CloseSABS");
		}

		//�������� ��
		if(iscontres)
		{
			Log.msg("������� ������ �� �������� ��.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("PrProvod.PervVvod.ContrES");
			callScript("SABS.CloseSABS");


			String spack = Pack.copySPack("002");
			if(!spack.equals(""))
				logInfo("S ����� " + spack + " ���������� � " + Settings.fullfolder + "output\\002\\");
			String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\002\\", "s");


			if(!spack.equals("") && !spet.equals(""))
			{
				if(Pack.compareSPack(Settings.datafolder + "etalon\\002\\" + spet, Settings.fullfolder + "output\\002\\" + spack))							
					logTestResult("S ����� " + Settings.fullfolder + "output\\002\\" + spack + " ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\002\\" + spet + " �� ����� spack.msk .", true);
				else
					logTestResult("S ����� " + Settings.fullfolder + "output\\002\\" + spack + " �� ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\002\\" + spet + " �� ����� spack.msk .", false);

			}
			else if(spack.equals(""))
			{
				Log.msgCMP("���������� S ����� � " +  Settings.fullfolder + "output\\002\\" + spack + " ��� ���������.");
				logTestResult("���������� S ����� � " +  Settings.fullfolder + "output\\002\\" + spack + " ��� ���������.", false);
			}
			else if(spet.equals(""))
			{
				Log.msg("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\002\\" + spet + " ��� ���������.");
				logInfo("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\002\\" + spet + " ��� ���������.");
			}
		}		



		//�������� R-������
		if(isgenrpack)
		{
			Log.msg("������� ������ �� �������� R ������.");
			Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");

			Pack.createRpackError49();
			Pack.createBpackError49();

			String rfilename = Pack.getRPackName();
			callScript("SABS.VFD",new String[]{Settings.Sign.keyobr});
			callScript("SABS.SignFile",new String[]{Settings.Sign.signobr, Settings.fullfolder + "\\input\\002\\rpack.txt", Settings.fullfolder + "\\input\\002\\rpack_1sgn.txt"});
			callScript("SABS.VFD",new String[]{Settings.Sign.keycontr});			
			callScript("SABS.SignFile",new String[]{Settings.Sign.signcontr, Settings.fullfolder + "\\input\\002\\rpack_1sgn.txt", Settings.fullfolder + "\\input\\002\\" + rfilename});

			logInfo("������ R ����� " +  rfilename + " � ����� ������ 49.");
			Pack.copyFile(Settings.fullfolder + "\\input\\002\\" + rfilename, Settings.path + "post\\kPuI\\" + rfilename);
			
			String bfilename = Pack.getBPackName();

			callScript("SABS.VFD",new String[]{Settings.Sign.keyobr});
			callScript("SABS.SignFile",new String[]{Settings.Sign.signobr, Settings.fullfolder + "\\input\\002\\bpack.txt", Settings.fullfolder + "\\input\\002\\bpack_1sgn.txt"});
			callScript("SABS.VFD",new String[]{Settings.Sign.keycontr});
			callScript("SABS.SignFile",new String[]{Settings.Sign.signcontr, Settings.fullfolder + "\\input\\002\\bpack_1sgn.txt", Settings.fullfolder + "\\input\\002\\" + bfilename});
			logInfo("������ B ����� " +  bfilename + " � ����� ������ 49.");
						
			Pack.copyFile(Settings.fullfolder + "\\input\\002\\" + bfilename, Settings.path + "post\\kPuI\\" + bfilename);
			
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
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			sleep(5);
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("PrProvod.PervVvod.ContrRpack");
			callScript("SABS.CloseSABS");
		}

		//��������� R-������
		if(isobrrpack)
		{
			Log.msg("������� ������ �� ��������� R ������.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("PrProvod.PervVvod.ObrRpack");			
			callScript("SABS.CloseSABS");
		}
		if(isdeltadbr)
		{
			Log.msg("��������� ����������� ��������� � ��.");
			DeltaDB.createXML("002\\rpack.xml");
			DeltaDB.deleteDBLog();


			if(new File(Settings.datafolder + "etalon\\002\\rpack.xml").exists() && new File(Settings.fullfolder + "output\\002\\rpack.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\002\\rpack.xml", Settings.fullfolder + "output\\002\\rpack.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\002\\rpack.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\002\\rpack.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\002\\rpack.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\002\\rpack.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\002\\rpack.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\002\\rpack.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\002\\rpack.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\002\\rpack.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\002\\rpack.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\002\\rpack.xml ��� ���������.");
			}
		}
		Log.msg("���������� ����� '��������� ������ � ���������� R � B ������' ���������.");
		Log.close();
	}
}

