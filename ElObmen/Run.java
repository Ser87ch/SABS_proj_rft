package ElObmen;

import java.io.File;
import java.io.IOException;

import resources.ElObmen.RunHelper;
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

import com.ibm.rational.test.ft.object.interfaces.sapwebportal.*;

import com.rational.test.tss.TSSException;
import com.rational.test.tss.TSSUtility;

/**
 * Description   : Functional Test Script
 * @author Admin
 */
public class Run extends RunHelper
{
	
	public void testMain(Object[] args) 
	{
		// ���������� �������� 

		boolean isDeltaDbEoIn   =       false ;
		boolean isDeltaDbVrOut  =       false ;
		boolean isDeltaDbVrIn   =       false ;
		boolean isDeltaDbVrRf   =       false ;
		boolean isDeltaDbEoOut  =       false ;
		boolean isDeltaDb = (isDeltaDbEoIn  || 
				             isDeltaDbVrOut || 
				             isDeltaDbVrIn  || 
				             isDeltaDbVrRf  || 
				             isDeltaDbEoOut)  ;

		boolean isDeltaXfVrOut  =       false ;
		boolean isDeltaXfVrRf   =       false ;
		boolean isDeltaXfEoOut  =       false ;
		
		boolean isGenEoPack     =       false ;		

		boolean isCopyNach      = true        ;
		boolean isCopyOtvet     = true        ;	
		
		
		//!!!
		boolean isDbgOotvet     =       false ;
		
		
		//						= true        ;
		//						=       false ;

		
		//
		boolean isCreateLog     = true        ;
		boolean isReset         = true        ;
		
		boolean isEoInContr     = true        ;		
		boolean isEoInOtvet     = true        ;		

		boolean isVerNcontr     = true        ;		
		boolean isVerNOtvet     = true        ;		

		boolean isVerOcontr     = true        ;			
		boolean isVerOotvet     = true        ;			
		boolean isVerRcontr     = true        ;	
			
		boolean isEoOutContr    = true        ;	
		boolean isEoOutOtvet    = true        ;	

		
		
		
		try 
		{
			Settings.testProj = TSSUtility.getScriptOption("projFolder");		
		} 
		catch (TSSException e) {
			e.printStackTrace();
			Log.msg(e);			
		}
		
		if(Settings.testProj == null)
			Settings.testProj = (String) args[0];


		
		// 
		if(isCreateLog)
		{
			Init.mkDataFolder();
			Init.mkfolder();
		}
		else
		{
			Init.load();	
		}
		
		

		Log.msg(" ");
		Log.msg("--------�������� ��������---------");
				
		Settings.readXML(Settings.testProj + "settings\\general.xml");				
		
		Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
		
		
		Log.msg(" ");
		Log.msg("==========������ ���������� ����� '�� � ��������� ���'===========");
		Log.msg(" ");
		
		
		
		// �������������� ��������� ����� � ������� �/�
		if(isReset)
			callScript("SABS.Reset");

		
	
		//�������� ������� ��
		if(isGenEoPack==false)
		{
			if(isCopyNach)
			{
				///
				//Pack.copyEoFiles("c:\\dm\\eo\\in\\", Settings.path + "post\\ufebs\\in\\","k");
				//Pack.copyEoFiles("c:\\dm\\eo\\in\\", Settings.path + "post\\ufebs\\in\\","b");
				///
				
				Log.msg(" ");
				Log.msg("-------����������� ���������� �������� ������� �� ��-------");

				String sf = "c:\\dm\\eo\\in\\";
				String df = Settings.path + "post\\ufebs\\in\\" ;
				Pack.copyEoFiles(sf, df,"k");
				Pack.copyEoFiles(sf, df,"b");
				///
			}
		}	
		else
		{
			Log.msg(" ");
			Log.msg("-------C������� ������� �� ��� ������������ ��-------");

			/*
			 
            */
			Pack.copyEoFiles(Settings.datafolder + "\\input\\006\\", Settings.path + "post\\kPuI\\","k");
			Pack.copyEoFiles(Settings.datafolder + "\\input\\006\\", Settings.path + "post\\kPuI\\","b");
			
			Log.msg("�������� ������� �� ��� ������������ �� ���������.");
		}
		
		
		
		//�������� ������ � ��������� ��� ����
		// ???
		if(isDeltaDb)
		{
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			//??? ����� ����� ������������ "������" ���-����(�� ��-��-����)??? 
			DeltaDB.createDBLog();
		}
		
		if(isDeltaDbEoIn)
		{
			Log.msg(">>>>>>������ ����������� ��������� � ��(006 - ����� ��).");
		}

		
		
		//��������� �� - ����� ��
		if(isEoInContr)
		{
			Log.msg(" ");
			Log.msg("-------�������� �������� �� �� ��(�����)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eocontr.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eocontr.user, Settings.Login.eocontr.pwd, Settings.Login.eocontr.sign});		
			callScript("ElObmen.EoIn.EoInContr");
			callScript("SABS.CloseSABS");
		}


		
		//���������������� �� - ����� ��
		if(isEoInOtvet)
		{
			Log.msg(" ");
			Log.msg("-------��������� �������� �� �� ��(�����)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eootvet.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eootvet.user, Settings.Login.eootvet.pwd, Settings.Login.eootvet.sign});		
			callScript("ElObmen.EoIn.EoInOtvet");
			callScript("SABS.CloseSABS");
		}

		
		
		// DUMP "eoIn"
		
		
		
		if(isDeltaDbEoIn)
		{
			Log.msg("<<<<<<��������� ����������� ��������� � ��(006 - ����� ��).");
			DeltaDB.createXML("006\\eoin.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\eoin.xml").exists() && new File(Settings.fullfolder + "output\\006\\eoin.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\eoin.xml", Settings.fullfolder + "output\\006\\eoin.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\eoin.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\eoin.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\eoin.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\eoin.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\eoin.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\eoin.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\eoin.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\eoin.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\eoin.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\eoin.xml ��� ���������.");
			}
		}

		if(isDeltaDbVrOut)
		{
			Log.msg(">>>>>>>������ ����������� ��������� � ��(006 - �������� ���-��).");
		}

		
		
		//���������������� ���-�� - ������������ �� ���-��
		if(isVerNOtvet)
		{
			Log.msg(" ");
			Log.msg("-------������������ ��������� �� ���-��-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("ElObmen.EoVer.EoVerNotvet");
			callScript("SABS.CloseSABS");
		}

		
		
		//��������� ���-�� - ������������ �� ���-��
		if(isVerNcontr)
		{
			Log.msg(" ");
			Log.msg("-------�������� ��������� �� ���-��-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ElObmen.EoVer.EoVerNcontr");
			callScript("SABS.CloseSABS");
			
			if(isDeltaXfVrOut)
			{
				//��������� ��������� S-�������
				String spack = Pack.copySPack("006");
				if(spack != "")
					logInfo("S ����� " + spack + " ���������� � " + Settings.fullfolder + "output\\006\\");
				
				String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\006\\", "s");

				if(spack != "" && spet != "")
				{
					if(Pack.compareSPack(Settings.datafolder + "etalon\\006\\" + spet, Settings.fullfolder + "output\\006\\" + spack))							
						logTestResult("S ����� " + Settings.fullfolder + "output\\006\\" + spack + " ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\006\\" + spet + " �� ����� spack.msk .", true);
					else
						logTestResult("S ����� " + Settings.fullfolder + "output\\006\\" + spack + " �� ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\006\\" + spet + " �� ����� spack.msk .", false);

				}
				else if(spack == "")
				{
					Log.msgCMP("���������� S ����� � " +  Settings.fullfolder + "output\\006\\" + spack + " ��� ���������.");
					logTestResult("���������� S ����� � " +  Settings.fullfolder + "output\\006\\" + spack + " ��� ���������.", false);
				}
				else if(spet == "")
				{
					Log.msg("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\006\\" + spet + " ��� ���������.");
					logInfo("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\006\\" + spet + " ��� ���������.");
				}
			}
		}


		
		// DUMP "eoNver"
		
		

		if(isDeltaDbVrOut)
		{
			Log.msg("<<<<<<<��������� ����������� ��������� � ��(006 - �������� ���-��).");
			DeltaDB.createXML("006\\verout.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\verout.xml").exists() && new File(Settings.fullfolder + "output\\006\\verout.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\verout.xml", Settings.fullfolder + "output\\006\\verout.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\verout.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\verout.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\verout.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\verout.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\verout.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\verout.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\verout.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\verout.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\verout.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\verout.xml ��� ���������.");
			}
		}

		
		
		if(isDeltaDbVrIn)
		{
			Log.msg(">>>>>>>������ ����������� ��������� � ��(006 - ����� ���-��)");
		}

		
		
		// ����������� �������� ������� ���-��
		
		if(isGenEoPack==false)
		{
			if (isCopyOtvet)
			{
				Log.msg(" ");
				Log.msg("-------����������� ���������� �������� ������� �� ���-��-------");

				Pack.copyEoFiles("c:\\dm\\ver-pu\\in\\", Settings.path + "post\\kPuI\\","p");	
			}
			
		}	
		else
		{
			Log.msg(" ");
			Log.msg("-------�������� �/��� ����������� �������� ������� �� ���-��-------");

			/*
			 
            */
			Pack.copyEoFiles(Settings.datafolder + "\\input\\006\\", Settings.path + "post\\kPuI\\","p");
		}
		

		
		if(isVerOcontr)
		{
			Log.msg(" ");
			Log.msg("-------�������� �������� ������� �� ���-�� (���� ��)-------");			
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ElObmen.EoVer.EoVerOcontr");
			callScript("SABS.CloseSABS");
		}


		
		
		if(isVerOotvet)
		{
			Log.msg(" ");
			Log.msg("-------��������� �������� ������� �� ���-�� (���� ��)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
		
			//!!!
			if(isDbgOotvet)
			{
				callScript("SABS.VFD",new String[]{Settings.Login.eoadmin.key});
				callScript("SABS.StartSABS",new String[]{Settings.Login.eoadmin.user, Settings.Login.eoadmin.pwd, Settings.Login.eoadmin.sign});
			}
			else
			{
				callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
				callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			}

			callScript("ElObmen.EoVer.EoVerOotvet");			
			callScript("SABS.CloseSABS");
		}
		

		
		if(isDeltaDbVrIn)
		{
			Log.msg("<<<<<<<��������� ����������� ��������� � ��(006 - ����� ���-��).");
			DeltaDB.createXML("006\\verin.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\verin.xml").exists() && new File(Settings.fullfolder + "output\\006\\verin.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\verin.xml", Settings.fullfolder + "output\\006\\verin.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\verin.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\verin.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\verin.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\verin.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\verin.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\verin.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\verin.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\verin.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\verin.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\verin.xml ��� ���������.");
			}
		}

		
		
		if(isDeltaDbVrRf)
		{
			Log.msg(">>>>>>>������ ����������� ��������� � ��(006 - �������� R-������ ���-��).");
		}

		
		
		if(isVerRcontr)
		{
			Log.msg(" ");
			Log.msg("-------�������� ��������� R-������� �� ���-�� (���� ��)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ElObmen.EoVer.EoVerRcontr");
			callScript("SABS.CloseSABS");

			if(isDeltaXfVrRf)
			{
				/*	S-->R
				  
				//��������� ��������� S-�������
				String spack = Pack.copySPack("006");
				if(spack != "")
					logInfo("S ����� " + spack + " ���������� � " + Settings.fullfolder + "output\\006\\");
				
				String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\006\\", "s");

				if(spack != "" && spet != "")
				{
					if(Pack.compareSPack(Settings.datafolder + "etalon\\006\\" + spet, Settings.fullfolder + "output\\006\\" + spack))							
						logTestResult("S ����� " + Settings.fullfolder + "output\\006\\" + spack + " ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\006\\" + spet + " �� ����� spack.msk .", true);
					else
						logTestResult("S ����� " + Settings.fullfolder + "output\\006\\" + spack + " �� ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\006\\" + spet + " �� ����� spack.msk .", false);

				}
				else if(spack == "")
				{
					Log.msgCMP("���������� S ����� � " +  Settings.fullfolder + "output\\006\\" + spack + " ��� ���������.");
					logTestResult("���������� S ����� � " +  Settings.fullfolder + "output\\006\\" + spack + " ��� ���������.", false);
				}
				else if(spet == "")
				{
					Log.msg("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\006\\" + spet + " ��� ���������.");
					logInfo("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\006\\" + spet + " ��� ���������.");
				}
				*/
				
			}
		}
		
		
		
		// DUMP "eoOver"
		
		
		
		if(isDeltaDbVrRf)
		{
			Log.msg("<<<<<<<��������� ����������� ��������� � ��(006 - ����� ���-��).");
			DeltaDB.createXML("006\\verrf.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\verrf.xml").exists() && new File(Settings.fullfolder + "output\\006\\verrf.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\verrf.xml", Settings.fullfolder + "output\\006\\verrf.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\verrf.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\verrf.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\verrf.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\verrf.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\verrf.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\verrf.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\verrf.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\verrf.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\verrf.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\verrf.xml ��� ���������.");
			}
			
		}
		
		
		
		if(isDeltaDbEoOut)
		{
			Log.msg(">>>>>>>������ ����������� ��������� � ��(006 - �������� ��).");
		}

		
		
		//���������������� �� - ������������ ��
		if(isEoOutOtvet)
		{
			Log.msg(" ");
			Log.msg("-------������������ ��������� �� ����� ��-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eootvet.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eootvet.user, Settings.Login.eootvet.pwd, Settings.Login.eootvet.sign});
			callScript("ElObmen.EoOut.EoOutOtvet");
			callScript("SABS.CloseSABS");
		}


		
		//��������� �� - �������� ��
		if(isEoOutContr)
		{
			Log.msg(" ");
			Log.msg("-------�������� ��������� �� ����� ��-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eocontr.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eocontr.user, Settings.Login.eocontr.pwd, Settings.Login.eocontr.sign});
			callScript("ElObmen.EoOut.EoOutContr");
			callScript("SABS.CloseSABS");

			if(isDeltaXfEoOut)
			{
			
			// ?????
			/* ��������� ��������� ��� ��������-��� �������
			 * 
			String spack = Pack.copySPack("006");
			if(spack != "")
				logInfo("S ����� " + spack + " ���������� � " + Settings.fullfolder + "output\\006\\");
				
			String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\006\\", "s");



			if(spack != "" && spet != "")
			{
				if(Pack.compareSPack(Settings.datafolder + "etalon\\006\\" + spet, Settings.fullfolder + "output\\006\\" + spack))							
					logTestResult("S ����� " + Settings.fullfolder + "output\\006\\" + spack + " ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\006\\" + spet + " �� ����� spack.msk .", true);
				else
					logTestResult("S ����� " + Settings.fullfolder + "output\\006\\" + spack + " �� ��������� � ��������� S ������� " + Settings.datafolder + "etalon\\006\\" + spet + " �� ����� spack.msk .", false);

			}
			else if(spack == "")
			{
				Log.msgCMP("���������� S ����� � " +  Settings.fullfolder + "output\\006\\" + spack + " ��� ���������.");
				logTestResult("���������� S ����� � " +  Settings.fullfolder + "output\\006\\" + spack + " ��� ���������.", false);
			}
			else if(spet == "")
			{
				Log.msg("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\006\\" + spet + " ��� ���������.");
				logInfo("���������� ��������� S ����� � " +  Settings.datafolder + "etalon\\006\\" + spet + " ��� ���������.");
			}
			*
			*/
				
			}
		}		


		if(isDeltaDbEoOut)
		{
			Log.msg("<<<<<<<��������� ����������� ��������� � ��(006 - ����� ���-��).");
			DeltaDB.createXML("006\\eoout.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\eoout.xml").exists() && new File(Settings.fullfolder + "output\\006\\eoout.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\eoout.xml", Settings.fullfolder + "output\\006\\eoout.xml"))							
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\eoout.xml" + " ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\eoout.xml" + " .", true);
				else
					logTestResult("��������� � �� " + Settings.fullfolder + "output\\006\\eoout.xml" + " �� ��������� � ���������� ����������� " + Settings.datafolder + "etalon\\006\\eoout.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\eoout.xml").exists())
			{
				Log.msgCMP("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\eoout.xml ��� ���������.");
				logTestResult("���������� ��������� � �� � " +  Settings.fullfolder + "output\\006\\eoout.xml ��� ���������.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\eoout.xml").exists())
			{
				Log.msg("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\eoout.xml ��� ���������.");
				logInfo("���������� ��������� ��������� � �� � " +  Settings.datafolder + "etalon\\006\\eoout.xml ��� ���������.");
			}
			
		}
		
		
		
		Log.msg(" ");
		Log.msg("===========���� '�� � ��������� ���' ��������==============");
		Log.msg(" ");
		Log.close();
		
	}
}



