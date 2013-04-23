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
		// управление отладкой 

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
		Log.msg("--------Загрузка настроек---------");
				
		Settings.readXML(Settings.testProj + "settings\\general.xml");				
		
		Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
		
		
		Log.msg(" ");
		Log.msg("==========Начало выполнения теста 'ЭО с клиентами УБР'===========");
		Log.msg(" ");
		
		
		
		// восстановление страховой копии и очистка п/я
		if(isReset)
			callScript("SABS.Reset");

		
	
		//создание пакетов ЭС
		if(isGenEoPack==false)
		{
			if(isCopyNach)
			{
				///
				//Pack.copyEoFiles("c:\\dm\\eo\\in\\", Settings.path + "post\\ufebs\\in\\","k");
				//Pack.copyEoFiles("c:\\dm\\eo\\in\\", Settings.path + "post\\ufebs\\in\\","b");
				///
				
				Log.msg(" ");
				Log.msg("-------Копирование отладочных ответных пакетов ЭС ЭО-------");

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
			Log.msg("-------Cоздание пакетов ЭС для тестирования ЭО-------");

			/*
			 
            */
			Pack.copyEoFiles(Settings.datafolder + "\\input\\006\\", Settings.path + "post\\kPuI\\","k");
			Pack.copyEoFiles(Settings.datafolder + "\\input\\006\\", Settings.path + "post\\kPuI\\","b");
			
			Log.msg("Создание пакетов ЭС для тестирования ЭО выполнено.");
		}
		
		
		
		//Создание таблиц и триггеров для лога
		// ???
		if(isDeltaDb)
		{
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			//??? может нужно использовать "другой" лог-файл(не ВР-ПУ-шный)??? 
			DeltaDB.createDBLog();
		}
		
		if(isDeltaDbEoIn)
		{
			Log.msg(">>>>>>Начало логирования изменений в БД(006 - прием ЭО).");
		}

		
		
		//контролер ЭО - прием ЭС
		if(isEoInContr)
		{
			Log.msg(" ");
			Log.msg("-------Контроль входящих ЭС по ЭО(УФЭБС)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eocontr.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eocontr.user, Settings.Login.eocontr.pwd, Settings.Login.eocontr.sign});		
			callScript("ElObmen.EoIn.EoInContr");
			callScript("SABS.CloseSABS");
		}


		
		//ответисполнитель ЭО - прием ЭС
		if(isEoInOtvet)
		{
			Log.msg(" ");
			Log.msg("-------Обработка входящих ЭС по ЭО(УФЭБС)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eootvet.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eootvet.user, Settings.Login.eootvet.pwd, Settings.Login.eootvet.sign});		
			callScript("ElObmen.EoIn.EoInOtvet");
			callScript("SABS.CloseSABS");
		}

		
		
		// DUMP "eoIn"
		
		
		
		if(isDeltaDbEoIn)
		{
			Log.msg("<<<<<<Окончание логирования изменений в БД(006 - прием ЭО).");
			DeltaDB.createXML("006\\eoin.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\eoin.xml").exists() && new File(Settings.fullfolder + "output\\006\\eoin.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\eoin.xml", Settings.fullfolder + "output\\006\\eoin.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\eoin.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\eoin.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\eoin.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\eoin.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\eoin.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\eoin.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\eoin.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\eoin.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\eoin.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\eoin.xml для сравнения.");
			}
		}

		if(isDeltaDbVrOut)
		{
			Log.msg(">>>>>>>Начало логирования изменений в БД(006 - отправка ВЭР-ПУ).");
		}

		
		
		//ответисполнитель ВЭР-ПУ - формирование ЭС ВЭР-ПУ
		if(isVerNOtvet)
		{
			Log.msg(" ");
			Log.msg("-------Формирование начальных ЭС ВЭР-ПУ-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("ElObmen.EoVer.EoVerNotvet");
			callScript("SABS.CloseSABS");
		}

		
		
		//контролер ВЭР-ПУ - формирование ЭС ВЭР-ПУ
		if(isVerNcontr)
		{
			Log.msg(" ");
			Log.msg("-------Контроль начальных ЭС ВЭР-ПУ-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ElObmen.EoVer.EoVerNcontr");
			callScript("SABS.CloseSABS");
			
			if(isDeltaXfVrOut)
			{
				//сравнение созданных S-пакетов
				String spack = Pack.copySPack("006");
				if(spack != "")
					logInfo("S пакет " + spack + " скопирован в " + Settings.fullfolder + "output\\006\\");
				
				String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\006\\", "s");

				if(spack != "" && spet != "")
				{
					if(Pack.compareSPack(Settings.datafolder + "etalon\\006\\" + spet, Settings.fullfolder + "output\\006\\" + spack))							
						logTestResult("S пакет " + Settings.fullfolder + "output\\006\\" + spack + " совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\006\\" + spet + " по маске spack.msk .", true);
					else
						logTestResult("S пакет " + Settings.fullfolder + "output\\006\\" + spack + " не совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\006\\" + spet + " по маске spack.msk .", false);

				}
				else if(spack == "")
				{
					Log.msgCMP("Отсуствует S пакет в " +  Settings.fullfolder + "output\\006\\" + spack + " для сравнения.");
					logTestResult("Отсуствует S пакет в " +  Settings.fullfolder + "output\\006\\" + spack + " для сравнения.", false);
				}
				else if(spet == "")
				{
					Log.msg("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\006\\" + spet + " для сравнения.");
					logInfo("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\006\\" + spet + " для сравнения.");
				}
			}
		}


		
		// DUMP "eoNver"
		
		

		if(isDeltaDbVrOut)
		{
			Log.msg("<<<<<<<Окончание логирования изменений в БД(006 - отправка ВЭР-ПУ).");
			DeltaDB.createXML("006\\verout.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\verout.xml").exists() && new File(Settings.fullfolder + "output\\006\\verout.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\verout.xml", Settings.fullfolder + "output\\006\\verout.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\verout.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\verout.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\verout.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\verout.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\verout.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\verout.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\verout.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\verout.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\verout.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\verout.xml для сравнения.");
			}
		}

		
		
		if(isDeltaDbVrIn)
		{
			Log.msg(">>>>>>>Начало логирования изменений в БД(006 - прием ВЭР-ПУ)");
		}

		
		
		// копирование ответных пакетов ВЭР-ПУ
		
		if(isGenEoPack==false)
		{
			if (isCopyOtvet)
			{
				Log.msg(" ");
				Log.msg("-------Копирование отладочных ответных пакетов ЭС ВЭР-ПУ-------");

				Pack.copyEoFiles("c:\\dm\\ver-pu\\in\\", Settings.path + "post\\kPuI\\","p");	
			}
			
		}	
		else
		{
			Log.msg(" ");
			Log.msg("-------Создание и/или копирование ответных пакетов ЭС ВЭР-ПУ-------");

			/*
			 
            */
			Pack.copyEoFiles(Settings.datafolder + "\\input\\006\\", Settings.path + "post\\kPuI\\","p");
		}
		

		
		if(isVerOcontr)
		{
			Log.msg(" ");
			Log.msg("-------Контроль ответных пакетов ЭС ВЭР-ПУ (тест ЭО)-------");			
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ElObmen.EoVer.EoVerOcontr");
			callScript("SABS.CloseSABS");
		}


		
		
		if(isVerOotvet)
		{
			Log.msg(" ");
			Log.msg("-------Обработка ответных пакетов ЭС ВЭР-ПУ (тест ЭО)-------");
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
			Log.msg("<<<<<<<Окончание логирования изменений в БД(006 - прием ВЭР-ПУ).");
			DeltaDB.createXML("006\\verin.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\verin.xml").exists() && new File(Settings.fullfolder + "output\\006\\verin.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\verin.xml", Settings.fullfolder + "output\\006\\verin.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\verin.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\verin.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\verin.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\verin.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\verin.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\verin.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\verin.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\verin.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\verin.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\verin.xml для сравнения.");
			}
		}

		
		
		if(isDeltaDbVrRf)
		{
			Log.msg(">>>>>>>Начало логирования изменений в БД(006 - отправка R-пакета ВЭР-ПУ).");
		}

		
		
		if(isVerRcontr)
		{
			Log.msg(" ");
			Log.msg("-------Контроль начальных R-пакетов ЭС ВЭР-ПУ (тест ЭО)-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("ElObmen.EoVer.EoVerRcontr");
			callScript("SABS.CloseSABS");

			if(isDeltaXfVrRf)
			{
				/*	S-->R
				  
				//сравнение созданных S-пакетов
				String spack = Pack.copySPack("006");
				if(spack != "")
					logInfo("S пакет " + spack + " скопирован в " + Settings.fullfolder + "output\\006\\");
				
				String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\006\\", "s");

				if(spack != "" && spet != "")
				{
					if(Pack.compareSPack(Settings.datafolder + "etalon\\006\\" + spet, Settings.fullfolder + "output\\006\\" + spack))							
						logTestResult("S пакет " + Settings.fullfolder + "output\\006\\" + spack + " совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\006\\" + spet + " по маске spack.msk .", true);
					else
						logTestResult("S пакет " + Settings.fullfolder + "output\\006\\" + spack + " не совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\006\\" + spet + " по маске spack.msk .", false);

				}
				else if(spack == "")
				{
					Log.msgCMP("Отсуствует S пакет в " +  Settings.fullfolder + "output\\006\\" + spack + " для сравнения.");
					logTestResult("Отсуствует S пакет в " +  Settings.fullfolder + "output\\006\\" + spack + " для сравнения.", false);
				}
				else if(spet == "")
				{
					Log.msg("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\006\\" + spet + " для сравнения.");
					logInfo("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\006\\" + spet + " для сравнения.");
				}
				*/
				
			}
		}
		
		
		
		// DUMP "eoOver"
		
		
		
		if(isDeltaDbVrRf)
		{
			Log.msg("<<<<<<<Окончание логирования изменений в БД(006 - прием ВЭР-ПУ).");
			DeltaDB.createXML("006\\verrf.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\verrf.xml").exists() && new File(Settings.fullfolder + "output\\006\\verrf.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\verrf.xml", Settings.fullfolder + "output\\006\\verrf.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\verrf.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\verrf.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\verrf.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\verrf.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\verrf.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\verrf.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\verrf.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\verrf.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\verrf.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\verrf.xml для сравнения.");
			}
			
		}
		
		
		
		if(isDeltaDbEoOut)
		{
			Log.msg(">>>>>>>Начало логирования изменений в БД(006 - отправка ЭО).");
		}

		
		
		//ответисполнитель ЭО - формирование ЭС
		if(isEoOutOtvet)
		{
			Log.msg(" ");
			Log.msg("-------Формирование начальных ЭС УФЭБС ЭО-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eootvet.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eootvet.user, Settings.Login.eootvet.pwd, Settings.Login.eootvet.sign});
			callScript("ElObmen.EoOut.EoOutOtvet");
			callScript("SABS.CloseSABS");
		}


		
		//контролер ЭО - контроль ЭС
		if(isEoOutContr)
		{
			Log.msg(" ");
			Log.msg("-------Контроль начальных ЭС УФЭБС ЭО-------");
			//Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.eocontr.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.eocontr.user, Settings.Login.eocontr.pwd, Settings.Login.eocontr.sign});
			callScript("ElObmen.EoOut.EoOutContr");
			callScript("SABS.CloseSABS");

			if(isDeltaXfEoOut)
			{
			
			// ?????
			/* сравнение созданных для клиентов-УЭО пакетов
			 * 
			String spack = Pack.copySPack("006");
			if(spack != "")
				logInfo("S пакет " + spack + " скопирован в " + Settings.fullfolder + "output\\006\\");
				
			String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\006\\", "s");



			if(spack != "" && spet != "")
			{
				if(Pack.compareSPack(Settings.datafolder + "etalon\\006\\" + spet, Settings.fullfolder + "output\\006\\" + spack))							
					logTestResult("S пакет " + Settings.fullfolder + "output\\006\\" + spack + " совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\006\\" + spet + " по маске spack.msk .", true);
				else
					logTestResult("S пакет " + Settings.fullfolder + "output\\006\\" + spack + " не совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\006\\" + spet + " по маске spack.msk .", false);

			}
			else if(spack == "")
			{
				Log.msgCMP("Отсуствует S пакет в " +  Settings.fullfolder + "output\\006\\" + spack + " для сравнения.");
				logTestResult("Отсуствует S пакет в " +  Settings.fullfolder + "output\\006\\" + spack + " для сравнения.", false);
			}
			else if(spet == "")
			{
				Log.msg("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\006\\" + spet + " для сравнения.");
				logInfo("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\006\\" + spet + " для сравнения.");
			}
			*
			*/
				
			}
		}		


		if(isDeltaDbEoOut)
		{
			Log.msg("<<<<<<<Окончание логирования изменений в БД(006 - прием ВЭР-ПУ).");
			DeltaDB.createXML("006\\eoout.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\006\\eoout.xml").exists() && new File(Settings.fullfolder + "output\\006\\eoout.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\006\\eoout.xml", Settings.fullfolder + "output\\006\\eoout.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\eoout.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\eoout.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\006\\eoout.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\006\\eoout.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\006\\eoout.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\eoout.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\006\\eoout.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\006\\eoout.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\eoout.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\006\\eoout.xml для сравнения.");
			}
			
		}
		
		
		
		Log.msg(" ");
		Log.msg("===========Тест 'ЭО с клиентами УБР' завершен==============");
		Log.msg(" ");
		Log.close();
		
	}
}



