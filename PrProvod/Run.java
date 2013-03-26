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

		Log.msg("Начало выполнения теста 'Начальный провод с генерацией R пакета'.");

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

		//Создание таблиц и триггеров для лога
		if(isdeltadb)
		{
			Log.msg("Начало логирования изменений в БД.");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}

		PaymentDocumentList pl = new PaymentDocumentList();
		if(ispervvod || iscontrvvod)
		{			
			pl.readXML(Settings.datafolder + "input\\001\\paydocs.xml");
			//System.out.println(pl.toString());
		}

		//первичный ввод
		if(ispervvod)
		{
			Log.msg("Запущен скрипт по первичному вводу документов.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.pervvod.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.pervvod.user, Settings.Login.pervvod.pwd, Settings.Login.pervvod.sign});		
			callScript("PrProvod.PervVvod.Vvod", new Object[]{ pl});
			callScript("SABS.CloseSABS");
		}


		//контрольный ввод
		if(iscontrvvod)
		{
			Log.msg("Запущен скрипт по контрольному вводу документов.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contrvvod.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contrvvod.user, Settings.Login.contrvvod.pwd, Settings.Login.contrvvod.sign});
			callScript("PrProvod.PervVvod.ContrVvod", new Object[]{ pl});
			callScript("SABS.CloseSABS");
		}

		//создание xml с измениями
		if(isdeltadb)
		{
			Log.msg("Окончание логирования изменений в БД.");
			DeltaDB.createXML("001\\vvod.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\001\\vvod.xml").exists() && new File(Settings.fullfolder + "output\\001\\vvod.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\001\\vvod.xml", Settings.fullfolder + "output\\001\\vvod.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\001\\vvod.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\001\\vvod.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\001\\vvod.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\001\\vvod.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\001\\vvod.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\001\\vvod.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\001\\vvod.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\001\\vvod.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\001\\vvod.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\001\\vvod.xml для сравнения.");
			}
		}
		//формирование ЭС
		if(isformes)
		{
			Log.msg("Запущен скрипт по формирования ЭС.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("PrProvod.PervVvod.FormES");
			callScript("SABS.CloseSABS");
		}

		//контроль ЭС
		if(iscontres)
		{
			Log.msg("Запущен скрипт по контролю ЭС.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("PrProvod.PervVvod.ContrES");
			callScript("SABS.CloseSABS");


			String spack = Pack.copySPack("001");
			if(spack != "")
				logInfo("S пакет " + spack + " скопирован в " + Settings.fullfolder + "output\\001\\");
			String spet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\001\\", "s");


			if(spack != "" && spet != "")
			{
				if(Pack.compareSPack(Settings.datafolder + "etalon\\001\\" + spet, Settings.fullfolder + "output\\001\\" + spack))							
					logTestResult("S пакет " + Settings.fullfolder + "output\\001\\" + spack + " совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\001\\" + spet + " по маске spack.msk .", true);
				else
					logTestResult("S пакет " + Settings.fullfolder + "output\\001\\" + spack + " не совпадает с эталонным S пакетом " + Settings.datafolder + "etalon\\001\\" + spet + " по маске spack.msk .", false);

			}
			else if(spack == "")
			{
				Log.msgCMP("Отсуствует S пакет в " +  Settings.fullfolder + "output\\001\\" + spack + " для сравнения.");
				logTestResult("Отсуствует S пакет в " +  Settings.fullfolder + "output\\001\\" + spack + " для сравнения.", false);
			}
			else if(spet == "")
			{
				Log.msg("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\001\\" + spet + " для сравнения.");
				logInfo("Отсуствует эталонный S пакет в " +  Settings.datafolder + "etalon\\001\\" + spet + " для сравнения.");
			}
		}		



		//создание R-пакета
		if(isgenrpack)
		{
			Log.msg("Запущен скрипт по созданию R пакета.");
			Settings.Sign.readXML(Settings.testProj + "settings\\sign.xml");

			Pack.createRpack();

			String rfilename = Pack.getRPackName();
			callScript("SABS.VFD",new String[]{Settings.Sign.keyobr});
			callScript("SABS.SignFile",new String[]{Settings.Sign.signobr, Settings.fullfolder + "\\input\\001\\rpack.txt", Settings.fullfolder + "\\input\\001\\rpack_1sgn.txt"});
			callScript("SABS.VFD",new String[]{Settings.Sign.keycontr});			
			callScript("SABS.SignFile",new String[]{Settings.Sign.signcontr, Settings.fullfolder + "\\input\\001\\rpack_1sgn.txt", Settings.fullfolder + "\\input\\001\\" + rfilename});
			logInfo("Создан R пакет " +  rfilename + " .");

			try {
				Pack.copyFile(Settings.fullfolder + "\\input\\001\\" + rfilename, Settings.path + "post\\kPuI\\" + rfilename);
			} catch (IOException e) {
				e.printStackTrace();
				Log.msg(e);
			}

		}
		if(isdeltadbr)
		{
			Log.msg("Начало логирования изменений в БД.");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}
		//контроль R-пакета
		if(iscontrrpack)
		{
			Log.msg("Запущен скрипт по контролю R пакета.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			sleep(5);
			callScript("SABS.VFD",new String[]{Settings.Login.contres.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.contres.user, Settings.Login.contres.pwd, Settings.Login.contres.sign});
			callScript("PrProvod.PervVvod.ContrRpack");
			callScript("SABS.CloseSABS");
		}

		//обработка R-пакета
		if(isobrrpack)
		{
			Log.msg("Запущен скрипт по обработке R пакета.");
			Settings.Login.readXML(Settings.testProj + "settings\\login.xml");
			callScript("SABS.VFD",new String[]{Settings.Login.formes.key});
			callScript("SABS.StartSABS",new String[]{Settings.Login.formes.user, Settings.Login.formes.pwd, Settings.Login.formes.sign});
			callScript("PrProvod.PervVvod.ObrRpack");			
			callScript("SABS.CloseSABS");
		}
		if(isdeltadbr)
		{
			Log.msg("Окончания логирования изменений в БД.");
			DeltaDB.createXML("001\\rpack.xml");
			DeltaDB.deleteDBLog();


			if(new File(Settings.datafolder + "etalon\\001\\rpack.xml").exists() && new File(Settings.fullfolder + "output\\001\\rpack.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\001\\rpack.xml", Settings.fullfolder + "output\\001\\rpack.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\001\\rpack.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\001\\rpack.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\001\\rpack.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\001\\rpack.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\001\\rpack.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\001\\rpack.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\001\\rpack.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\001\\rpack.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\001\\rpack.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\001\\rpack.xml для сравнения.");
			}
		}
		Log.msg("Выполнение теста 'Начальный провод с генерацией R пакета' завершено.");
		Log.close();
	}
}

