package Modules;

import java.io.File;

import resources.Modules.CompareOutputHelper;
import ru.sabstest.DeltaDB;
import ru.sabstest.Log;
import ru.sabstest.ReadEDList;
import ru.sabstest.Settings;

public class CompareOutput extends CompareOutputHelper {

    public void testMain(Object[] args) {
	// List<String> st = Arrays.asList((String[]) args[0]);
	String num = (String) args[1];

	// сравнение ЭС
	ReadEDList plOut = new ReadEDList();
	plOut.readFolder(Settings.fullfolder + "\\output\\" + num);
	ReadEDList plEt = new ReadEDList();
	plEt.readFolder(Settings.datafolder + "etalon\\" + num);

	if (plOut.getSize() != 0 && plEt.getSize() != 0) {
	    boolean cmp = plOut.equals(plEt);

	    vpManual("CompareOutput", cmp, true).performTest();

	    if (cmp) {
		Log.msgCMP("Результаты совпадают с эталонами.");
		logTestResult("Результаты совпадают с эталонами.", true);

	    } else {
		Log.msgCMP("Результаты не совпадают с эталонами.");
		logTestResult("Результаты не совпадают с эталонами.", false);
	    }
	} else if (plOut.getSize() == 0) {
	    Log.msg("Отсутствуют выходные данные.");
	    logTestResult("Отсуствуют выходные данные.", false);
	} else if (plEt.getSize() == 0) {
	    Log.msg("Отсутствуют эталонные данные.");
	    logInfo("Отсуствуют эталонные данные.");
	}

	// сравнение изменений в БД
	File[] dbet = DeltaDB.getDeltaDBFiles(Settings.datafolder + "etalon\\"
		+ num);
	File[] dbout = DeltaDB.getDeltaDBFiles(Settings.fullfolder
		+ "\\output\\" + num);

	if (dbet == null)
	    dbet = new File[0];

	if (dbout == null)
	    dbout = new File[0];

	if (dbet.length != 0 && dbout.length != 0) {
	    if (DeltaDB.cmpDeltaDBfld(dbet, dbout)) {
		Log.msgCMP("Изменения в БД совпадают с эталонами.");
		logTestResult("Изменения в БД совпадают с эталонами.", true);
	    } else {
		Log.msgCMP("Изменения в БД не совпадают с эталонами.");
		logTestResult("Изменения в БД не совпадают с эталонами.", false);
	    }
	} else if (dbout.length == 0 && dbet.length != 0) {
	    Log.msg("Отсутствуют выходные изменения в БД.");
	    logTestResult("Отсуствуют выходные изменения в БД.", false);
	} else if (dbet.length == 0 && dbout.length != 0) {
	    Log.msg("Отсутствуют эталонные изменения в БД.");
	    logInfo("Отсуствуют эталонные изменения в БД.");
	}

    }
}
