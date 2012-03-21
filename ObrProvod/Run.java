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

		Log.msg("Начало выполнения теста 'Обратный провод S пакета без ошибок'.");

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
		try {
			Pack.copyFile(Settings.datafolder + "\\input\\003\\" + sfilename, Settings.path + "post\\kPuI\\" + sfilename);
		} catch (IOException e) {
			e.printStackTrace();
			Log.msg(e);
		}
		logInfo("S пакет " +  sfilename+ " скопирован в САБС.");

		//Создание таблиц и триггеров для лога
		if(isdeltadb)
		{
			Log.msg("Начало логирования изменений в БД.");
			DeltaDB.readXMLSettings(Settings.testProj + "settings\\deltadb.xml");
			DeltaDB.createDBLog();
		}


		//контроль S-пакета
		if(iscontrspack)
		{
			Log.msg("Запущен скрипт по контролю S пакета.");
			Settings.ContrES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contres.xml");
			callScript("SABS.VFD",new String[]{Settings.ContrES.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrES.user, Settings.ContrES.pwd, Settings.ContrES.sign});
			callScript("ObrProvod.Vvod.ContrSpack");
			callScript("SABS.CloseSABS");
		}

		//обработка S-пакета
		if(isobrspack)
		{
			Log.msg("Запущен скрипт по обработке S пакета.");
			Settings.FormES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\formes.xml");
			callScript("SABS.VFD",new String[]{Settings.FormES.key});
			callScript("SABS.StartSABS",new String[]{Settings.FormES.user, Settings.FormES.pwd, Settings.FormES.sign});
			callScript("ObrProvod.Vvod.ObrSpack");			
			callScript("SABS.CloseSABS");
		}


		//контроль R-пакета
		if(iscontrrpack)
		{
			Log.msg("Запущен скрипт по контролю R пакета.");
			Settings.ContrES.readXML(Settings.testProj + "settings\\" + Settings.pervfolder + "\\contres.xml");
			callScript("SABS.VFD",new String[]{Settings.ContrES.key});
			callScript("SABS.StartSABS",new String[]{Settings.ContrES.user, Settings.ContrES.pwd, Settings.ContrES.sign});
			callScript("ObrProvod.Vvod.ContrRpack");
			callScript("SABS.CloseSABS");	
			
			String rpack = Pack.copyRPack("003");
			if(rpack != "")
				logInfo("R пакет " + rpack + " скопирован в " + Settings.fullfolder + "output\\003\\");
			
			String rpet = Pack.getPackNameFolder(Settings.datafolder + "etalon\\003\\", "r");
			
			if(rpack != "" && rpet != "")
			{
				if(Pack.compareRPack(Settings.datafolder + "etalon\\003\\" + rpet, Settings.fullfolder + "output\\003\\" + rpack))							
					logTestResult("R пакет " + Settings.fullfolder + "output\\003\\" + rpack + " совпадает с эталонным R пакетом " + Settings.datafolder + "etalon\\003\\" + rpet + " по маске rpack.msk .", true);
				else
					logTestResult("R пакет " + Settings.fullfolder + "output\\003\\" + rpack + " не совпадает с эталонным R пакетом " + Settings.datafolder + "etalon\\003\\" + rpet + " по маске rpack.msk .", false);

			}
			else if(rpack == "")
			{
				Log.msgCMP("Отсуствует R пакет в " +  Settings.fullfolder + "output\\003\\" + rpack + " для сравнения.");
				logTestResult("Отсуствует R пакет в " +  Settings.fullfolder + "output\\003\\" + rpack + " для сравнения.", false);
			}
			else if(rpet == "")
			{
				Log.msg("Отсуствует эталонный R пакет в " +  Settings.datafolder + "etalon\\003\\" + rpet + " для сравнения.");
				logInfo("Отсуствует эталонный R пакет в " +  Settings.datafolder + "etalon\\003\\" + rpet + " для сравнения.");
			}

		}

		//создание xml с измениями
		if(isdeltadb)
		{
			Log.msg("Окончание логирования изменений в БД.");
			DeltaDB.createXML("003\\spack.xml");
			DeltaDB.deleteDBLog();

			if(new File(Settings.datafolder + "etalon\\003\\spack.xml").exists() && new File(Settings.fullfolder + "output\\003\\spack.xml").exists())
			{
				if(DeltaDB.cmpDeltaDB(Settings.datafolder + "etalon\\003\\spack.xml", Settings.fullfolder + "output\\003\\spack.xml"))							
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\003\\spack.xml" + " совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\003\\spack.xml" + " .", true);
				else
					logTestResult("Изменения в БД " + Settings.fullfolder + "output\\003\\spack.xml" + " не совпадают с эталонными изменениями " + Settings.datafolder + "etalon\\003\\spack.xml" + " .", false);
			}
			else if(!new File(Settings.fullfolder + "output\\003\\spack.xml").exists())
			{
				Log.msgCMP("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\003\\spack.xml для сравнения.");
				logTestResult("Отсуствуют изменения в БД в " +  Settings.fullfolder + "output\\003\\spack.xml для сравнения.", false);
			}
			else if(!new File(Settings.datafolder + "etalon\\003\\spack.xml").exists())
			{
				Log.msg("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\003\\spack.xml для сравнения.");
				logInfo("Отсуствуют эталонные изменения в БД в " +  Settings.datafolder + "etalon\\003\\spack.xml для сравнения.");
			}

		}

		

		Log.msg("Выполнение теста 'Обратный провод S пакета без ошибок' завершено.");
		Log.close();
	}
}

